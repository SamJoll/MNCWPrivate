package mncw.main.CustomItems;

import mncw.main.Main;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Flag implements Listener {
//    Главный класс плагина
    Main plugin;

//    Класс предмета FlagCloth
    FlagCloth flagCloth;
//    Класс предмета FlagBasic
    FlagBasic flagBasic;

//    Предмет
    ItemStack flag;
//    Ключ предмета
    NamespacedKey flagKey;
//    Рецепт крафта
    ShapelessRecipe flagRecipe;

//    Конструктор класса
    public Flag(Main plugin) {
        this.plugin = plugin;
        flagBasic = new FlagBasic(plugin);
        flagCloth = new FlagCloth(plugin);

        InitItem();
        SetRecipe();
    }

//    Создание рецепта
    void SetRecipe() {
        flagRecipe = new ShapelessRecipe(flagKey, flag);
        flagRecipe.addIngredient(Material.GOLDEN_HELMET);
        flagRecipe.addIngredient(flagCloth.GetItem().getType());
        flagRecipe.addIngredient(flagBasic.GetItem().getType());
    }
    public Recipe GetRecipe() {
        return flagRecipe;
    }
//    Иницилизация предмета
    void InitItem() {
        flagKey = new NamespacedKey(plugin, "flag");

        flag = new ItemStack(Material.getMaterial(plugin.GetCustomItemMaterial("flag")));

        flag.addUnsafeEnchantment(Enchantment.LURE, 0);

        ItemMeta flagMeta = flag.getItemMeta();

        flagMeta.setDisplayName(plugin.GetCustomItemName("flag"));
        flagMeta.setLore(plugin.GetCustomItemLore("flag"));

        flagMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        flag.setItemMeta(flagMeta);
    }

    @EventHandler
    void PrepareFlagCraftingEvent(PrepareItemCraftEvent event) {
//        Текущий рецепт
        Recipe recipe = event.getRecipe();
//        Предметы в верстаке
        ItemStack[] craftingSlots = event.getInventory().getMatrix();

        if(recipe instanceof Keyed) {
            if(((Keyed)flagRecipe).getKey().equals(((Keyed)recipe).getKey())) {
//                Проверка на предметы в крафте
                boolean isFlagBasic = false;
                boolean isFlagCloth = false;

//                Результат крафта
                ItemStack newFlag = flag;
                ItemMeta newFlagMeta = newFlag.getItemMeta();

//                Перебор предметов для крафта
                for(ItemStack item : craftingSlots) {
                    if(item != null) {
                        if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "pillarBlockId"), PersistentDataType.STRING)) {
                            newFlagMeta.getPersistentDataContainer().set(
                                    new NamespacedKey(plugin, "pillarBlockId"),
                                    PersistentDataType.STRING,
                                    item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "pillarBlockId"), PersistentDataType.STRING)
                            );

                            isFlagBasic = true;
                        } else if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "woolsId"), PersistentDataType.STRING)) {
                            newFlagMeta.getPersistentDataContainer().set(
                                    new NamespacedKey(plugin, "woolsId"),
                                    PersistentDataType.STRING,
                                    item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "woolsId"), PersistentDataType.STRING)
                            );

                            isFlagCloth = true;
                        }
                    }
                }

                newFlagMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "isFlag"), PersistentDataType.STRING, "true");

                newFlag.setItemMeta(newFlagMeta);

                if(isFlagBasic && isFlagCloth) {
                    event.getInventory().setResult(newFlag);
                } else {
                    event.getInventory().setResult(null);
                }
            }
        }
    }

    @EventHandler
    void PlaceFlagEvent(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null) {
//            Предмет, с которым было взаимодействие
            ItemStack usedItem = event.getItem();
            ItemMeta usedItemMeta = usedItem.getItemMeta();

            if(event.getHand() == EquipmentSlot.HAND && usedItemMeta.getPersistentDataContainer().has(new NamespacedKey(plugin, "isFlag"), PersistentDataType.STRING)) {
                usedItem.setAmount(usedItem.getAmount()-1);

                event.setCancelled(true);
            }
        }
    }
}
