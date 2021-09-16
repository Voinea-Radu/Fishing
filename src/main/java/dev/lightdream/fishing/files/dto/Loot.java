package dev.lightdream.fishing.files.dto;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.files.dto.Serializable;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.fishing.Main;
import gnu.trove.impl.hash.TDoubleCharHash;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Objects;

public class Loot extends Serializable implements java.io.Serializable {

    public String type;
    public Item item;
    public double money;
    public int xp;
    public Fish fish;
    public String command;
    public double chance;

    public Loot(Item item, double chance) {
        this.item = item;
        this.type = "item";
        this.chance = chance;
    }

    public Loot(double money, double chance) {
        this.money = money;
        this.type = "money";
        this.chance = chance;
    }

    public Loot(int xp, double chance) {
        this.xp = xp;
        this.type = "xp";
        this.chance =chance;
    }

    public Loot(Fish fish, double chance) {
        this.fish = fish;
        this.type = "fish";
        this.chance = chance;
    }

    public Loot(String command, double chance) {
        this.command = command;
        this.type = "command";
        this.chance = chance;
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
