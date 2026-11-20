package de.codex.schematicbuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public final class SchematicBuilderClient implements ClientModInitializer {
    public static final String MOD_ID = "schematic_builder_bot";
    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        BuilderConfig.load();
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.schematic_builder_bot.open_menu", GLFW.GLFW_KEY_O,
                KeyBinding.Category.create("schematic_builder_bot:main")));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new BuilderScreen());
            }
            BotController.INSTANCE.tick();
        });
    }
}
