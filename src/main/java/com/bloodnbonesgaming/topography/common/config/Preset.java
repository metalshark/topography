package com.bloodnbonesgaming.topography.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.EventHandlers;
import com.bloodnbonesgaming.topography.common.util.EventSide;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class Preset {
	
	public final String internalID;
	public String displayName;
	public String imageLocation = null;
	public String description = null;
	private String guiBackground = null;
	
	//ID, script path
	public final Map<ResourceLocation, String> dimensions = new HashMap<>();
	public final Map<ResourceLocation, DimensionDef> defs = new HashMap<>();
	//Script event subscribers
	private final Map<EventSide, Map<Class<? extends Event>, List<Consumer<Event>>>> scriptEventSubscribers = new HashMap<>();
	
	public Preset(String internalID) {
		this.internalID = internalID;
		this.displayName = internalID;
	}
	
	public void clean() {
		//TODO Un/re-register dimensions once preset is no longer loaded - Maybe not needed since dimensions can be registered per-world
	}
	
	public void readDimensionDefs() {
		final ScriptEngineManager factory = new ScriptEngineManager(null);
		
		for (Entry<ResourceLocation, String> entry : dimensions.entrySet()) {
			DimensionDef def;
			try {
				def = DimensionDef.read(entry.getValue(), factory);
				this.defs.put(entry.getKey(), def);
			} catch (Exception e) {
				Topography.getLog().error("Error reading file " + entry.getValue() + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public Preset image(String imageLocation) {
		this.imageLocation = imageLocation;
		return this;
	}
	
	public Preset displayName(String displayName) {
		this.displayName = displayName;
		return this;
	}
	
	public Preset description(String description) {
		this.description = description;
		return this;
	}
	
	public Preset registerDimension(String id, String script) {
		this.dimensions.put(new ResourceLocation(id), script);
		return this;
	}
	
	public Preset setGuiBackground(String location) {
		this.guiBackground = location;
		return this;
	}

	public Preset registerEventHandler(final String eventType, final Consumer<Event> event) {
		return this.registerEventHandler(eventType, EventSide.ANY, event);
	}

	public Preset registerEventHandler(final String eventType, final EventSide side, final Consumer<Event> eventConsumer) {
		final Class<? extends Event> eventClass = EventHandlers.getEventClassByName(eventType);
		EventHandlers.registerEventClassHandler(eventClass);
		scriptEventSubscribers
				.computeIfAbsent(side, k -> new HashMap<>())
				.computeIfAbsent(eventClass, k -> new ArrayList<>())
				.add(eventConsumer);
		return this;
	}

	public Preset registerEventHandler(final String eventType, final Class<Consumer<Event>> eventClass) throws InstantiationException, IllegalAccessException {
		return registerEventHandler(eventType, EventSide.ANY, eventClass);
	}

	public Preset registerEventHandler(final String eventType, final EventSide side, final Class<Consumer<Event>> eventConsumerClass) throws InstantiationException, IllegalAccessException {
		final Consumer<Event> eventConsumer = eventConsumerClass.newInstance();
		return registerEventHandler(eventType, side, eventConsumer);
	}
	
	public void fireEventSubscribers(Event event, EventSide... sides) {
		for (final EventSide side : sides) {
			final Map<Class<? extends Event>, List<Consumer<Event>>> eventMap = scriptEventSubscribers.get(side);
			if (eventMap == null)
				continue;

			final List<Consumer<Event>> consumers = eventMap.get(event.getClass());
			if (consumers == null)
				continue;

			for (final Consumer<Event> consumer : consumers) {
				try {
					consumer.accept(event);
				} catch(Exception e) {
					Topography.getLog().error("Script error: ", e);
				}
			}
		}
		for (final DimensionDef def : defs.values()) {
			def.fireEventSubscribers(event);
		}
	}
	
	public String getImageLocation() {
		return this.imageLocation;
	}
	
	public String getGuiBackground() {
		return this.guiBackground;
	}
}
