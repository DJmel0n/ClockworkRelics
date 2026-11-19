package com.djmelon.clockworkrelics.events;

import com.djmelon.clockworkrelics.ClockworkRelics;

import com.djmelon.clockworkrelics.network.RelicNetwork;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import top.theillusivec4.curios.api.CuriosApi;


@EventBusSubscriber(modid = ClockworkRelics.MODID)
public class ChronometerEvents {

    private static final int COOLDOWN_TICKS = 45 * 20;
    private static final int EFFECT_TICKS = 7 * 20;

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        boolean hasChronometer = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findCurios("relic").stream()
                        .anyMatch(result ->
                                result.stack().is(ClockworkRelics.CHRONOMETER.get())
                        ))
                .orElse(false);

        if (!hasChronometer) {
            return;
        }

        if (RelicCooldownManager.isOnCooldown(
                player, ClockworkRelics.CHRONOMETER.get())) {
            return;
        }

        if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            return;
        }

        // Запускаем перезарядку
        RelicCooldownManager.setCooldown(
                player,
                ClockworkRelics.CHRONOMETER.get(),
                COOLDOWN_TICKS
        );

        // Выдаём скорость II на 7 секунд
        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                EFFECT_TICKS,
                1,
                false,
                true
        ));
    }

    
@SubscribeEvent
public static void onPlayerTick(PlayerTickEvent.Post event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
        return;
    }

    if (player.tickCount % 20 != 0) {
        return;
    }

    boolean hasChronometer = CuriosApi.getCuriosInventory(player)
            .map(handler -> handler.findCurios("relic").stream()
                    .anyMatch(result ->
                            result.stack().is(ClockworkRelics.CHRONOMETER.get())
                    ))
            .orElse(false);

    if (!hasChronometer) {
        return;
    }

    int remainingTicks = RelicCooldownManager.getRemainingTicks(
            player,
            ClockworkRelics.CHRONOMETER.get()
    );

    if (remainingTicks <= 0) {
        return;
    }

    RelicNetwork.sendCooldown(
            player,
            "chronometer",
            remainingTicks,
            COOLDOWN_TICKS
    );

}
}
