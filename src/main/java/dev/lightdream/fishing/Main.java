package dev.lightdream.fishing;

import dev.lightdream.api.API;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.conifgs.SQLConfig;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.managers.MessageManager;
import dev.lightdream.fishing.commands.SellCommand;
import dev.lightdream.fishing.files.config.Config;
import dev.lightdream.fishing.files.config.Lang;
import dev.lightdream.fishing.files.dto.Loot;
import dev.lightdream.fishing.manager.DatabaseManager;
import dev.lightdream.fishing.manager.EventManager;
import dev.lightdream.libs.fasterxml.databind.module.SimpleModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class Main extends LightDreamPlugin {

    public static Main instance;
    public DatabaseManager databaseManager;

    //Settings
    public Config config;
    public Lang lang;

    @Override
    public void onEnable() {
        init("Fishing", "fish", "1.7");
        instance = this;

        new EventManager(this);

        fileManager.registerModule(new SimpleModule().addKeyDeserializer(Loot.class, getKeyDeserializerManager()));
        databaseManager = new DatabaseManager(this);
    }


    @Override
    public @NotNull String parsePapi(OfflinePlayer offlinePlayer, String s) {
        return "";
    }

    @Override
    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
        baseConfig = config;
        lang = fileManager.load(Lang.class, fileManager.getFile(baseConfig.baseLang));
        baseLang = lang;
    }

    @Override
    public void disable() {

    }

    @Override
    public void registerFileManagerModules() {

    }

    @Override
    public void loadBaseCommands() {
        baseSubCommands.add(new SellCommand(this));
    }

    @Override
    public MessageManager instantiateMessageManager() {
        return new MessageManager(this, Main.class);
    }

    @Override
    public void registerLangManager() {
        API.instance.langManager.register(Main.class, getLangs());
    }

    @Override
    public HashMap<String, Object> getLangs() {
        HashMap<String, Object> langs = new HashMap<>();

        baseConfig.langs.forEach(lang -> {
            Lang l = fileManager.load(Lang.class, fileManager.getFile(lang));
            langs.put(lang, l);
        });

        return langs;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void setLang(Player player, String s) {
        User user = databaseManager.getUser(player);
        user.setLang(s);
        databaseManager.save(user);
    }


}
