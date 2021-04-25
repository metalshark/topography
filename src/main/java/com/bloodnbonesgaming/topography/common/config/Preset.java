package com.bloodnbonesgaming.topography.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.Topography;
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
	public final Map<ResourceLocation, String> dimensions = new HashMap<ResourceLocation, String>();
	public final Map<ResourceLocation, DimensionDef> defs = new HashMap<ResourceLocation, DimensionDef>();
	//Script event subscribers
	private final Map<EventSide, Map<String, List<Consumer>>> scriptEventSubscribers = new HashMap<EventSide, Map<String, List<Consumer>>>();
	
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
				Topography.getLog().error("Error reading DimensionDef: " + e.getMessage());
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
	
	public Preset registerEventHandler(String eventType, Consumer event) {
		return this.registerEventHandler(eventType, EventSide.ANY, event);
	}
	
	public Preset registerEventHandler(String eventType, EventSide side, Consumer event) {
		if (!this.scriptEventSubscribers.containsKey(side)) {
			this.scriptEventSubscribers.put(side, new HashMap<String, List<Consumer>>());
		}
		Map<String, List<Consumer>> eventMap = this.scriptEventSubscribers.get(side);
		if (!eventMap.containsKey(eventType)) {
			eventMap.put(eventType, new ArrayList<Consumer>());
		}
		eventMap.get(eventType).add(event);
		return this;
	}
	
	public Preset registerEventHandler(String eventType, Class eventClass) throws InstantiationException, IllegalAccessException {
		return registerEventHandler(eventType, EventSide.ANY, eventClass);
	}
	
	public Preset registerEventHandler(String eventType, EventSide side, Class eventClass) throws InstantiationException, IllegalAccessException {
		Consumer event = (Consumer) eventClass.newInstance();
		return registerEventHandler(eventType, side, event);
	}
	
	public void fireEventSubscribers(String eventType, Event event, EventSide... sides) {
		for (EventSide side : sides) {
			if (this.scriptEventSubscribers.containsKey(side)) {
				Map<String, List<Consumer>> eventMap = this.scriptEventSubscribers.get(side);
				
				if (eventMap.containsKey(eventType)) {
					for (Consumer consumer : eventMap.get(eventType)) {
						consumer.accept((Object)event);
					}
				}
			}
		}
		for (DimensionDef def : this.defs.values()) {
			def.fireEventSubscribers(eventType, event);
		}
	}
	
	public String getImageLocation() {
		return this.imageLocation;
	}
	
	public String getGuiBackground() {
		return this.guiBackground;
	}
}
