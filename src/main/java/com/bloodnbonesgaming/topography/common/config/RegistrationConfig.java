package com.bloodnbonesgaming.topography.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.FileHelper;

import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistrationConfig {
	
	private static RegistrationConfig INSTANCE = new RegistrationConfig();

	public void register(IForgeRegistryEntry toRegister) {
		Topography.RegistryEvents.toRegister.add(toRegister);
	}
	
	public static void init() {
		INSTANCE.read();
	}
	
	private void read() {
		Topography.getLog().info("Reading Registration files");
		
		final File scriptFolder = new File(ModInfo.CONFIG_FOLDER);
		
		if (!scriptFolder.exists())
		{
			scriptFolder.mkdirs();
			scriptFolder.mkdir();
		}
		
		final ScriptEngineManager factory = new ScriptEngineManager(null);

		try (Stream<Path> walk = Files.walk(Paths.get(ModInfo.CONFIG_FOLDER), Integer.MAX_VALUE).filter(p -> p.toString().endsWith("Registration.js") && (!ConfigurationManager.getGlobalConfig().disableExamples() || !p.toString().contains("examples")))) {
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {

				final Path path = it.next();
				final File scriptFile = path.toFile();

				ScriptEngine engine = factory.getEngineByName("nashorn");
				
				try (BufferedReader reader = FileHelper.openReader(scriptFile))
				{
					engine.eval("var ForgeEvents = Java.type(\"com.bloodnbonesgaming.topography.common.util.ForgeEvents\")");
					engine.eval("var Preset = Java.type(\"com.bloodnbonesgaming.topography.common.config.Preset\")");
					engine.eval("var GenerationStage = Java.type(\"net.minecraft.world.gen.GenerationStage\")");
					engine.eval("var Feature = Java.type(\"net.minecraft.world.gen.feature.Feature\")");
//					engine.eval("var BlockHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BlockHelper\")");
//					engine.eval("var BiomeHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BiomeHelper\")");
					engine.eval("var OreFeatureConfig = Java.type(\"net.minecraft.world.gen.feature.OreFeatureConfig\")");
					engine.eval("var AlwaysTrueRuleTest = Java.type(\"net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest\")");
					engine.eval("var ResourceLocation = Java.type(\"net.minecraft.util.ResourceLocation\")");
					engine.eval("var OreFeatureConfig = Java.type(\"net.minecraft.world.gen.feature.OreFeatureConfig\")");
					engine.eval("var ForgeRegistries = Java.type(\"net.minecraftforge.registries.ForgeRegistries\")");
//					engine.eval("var FeatureHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.features.FeatureHelper\")");
//					engine.eval("var OreHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.features.OreHelper\")");
//					engine.eval("var EntityHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.EntityHelper\")");
//					engine.eval("var ItemHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.ItemHelper\")");
					engine.eval("var Consumer = Java.type(\"java.util.function.Consumer\")");
					engine.eval("var BiomeDictionary = Java.type(\"net.minecraftforge.common.BiomeDictionary\")");
//					engine.eval("var StructureHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.features.StructureHelper\")");
//					engine.eval("var WorldHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.WorldHelper\")");
					engine.eval("var Util = Java.type(\"com.bloodnbonesgaming.topography.common.util.Util\")");
					
					engine.put("register", (Consumer<IForgeRegistryEntry>)this::register);
					engine.eval(reader);
					
				} catch (final Exception e)
				{
					e.printStackTrace();
					Topography.getLog().error(e.getMessage());
				}
			}
		} catch (IOException e) {
			//Ignore, as any other file will error
		}
	}
}
