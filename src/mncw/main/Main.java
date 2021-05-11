package mncw.main;

import mncw.main.CustomItems.Flag;
import mncw.main.CustomItems.FlagBasic;
import mncw.main.CustomItems.FlagCloth;
import mncw.main.CustomItems.FlagPillar;
import mncw.main.EventHandlers.MNCWPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

///--------------------------------------------------------------\
//|                                                              |
//| Плагин представляет из себя альтернативу приватам на сервере |
//|                                                              |
//\--------------------------------------------------------------/

/*==========ГЛАВНЫЙ КЛАСС ПЛАГИНА==========*/
public class Main extends JavaPlugin {
    //Доступ к консоли майнкрафта
    Logger log = Logger.getLogger("Minecraft");

//    Папка плагина
    final String pluginFolderPath = "plugins/MNCWPrivate/";
//    Путь до конфига плагина
    final String pluginConfigPath = pluginFolderPath + "config.yml";
//    Путь до данных флагов
    public final String flagsDataFolderPath = pluginFolderPath + "Flags/";
//    Путь до данных игроков
    public final String playersDataFolderPath = pluginFolderPath + "Players/";

//    СОЗДАНИЕ ПАПКИ ПЛАГИНА
    void CreatePluginFolder() {
//        Папка плагина
        File pluginFolderFile = new File(pluginFolderPath);

        if(!pluginFolderFile.exists()) {
            pluginFolderFile.mkdirs();
        }
    }
//    СОЗДАНИЕ КОНФИГА ПЛАГИНА
    void CreateCustomConfig() {
//        Файл конфига
        File customConfigFile = new File(pluginConfigPath);
//        Конфиг
        FileConfiguration config = YamlConfiguration.loadConfiguration(customConfigFile);

        if(!customConfigFile.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }
//    СОЗДАНИЕ ДИРЕКТОРИЙ ДАННЫХ
    void CreateDataFolders() {
//        Папка данных флагов
        File flagsDataFile = new File(flagsDataFolderPath);
//        Папка данных игроков
        File playersDataFile = new File(playersDataFolderPath);

        if(!playersDataFile.exists() || !flagsDataFile.exists()) {
            flagsDataFile.mkdir();
            playersDataFile.mkdir();
        }
    }

//    Проверка на то, записан ли игрок в данные
    public boolean isPlayerExists(UUID uuid) {
        File playerDataFile = new File(playersDataFolderPath + uuid.toString() + ".yml");

        if(playerDataFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
//    Добавление данных игрока
    public void CreatePlayerYmlData(UUID uuid) {
//        Файл данных игрока
        File playerDataFile = new File(playersDataFolderPath + uuid.toString() + ".yml");

        try {
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
            playerData.set("flagId", "none");
            playerData.set("role", "none");
            playerData.save(playerDataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    Проверка на существование флага с паттерном flagPattern
    public boolean isFlagExists(String flagPattern) {
//        Файл данных флага
        File flagDataFile = new File(flagsDataFolderPath + flagPattern + ".yml");

        return flagDataFile.exists();
    }
//    Добавление нового флага
    public void AddNewFlag(String flagPattern, Player player, Location flagPos) {
//        ID для флага
        UUID uniqueFlagId = UUID.randomUUID();
//        Файл данных флага
        File flagDataFile = new File(flagsDataFolderPath + uniqueFlagId.toString() + ".yml");

        try {
            YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);
            flagData.createSection("flagId");
            flagData.set("flagId", uniqueFlagId.toString());
            flagData.createSection("flagPattern");
            flagData.set("flagPattern", flagPattern);
            flagData.createSection("members");
            flagData.createSection("coordinates");
            flagData.createSection("coordinates.x");
            flagData.set("coordinates.x", flagPos.getBlockX());
            flagData.createSection("coordinates.y");
            flagData.set("coordinates.y", flagPos.getBlockY());
            flagData.createSection("coordinates.z");
            flagData.set("coordinates.z", flagPos.getBlockZ());
            flagData.createSection("roles");
            flagData.createSection("roles.head");
            flagData.createSection("roles.head.all-access");
            flagData.set("roles.head.all-access", "true");
            flagData.save(flagDataFile);
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        SetPlayerFlag(uniqueFlagId.toString(), player, "head");
        AddFlagMember(uniqueFlagId.toString(), player);
    }
//    Получение файла флага с помощью узора
    public File GetFlagDataFileWithPattern(String flagPattern) {
        for(File flagDataFile : new File(flagsDataFolderPath).listFiles()) {
            try {
//                Данные флага
                YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);
                if(flagData.get("flagPattern").equals(flagPattern)) {
                    return flagDataFile;
                } else {
                    continue;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    //    Установление флага игрока
    public void SetPlayerFlag(String flagId, Player player, String role) {
//        Файл данных игрока
        File playerDataFile = new File(playersDataFolderPath + player.getUniqueId().toString() + ".yml");

        try {
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
            playerData.set("flagId", flagId);
            playerData.set("role", role);
            playerData.save(playerDataFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
//    Удаление флага игрока
    public void RemovePlayerFlag(Player player) {
//        Файл данных игрока
        File playerDataFile = new File(playersDataFolderPath + player.getUniqueId().toString() + ".yml");

        try {
//            Данные игрока
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
            playerData.set("flagId", "none");
            playerData.set("role", "none");
            playerData.save(playerDataFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
//    Добавление участника флага
    public void AddFlagMember(String flagId, Player player) {
//        Файл данных флага
        File flagDataFile = new File(flagsDataFolderPath + flagId + ".yml");

        try {
//            Данные флага
            YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);
//            Список участников флага
            List<String> membersList = flagData.getStringList("members");
            membersList.add(player.getName());
            flagData.set("members", membersList);
            flagData.save(flagDataFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
//    Удаление участника флага
    public void RemoveFlagMember(String flagId, Player player) {
//        Файл данных флага
        File flagDataFile = new File(flagsDataFolderPath + flagId + ".yml");

        try {
//            Данные флага
            YamlConfiguration flagData = YamlConfiguration.loadConfiguration(flagDataFile);
            List<String> membersList = flagData.getStringList("members");
            membersList.remove(player.getName());
            flagData.set("members", membersList);
            flagData.save(flagDataFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

//    Получение материала предмета
    public String GetCustomItemMaterial(String itemID) {
        return getConfig().getString("custom-items." + itemID + ".material");
    }
//    Получение имени предмета
    public String GetCustomItemName(String itemID) {
        return getConfig().getString("custom-items." + itemID + ".name");
    }
//    Получение описания предмета
    public List<String> GetCustomItemLore(String itemID) {
        return getConfig().getStringList("custom-items." + itemID + ".lore");
    }


//    Активация плагина
    @Override
    public void onEnable() {
        CreatePluginFolder();
        CreateCustomConfig();
        CreateDataFolders();

        Bukkit.getPluginManager().registerEvents(new MNCWPlayerHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new FlagBasic(this), this);
        Bukkit.getPluginManager().registerEvents(new FlagCloth(this), this);
        Bukkit.getPluginManager().registerEvents(new Flag(this), this);

        Bukkit.addRecipe(new FlagPillar(this).GetRecipe());
        Bukkit.addRecipe(new FlagBasic(this).GetRecipe());
        Bukkit.addRecipe(new FlagCloth(this).GetRecipe());
        Bukkit.addRecipe(new Flag(this).GetRecipe());

        log.info(ColorCode.ANSI_CYAN + "[MNCWPrivate] Plugin is enable!" + ColorCode.ANSI_RESET);
    }
//    Отключение плагина
    @Override
    public void onDisable() {
        log.info(ColorCode.ANSI_RED + "[MNCWPrivate] Plugin is disable!" + ColorCode.ANSI_RESET);
    }
}
