package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class ExampleModClient implements ClientModInitializer {
    private static int ticksActive = 0;
    private static boolean wasInWorld = false;

    @Override
    public void onInitializeClient() {
        // عداد يحسب الوقت بدقة طالما أنت داخل العالم واللعبة ليست متوقفة
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !client.isPaused()) {
                if (!wasInWorld) {
                    wasInWorld = true;
                    ticksActive = 0; 
                }
                ticksActive++;
            } else if (client.level == null) {
                wasInWorld = false;
            }
        });

        // رسم الوقت الصافي فقط على الشاشة
        HudRenderCallback.EVENT.register((guiGraphics, tickCounter) -> {
            Minecraft client = Minecraft.getInstance();
            if (client.level == null) return;

            Font fontRenderer = client.font;
            
            int totalSeconds = ticksActive / 20;
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            // تنسيق الوقت المُنقذ (00:00:00)
            String timeDisplay = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            
            // رسم النص أعلى اليسار باستخدام نظام الـ GuiGraphics الرسمي
            guiGraphics.drawString(fontRenderer, timeDisplay, 10, 10, 0xFFFFFF, true);
        });
    }
}
