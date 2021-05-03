package mncw.main.CustomItems;

import mncw.main.Main;
import mncw.main.MaterialsGroups;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FlagCloth implements Listener {
//    Главный класс плагина
    Main plugin;

//    Предмет
    ItemStack flagCloth;
    public ItemStack GetItem() {
        return flagCloth;
    }
//    Ключ предмета
    NamespacedKey flagClothKey;
//    Рецепт
    ShapedRecipe flagClothRecipe;

//    Конструктор
    public FlagCloth(Main plugin) {
        this.plugin = plugin;

        InitItem();
        SetRecipe();
    }

//    Создание рецепта
    void SetRecipe() {
        flagClothRecipe = new ShapedRecipe(flagClothKey, flagCloth);
        flagClothRecipe.shape(
                "WWW",
                "WWW",
                "WWW"
        );
        flagClothRecipe.setIngredient('W', MaterialsGroups.WOOLS);
    }
    public Recipe GetRecipe() {
        return flagClothRecipe;
    }
//    Иницилизация предмета
    void InitItem() {
        flagClothKey = new NamespacedKey(plugin, "flag_cloth");

        flagCloth = new ItemStack(Material.getMaterial(plugin.GetCustomItemMaterial("flagCloth")));

        ItemMeta flagClothMeta = flagCloth.getItemMeta();

        flagClothMeta.setDisplayName(plugin.GetCustomItemName("flagCloth"));
        flagClothMeta.setLore(plugin.GetCustomItemLore("flagCloth"));
        flagClothMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        flagClothMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        flagCloth.setItemMeta(flagClothMeta);
    }

//    Проверка на крафт
    @EventHandler
    void prepareFlagClothCrafting(PrepareItemCraftEvent event) {
//        Текущий рецепт
        Recipe recipe = event.getRecipe();
//        Предметы в верстаке
        ItemStack[] craftingSlots = event.getInventory().getMatrix();

        if(recipe instanceof Keyed) {
            if(((Keyed)flagClothRecipe).getKey().equals(((Keyed)recipe).getKey())) {
//                Результат крафта
                ItemStack newFlagCloth = flagCloth;

//                Список айди шерстей
                StringBuilder woolsID = new StringBuilder();

//                Перебор предметов для крафта
                for(ItemStack item : craftingSlots) {
                    woolsID.append(item.getType().name() + "-");
                }

                ItemMeta newFlagClothMeta = newFlagCloth.getItemMeta();

                newFlagClothMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "woolsId"), PersistentDataType.STRING, woolsID.toString());

                newFlagCloth.setItemMeta(newFlagClothMeta);

                event.getInventory().setResult(newFlagCloth);
            }
        }
    }
}
