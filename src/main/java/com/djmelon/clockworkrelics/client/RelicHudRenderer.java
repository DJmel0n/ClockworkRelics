
package com.djmelon.clockworkrelics.client;

import com.djmelon.clockworkrelics.ClockworkRelics;
import com.djmelon.clockworkrelics.network.RelicCooldownClientData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(
        modid = ClockworkRelics.MODID,
        value = Dist.CLIENT
)
public class RelicHudRenderer {

    private static final String CHRONOMETER_ID = "chronometer";

    private static final int PANEL_WIDTH = 160;
    private static final int PANEL_HEIGHT = 32;

    private static final int BACKGROUND_COLOR = 0xD91B2023;
    private static final int BAR_BACKGROUND = 0xFF41494C;
    private static final int BAR_COLOR = 0xFFD6A64B;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        RelicCooldownClientData.CooldownState state =
                RelicCooldownClientData.get(CHRONOMETER_ID);

        if (state == null || state.totalTicks() <= 0) {
            return;
        }

        long elapsedNanos = System.nanoTime() - state.receivedAt();
        int elapsedTicks = (int) (elapsedNanos / 50_000_000L);

        int remainingTicks = Math.max(
                0,
                state.remainingTicks() - elapsedTicks
        );

        if (remainingTicks <= 0) {
            return;
        }

        float progress = 1.0F -
                (float) remainingTicks / state.totalTicks();

        progress = Math.clamp(progress, 0.0F, 1.0F);

        GuiGraphics graphics = event.getGuiGraphics();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int x = (screenWidth - PANEL_WIDTH) / 2;
        int y = screenHeight - 95;

        // Фон панели
        graphics.fill(
                x, y,
                x + PANEL_WIDTH, y + PANEL_HEIGHT,
                BACKGROUND_COLOR
        );

        // Название талисмана
        graphics.drawString(
                minecraft.font,
                "CHRONOMETER",
                x + 8,
                y + 6,
                TEXT_COLOR,
                false
        );

        // Оставшееся время
        int seconds = (int) Math.ceil(remainingTicks / 20.0);

        String timer = seconds + "s";

        graphics.drawString(
                minecraft.font,
                timer,
                x + PANEL_WIDTH - minecraft.font.width(timer) - 8,
                y + 6,
                BAR_COLOR,
                false
        );

        // Фон шкалы
        int barX = x + 8;
        int barY = y + 21;
        int barWidth = PANEL_WIDTH - 16;

        graphics.fill(
                barX, barY,
                barX + barWidth, barY + 4,
                BAR_BACKGROUND
        );

        // Заполнение шкалы
        int filledWidth = (int) (barWidth * progress);

        graphics.fill(
                barX, barY,
                barX + filledWidth, barY + 4,
                BAR_COLOR
        );
    }
}
