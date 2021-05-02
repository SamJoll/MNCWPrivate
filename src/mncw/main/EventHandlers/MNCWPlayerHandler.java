package mncw.main.EventHandlers;

import mncw.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MNCWPlayerHandler implements Listener {

//    Главный класс плагина
    Main plugin;

//    Конструктор класса
    public MNCWPlayerHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!plugin.isPlayerExists(player.getUniqueId())) {
            plugin.CreatePlayerYmlData(player.getUniqueId());
        } else {
            return;
        }
    }
}
