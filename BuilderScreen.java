package de.codex.schematicbuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public final class BuilderScreen extends Screen {
    private static final List<String> MODULES = List.of("Schematic Bot", "Movement", "Mining", "Inventory", "Building");
    private boolean settings;
    private int left, top, panelWidth, panelHeight;

    public BuilderScreen() { super(Text.literal("Schematic Builder Bot")); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        panelWidth = Math.min(680, this.width - 32);
        panelHeight = Math.min(390, this.height - 32);
        left = (this.width - panelWidth) / 2;
        top = (this.height - panelHeight) / 2;
        int alpha = BuilderConfig.INSTANCE.panelAlpha;
        fill(context, left, top, left + panelWidth, top + panelHeight, argb(alpha, 18, 22, 29));
        fill(context, left, top, left + panelWidth, top + 38, 0xF0252E39);
        context.drawText(textRenderer, "SCHEMATIC BUILDER", left + 16, top + 14, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "⚙", left + panelWidth - 31, top + 12, 0xFFFFFFFF, false);
        fill(context, left + 10, top + 48, left + 172, top + panelHeight - 12, argb(alpha, 26, 32, 41));
        for (int i = 0; i < MODULES.size(); i++) {
            int y = top + 60 + i * 37;
            if (i == BuilderConfig.INSTANCE.selectedModule) {
                fill(context, left + 14, y - 5, left + 168, y + 25, BuilderConfig.INSTANCE.accent);
            }
            context.drawText(textRenderer, MODULES.get(i), left + 23, y + 4,
                    i == BuilderConfig.INSTANCE.selectedModule ? 0xFF101820 : 0xFFE2E8F0, false);
        }
        int right = left + 190;
        if (settings) renderSettings(context, right);
        else renderModule(context, right);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderModule(DrawContext c, int x) {
        String module = MODULES.get(BuilderConfig.INSTANCE.selectedModule);
        c.drawText(textRenderer, module, x, top + 55, 0xFFFFFFFF, false);
        if (BuilderConfig.INSTANCE.selectedModule == 0) {
            boolean hasLitematica = FabricLoader.getInstance().isModLoaded("litematica");
            boolean hasBaritone = FabricLoader.getInstance().isModLoaded("baritone");
            c.drawText(textRenderer, "Litematica: " + (hasLitematica ? "erkannt" : "nicht installiert"), x, top + 81, 0xFFB9C4D0, false);
            c.drawText(textRenderer, "Baritone: " + (hasBaritone ? "erkannt" : "nicht installiert"), x, top + 101, 0xFFB9C4D0, false);
            c.drawText(textRenderer, "Placement Nr.: " + BuilderConfig.INSTANCE.placementNumber + "     [klicken: − / +]", x, top + 128, 0xFFDAE2EA, false);
            c.drawText(textRenderer, "Multiplayer-Regeln geprüft: " + (BuilderConfig.INSTANCE.allowMultiplayer ? "JA" : "NEIN"), x, top + 151, 0xFFFFD27A, false);
            c.drawText(textRenderer, "Status: " + BotController.INSTANCE.state(), x, top + 178, statusColor(), false);
            c.drawText(textRenderer, BotController.INSTANCE.message(), x, top + 198, 0xFFB9C4D0, false);
            button(c, x, top + 224, "Starten");
            button(c, x, top + 257, "Pausieren");
            button(c, x, top + 290, "Fortsetzen");
            button(c, x, top + 323, "Stoppen");
            c.drawText(textRenderer, "Steuerung erfolgt über Baritone.", x, top + panelHeight - 34, 0xFF8593A3, false);
        } else {
            c.drawText(textRenderer, "Bewegung, Abbau und Inventar folgen", x, top + 88, 0xFFB9C4D0, false);
            c.drawText(textRenderer, "dem Baritone-Bauauftrag. Details und", x, top + 108, 0xFFB9C4D0, false);
            c.drawText(textRenderer, "Materialzugriff aus Truhen sind in", x, top + 128, 0xFFB9C4D0, false);
            c.drawText(textRenderer, "dieser Mod noch nicht integriert.", x, top + 148, 0xFFFFD27A, false);
        }
    }

    private int statusColor() {
        return switch (BotController.INSTANCE.state()) {
            case RUNNING -> 0xFF63D6B2;
            case PAUSED -> 0xFFFFD27A;
            case ERROR -> 0xFFFF7373;
            default -> 0xFFB9C4D0;
        };
    }

    private void renderSettings(DrawContext c, int x) {
        c.drawText(textRenderer, "Globale Einstellungen", x, top + 55, 0xFFFFFFFF, false);
        c.drawText(textRenderer, "Hintergrund-Deckkraft: " + BuilderConfig.INSTANCE.panelAlpha + " (klicken)", x, top + 91, 0xFFDAE2EA, false);
        c.drawText(textRenderer, "Akzentfarbe: klicken zum Wechseln", x, top + 124, BuilderConfig.INSTANCE.accent, false);
        c.drawText(textRenderer, "Animationen: " + (BuilderConfig.INSTANCE.animations ? "AN" : "AUS"), x, top + 157, 0xFFDAE2EA, false);
        c.drawText(textRenderer, "ESC schließt das Menü; Einstellungen werden gespeichert.", x, top + 200, 0xFF9AA8B7, false);
    }

    private void button(DrawContext c, int x, int y, String label) {
        fill(c, x, y, x + 210, y + 27, BuilderConfig.INSTANCE.accent);
        c.drawText(textRenderer, label, x + 10, y + 9, 0xFF101820, false);
    }

    private static void fill(DrawContext c, int x1, int y1, int x2, int y2, int color) { c.fill(x1, y1, x2, y2, color); }
    private static int argb(int a, int r, int g, int b) { return (a << 24) | (r << 16) | (g << 8) | b; }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        if (mouseX >= left + panelWidth - 42 && mouseY >= top && mouseY <= top + 40) {
            settings = !settings;
            return true;
        }
        if (!settings && mouseX >= left + 12 && mouseX <= left + 174) {
            int row = (int) ((mouseY - top - 55) / 37);
            if (row >= 0 && row < MODULES.size()) {
                BuilderConfig.INSTANCE.selectedModule = row;
                BuilderConfig.save();
                return true;
            }
        }
        if (!settings && BuilderConfig.INSTANCE.selectedModule == 0) {
            int right = left + 190;
            if (mouseX >= right && mouseX <= right + 280 && mouseY >= top + 120 && mouseY < top + 145) {
                BuilderConfig.INSTANCE.placementNumber = Math.max(1, Math.min(99,
                        BuilderConfig.INSTANCE.placementNumber + (mouseX < right + 140 ? -1 : 1)));
                BuilderConfig.save();
                return true;
            }
            if (mouseX >= right && mouseX <= right + 320 && mouseY >= top + 145 && mouseY < top + 171) {
                BuilderConfig.INSTANCE.allowMultiplayer = !BuilderConfig.INSTANCE.allowMultiplayer;
                BuilderConfig.save();
                return true;
            }
            if (mouseX >= right && mouseX <= right + 220) {
                MinecraftClient client = MinecraftClient.getInstance();
                if (mouseY >= top + 220 && mouseY < top + 253) BotController.INSTANCE.start(client);
                else if (mouseY >= top + 253 && mouseY < top + 286) BotController.INSTANCE.pause();
                else if (mouseY >= top + 286 && mouseY < top + 319) BotController.INSTANCE.resume();
                else if (mouseY >= top + 319 && mouseY < top + 352) BotController.INSTANCE.stop();
                else return super.mouseClicked(mouseX, mouseY, button);
                return true;
            }
        }
        if (settings && mouseX >= left + 190 && mouseY >= top + 76 && mouseY <= top + 180) {
            if (mouseY < top + 112) BuilderConfig.INSTANCE.panelAlpha = BuilderConfig.INSTANCE.panelAlpha >= 240 ? 120 : BuilderConfig.INSTANCE.panelAlpha + 20;
            else if (mouseY < top + 145) {
                int[] palette = {0xFF63D6B2, 0xFF6EA8FE, 0xFFFF8C69, 0xFFC792EA, 0xFFF2CC60};
                int index = 0;
                while (index < palette.length - 1 && palette[index] != BuilderConfig.INSTANCE.accent) index++;
                BuilderConfig.INSTANCE.accent = palette[(index + 1) % palette.length];
            } else BuilderConfig.INSTANCE.animations = !BuilderConfig.INSTANCE.animations;
            BuilderConfig.save();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public boolean shouldPause() { return false; }
    @Override public void close() { BuilderConfig.save(); super.close(); }
}
