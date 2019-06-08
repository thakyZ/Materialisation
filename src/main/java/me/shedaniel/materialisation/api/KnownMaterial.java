package me.shedaniel.materialisation.api;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Map;
import java.util.Set;

public interface KnownMaterial extends KnownMaterials.RepairAmountGetter {
    
    int getToolHandleColor();
    
    int getToolHeadColor();
    
    String getMaterialTranslateKey();
    
    Set<Ingredient> getIngredients();
    
    default int getEnchantability() {
        return 0;
    }
    
    @Deprecated
    Map<Ingredient, Float> getIngredientMap();
    
    String getName();
    
    boolean isBright();
    
    float getHandleDurabilityMultiplier();
    
    float getHandleBreakingSpeedMultiplier();
    
    float getAttackDamage();
    
    int getHeadDurability();
    
    float getPickaxeHeadSpeed();
    
    int getMiningLevel();
    
    int getFullAmount();
    
    float getRepairMultiplier(ItemStack stack);
    
    public static interface AmountGetter {
        float getFrom(Ingredient ingredient);
    }
    
}
