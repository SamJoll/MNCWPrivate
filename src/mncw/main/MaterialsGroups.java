package mncw.main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class MaterialsGroups {
//    Все бревна
    public static RecipeChoice.MaterialChoice LOGS = new RecipeChoice.MaterialChoice(
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.DARK_OAK_LOG,
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_SPRUCE_LOG
    );
//    Все колончатые блоки
    public static RecipeChoice.MaterialChoice PILLARS = new RecipeChoice.MaterialChoice(
        Material.ACACIA_LOG,
        Material.BIRCH_LOG,
        Material.JUNGLE_LOG,
        Material.DARK_OAK_LOG,
        Material.OAK_LOG,
        Material.SPRUCE_LOG,
        Material.STRIPPED_ACACIA_LOG,
        Material.STRIPPED_BIRCH_LOG,
        Material.STRIPPED_DARK_OAK_LOG,
        Material.STRIPPED_OAK_LOG,
        Material.STRIPPED_JUNGLE_LOG,
        Material.STRIPPED_SPRUCE_LOG,
        Material.CRIMSON_STEM,
        Material.WARPED_STEM,
        Material.STRIPPED_WARPED_STEM,
        Material.STRIPPED_CRIMSON_STEM,
        Material.QUARTZ_PILLAR,
        Material.PURPUR_PILLAR,
        Material.BONE_BLOCK,
        Material.POLISHED_BASALT
    );
}
