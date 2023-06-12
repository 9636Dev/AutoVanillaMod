package org._9636dev.autovanilla.mixin;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import org._9636dev.autovanilla.common.recipe.ISmithingRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(UpgradeRecipe.class)
public abstract class UpgradeRecipeMixin implements ISmithingRecipe {

    @Shadow
    @Final
    public Ingredient base;

    @Shadow
    @Final
    public Ingredient addition;

    @Override
    public Ingredient getBase() {
        return base;
    }

    @Override
    public Ingredient getAddition() {
        return addition;
    }
}
