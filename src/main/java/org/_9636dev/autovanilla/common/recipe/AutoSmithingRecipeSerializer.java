package org._9636dev.autovanilla.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import org._9636dev.autovanilla.common.config.AutoCommonConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoSmithingRecipeSerializer implements RecipeSerializer<AutoSmithingRecipe> {

    static final AutoSmithingRecipeSerializer INSTANCE = new AutoSmithingRecipeSerializer();

    private AutoSmithingRecipeSerializer() {}

    @Override
    public @NotNull AutoSmithingRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pJson) {
        Ingredient base = Ingredient.fromJson(pJson.get("base"));
        Ingredient addition = Ingredient.fromJson(pJson.get("addition"));
        if (!pJson.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack result;
        if (pJson.get("result").isJsonObject()) result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
        else {
            String s1 = GsonHelper.getAsString(pJson, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
            if (item == null)
                throw new IllegalStateException("Item: " + s1 + " does not exist");
            result = new ItemStack(item);
        }

        int ticks = pJson.has("ticks") ? GsonHelper.getAsInt(pJson, "ticks") :
                AutoCommonConfig.getInstance().defaultSmithingTimeInTicks.get();
        int energy = pJson.has("energy") ? GsonHelper.getAsInt(pJson, "energy") :
                AutoCommonConfig.getInstance().defaultSmithingEnergyPerTick.get();

        return new AutoSmithingRecipe(pRecipeId, base, addition, result, ticks, energy);
    }

    @Override
    public @Nullable AutoSmithingRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
        Ingredient base = Ingredient.fromNetwork(pBuffer);
        Ingredient addition = Ingredient.fromNetwork(pBuffer);
        ItemStack result = pBuffer.readItem();
        int ticks = pBuffer.readInt();
        int energy = pBuffer.readInt();

        return new AutoSmithingRecipe(pRecipeId, base, addition, result, ticks, energy);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull AutoSmithingRecipe pRecipe) {
        pRecipe.base.toNetwork(pBuffer);
        pRecipe.addition.toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.result);
        pBuffer.writeInt(pRecipe.ticksRequired);
        pBuffer.writeInt(pRecipe.energyPerTick);
    }
}
