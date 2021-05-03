package mncw.main.CustomItems;

import mncw.main.Main;
import mncw.main.MaterialsGroups;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class FlagPillar {
//    Главный класс плагина
    Main plugin;

//    Предмет
    ItemStack flagPillar;
    public ItemStack GetItem() {
        return flagPillar;
    }
//    Ключ предмета
    NamespacedKey flagPillarKey;
//    Рецепт предмета
    ShapedRecipe flagPillarRecipe;

//    Конструктор класса
    public FlagPillar(Main plugin) {
        this.plugin = plugin;

        InitItem();
        SetRecipe();
    }

//    Создание рецепта
    void SetRecipe() {
        flagPillarRecipe = new ShapedRecipe(flagPillarKey, flagPillar);
        flagPillarRecipe.shape(
                "L",
                "L",
                "L"
        );
        flagPillarRecipe.setIngredient('L', MaterialsGroups.LOGS);
    }
//    Получение рецепта
    public Recipe GetRecipe() {
        return flagPillarRecipe;
    }
//    Иницилизация предмета
    void InitItem() {
        flagPillarKey = new NamespacedKey(plugin, "flag_pillar");

        flagPillar = new ItemStack(Material.getMaterial(plugin.GetCustomItemMaterial("flagPillar")));

        ItemMeta flagPillarMeta = flagPillar.getItemMeta();

        flagPillarMeta.setDisplayName(plugin.GetCustomItemName("flagPillar"));
        flagPillarMeta.setLore(plugin.GetCustomItemLore("flagPillar"));

        flagPillar.setItemMeta(flagPillarMeta);
    }
}
