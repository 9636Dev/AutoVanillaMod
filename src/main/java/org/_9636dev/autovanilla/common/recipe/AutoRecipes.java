package org._9636dev.autovanilla.common.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org._9636dev.autovanilla.AutoVanilla;

@SuppressWarnings("unused")
public class AutoRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, AutoVanilla.MODID);

    public static final RegistryObject<RecipeType<AutoSmithingRecipe>> AUTO_SMITHING = RECIPE_TYPES.register("auto_smithing", () -> new RecipeType<>() {});

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AutoVanilla.MODID);

    public static final RegistryObject<AutoSmithingRecipeSerializer> AUTO_SMITHING_SERIALIZER = RECIPE_SERIALIZERS.register("auto_smithing",
            () -> AutoSmithingRecipeSerializer.INSTANCE);
}
