package com.bloodnbonesgaming.topography.common.util;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.reflections.Reflections;

public class EventHandlers {
    static private Reflections minecraftForgeReflections;

    static private final Map<String, Class<? extends Event>> eventNameToClass = new HashMap<>();
    private static final Map<Class<? extends Event>, Consumer<? extends Event>> eventClassesHandled = new HashMap<>();

    static public Class<? extends Event> getEventClassByName(final String name) {
        return eventNameToClass.computeIfAbsent(name, k -> {
            if (minecraftForgeReflections == null)
                minecraftForgeReflections = new Reflections("net.minecraftforge");
            return minecraftForgeReflections.getSubTypesOf(Event.class)
                    .stream()
                    .filter(eventClass -> eventClass.getSimpleName().equals(name))
                    .findFirst()
                    .orElse(null);
        });
    }

    private static <T extends Event> Consumer<T> eventClassHandlerFactory(final Class<T> eventClass) {
        final Consumer<T> eventClassHandler = (event) -> {
            final GlobalConfig global = ConfigurationManager.getGlobalConfig();
            if (global == null)
                return;

            final Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
            if (preset == null)
                return;

            if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
                preset.fireEventSubscribers(event, EventSide.DEDICATED, EventSide.SERVER, EventSide.ANY);
            } else {
                preset.fireEventSubscribers(event, EventSide.CLIENT, EventSide.ANY);
            }
        };
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, false, eventClass, eventClassHandler);
        return eventClassHandler;
    }

    public static <T extends Event> void registerEventClassHandler(final Class<T> eventClass) {
        eventClassesHandled.computeIfAbsent(eventClass, k -> {
            Topography.getLog().error("Registering event handler for " + k);
            return eventClassHandlerFactory(k);
        });
    }
}
