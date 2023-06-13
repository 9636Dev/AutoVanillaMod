package org._9636dev.autovanilla.common.blockenttiy;

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
import net.minecraftforge.items.ItemHandlerHelper;
import org._9636dev.autovanilla.common.capability.*;
import org._9636dev.autovanilla.common.config.AutoCommonConfig;
import org._9636dev.autovanilla.common.container.AutoSmithingTableContainer;
import org._9636dev.autovanilla.common.recipe.AutoRecipes;
import org._9636dev.autovanilla.common.recipe.AutoSmithingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static org._9636dev.autovanilla.common.block.AutoBlock.HORIZONTAL_FACING;

public class AutoSmithingTableBlockEntity extends EnergyBlockEntity implements WorldlyContainer {

    public static final int DATA_ENERGY_MSB = 0;
    public static final int DATA_ENERGY_LSB = 1;
    public static final int DATA_MAX_ENERGY_MSB = 2;
    public static final int DATA_MAX_ENERGY_LSB = 3;
    public static final int DATA_PROGRESS = 4;
    public static final int DATA_MAX_PROGRESS = 5;
    public static final int DATA_DIRECTION = 6;
    public static final int DATA_COUNT = 7;
    public static final int SLOT_COUNT = 3;
    private int progress;
    private AutoSmithingRecipe currentRecipe;
    private final SidedConfig sidedConfig;

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pState) {
        super(AutoBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pState);

        this.progress = 0;
        this.currentRecipe = null;
        this.sidedConfig = new SidedConfig();
        this.setupSides();
    }

    private void setupSides() {
        this.sidedConfig.setSide(Direction.UP, SidedConfig.Side.INPUT_1);
        this.sidedConfig.setSide(Direction.NORTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.EAST, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.SOUTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.WEST, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.NORTH, SidedConfig.Side.INPUT_2_OUTPUT_1);
        this.sidedConfig.setSide(Direction.DOWN, SidedConfig.Side.OUTPUT_1);
    }

    @Override
    protected void tickServer() {
        // Return early to save resources if inventory is empty, recipe not present, or output is full
        if ((this.getItem(0).isEmpty() && this.getItem(1).isEmpty()) ||
                this.getItem(2).getCount() == this.getItem(2).getMaxStackSize() ||
                this.currentRecipe == null) return;

        // Extract Energy
        if (this.extractEnergy(this.currentRecipe.energyPerTick, true)
                != this.currentRecipe.energyPerTick) { // Not enough energy
            this.progress = Math.min(0, this.progress - 1); // Slowly decrease progress
        }
        else this.extractEnergy(this.currentRecipe.energyPerTick, false);

        if (this.progress >= this.currentRecipe.energyPerTick) { // Recipe should craft
            ItemStack result = this.currentRecipe.getResultItem().copy();
            CompoundTag tag = this.getItem(0).getTag();
            if (tag != null) result.setTag(tag.copy());

            ItemStack itemPresent = getItem(2);
            if ((itemPresent.isEmpty() || ItemHandlerHelper.canItemStacksStack(itemPresent, result)) &&
                    itemPresent.getMaxStackSize() - itemPresent.getCount() >= result.getCount()) { // Can insert item
                this.progress = 0;
                ItemStack base = this.getItem(0);
                if (!base.isEmpty()) base.shrink(1);
                ItemStack addition = this.getItem(1);
                if (!addition.isEmpty()) addition.shrink(1);
                if (itemPresent == ItemStack.EMPTY) this.setItem(2, result);
                else itemPresent.grow(result.getCount());

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
        Optional<AutoSmithingRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            // Same Recipe, return early to avoid calling onRecipeChange
            if (recipe.get().equals(this.currentRecipe)) return;
            this.currentRecipe = recipe.get();
        }
        else this.currentRecipe = null;
        onRecipeChange();
    }

    @Override
    protected void onInventoryChanged() {
        this.updateRecipe();
    }

    private void onRecipeChange() {
        // Reset Progress if recipe changes
        this.progress = 0;
    }

    private Optional<AutoSmithingRecipe> getCurrentRecipe() {
        assert level != null;
        RecipeManager recipeManager = level.getRecipeManager();
        return recipeManager.getRecipeFor(AutoRecipes.AUTO_SMITHING.get(),
                this, level).or(() -> recipeManager.getRecipeFor(RecipeType.SMITHING,
                this, level).map(AutoSmithingRecipe::fromVanilla));
    }

    @Override
    protected AutoEnergyStorage createEnergyStorage() {
        return new AutoEnergyStorage(AutoCommonConfig.getInstance().maxEnergyStored.get());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.autovanilla.smithing_table");
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
                    case 5 -> currentRecipe == null ? -1 : currentRecipe.ticksRequired;
                    case 6 -> getBlockState().getValue(HORIZONTAL_FACING).get2DDataValue();
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0,1,2,3,5,6 -> {}
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
            case NONE,INPUT_3,OUTPUT_2 -> new int[0];
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
            case NONE,INPUT_3,OUTPUT_1,OUTPUT_2 -> false;
            case INPUT_1,INPUT_1_OUTPUT_1,INPUT_1_OUTPUT_2 -> pIndex == 0;
            case INPUT_2,INPUT_2_OUTPUT_1,INPUT_2_OUTPUT_2 -> pIndex == 1;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
        return switch (this.sidedConfig.getSide(pDirection)) {
            case NONE,INPUT_1,INPUT_2,INPUT_3,OUTPUT_2,INPUT_1_OUTPUT_2,INPUT_2_OUTPUT_2 -> false;
            case OUTPUT_1,INPUT_1_OUTPUT_1,INPUT_2_OUTPUT_1 -> pIndex == 2;
        };
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AutoSmithingTableContainer(pContainerId, pPlayerInventory, this.getBlockPos(), this.data, this);
    }
}
