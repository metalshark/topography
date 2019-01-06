package com.bloodnbonesgaming.topography.world.generator.vanilla.structure;

import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class NetherBridgeGenerator extends MapGenNetherBridge {
	
	public final int frequency;
	public final int totalArea;
	public final int randomArea;
	
	public NetherBridgeGenerator(final int frequency, final int totalArea, final int randomArea)
	{
		this.frequency = frequency;
		this.totalArea = totalArea;
		this.randomArea = randomArea;
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX / totalArea;
        int j = chunkZ / totalArea;
        this.rand.setSeed((long)(i ^ j * totalArea) ^ this.world.getSeed());
        int randValue = totalArea / 2 > 0 ? totalArea / 2 : 1;
        
        if (chunkX % totalArea == this.rand.nextInt(randValue) + randomArea && chunkZ % totalArea == this.rand.nextInt(randValue) + randomArea)
        {
            this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
            return this.frequency > 1 ? this.rand.nextInt(this.frequency) == 0 : true;
        }
        return false;
    }
}
