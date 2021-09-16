package dev.lightdream.fishing.files.dto;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.files.dto.XMaterial;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.NbtUtils;
import dev.lightdream.fishing.Main;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class Fish {

    public FishType type;
    public FishRarity rarity;
    private boolean cooked;
    private int amount;

    public Fish(FishType type, FishRarity rarity, boolean cooked) {
        this.type = type;
        this.rarity = rarity;
        this.cooked = cooked;
        this.amount = 1;
    }

    @SuppressWarnings("ConstantConditions")
    public static Fish of(ItemStack item) {
        Fish fish = new Fish();
        if (item == null) {
            return null;
        }
        if (NbtUtils.getNBT(item, "type") == null ||
                NbtUtils.getNBT(item, "rarity") == null ||
                NbtUtils.getNBT(item, "cooked") == null) {
            return null;
        }
        fish.type = FishType.valueOf((String) NbtUtils.getNBT(item, "type"));
        fish.rarity = FishRarity.valueOf((String) NbtUtils.getNBT(item, "rarity"));
        fish.cooked = (boolean) NbtUtils.getNBT(item, "cooked");
        fish.amount = item.getAmount();
        if (fish.type == null ||
                fish.rarity == null) {
            return null;
        }
        return fish;
    }

    @SuppressWarnings("unchecked")
    public void give(User user) {
        if (!user.isOnline()) {
            return;
        }

        Item item = cooked ? new Item(type.cooked) : new Item(type.raw);

        item.nbtTags = new HashMap<String, Object>() {{
            put("type", type.toString());
            put("rarity", rarity.toString());
            put("cooked", cooked);
        }};
        item.lore = (List<String>) new MessageBuilder(Main.instance.config.fishLootLore).addPlaceholders(new HashMap<String, String>() {{
            put("rarity", rarity.toString());
        }}).parse();

        user.getPlayer().getInventory().addItem(item.parseItem());
    }

    public double sell(User user) {
        double price = Main.instance.config.fishes.get(type).amount;
        if (cooked) {
            price *= Main.instance.config.cookedVersionMultiplier;
        }
        price *= Main.instance.config.fishRarities.get(rarity).amount;
        price *= amount;
        user.addMoney(price);
        user.getPlayer().setItemInHand(null);
        //itemStack.setType(Material.AIR);
        return price;
    }

    @Override
    public String toString() {
        return "Fish{" +
                "type=" + type +
                ", rarity=" + rarity +
                '}';
    }

    public enum FishType {

        SALMON(XMaterial.SALMON, XMaterial.COOKED_SALMON),
        TROPICAL(XMaterial.TROPICAL_FISH, null),
        COD(XMaterial.COD, XMaterial.COOKED_COD),
        PUFFERFISH(XMaterial.PUFFERFISH, null);

        private final XMaterial raw;
        private final XMaterial cooked;

        FishType(XMaterial raw, XMaterial cooked) {
            this.raw = raw;
            this.cooked = cooked;
        }
    }

    public enum FishRarity {

        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FishConfig {
        public double amount;
        public double chance;
    }
}
