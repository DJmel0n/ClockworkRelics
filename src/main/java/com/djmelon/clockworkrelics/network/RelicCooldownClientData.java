
package com.djmelon.clockworkrelics.network;

import java.util.HashMap;
import java.util.Map;

public class RelicCooldownClientData {

    public record CooldownState(
            int remainingTicks,
            int totalTicks,
            long receivedAt
    ) {}

    private static final Map<String, CooldownState> COOLDOWNS =
            new HashMap<>();

    public static void update(RelicCooldownPayload payload) {
        COOLDOWNS.put(
                payload.relicId(),
                new CooldownState(
                        payload.remainingTicks(),
                        payload.totalTicks(),
                        System.nanoTime()
                )
        );
    }

    public static CooldownState get(String relicId) {
        return COOLDOWNS.get(relicId);
    }
}
