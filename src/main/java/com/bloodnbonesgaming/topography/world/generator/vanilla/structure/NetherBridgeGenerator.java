package com.bloodnbonesgaming.topography.world.generator.vanilla.structure;

import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class NetherBridgeGenerator extends MapGenNetherBridge {
	
	public final int frequency;
	
	public NetherBridgeGenerator(final int frequency)
	{
		this.frequency = frequency;
	}
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX >> 4;
        int j = chunkZ >> 4;
        this.rand.setSeed((long)(i ^ j << 4) ^ this.world.getSeed());
        
        if (chunkX % 16 == this.rand.nextInt(8) + 4 && chunkZ % 16 == this.rand.nextInt(8) + 4)
        {
            this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
            return this.frequency > 1 ? this.rand.nextInt(this.frequency) == 0 : true;
        }
        return false;
    }
}
