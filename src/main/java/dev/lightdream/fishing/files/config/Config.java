package dev.lightdream.fishing.files.config;

import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.files.dto.XMaterial;
import dev.lightdream.api.utils.Utils;
import dev.lightdream.fishing.files.dto.Fish;
import dev.lightdream.fishing.files.dto.Loot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Config extends dev.lightdream.api.files.config.Config {

    public HashMap<Loot, Double> lootTable = new HashMap<Loot, Double>() {{
        put(new Loot(new Item(XMaterial.STONE)), 100.0);
        put(new Loot(100.0), 100.0);
        put(new Loot(100), 100.0);
        put(new Loot(new Fish(Fish.FishType.COD, Fish.FishRarity.COMMON, false)), 100.0);
        put(new Loot("say Hello"), 100.0);
    }};

    public List<String> fishLootLore = Arrays.asList(
            "Rarity %rarity%",
            "",
            "This fish is a loot",
            "If you are going to cook this in a furnace",
            "it is going to lose its value on market"
    );

    public HashMap<Fish.FishType, Fish.FishConfig> fishes = new HashMap<Fish.FishType,Fish.FishConfig>(){{
        put(Fish.FishType.TROPICAL, new Fish.FishConfig(1000.0, 25));
        put(Fish.FishType.COD, new Fish.FishConfig(1000.0, 25));
        put(Fish.FishType.PUFFERFISH, new Fish.FishConfig(1000.0, 25));
        put(Fish.FishType.SALMON, new Fish.FishConfig(1000.0, 25));
    }};

    public Double cookedVersionMultiplier = 2.0;

    public HashMap<Fish.FishRarity, Fish.FishConfig> fishRarities = new HashMap<Fish.FishRarity, Fish.FishConfig>(){{
        put(Fish.FishRarity.COMMON, new Fish.FishConfig(1.0, 40));
        put(Fish.FishRarity.UNCOMMON, new Fish.FishConfig(1.5, 30));
        put(Fish.FishRarity.RARE, new Fish.FishConfig(2.0, 15));
        put(Fish.FishRarity.EPIC, new Fish.FishConfig(2.5,10));
        put(Fish.FishRarity.LEGENDARY, new Fish.FishConfig(3.0, 5));
    }};

    public Loot getRandomLoot() {
        AtomicReference<Double> maxChances = new AtomicReference<>(0.0);
        lootTable.forEach((loot, chance) -> maxChances.updateAndGet(v -> v + chance));
        double rnd = Utils.generateRandom(0, maxChances.get());

        for (Loot loot : lootTable.keySet()) {
            double chance = lootTable.get(loot);
            if (rnd <= chance) {
                return loot;
            } else {
                rnd -= chance;
            }
        }
        return null;
    }

    public Fish.FishType getRandomFish() {
        AtomicReference<Double> maxChances = new AtomicReference<>(0.0);
        fishes.forEach((loot, config) -> maxChances.updateAndGet(v -> v + config.chance));
        double rnd = Utils.generateRandom(0, maxChances.get());

        for (Fish.FishType fishType : fishes.keySet()) {
            Fish.FishConfig config = fishes.get(fishType);
            if (config.chance <= rnd) {
                return fishType;
            } else {
                rnd -= config.chance;
            }
        }
        return null;
    }

    public Fish.FishRarity getRandomRarity() {
        AtomicReference<Double> maxChances = new AtomicReference<>(0.0);
        fishRarities.forEach((loot, config) -> maxChances.updateAndGet(v -> v + config.chance));
        double rnd = Utils.generateRandom(0, maxChances.get());

        for (Fish.FishRarity fishRarity : fishRarities.keySet()) {
            Fish.FishConfig config = fishRarities.get(fishRarity);
            if (config.chance <= rnd) {
                return fishRarity;
            } else {
                rnd -= config.chance;
            }
        }
        return null;
    }

}
