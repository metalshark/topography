package com.bloodnbonesgaming.topography.common.world;

import java.util.OptionalLong;

import com.bloodnbonesgaming.topography.common.config.Preset;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.IBiomeMagnifier;

public class DimensionTypeTopography extends DimensionType {

	public final Preset preset;

	public DimensionTypeTopography(Preset preset, OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, IBiomeMagnifier magnifier, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
		super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, hasDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, magnifier, infiniburn, effects, ambientLight);
		this.preset = preset;
	}

}
