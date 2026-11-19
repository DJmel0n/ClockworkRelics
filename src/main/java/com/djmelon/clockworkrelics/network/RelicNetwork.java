
package com.djmelon.clockworkrelics.network;

import com.djmelon.clockworkrelics.ClockworkRelics;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(
        modid = ClockworkRelics.MODID,
        bus = EventBusSubscriber.Bus.MOD
)
public class RelicNetwork {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {

        var registrar = event.registrar("1");

        registrar.playToClient(
                RelicCooldownPayload.TYPE,
                RelicCooldownPayload.STREAM_CODEC,
                (payload, context) -> {
                    RelicCooldownClientData.update(payload);
                }
        );
    }
    public static void sendCooldown(
            ServerPlayer player,
            String relicId,
            int remainingTicks,
            int totalTicks
    ) {
        PacketDistributor.sendToPlayer(
                player,
                new RelicCooldownPayload(
                        relicId,
                        remainingTicks,
                        totalTicks
                )
        );
    }
}
