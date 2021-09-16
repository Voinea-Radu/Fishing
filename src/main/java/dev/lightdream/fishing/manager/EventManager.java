package dev.lightdream.fishing.manager;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.fishing.Main;
import dev.lightdream.fishing.files.dto.Loot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.HashMap;

public class EventManager implements Listener {

    private final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        //todo remove the comments
        //if(!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)){
        //    return;
        //}

        User user = Main.instance.databaseManager.getUser(event.getPlayer());
        Loot loot = Main.instance.config.getRandomLoot();
        loot.giveLoot(user);
        Main.instance.getMessageManager().sendMessage(user, new MessageBuilder(loot.getCatchText()).addPlaceholders(new HashMap<String, String>() {{
            put("item", loot.item == null ? "" : loot.item.displayName);
            put("count", loot.item == null ? "" : String.valueOf(loot.item.amount));
            put("money_amount", String.valueOf(loot.money));
            put("xp_amount", String.valueOf(loot.xp));
            put("fish", loot.fish == null ? "" : loot.fish.type.toString());
            put("rarity", loot.fish == null ? "" : loot.fish.rarity.toString());
        }}));
        event.setCancelled(true);
    }


}
