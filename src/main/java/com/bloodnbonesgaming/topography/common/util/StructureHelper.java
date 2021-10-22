package com.bloodnbonesgaming.topography.common.util;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ISpawnWorldInfo;

public class StructureHelper {

	public static BlockPos getSpawn(final Template template)
    {
        for (final BlockInfo entry : template.func_215386_a(new BlockPos(0, 0, 0), new PlacementSettings(), Blocks.STRUCTURE_BLOCK, false)) {
        	if (entry.state.getBlock() == Blocks.STRUCTURE_BLOCK && entry.nbt != null) {
        		String mode = entry.nbt.getString("mode");
        		
        		if (!mode.isEmpty() && StructureMode.valueOf(mode) == StructureMode.DATA) {
        			String meta = entry.nbt.getString("metadata");
        			
        			if (meta.equalsIgnoreCase("spawn") || meta.equalsIgnoreCase("spawn_point")) {
        				return entry.pos;
        			}
        		}
        	}
        }
        return new BlockPos(-1, 128, -1);
    }
	
	public static BlockPos generateSpawnIsland(ServerWorld world, DimensionDef def, ResourceLocation dimID, BlockPos basePos) {
		int preloadArea = def.spawnStructure.getSize().getX();
        preloadArea = def.spawnStructure.getSize().getZ() > preloadArea ? def.spawnStructure.getSize().getZ() : preloadArea;
        preloadArea = preloadArea / 16;
        preloadArea += 4;
        Topography.getLog().info("Preloading " + ((preloadArea * 2 + 1) * (preloadArea * 2 + 1)) + " chunks for spawn structure in dimension " + dimID);
    	
    	for (int x = basePos.getX() - preloadArea; x < preloadArea + basePos.getX(); x++)
        {
            for (int z = basePos.getZ() - preloadArea; z < preloadArea + basePos.getZ(); z++)
            {
            	world.getChunkProvider().getChunk(x, z, true);
            }
        }
    	Topography.getLog().info("Spawning structure for dimension " + dimID + " at chunk coords " + basePos.getX() + "/" + basePos.getZ());
    	BlockPos pos = new BlockPos(basePos.getX() * 16, def.spawnStructureHeight, basePos.getZ() * 16);
    	def.spawnStructure.func_237146_a_(world, pos, pos, new PlacementSettings(), world.rand, 2);
        final BlockPos spawn = StructureHelper.getSpawn(def.spawnStructure).add(basePos.getX() * 16, def.spawnStructureHeight, basePos.getZ() * 16);
        
        if (spawn != null) {
        	world.setBlockState(spawn, Blocks.AIR.getDefaultState(), 2);
        }
        
        return spawn;
	}
}