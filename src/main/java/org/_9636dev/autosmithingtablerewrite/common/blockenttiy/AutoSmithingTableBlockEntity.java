package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org._9636dev.autosmithingtablerewrite.common.capability.*;
import org._9636dev.autosmithingtablerewrite.common.config.AutoCommonConfig;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainer;
import org._9636dev.autosmithingtablerewrite.common.recipe.AutoRecipes;
import org._9636dev.autosmithingtablerewrite.common.recipe.AutoSmithingRecipe;
import org._9636dev.autosmithingtablerewrite.common.recipe.ISmithingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public class AutoSmithingTableBlockEntity extends EnergyBlockEntity implements WorldlyContainer {

    public static final int DATA_ENERGY_MSB = 0;
    public static final int DATA_ENERGY_LSB = 1;
    public static final int DATA_MAX_ENERGY_MSB = 2;
    public static final int DATA_MAX_ENERGY_LSB = 3;
    public static final int DATA_PROGRESS = 4;
    public static final int DATA_MAX_PROGRESS = 5;


    public static final int DATA_COUNT = 6;
    public static final int SLOT_COUNT = 3;
    private int progress;
    private ISmithingRecipe currentRecipe;
    private final SidedConfig sidedConfig;

    private static final SidedConfig.Side[] availableSides = {
        SidedConfig.Side.NONE,
        SidedConfig.Side.INPUT_1,
        SidedConfig.Side.INPUT_2,
        SidedConfig.Side.OUTPUT_1,
        SidedConfig.Side.INPUT_1_OUTPUT_1,
        SidedConfig.Side.INPUT_2_OUTPUT_1,
        SidedConfig.Side.INPUT_1_OUTPUT_2,
        SidedConfig.Side.INPUT_2_OUTPUT_2,
    };

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pState) {
        super(AutoBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pState);

        this.progress = 0;
        this.currentRecipe = null;
        this.sidedConfig = new SidedConfig();
        this.sidedConfig.setSide(Direction.UP, SidedConfig.Side.INPUT_1);
        this.sidedConfig.setSide(Direction.NORTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.EAST, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.SOUTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.WEST, SidedConfig.Side.INPUT_2_OUTPUT_1);this.sidedConfig.setSide(Direction.NORTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.DOWN, SidedConfig.Side.OUTPUT_1);
    }

    @Override
    protected void tickServer() {
        // Return early to save resources if inventory is empty, recipe not present, or output is full
        if (this.itemHandler.map(i -> i.slotsAreEmpty(0, 1))
                .or(() -> this.itemHandler.map(i -> i.slotIsFull(2))).orElse(false)
                || this.currentRecipe == null) return;

        // Extract Energy
        if (this.extractEnergy(this.currentRecipe.getEnergy(), true)
                != this.currentRecipe.getEnergy()) { // Not enough energy
            this.progress = Math.min(0, this.progress - 1); // Slowly decrease progress
        }
        else this.extractEnergy(this.currentRecipe.getEnergy(), false);

        if (this.progress >= this.currentRecipe.getTicks()) { // Recipe should craft
            ItemStack result = this.currentRecipe.getResultItem().copy();
            CompoundTag tag = this.getItem(0).getTag();
            if (tag != null) result.setTag(tag.copy());

            if (this.insertItem(2, result, true).isEmpty()) { // Can insert item
                this.progress = 0;
                ItemStack base = this.getItem(0);
                if (!base.isEmpty()) base.shrink(1);
                ItemStack addition = this.getItem(1);
                if (!addition.isEmpty()) addition.shrink(1);
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

        pTag.put("autosmithingtable.sides", this.sidedConfig.serializeNBT());
        pTag.putInt("autosmithingtable.progress", this.progress);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        this.sidedConfig.deserializeNBT(pTag.getCompound("autosmithingtable.sides"));
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
        Optional<AutoSmithingRecipe> autoSmithingRecipe = recipeManager.getRecipeFor(AutoRecipes.AUTO_SMITHING.get(),
                this, level);
        if (autoSmithingRecipe.isPresent()) return autoSmithingRecipe.map(a -> a); // Implicit cast to ISmithingRecipe
        return recipeManager.getRecipeFor(RecipeType.SMITHING, this, level).map(a -> (ISmithingRecipe) a);
    }

    @Override
    protected AutoEnergyStorage createEnergyStorage() {
        return new AutoEnergyStorage(AutoCommonConfig.getInstance().maxEnergyStored.get());
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
    protected <T> LazyOptional<T> getInventoryCap(@NotNull Capability<T> cap, @Nullable Direction side) {
        /*
        add side config later and rewrite cleaner code,
        avoid using dual handlers in be (ItemStackHandler and SidedInvWrapper),
        implement own handler in InventoryBlockEntity with NonNullList
         */
        return LazyOptional.of(() -> new SidedInvWrapper(this, side)).cast();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.autosmithingtable.smithing_table");
    }

    @Override
    public ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> getMSBofEnergy();
                    case 1 -> getLSBofEnergy();
                    case 2 -> AutoEnergyStorage.getMSB(getMaxEnergyStored());
                    case 3 -> AutoEnergyStorage.getLSB(getMaxEnergyStored());
                    case 4 -> progress;
                    case 5 -> currentRecipe == null ? -1 : currentRecipe.getTicks();
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0,1,2,3,5 -> {}
                    case 4 -> progress = pValue;
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction pSide) {
        return switch (this.sidedConfig.getSide(pSide)) {
            case NONE,OUTPUT_2 -> new int[0];
            case INPUT_1,INPUT_1_OUTPUT_2 -> new int[] {0};
            case INPUT_2,INPUT_2_OUTPUT_2 -> new int[] {1};
            case OUTPUT_1 -> new int[] {2};
            case INPUT_1_OUTPUT_1 -> new int[] {0, 2};
            case INPUT_2_OUTPUT_1 -> new int[] {1, 2};
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, @NotNull ItemStack pItemStack, @Nullable Direction pDirection) {
        if (pDirection == null) return true;
        return switch (this.sidedConfig.getSide(pDirection)) {
            case NONE,OUTPUT_1,OUTPUT_2 -> false;
            case INPUT_1,INPUT_1_OUTPUT_1,INPUT_1_OUTPUT_2,INPUT_2,INPUT_2_OUTPUT_1,INPUT_2_OUTPUT_2 ->
                    canPlaceItem(pIndex, pItemStack);
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
        return switch (this.sidedConfig.getSide(pDirection)) {
            case NONE,INPUT_1,INPUT_2 -> false;
            case OUTPUT_1,INPUT_1_OUTPUT_1,INPUT_2_OUTPUT_1, OUTPUT_2,INPUT_1_OUTPUT_2,INPUT_2_OUTPUT_2 ->
                    getItem(pIndex).equals(pStack, true);
        };
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AutoSmithingTableContainer(pContainerId, pPlayerInventory, this.getBlockPos(), this.data,
                this.itemHandler.orElseThrow(() -> new IllegalStateException("Could not get the ItemHandler of block")));
    }
}
