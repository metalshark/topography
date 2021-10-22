package com.bloodnbonesgaming.topography.common.commands.topography.island;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.StructureHelper;
import com.bloodnbonesgaming.topography.common.util.capabilities.TopographyPlayerData;
import com.bloodnbonesgaming.topography.common.util.storage.TopographyWorldData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class IslandNew {
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("new")
				.requires(r -> r.hasPermissionLevel(3))
				.then(Commands.argument("target", EntityArgument.player())
						.executes(ctx -> execute(ctx, EntityArgument.getPlayer(ctx, "target"))));
	}
	
	private static int execute(CommandContext<CommandSource> context, ServerPlayerEntity target) throws CommandSyntaxException, CommandException {
		try {
			GlobalConfig global = ConfigurationManager.getGlobalConfig();
			
			if (global == null) {
				throw new CommandException(new StringTextComponent("Topography not initialized"));
			}
			Preset preset = global.getPreset();
			
			if (preset == null) {
				throw new CommandException(new StringTextComponent("A Topography preset is not being used"));
			}
			DimensionDef def = preset.defs.get(new ResourceLocation("minecraft:overworld"));
			
			if (def == null) {
				throw new CommandException(new StringTextComponent("The overworld does not have a dimension definition"));
			}

			RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("minecraft:overworld"));
			final ServerWorld world = target.getServerWorld().getServer().getWorld(worldKey);
			BlockPos nextIsland = findNextIsland(world, target, def);
			
			target.sendStatusMessage(new StringTextComponent("Generating island"), true);
			BlockPos spawn = StructureHelper.generateSpawnIsland(world, def, new ResourceLocation("minecraft:overworld"), nextIsland);

			target.sendStatusMessage(new StringTextComponent("Teleporting to island"), true);
			setPlayerSpawn(target, spawn, worldKey);
			
		} catch(Exception e) {
			Topography.getLog().info(e.getMessage());
			e.printStackTrace();
			throw new CommandException(new StringTextComponent(e.getMessage()));
		}
		return 0;
	}
    
    private static void setPlayerSpawn(final ServerPlayerEntity player, final BlockPos pos, RegistryKey<World> key)
    {
    	BlockPos spawn = pos.add(0.5, 0, 0.5);
    	player.func_242111_a(key, spawn, 0, true, false);
        player.setPositionAndUpdate(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);

        player.getCapability(TopographyPlayerData.CAPABILITY_TOPOGRAPHY_PLAYER_DATA, null).ifPresent((data) -> {
        	data.setIsland(pos.getX(), pos.getZ());
        });
    }
	
	private static BlockPos findNextIsland(ServerWorld world, final ServerPlayerEntity player, final DimensionDef def) throws CommandException
    {
        int x = 0;
        int z = 0;
        
        if (def.spawnStructure != null)
        {
        	int islandIndex = TopographyWorldData.getIslandIndex(world);
        	int index = 0;
        	int ring = 0;
        	int posInRing = 0;
        	
        	for (int i = 1; i < 10000; i++)
        	{
        		index += (i * 8);
        		if (index >= islandIndex)
        		{
        			ring = i;
        			posInRing = islandIndex - (index - i * 8);
        			break;
        		}
        	}
        	int ringSideLength = (ring - 1) * 2 + 1;
        	x = ring;
        	z = ring + 1;
        	
        	for (int i = 0; i <= (ringSideLength + 1) * 4; i++)
        	{
        		if (i <= ringSideLength + 1)
        		{
        			z--;
        		}
        		else if (i <= (ringSideLength + 1) * 2)
        		{
        			x--;
        		}
        		else if (i <= (ringSideLength + 1) * 3)
        		{
        			z++;
        		}
        		else
        		{
        			x++;
        		}
        		if (i == posInRing)
        		{
        			break;
        		}
        	}
        	
        	islandIndex++;
        	TopographyWorldData.saveIslandIndex(islandIndex, world);
        	
        	return new BlockPos(x * def.spawnStructureSpacing, 0, z * def.spawnStructureSpacing);
        }
        throw new CommandException(new StringTextComponent("This preset does not have spawn structures"));
    }
}
