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

    public List<Loot> lootTable = Arrays.asList(
            new Loot(new Item(XMaterial.STONE), Arrays.asList(100.0, 100.0, 100.0, 100.0)),
            new Loot(100.0, Arrays.asList(100.0, 100.0, 100.0, 100.0)),
            new Loot(100, Arrays.asList(100.0, 100.0, 100.0, 100.0)),
            new Loot(new Fish(Fish.FishType.COD, Fish.FishRarity.COMMON, false), Arrays.asList(100.0, 100.0, 100.0, 100.0)),
            new Loot("say Hello", Arrays.asList(100.0, 100.0, 100.0, 100.0))
    );

    public List<String> fishLootLore = Arrays.asList(
            "Rarity %rarity%",
            "",
            "This fish is a loot",
            "If you are going to cook this in a furnace",
            "it is going to lose its value on market"
    );

    public HashMap<Fish.FishType, Double> fishes = new HashMap<Fish.FishType, Double>() {{
        put(Fish.FishType.TROPICAL, 1000.0);
        put(Fish.FishType.COD, 1000.0);
        put(Fish.FishType.PUFFERFISH, 1000.0);
        put(Fish.FishType.SALMON, 1000.0);
    }};

    public Double cookedVersionMultiplier = 2.0;

    public HashMap<Fish.FishRarity, Double> fishRarities = new HashMap<Fish.FishRarity, Double>() {{
        put(Fish.FishRarity.COMMON, 1.0);
        put(Fish.FishRarity.UNCOMMON, 1.5);
        put(Fish.FishRarity.RARE, 2.0);
        put(Fish.FishRarity.EPIC, 2.5);
        put(Fish.FishRarity.LEGENDARY, 3.0);
    }};

    public Loot randomLoot(int luckOfTheSea) {
        AtomicReference<Double> maxChances = new AtomicReference<>(0.0);
        lootTable.forEach(loot -> maxChances.updateAndGet(v -> v + loot.chance.get(luckOfTheSea)));
        double rnd = Utils.generateRandom(0, maxChances.get());

        for (Loot loot : lootTable) {
            double chance = loot.chance.get(luckOfTheSea);
            if (rnd <= chance) {
                return loot;
            } else {
                rnd -= chance;
            }
        }
        return null;
    }
}
