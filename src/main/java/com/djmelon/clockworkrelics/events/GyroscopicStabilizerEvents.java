package com.djmelon.clockworkrelics.events;

import com.djmelon.clockworkrelics.ClockworkRelics;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = ClockworkRelics.MODID)
public class GyroscopicStabilizerEvents {

    @SubscribeEvent
    public static void onFallDamage(LivingDamageEvent.Pre event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!event.getSource().is(DamageTypes.FALL)) {
            return;
        }

        boolean hasStabilizer = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findCurios("relic").stream()
                        .anyMatch(result ->
                                result.stack().is(ClockworkRelics.GYROSCOPIC_STABILIZER.get())
                        ))
                .orElse(false);
        if (!hasStabilizer) {
            return;
        }

        event.setNewDamage(event.getNewDamage() * 0.8F);
    }
}
