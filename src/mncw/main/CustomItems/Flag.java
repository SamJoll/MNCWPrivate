package mncw.main.CustomItems;

import com.sun.istack.internal.NotNull;
import mncw.main.Main;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

//    Дистанция защиты флага
    final int flagProtectDistance = 3;

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

//    Функция размещения флага
    Runnable SetFlag(Player player, ItemStack flag, Location flagLoc, @NotNull String flagPattern) {
//        Координаты начала флага
        Location fPos = flagLoc;

//        Информация о флаге
        ItemMeta newFlagMeta = flag.getItemMeta();
//        Блок для столба
        Material pillarMaterial = Material.getMaterial(newFlagMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "pillarBlockId"), PersistentDataType.STRING));
//        Список блоков флага
        ArrayList<Material> woolsMaterial = new ArrayList<Material>();

        for(String woolID : newFlagMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "woolsId"), PersistentDataType.STRING).split("-")) {
            woolsMaterial.add(Material.getMaterial(woolID));
        }


        return new Runnable() {
            @Override
            public void run() {
//                Индекс блока флага
                int blockIndex = 0;
//                Файл данных флага
                File flagDataFile = plugin.GetFlagDataFileWithPattern(flagPattern);

                try {
//                    Данные флага
                    YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);

                    flagData.createSection("flag-blocks");

                    for (int pI = 0; pI < 6; pI++) {
                        fPos.add(0, 1, 0);
                        player.getWorld().getBlockAt(fPos).setType(pillarMaterial);

                        blockIndex = pI;
                        flagData.createSection("flag-blocks.block-" + blockIndex);
                        flagData.createSection("flag-blocks.block-" + blockIndex + ".material");
                        flagData.set("flag-blocks.block-" + blockIndex + ".material", player.getWorld().getBlockAt(fPos).getType().name());
                        flagData.createSection("flag-blocks.block-" + blockIndex + ".coordinates");
                        flagData.set("flag-blocks.block-" + blockIndex + ".coordinates", fPos.toVector());
                    }

                    player.getWorld().getBlockAt(fPos.add(0, 1, 0)).setType(Material.GOLD_BLOCK);

                    blockIndex++;
                    flagData.createSection("flag-blocks.block-" + blockIndex);
                    flagData.createSection("flag-blocks.block-" + blockIndex + ".material");
                    flagData.set("flag-blocks.block-" + blockIndex + ".material", player.getWorld().getBlockAt(fPos).getType().name());
                    flagData.createSection("flag-blocks.block-" + blockIndex + ".coordinates");
                    flagData.set("flag-blocks.block-" + blockIndex + ".coordinates", fPos.toVector());

                    fPos.add(0, -1, 0);
                    for (int cI = 1; cI <= 9; cI++) {
                        fPos.add(1, 0, 0);

                        player.getServer().getWorld(player.getWorld().getName()).getBlockAt(fPos).setType(woolsMaterial.get(cI - 1));

                        blockIndex++;
                        flagData.createSection("flag-blocks.block-" + blockIndex);
                        flagData.createSection("flag-blocks.block-" + blockIndex + ".material");
                        flagData.set("flag-blocks.block-" + blockIndex + ".material", player.getWorld().getBlockAt(fPos).getType().name());
                        flagData.createSection("flag-blocks.block-" + blockIndex + ".coordinates");
                        flagData.set("flag-blocks.block-" + blockIndex + ".coordinates", fPos.toVector());

                        if (cI % 3 == 0) {
                            fPos.add(-3, -1, 0);
                        }
                    }

                    flagData.save(flagDataFile);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }
    //    Разрушение флага
    public void DestroyFlag(String flagId, Player player) {
//        Файл данных флага
        File flagDataFile = new File(plugin.flagsDataFolderPath + flagId + ".yml");

        try {
//            Данные флага
            YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);

            for(int bI = 0; bI < flagData.getConfigurationSection("flag-blocks").getKeys(false).size(); bI++) {
                int x = flagData.getVector("flag-blocks.block-" + bI + ".coordinates").getBlockX();
                int y = flagData.getVector("flag-blocks.block-" + bI + ".coordinates").getBlockY();
                int z = flagData.getVector("flag-blocks.block-" + bI + ".coordinates").getBlockZ();
                player.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
//                Узор флага
                String flagPattern = usedItemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "woolsId"), PersistentDataType.STRING);
//                Информация о флаге
                File flagDataFile = plugin.GetFlagDataFileWithPattern(flagPattern);
//                Информация о игроке
                File playerDataFile = new File(plugin.playersDataFolderPath + event.getPlayer().getUniqueId().toString() + ".yml");

                if(!playerDataFile.exists()) {
                    event.getPlayer().sendMessage(plugin.getConfig().getString("messages.exceptions.error"));
                    event.setCancelled(true);
                    return;
                }

                YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);

                if(playerData.get("flagId").equals("none")) {
                    if(flagDataFile == null || !flagDataFile.exists()) {
                        for(File fDF : new File(plugin.flagsDataFolderPath).listFiles()) {
                            try {
//                                Данные флага
                                YamlConfiguration fD = YamlConfiguration.loadConfiguration(fDF);
//                                Расположение флага
                                Location flagCoordinates = new Location(event.getPlayer().getWorld(),
                                        fD.getDouble("coordinates.x"),
                                        fD.getDouble("coordinates.y"),
                                        fD.getDouble("coordinates.z")
                                        );
                                if(flagCoordinates.getChunk() == event.getClickedBlock().getLocation().getChunk()) {
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(plugin.getConfig().getString("messages.exceptions.chunk-busy"));
                                    return;
                                }
//                                Функция для реализации отступа от границы чанка
//                                int xBorder1 = flagCoordinates.getChunk().getBlock(0, 0, 0).getLocation().getBlockX();
//                                int zBorder1 = flagCoordinates.getChunk().getBlock(0, 0, 0).getLocation().getBlockZ();
//                                int xBorder2 = flagCoordinates.getChunk().getBlock(15, 0, 15).getLocation().getBlockX();
//                                int zBorder2 = flagCoordinates.getChunk().getBlock(15, 0, 15).getLocation().getBlockZ();
//                                int xPlacedBlock = event.getClickedBlock().getLocation().getBlockX();
//                                int zPlacedBlock = event.getClickedBlock().getLocation().getBlockZ();
//
//                                if(
//                                        (xBorder1-xPlacedBlock)*(xBorder1-xPlacedBlock) < 9 ||
//                                        (xBorder2-xPlacedBlock)*(xBorder2-xPlacedBlock) < 9 ||
//                                        (zBorder1-zPlacedBlock)*(zBorder1-zPlacedBlock) < 9 ||
//                                        (zBorder2-zPlacedBlock)*(zBorder2-zPlacedBlock) < 9)
//                                {
//                                    event.setCancelled(true);
//                                    return;
//                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        event.setCancelled(true);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, SetFlag(event.getPlayer(), usedItem, event.getClickedBlock().getLocation(), flagPattern), 2L);

                        plugin.AddNewFlag(flagPattern, event.getPlayer(), event.getClickedBlock().getLocation());

                        usedItem.setAmount(usedItem.getAmount() - 1);
                    } else {
                        event.getPlayer().sendMessage(plugin.getConfig().getString("messages.exceptions.flag-exists"));
                    }
                } else {
                    event.getPlayer().sendMessage(plugin.getConfig().getString("messages.exceptions.has-flag"));
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void ProtectChunk(BlockBreakEvent event) {
//        Игрок
        Player player = event.getPlayer();
//        Координаты разрушенного блока
        Location breakBlockCoordinates = event.getBlock().getLocation();
//        Файл данных игрока
        File playerDataFile = new File(plugin.playersDataFolderPath + player.getUniqueId() + ".yml");

        try {
//            Данные игрока
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);

            for (File flagDataFile : new File(plugin.flagsDataFolderPath).listFiles()) {
//                Данные флага
                YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);

//                Id флага
                String flagId = flagData.getString("flagId");
//                Список членов флага
                List<String> membersList = flagData.getStringList("members");
//                Координаты расположения флага
                Location flagCoordinates = new Location(player.getWorld(),
                        flagData.getDouble("coordinates.x"),
                        flagData.getDouble("coordinates.y"),
                        flagData.getDouble("coordinates.z")
                );

                if(flagCoordinates.getChunk() == breakBlockCoordinates.getChunk()) {
                    if(event.getBlock().getType().equals(Material.GOLD_BLOCK)) {
                        event.setCancelled(true);
                        DestroyFlag(flagId, player);
                        return;
                    }
                    if (membersList.contains(player.getName()) && flagId.equals(playerData.getString("flagId"))) {

                        int xSqr = (flagCoordinates.getBlockX() - breakBlockCoordinates.getBlockX())*(flagCoordinates.getBlockX() - breakBlockCoordinates.getBlockX());
                        int zSqr = (flagCoordinates.getBlockZ() - breakBlockCoordinates.getBlockZ())*(flagCoordinates.getBlockZ() - breakBlockCoordinates.getBlockZ());
                        int flagDisSqr = flagProtectDistance*flagProtectDistance;

                        if(xSqr <= flagDisSqr && zSqr <= flagDisSqr) {
                            event.setCancelled(true);
                            player.sendMessage(plugin.getConfig().getString("messages.exceptions.protected-flag-zone"));
                        }
                        return;
                    }
                    event.setCancelled(true);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
