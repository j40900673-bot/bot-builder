package de.codex.schematicbuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BuilderConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("schematic-builder-bot.json");
    public static final BuilderConfig INSTANCE = new BuilderConfig();

    public int accent = 0xFF63D6B2;
    public int panelAlpha = 220;
    public boolean animations = true;
    public int selectedModule = 0;
    public boolean botEnabled;
    public boolean allowMultiplayer;
    public int placementNumber = 1;

    private BuilderConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        try {
            BuilderConfig read = GSON.fromJson(Files.readString(FILE), BuilderConfig.class);
            if (read != null) {
                INSTANCE.accent = read.accent;
                INSTANCE.panelAlpha = Math.max(80, Math.min(255, read.panelAlpha));
                INSTANCE.animations = read.animations;
                INSTANCE.selectedModule = Math.max(0, Math.min(4, read.selectedModule));
                INSTANCE.botEnabled = read.botEnabled;
                INSTANCE.allowMultiplayer = read.allowMultiplayer;
                INSTANCE.placementNumber = Math.max(1, Math.min(99, read.placementNumber));
            }
        } catch (IOException | RuntimeException ignored) { }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException ignored) { }
    }
}
