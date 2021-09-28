package dev.lightdream.fishing.manager;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.fishing.Main;
import dev.lightdream.fishing.files.dto.Loot;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EventManager implements Listener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            return;
        }

        User user = Main.instance.databaseManager.getUser(event.getPlayer());

        if(user.getPlayer()==null){
            return;
        }
        Loot loot = Main.instance.config.randomLoot(user.getPlayer().getItemInHand().getEnchantments().getOrDefault(Enchantment.LUCK, 0));
        loot.giveLoot(user);
        Main.instance.getMessageManager().sendMessage(user, new MessageBuilder(loot.catchText()).addPlaceholders(new HashMap<String, String>() {{
            put("item", loot.item == null ? "" : loot.item.displayName);
            put("count", loot.item == null ? "" : String.valueOf(loot.item.amount));
            put("money_amount", String.valueOf(loot.money));
            put("xp_amount", String.valueOf(loot.xp));
            put("fish", loot.fish == null ? "" : loot.fish.type.toString());
            put("rarity", loot.fish == null ? "" : loot.fish.rarity.toString());
        }}));
        ItemStack is = user.getPlayer().getItemInHand();
        event.getHook().remove();
        //user.getPlayer().setItemInHand(null);
        //Bukkit.getScheduler().runTaskLater(plugin,()->user.getPlayer().setItemInHand(is),1);
        event.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessageSend(AsyncPlayerChatEvent event){
        if(event.getMessage().startsWith("/fish sell")){
            if(event.getPlayer().hasPermission("fish.sell.bypass")){
                event.setCancelled(false);
            }
        }
    }


}
