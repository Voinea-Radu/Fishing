package dev.lightdream.fishing.files.dto;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.files.dto.Serializable;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.fishing.Main;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Objects;

public class Loot extends Serializable implements java.io.Serializable {

    //todo key deserializer
    public String type;
    public Item item;
    public double money;
    public int xp;
    public Fish fish;
    public String command;

    public Loot(Item item) {
        this.item = item;
        this.type = "item";
    }

    public Loot(double money) {
        this.money = money;
        this.type = "money";
    }

    public Loot(int xp) {
        this.xp = xp;
        this.type = "xp";
    }

    public Loot(Fish fish) {
        this.fish = fish;
        this.type = "fish";
    }

    public Loot(String command) {
        this.command = command;
        this.type = "command";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loot loot = (Loot) o;
        return Double.compare(loot.money, money) == 0 && xp == loot.xp && Objects.equals(type, loot.type) && Objects.equals(item, loot.item) && Objects.equals(fish, loot.fish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item, money, xp, fish);
    }

    @Override
    public String toString() {
        return "Loot{" +
                "type='" + type + '\'' +
                ", item=" + item +
                ", money=" + money +
                ", xp=" + xp +
                ", fish=" + fish +
                '}';
    }

    public void giveLoot(User user) {
        switch (type) {
            case "item":
                user.getPlayer().getInventory().addItem(this.item.parseItem());
                break;
            case "money":
                user.addMoney(this.money);
                break;
            case "xp":
                user.addXP(this.xp);
                break;
            case "fish":
                this.fish.give(user);
                break;
            case "command":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) new MessageBuilder(command).addPlaceholders(new HashMap<String, String>(){{
                    put("player", user.name);
                }}).parse());
                break;
        }
    }

    public String catchText(){
            switch (type) {
                case "item":
                    return Main.instance.lang.itemCatch;
                case "money":
                    return Main.instance.lang.moneyCatch;
                case "xp":
                    return Main.instance.lang.xpCatch;
                case "fish":
                    return Main.instance.lang.fishCatch;
                case "command":
                    return Main.instance.lang.commandCatch;
        }
        return "";
    }

}
