package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.Random;

import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TestEventHandler {
	
	
	@SubscribeEvent
	public <T extends InitNoiseGensEvent.Context> void onNoiseGenerator(final InitNoiseGensEvent<T> event)
	{
		if (event.getOriginal() instanceof ContextOverworld)
		{
			final Random rand = new Random(event.getWorld().getSeed());
			final InitNoiseGensEvent.ContextOverworld context = (ContextOverworld) event.getNewValues();
	        
//			final VanillaNoiseWrapper test = new VanillaNoiseWrapper(event.getWorld().getSeed(), 16);
//	        context.setLPerlin1(test);
//	        context.getLPerlin2(new VanillaNoiseWrapper(event.getWorld().getSeed(), 16));
//	        context.getPerlin(new VanillaNoiseWrapper(event.getWorld().getSeed(), 8));
//	        new NoiseGeneratorPerlin(rand, 4);
////	        context.getHeight();
//	        new VanillaNoiseWrapper(rand, 10);
////	        context.getScale();
//	        new VanillaNoiseWrapper(rand, 16);
////	        context.getDepth();
//	        new VanillaNoiseWrapper(rand, 8);
//	        context.getForest();
	        
	        context.setLPerlin1(new NoiseGeneratorOctavesDoubleTest(rand, 8));
	        context.getLPerlin2(new NoiseGeneratorOctavesDoubleTest(rand, 8));
	        context.getPerlin(new NoiseGeneratorOctaves(rand, 8));
	        
	        
	        
	        
	        
//	        double[] vanilla = event.getOriginal().getLPerlin1().generateNoiseOctaves(null, 0, 0, 0, 5, 33, 5, (double)684.412F, (double) 684.412F, (double)684.412F);
//	        double[] vanilla2 = event.getOriginal().getLPerlin1().generateNoiseOctaves(null, 0, 0, 0, 5, 1, 5, (double)684.412F, (double) 684.412F, (double)684.412F);
//	        double[] threaded = test.generateVanillaNoise(null, 0, 0, 0, 5, 33, 5, (double)684.412F, (double) 684.412F, (double)684.412F);
//	        double[] threaded2 = test.generateNoiseOctaves(null, 0, 0, 0, 5, 33, 5, (double)684.412F, (double) 684.412F, (double)684.412F);

//	        Topography.instance.getLog().info(Arrays.toString(threaded));
//
//	        Topography.instance.getLog().info(Arrays.toString(threaded2));
//	        Topography.instance.getLog().info(vanilla2[0] + "/" + threaded2[0]);
	        
//	        for (int y = 0; y < 33; y++)
//	        {
//	        	for (int x = 0; x < 5; x++)
//	        	{
//	        		for (int z = 0; z < 5; z++)
//	        		{
//	        			Topography.instance.getLog().info(((x * 5 + z) * 33 + y) + " " + x + "/" + y + "/" + z + " " + threaded[((x * 5 + z) * 33 + y)]);
//	        		}
//	        	}
//	        }
		}
	}
	
	final NoiseGeneratorOctaves noise = new NoiseGeneratorOctaves(new Random(1), 16);
	
	private int tickCount = 0;
	private int totalCount = 0;
	private long totalTime = 0;
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			if (!event.world.isRemote && event.world.provider.getDimension() == 0)
			{
				this.tickCount++;
				if (tickCount > 500 && tickCount % 10 == 0)
				{
					this.totalCount++;
					final long time = System.nanoTime();
//					event.world.getChunkFromChunkCoords(this.totalCount, 0);
					this.noise.generateNoiseOctaves(null, 0, 0, 0, 5, 33, 5, 684.412F, 684.412F, 684.412F);
					this.totalTime += (System.nanoTime() - time);
					System.out.println((this.totalTime / totalCount / 1000000000D) + " " + totalCount);
				}
			}
		}
	}
}
