package com.bloodnbonesgaming.topography.command;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.StructureHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.util.SpawnStructure;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class IslandCommand extends CommandBase
{
    final List<String> aliases = new ArrayList<String>();

    @Override
    public int compareTo(ICommand arg0)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "island";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "Use /topography island <player>";
    }

    @Override
    public List<String> getAliases()
    {
        return this.aliases;
    }
    
    private int islandIndex = 1;

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
        	Entity entity = getEntity(server, sender, args[0], EntityPlayerMP.class);
        	
            if (!checkEntity(entity))
            {
                throw new CommandException("The entity selected (%s) is not valid.", entity.getName());
            }
            final BlockPos pos = this.findNextIsland((EntityPlayerMP) entity);
            this.spawnIslands((EntityPlayerMP) entity, pos.getX(), pos.getZ());
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public int getRequiredPermissionLevel()
    {
        return 3;
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }
    
    private BlockPos findNextIsland(final EntityPlayer player)
    {
    	int index = 0;
    	int ring = 0;
    	int posInRing = 0;
    	
    	for (int i = 1; i < 10000; i++)
    	{
    		index += (i * 8);
    		if (index >= this.islandIndex)
    		{
    			ring = i;
    			posInRing = this.islandIndex - (index - i * 8);
    			break;
    		}
    	}
    	int ringSideLength = (ring - 1) * 2 + 1;
    	int x = ring;
    	int z = ring + 1;
    	
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
    	    	player.sendMessage(new TextComponentString("Breaking"));
    			break;
    		}
    	}
    	
    	player.sendMessage(new TextComponentString(ring + "/" + posInRing + "-" + x + "/" + z));
    	this.islandIndex++;
    	
    	return new BlockPos(x * 2, 0, z * 2);
    }
    
    private void spawnIslands(final EntityPlayerMP player, final int xOffset, final int zOffset)
    {
    	final WorldServer overworld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    	String settings = overworld.getWorldInfo().getGeneratorOptions();
        
    	if (!settings.isEmpty())
    	{
            final JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(settings);
            
            if (element.isJsonObject())
            {
                if (((JsonObject) element).has("Topography-Preset"))
                {
                	player.sendMessage(new TextComponentString("This may take a few seconds. Please wait."));
                	
        			for (final int dimension : ConfigurationManager.getInstance().getPreset().getDimensions())
        			{
        				String script = ConfigurationManager.getInstance().getPreset().getScript(dimension);
        	            DimensionDefinition definition = new DimensionDefinition();
        	            IOHelper.loadDimensionDefinition(script, definition);
        	            
        	            SpawnStructure structure = definition.getSpawnStructure();
        	            
        	            if (structure != null)
        	            {
        	            	final World dimWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
                            
                            final Template template = IOHelper.loadStructureTemplate(structure.getStructure());
                            
                            if (template != null)
                            {
                                int preloadArea = template.getSize().getX();
                                preloadArea = template.getSize().getZ() > preloadArea ? template.getSize().getZ() : preloadArea;
                                preloadArea = preloadArea / 16;
                                preloadArea += 2;
                                Topography.instance.getLog().info("Preloading " + ((preloadArea * 2 + 1) * (preloadArea * 2 + 1)) + " chunks for spawn structure in dimension " + dimension);
            	            	
            	            	for (int x = -preloadArea; x < preloadArea; x++)
                                {
                                    for (int z = -preloadArea; z < preloadArea; z++)
                                    {
                                       dimWorld.getChunkProvider().provideChunk(x + xOffset, z + zOffset);
                                    }
                                }
            	            	final BlockPos structurePos = new BlockPos(xOffset * 16, structure.getHeight(), zOffset * 16);
            	            	Topography.instance.getLog().info("Spawning structure for dimension " + dimension + " at " + structurePos.toString());
            
                                template.addBlocksToWorld(dimWorld, structurePos, new PlacementSettings(), 2);
                                BlockPos spawn = StructureHelper.getSpawn(template);
                                spawn = spawn.add(xOffset * 16, structure.getHeight(), zOffset * 16);
                                player.setSpawnPoint(spawn, true);
                                player.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
                            }
        	            }
        			}
                	player.sendMessage(new TextComponentString("Finished spawning structures."));
                }
            }
    	}
    }

    //From the Forge CommandSetDimension
    private static boolean checkEntity(Entity entity)
    {
        // use vanilla portal logic, try to avoid doing anything too silly
        return !entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss();
    }
}
