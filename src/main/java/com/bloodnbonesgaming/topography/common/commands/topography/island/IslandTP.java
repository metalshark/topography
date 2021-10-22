package com.bloodnbonesgaming.topography.common.commands.topography.island;

public class IslandTP {
	
//	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
//		return Commands.literal("tp")
//				.requires(r -> r.hasPermissionLevel(3))
//				.then(Commands.argument("target", EntityArgument.player())
//						.executes(ctx -> execute(ctx, EntityArgument.getPlayer(ctx, "target"))));
//	}
//	
//	private static int execute(CommandContext<CommandSource> context, ServerPlayerEntity target) throws CommandSyntaxException, CommandException {
//		try {
//			GlobalConfig global = ConfigurationManager.getGlobalConfig();
//			
//			if (global == null) {
//				throw new CommandException(new StringTextComponent("Topography not initialized"));
//			}
//			Preset preset = global.getPreset();
//			
//			if (preset == null) {
//				throw new CommandException(new StringTextComponent("A Topography preset is not being used"));
//			}
//			DimensionDef def = preset.defs.get(new ResourceLocation("minecraft:overworld"));
//			
//			if (def == null) {
//				throw new CommandException(new StringTextComponent("The overworld does not have a dimension definition"));
//			}
//
//			RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("minecraft:overworld"));
//			final ServerWorld world = target.getServerWorld().getServer().getWorld(worldKey);
//			BlockPos nextIsland = findNextIsland(world, target, def);
//			
//			target.sendStatusMessage(new StringTextComponent("Generating island"), true);
//			BlockPos spawn = StructureHelper.generateSpawnIsland(world, def, new ResourceLocation("minecraft:overworld"), nextIsland);
//
//			target.sendStatusMessage(new StringTextComponent("Teleporting to island"), true);
//			setPlayerSpawn(target, spawn, worldKey);
//			
//		} catch(Exception e) {
//			Topography.getLog().info(e.getMessage());
//			e.printStackTrace();
//			throw new CommandException(new StringTextComponent(e.getMessage()));
//		}
//		return 0;
//	}
}
