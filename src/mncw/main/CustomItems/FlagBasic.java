package mncw.main.CustomItems;

import mncw.main.ColorCode;
import mncw.main.Main;
import mncw.main.MaterialsGroups;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class FlagBasic implements Listener {
//    Главный класс плагина
    Main plugin;
//    Класс предмета Flag Pillar
    FlagPillar flagPillar;

//    Предмет
    ItemStack flagBasic;
//    Ключ предмета
    NamespacedKey flagBasicKey;
//    Рецепт
    ShapelessRecipe flagBasicRecipe;

//    Конструктор класса
    public FlagBasic(Main plugin) {
        this.plugin = plugin;
        flagPillar = new FlagPillar(plugin);

        InitItem();
        SetRecipe();
    }
//    Создание рецепта
    void SetRecipe() {
        flagBasicRecipe = new ShapelessRecipe(flagBasicKey, flagBasic);
        flagBasicRecipe.addIngredient(flagPillar.flagPillar.getType());
        flagBasicRecipe.addIngredient(MaterialsGroups.PILLARS);
    }
//    Получение рецепта
    public Recipe GetRecipe() {
        return flagBasicRecipe;
    }
//    Иницилизация предмета
    void InitItem() {
        flagBasicKey = new NamespacedKey(plugin, "flag_basic");

        flagBasic = new ItemStack(Material.getMaterial(plugin.GetCustomItemMaterial("flagBasic")));

        ItemMeta flagBasicMeta = flagBasic.getItemMeta();

        flagBasicMeta.setDisplayName(plugin.getConfig().getString("custom-items.flagBasic.name"));
        flagBasicMeta.setLore(plugin.getConfig().getStringList("custom-items.flagBasic.lore"));

        flagBasic.setItemMeta(flagBasicMeta);
    }

//    Проверка на крафт
    @EventHandler
    void prepareFlagBasicCrafting(PrepareItemCraftEvent event) {
//        Текущий рецепт
        Recipe recipe = event.getRecipe();
//        Предметы в верстаке
        ItemStack[] craftingSlots = event.getInventory().getMatrix();

        if(recipe instanceof Keyed) {
            if(((Keyed)flagBasicRecipe).getKey().equals(((Keyed) recipe).getKey())) {

//                Проверка на предметы в крафте
                boolean isFlagPillar = false;
                boolean isLog = false;

//                Перебор предметов для крафта
                for(ItemStack item : craftingSlots) {
                    if(flagPillar.GetItem().isSimilar(item)) {
                        isFlagPillar = true;

                        System.out.println(ColorCode.ANSI_CYAN + "FlagPillar" + ColorCode.ANSI_RESET);
                    }
                    if(item != null && MaterialsGroups.PILLARS.getChoices().contains((item.getType()))) {
                        isLog = true;

                        System.out.println(ColorCode.ANSI_CYAN + "Log" + ColorCode.ANSI_RESET);
                    }
                }

                if(isLog && isFlagPillar) {
                    event.getInventory().setResult(flagBasic);
                } else {
                    event.getInventory().setResult(null);
                }
            }
        }
    }
}
