package org._9636dev.autosmithingtablerewrite.common.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org._9636dev.autosmithingtablerewrite.common.config.AutoCommonConfig;
import org.jetbrains.annotations.NotNull;

public interface ISmithingRecipe {
    Ingredient getBase();
    Ingredient getAddition();

    /**
     * Same method as Recipe class
     * @return result
     */
    ItemStack getResultItem();

    default int getTicks() {
        return AutoCommonConfig.getInstance().defaultSmithingTimeInTicks.get();
    }

    default int getEnergy() {
        return AutoCommonConfig.getInstance().defaultSmithingEnergyPerTick.get();
    }
}
