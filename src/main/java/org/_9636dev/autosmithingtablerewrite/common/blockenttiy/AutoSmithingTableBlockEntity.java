package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoEnergyStorage;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoItemHandler;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainer;
import org._9636dev.autosmithingtablerewrite.common.recipe.AutoRecipes;
import org._9636dev.autosmithingtablerewrite.common.recipe.ISmithingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AutoSmithingTableBlockEntity extends EnergyBlockEntity {

    public static final int DATA_COUNT = 4;
    public static final int SLOT_COUNT = 3;

    private int progress;
    private ISmithingRecipe currentRecipe;

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pState) {
        super(AutoBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pState);

        this.progress = 0;
        this.currentRecipe = null;
    }

    @Override
    protected void tickServer() {
        // Return early to save resources if inventory is empty or output is full
        if (this.itemHandler.map(i -> i.slotsAreEmpty(0, 1))
                .or(() -> this.itemHandler.map(i -> i.slotIsFull(2))).orElse(false)) return;

        if (this.currentRecipe == null) return;

        // Extract Energy
        if (this.extractEnergy(this.currentRecipe.getEnergy(), true) != this.currentRecipe.getEnergy()) { // Not enough energy
            this.progress = Math.min(0, this.progress - 1); // Slowly decrease progress
        }
        else this.extractEnergy(this.currentRecipe.getEnergy(), false);

        if (this.progress >= this.currentRecipe.getTicks()) { // Recipe should craft
            ItemStack result = this.currentRecipe.getResultItem().copy();
            CompoundTag tag = this.getStackInSlot(0).getTag();
            if (tag != null) result.setTag(tag.copy());

            if (this.insertItem(2, result, true).isEmpty()) { // Can insert item
                // TODO: 29.05.23 Do checks, don't assume recipe doesn't contain 'minecraft:air'

                this.progress = 0;
                this.getStackInSlot(0).shrink(1);
                this.getStackInSlot(1).shrink(1);
                this.insertItem(2, result, false);

                // Recheck recipe, maybe ingredients used up
                this.updateRecipe();
            }

        }
        else this.progress += 1;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putInt("autosmithingtable.progress", this.progress);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        this.progress = pTag.getInt("autosmithingtable.progress");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        // Check Recipe on Load
        this.updateRecipe();
    }

    private void updateRecipe() {
        Optional<ISmithingRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            // Same Recipe, return early to avoid calling onRecipeChange
            if (recipe.get().equals(this.currentRecipe)) return;
            this.currentRecipe = recipe.get();
        }
        else this.currentRecipe = null;
        onRecipeChange();
    }

    private void onRecipeChange() {
        // Reset Progress if recipe changes
        this.progress = 0;
    }

    private Optional<ISmithingRecipe> getCurrentRecipe() {
        assert level != null;
        RecipeManager recipeManager = level.getRecipeManager();
        Container container = new SimpleContainer(2);
        container.setItem(0, this.getStackInSlot(0));
        container.setItem(1, this.getStackInSlot(1));
        // TODO: 28.05.23 Make more readable, cleanup code
        return recipeManager.getRecipeFor(AutoRecipes.AUTO_SMITHING.get(), container, level).map(a -> (ISmithingRecipe)a)
                .or(() -> recipeManager.getRecipeFor(RecipeType.SMITHING, container, level).map(a -> (ISmithingRecipe)a));
    }

    @Override
    protected AutoEnergyStorage createEnergyStorage() {
        return new AutoEnergyStorage(100000);
    }

    @Override
    public AutoItemHandler createItemHandler() {
        return new AutoItemHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                // Check Recipe
                AutoSmithingTableBlockEntity.this.updateRecipe();
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.autosmithingtable.smithing_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AutoSmithingTableContainer(pContainerId, pPlayerInventory, this.getBlockPos(), this.data,
                this.itemHandler.orElseThrow(() -> new IllegalStateException("Could not get the ItemHandler of block")));
    }

    @Override
    public ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> getMSBofEnergy();
                    case 1 -> getLSBofEnergy();
                    case 2 -> progress;
                    case 3 -> currentRecipe == null ? -1 : currentRecipe.getTicks();
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0,1,3 -> {}
                    case 2 -> progress = pValue;
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }
}
