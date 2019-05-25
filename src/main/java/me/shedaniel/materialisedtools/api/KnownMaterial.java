package me.shedaniel.materialisedtools.api;

import net.minecraft.recipe.Ingredient;

public interface KnownMaterial {
    
    int getToolHandleColor();
    
    int getPickaxeHeadColor();
    
    String getMaterialTranslateKey();
    
    Ingredient getIngredient();
    
    String getName();
    
    boolean isBright();
    
    float getHandleDurabilityMultiplier();
    
    float getHandleBreakingSpeedMultiplier();
    
    int getPickaxeHeadDurability();
    
    float getPickaxeHeadSpeed();
    
    int getMiningLevel();
    
}