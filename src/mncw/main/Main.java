package mncw.main;

import mncw.main.CustomItems.Flag;
import mncw.main.CustomItems.FlagBasic;
import mncw.main.CustomItems.FlagCloth;
import mncw.main.CustomItems.FlagPillar;
import mncw.main.EventHandlers.MNCWPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    final String flagsDataFolderPath = pluginFolderPath + "Flags/";
//    Путь до данных игроков
    final String playersDataFolderPath = pluginFolderPath + "Players/";

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
            playerData.set("flagID", "none");
            playerData.set("role", "none");
            playerData.save(playerDataFile);
        } catch (Exception e) {
            e.printStackTrace();
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
