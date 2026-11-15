package com.djmelon.clockworkrelics.events;

import com.djmelon.clockworkrelics.ClockworkRelics;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = ClockworkRelics.MODID)
public class ChronometerTooltip {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!event.getItemStack().is(ClockworkRelics.CHRONOMETER.get())) {
            return;
        }

        event.getToolTip().add(
                Component.translatable("tooltip.clockwork_relics.chronometer.effect")
        );

        event.getToolTip().add(
                Component.translatable("tooltip.clockwork_relics.chronometer.cooldown")
        );
    }
}
