package com.djmelon.clockworkrelics.events;

import java.util.IdentityHashMap;
import java.util.Map;

import com.djmelon.clockworkrelics.ClockworkRelics;
import com.simibubi.create.foundation.item.ItemDescription;

import net.createmod.catnip.lang.FontHelper;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = ClockworkRelics.MODID, value = Dist.CLIENT)
public class RelicTooltipHandler {

    private static final Map<Item, ItemDescription.Modifier> MODIFIERS =
            new IdentityHashMap<>();

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();

        if (item != ClockworkRelics.CHRONOMETER.get()
                && item != ClockworkRelics.GYROSCOPIC_STABILIZER.get()) {
            return;
        }

        MODIFIERS.computeIfAbsent(item, key ->
                new ItemDescription.Modifier(
                        key,
                        FontHelper.Palette.STANDARD_CREATE
                )
        ).modify(event);
    }
}
