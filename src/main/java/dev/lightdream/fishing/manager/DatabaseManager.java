package dev.lightdream.fishing.manager;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;

public class DatabaseManager extends dev.lightdream.api.managers.DatabaseManager {
    public DatabaseManager(IAPI api) {
        super(api);
        setup(User.class);
    }
}
