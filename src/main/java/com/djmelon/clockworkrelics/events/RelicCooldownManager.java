package com.djmelon.clockworkrelics.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RelicCooldownManager {

    private static final Map<UUID, Map<Item, Long>> COOLDOWNS =
            new HashMap<>();

    private static final Map<UUID, Map<Item, Integer>> COOLDOWN_DURATIONS =
            new HashMap<>();

    // Запускаем перезарядку
    public static void setCooldown(Player player, Item item, int ticks) {
        COOLDOWNS
                .computeIfAbsent(player.getUUID(), uuid -> new HashMap<>())
                .put(item, player.level().getGameTime() + ticks);

        COOLDOWN_DURATIONS
                .computeIfAbsent(player.getUUID(), uuid -> new HashMap<>())
                .put(item, ticks);
    }

    // Получаем оставшееся время
    public static int getRemainingTicks(Player player, Item item) {
        Map<Item, Long> playerCooldowns = COOLDOWNS.get(player.getUUID());

        if (playerCooldowns == null) {
            return 0;
        }

        long endTime = playerCooldowns.getOrDefault(item, 0L);
        long remaining = endTime - player.level().getGameTime();

        return (int) Math.max(0, remaining);
    }

    // Проверяем, идёт ли перезарядка
    public static boolean isOnCooldown(Player player, Item item) {
        return getRemainingTicks(player, item) > 0;
    }

    // Получаем прогресс перезарядки от 0.0 до 1.0
    public static float getCooldownProgress(Player player, Item item) {
        int remaining = getRemainingTicks(player, item);

        Map<Item, Integer> playerDurations =
                COOLDOWN_DURATIONS.get(player.getUUID());

        if (playerDurations == null) {
            return 1.0F;
        }

        int total = playerDurations.getOrDefault(item, 0);

        if (total <= 0) {
            return 1.0F;
        }

        return 1.0F - (float) remaining / total;
    }
}
