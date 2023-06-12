package org._9636dev.autovanilla.common.recipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AutoSmithingRecipe implements Recipe<Container>,ISmithingRecipe {
    private final ResourceLocation recipeId;
    final Ingredient base, addition;
    final ItemStack result;
    public final int ticksRequired, energyPerTick;

    public AutoSmithingRecipe(ResourceLocation recipeId, Ingredient baseItem, Ingredient additionItem, ItemStack outputItem,
                              int ticksRequired, int energyPerTick) {
        this.recipeId = recipeId;
        this.base = baseItem;
        this.addition = additionItem;
        this.result = outputItem;
        this.ticksRequired = ticksRequired;
        this.energyPerTick = energyPerTick;
    }


    @Override
    public boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return this.base.test(pContainer.getItem(0)) && this.addition.test(pContainer.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container pContainer) {
        ItemStack itemstack = this.result.copy();
        CompoundTag compoundtag = pContainer.getItem(0).getTag();
        if (compoundtag != null) {
            itemstack.setTag(compoundtag.copy());
        }

        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public @NotNull RecipeSerializer<AutoSmithingRecipe> getSerializer() {
        return AutoSmithingRecipeSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<AutoSmithingRecipe> getType() {
        return AutoRecipes.AUTO_SMITHING.get();
    }

    @Override
    public Ingredient getBase() {
        return base;
    }

    @Override
    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutoSmithingRecipe that)) return false;
        return ticksRequired == that.ticksRequired && energyPerTick == that.energyPerTick && Objects.equals(recipeId, that.recipeId) && Objects.equals(base, that.base) && Objects.equals(addition, that.addition) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, base, addition, result, ticksRequired, energyPerTick);
    }
}
