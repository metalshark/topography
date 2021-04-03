package com.bloodnbonesgaming.topography.common.world.gen.feature.structure;

import java.util.List;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.world.gen.feature.structure.pieces.FortressPiecesTopo;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

public class FortressStructureTopo extends Structure<NoFeatureConfig> {
	   private static final List<MobSpawnInfo.Spawners> field_202381_d = ImmutableList.of(new MobSpawnInfo.Spawners(EntityType.BLAZE, 10, 2, 3), new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4), new MobSpawnInfo.Spawners(EntityType.WITHER_SKELETON, 8, 5, 5), new MobSpawnInfo.Spawners(EntityType.SKELETON, 2, 5, 5), new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 3, 4, 4));

	   public FortressStructureTopo(Codec<NoFeatureConfig> p_i231972_1_) {
	      super(p_i231972_1_);
	   }

	   protected boolean func_230363_a_(ChunkGenerator p_230363_1_, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome p_230363_8_, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_) {
	      return p_230363_5_.nextInt(5) < 2;
	   }

	   public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
	      return FortressStructureTopo.Start::new;
	   }

	   @Override
	   public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
	      return field_202381_d;
	   }

	   public static class Start extends StructureStart<NoFeatureConfig> {
	      public Start(Structure<NoFeatureConfig> p_i225812_1_, int p_i225812_2_, int p_i225812_3_, MutableBoundingBox p_i225812_4_, int p_i225812_5_, long p_i225812_6_) {
	         super(p_i225812_1_, p_i225812_2_, p_i225812_3_, p_i225812_4_, p_i225812_5_, p_i225812_6_);
	      }

	      public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
	    	  FortressPiecesTopo.Start fortresspieces$start = new FortressPiecesTopo.Start(this.rand, (p_230364_4_ << 4) + 2, (p_230364_5_ << 4) + 2);
	         this.components.add(fortresspieces$start);
	         fortresspieces$start.buildComponent(fortresspieces$start, this.components, this.rand);
	         List<StructurePiece> list = fortresspieces$start.pendingChildren;

	         while(!list.isEmpty()) {
	            int i = this.rand.nextInt(list.size());
	            StructurePiece structurepiece = list.remove(i);
	            structurepiece.buildComponent(fortresspieces$start, this.components, this.rand);
	         }

	         this.recalculateStructureSize();
	         this.func_214626_a(this.rand, 48, 70);
	      }
	   }
	   
	   @Override
	public StructureStart<?> func_242785_a(DynamicRegistries dynamicRegistries, ChunkGenerator generator, BiomeProvider provider, TemplateManager templateManager, long seed, ChunkPos pos, Biome biome, int refCount, SharedSeedRandom rand, StructureSeparationSettings settings, NoFeatureConfig config) {
		// TODO Auto-generated method stub

			Topography.getLog().info("FortressStructureTopo func_242785_a");
		return super.func_242785_a(dynamicRegistries, generator, provider, templateManager, seed, pos, biome, refCount, rand, settings, config);
	}
	   
	   
	}
