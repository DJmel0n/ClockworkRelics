
package com.djmelon.clockworkrelics.network;

import com.djmelon.clockworkrelics.ClockworkRelics;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RelicCooldownPayload(
        String relicId,
        int remainingTicks,
        int totalTicks
) implements CustomPacketPayload {

    public static final Type<RelicCooldownPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    ClockworkRelics.MODID,
                    "relic_cooldown"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, RelicCooldownPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    RelicCooldownPayload::relicId,
                    ByteBufCodecs.VAR_INT,
                    RelicCooldownPayload::remainingTicks,
                    ByteBufCodecs.VAR_INT,
                    RelicCooldownPayload::totalTicks,
                    RelicCooldownPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
