package com.djmelon.clockworkrelics.events;

import com.djmelon.clockworkrelics.ClockworkRelics;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = ClockworkRelics.MODID)
public class ChronometerEvents {

    private static final java.util.Map<java.util.UUID, Long> COOLDOWNS =
            new java.util.HashMap<>();

    private static final int COOLDOWN_TICKS = 45 * 20;
    private static final int EFFECT_TICKS = 7 * 20;

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        boolean hasChronometer = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(
                        stack -> stack.is(ClockworkRelics.CHRONOMETER.get()),
                        "relic"
                ).isPresent())
                .orElse(false);
        
        if (!hasChronometer) {
            return;
        }
        
        long currentTime = player.level().getGameTime();
        
        long nextActivation = COOLDOWNS.getOrDefault(
                player.getUUID(), 0L
        );
        
        if (currentTime < nextActivation) {
            return;
        }
        if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            return;
        }
        // Запускаем перезарядку
        COOLDOWNS.put(
                player.getUUID(),
                currentTime + COOLDOWN_TICKS
        );
        
        // Выдаём скорость I на 7 секунд
        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                EFFECT_TICKS,
                0,
                false,
                true
        ));
    }
}
