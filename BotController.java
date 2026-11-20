package de.codex.schematicbuilder;

import net.minecraft.client.MinecraftClient;

public final class BotController {
    public enum State { IDLE, RUNNING, PAUSED, ERROR }
    public static final BotController INSTANCE = new BotController();

    private State state = State.IDLE;
    private String message = "Bereit";

    private BotController() {}

    public State state() { return state; }
    public String message() { return message; }

    public void tick() {
        if (state != State.RUNNING) return;
        try {
            if (!BaritoneBridge.buildIsActive()) {
                state = State.IDLE;
                message = "Baritone meldet keinen aktiven Bauauftrag.";
            }
        } catch (ReflectiveOperationException error) {
            fail("Baritone-Status nicht lesbar: " + BaritoneBridge.rootMessage(error));
        }
    }

    public void start(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            fail("Bitte zuerst einer Welt beitreten.");
            return;
        }
        if (!BaritoneBridge.isAvailable()) {
            fail("Litematica und Baritone für 1.21.11 werden benötigt.");
            return;
        }
        if (!client.isInSingleplayer() && !BuilderConfig.INSTANCE.allowMultiplayer) {
            fail("Server-Nutzung erst nach Regelprüfung freigeben.");
            return;
        }
        try {
            boolean accepted = BaritoneBridge.command("litematica " + BuilderConfig.INSTANCE.placementNumber);
            if (!accepted) {
                fail("Baritone hat den Befehl nicht angenommen.");
                return;
            }
            state = State.RUNNING;
            message = "Bauauftrag an Baritone übergeben.";
        } catch (ReflectiveOperationException error) {
            fail("Baritone-API nicht kompatibel: " + BaritoneBridge.rootMessage(error));
        }
    }

    public void pause() { control("pause", State.PAUSED, "Pausiert."); }
    public void resume() { control("resume", State.RUNNING, "Fortsetzen angefordert."); }
    public void stop() { control("cancel", State.IDLE, "Abbruch angefordert."); }

    private void control(String command, State next, String success) {
        boolean valid = switch (command) {
            case "pause" -> state == State.RUNNING;
            case "resume" -> state == State.PAUSED;
            case "cancel" -> state == State.RUNNING || state == State.PAUSED;
            default -> false;
        };
        if (!valid) {
            message = "Kein aktiver Bauauftrag.";
            return;
        }
        try {
            if (!BaritoneBridge.command(command)) {
                fail("Baritone kennt den Befehl " + command + " nicht.");
                return;
            }
            state = next;
            message = success;
        } catch (ReflectiveOperationException error) {
            fail("Baritone-Steuerung fehlgeschlagen: " + BaritoneBridge.rootMessage(error));
        }
    }

    private void fail(String text) { state = State.ERROR; message = text; }
}
