package mncw.main;

import org.bukkit.Material;
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
//    Вся шерсть
    public static RecipeChoice.MaterialChoice WOOLS = new RecipeChoice.MaterialChoice(
        Material.BLACK_WOOL,
        Material.BLUE_WOOL,
        Material.BROWN_WOOL,
        Material.CYAN_WOOL,
        Material.GRAY_WOOL,
        Material.GREEN_WOOL,
        Material.LIGHT_BLUE_WOOL,
        Material.LIGHT_GRAY_WOOL,
        Material.LIME_WOOL,
        Material.MAGENTA_WOOL,
        Material.ORANGE_WOOL,
        Material.PINK_WOOL,
        Material.YELLOW_WOOL,
        Material.RED_WOOL,
        Material.WHITE_WOOL,
        Material.PURPLE_WOOL
    );
}
