package de.codex.schematicbuilder;

import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Optional runtime bridge: Baritone stays a separate client mod and is never bundled. */
public final class BaritoneBridge {
    private BaritoneBridge() {}

    public static boolean isAvailable() {
        return FabricLoader.getInstance().isModLoaded("baritone")
                && FabricLoader.getInstance().isModLoaded("litematica");
    }

    public static boolean command(String command) throws ReflectiveOperationException {
        Class<?> api = Class.forName("baritone.api.BaritoneAPI");
        Object provider = api.getMethod("getProvider").invoke(null);
        Object baritone = provider.getClass().getMethod("getPrimaryBaritone").invoke(provider);
        Object manager = baritone.getClass().getMethod("getCommandManager").invoke(baritone);
        Method execute = findStringMethod(manager.getClass(), "execute");
        Object accepted = execute.invoke(manager, command);
        return Boolean.TRUE.equals(accepted);
    }

    public static boolean buildIsActive() throws ReflectiveOperationException {
        Class<?> api = Class.forName("baritone.api.BaritoneAPI");
        Object provider = api.getMethod("getProvider").invoke(null);
        Object baritone = provider.getClass().getMethod("getPrimaryBaritone").invoke(provider);
        Object builder = baritone.getClass().getMethod("getBuilderProcess").invoke(baritone);
        return Boolean.TRUE.equals(builder.getClass().getMethod("isActive").invoke(builder));
    }

    private static Method findStringMethod(Class<?> type, String name) throws NoSuchMethodException {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == 1
                    && method.getParameterTypes()[0] == String.class) return method;
        }
        throw new NoSuchMethodException(type.getName() + "." + name + "(String)");
    }

    public static String rootMessage(Throwable error) {
        Throwable cause = error;
        while ((cause instanceof InvocationTargetException || cause.getCause() != null) && cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
    }
}
