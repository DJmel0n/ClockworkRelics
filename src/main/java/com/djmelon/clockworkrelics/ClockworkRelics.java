
package com.djmelon.clockworkrelics;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod(ClockworkRelics.MODID)
public class ClockworkRelics {

    public static final String MODID = "clockwork_relics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MODID);

    // Первый талисман — Хронометр
    public static final DeferredItem<Item> CHRONOMETER =
            ITEMS.register("chronometer",
                    () -> new Item(new Item.Properties()));
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
	        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB =
	        CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
	                .title(Component.translatable("itemGroup.clockwork_relics"))
	                .withTabsBefore(CreativeModeTabs.COMBAT)
	                .icon(() -> CHRONOMETER.get().getDefaultInstance())
	                .displayItems((parameters, output) -> {
	                    output.accept(CHRONOMETER.get());
	                })
	                .build());

    public ClockworkRelics(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.info("Clockwork Relics has been initialized!");
    }
}
