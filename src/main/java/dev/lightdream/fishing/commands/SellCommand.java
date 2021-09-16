package dev.lightdream.fishing.commands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.fishing.Main;
import dev.lightdream.fishing.files.dto.Fish;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SellCommand extends Command {
    public SellCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("sell"), "", "", true, false, "");
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list) {
        User user = api.getDatabaseManager().getUser(commandSender);
        Fish fish = Fish.of(user.getPlayer().getItemInHand());
        if (fish == null) {
            api.getMessageManager().sendMessage(user, Main.instance.lang.invalidFish);
            return;
        }
        api.getMessageManager().sendMessage(user, new MessageBuilder(Main.instance.lang.soldFish).addPlaceholders(new HashMap<String, String>() {{
            put("amount", String.valueOf(fish.sell(user)));
        }}));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return new ArrayList<>();
    }
}
