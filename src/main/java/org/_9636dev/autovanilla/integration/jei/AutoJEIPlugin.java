package org._9636dev.autovanilla.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org._9636dev.autovanilla.AutoVanilla;
import org._9636dev.autovanilla.common.item.AutoItems;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@JeiPlugin
public class AutoJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(AutoVanilla.MODID, "jei");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AutoSmithingRecipeCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {

    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
       registration.addRecipeCatalyst(new ItemStack(AutoItems.AUTO_SMITHING_TABLE.get()), AutoSmithingRecipeCategory.recipeType);
    }

}
