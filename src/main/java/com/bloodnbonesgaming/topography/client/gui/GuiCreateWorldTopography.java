package com.bloodnbonesgaming.topography.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.client.gui.element.EnumGuiLocation;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementText;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTexture;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTextureStretch;
import com.bloodnbonesgaming.topography.client.gui.newstuff.GuiOptionsListNew;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiCreateWorldTopography extends GuiCreateWorld
{
	private GuiOptionsListNew list;
	private List<ConfigPreset> presets;
	private GuiElementTexture texture;
	private final List<GuiElementText> description = new ArrayList<GuiElementText>();
	GuiButtonAlpha create;

    public GuiCreateWorldTopography(GuiScreen parent)
    {
    	super(parent);
    	
    	ConfigurationManager.setup();
        this.presets = new ArrayList<ConfigPreset>(ConfigurationManager.getInstance().getPresets().values());
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.worldNameField.updateCursorCounter();
        this.worldSeedField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.create = this.addButton(new GuiButtonAlpha(0, 0, this.height - 20, 100, 20, I18n.format("selectWorld.create"), 0.8F));
        this.buttonList.add(new GuiButtonAlpha(1, 100, this.height - 20, 100, 20, I18n.format("gui.cancel"), 0.8F));
        this.btnGameMode = this.addButton(new GuiButtonAlpha(2, 0, 46, 150, 20, I18n.format("selectWorld.gameMode"), 0.8F));
//        this.btnMoreOptions = this.addButton(new GuiButton(3, this.width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions")));
        this.btnMapFeatures = this.addButton(new GuiButtonAlpha(4, 0, 112, 150, 20, I18n.format("selectWorld.mapFeatures"), 0.8F));
        this.btnMapFeatures.visible = true;
        this.btnBonusItems = this.addButton(new GuiButtonAlpha(7, 0, 90, 150, 20, I18n.format("selectWorld.bonusItems"), 0.8F));
        this.btnBonusItems.visible = true;
        this.btnMapType = this.addButton(new GuiButtonAlpha(5, 0, 134, 150, 20, I18n.format("selectWorld.mapType"), 0.8F));
        this.btnMapType.visible = true;
        this.btnAllowCommands = this.addButton(new GuiButtonAlpha(6, 0, 68, 150, 20, I18n.format("selectWorld.allowCommands"), 0.8F));
        this.btnAllowCommands.visible = true;
        this.btnCustomizeType = this.addButton(new GuiButtonAlpha(8, 0, 156, 150, 20, I18n.format("selectWorld.customizeType"), 0.8F));
        this.btnCustomizeType.visible = WorldType.WORLD_TYPES[this.selectedIndex].isCustomizable();
        this.worldNameField = new GuiTextField(9, this.fontRenderer, 1, 1, 200, 20);
        this.worldNameField.setFocused(true);
        this.worldNameField.setText(this.worldName);
        this.worldSeedField = new GuiTextField(10, this.fontRenderer, 1, 24, 200, 20);
        if (this.worldSeed.isEmpty())
        {
        	this.worldSeed = Long.toString((new Random()).nextLong());
        }
        this.worldSeedField.setText(worldSeed);
        this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
        this.calcSaveDirName();
        this.updateDisplayState();
        
        if (this.list == null)
        {
            int listSize = 0;
            
            for (final ConfigPreset preset : this.presets)
            {
            	int name = this.fontRenderer.getStringWidth(preset.getName());
            	if (preset.locked())
            		name += 9;
            	listSize = name > listSize ? name : listSize;
            }
            listSize += 12;
            this.list = new GuiOptionsListNew(Minecraft.getMinecraft(), this.fontRenderer, listSize, this.height - 50, 0, this.height, this.width - listSize, this.width, this.height, this.presets, this);
        }
        if (WorldType.WORLD_TYPES[this.selectedIndex].getName().equals("topography"))
    	{
        	int currentIndex = this.list.getIndex();
            int listSize = 0;
            
            for (final ConfigPreset preset : this.presets)
            {
            	int name = this.fontRenderer.getStringWidth(preset.getName());
            	if (preset.locked())
            		name += 9;
            	listSize = name > listSize ? name : listSize;
            }
            listSize += 12;
            this.list = new GuiOptionsListNew(Minecraft.getMinecraft(), this.fontRenderer, listSize, this.height - 50, 0, this.height, this.width - listSize, this.width, this.height, this.presets, this);
            this.list.elementClicked(currentIndex, false);
    	}
    }

    /**
     * Determine a save-directory name from the world name
     */
    private void calcSaveDirName()
    {
        this.saveDirName = this.worldNameField.getText().trim();

        for (char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS)
        {
            this.saveDirName = this.saveDirName.replace(c0, '_');
        }

        if (StringUtils.isEmpty(this.saveDirName))
        {
            this.saveDirName = "World";
        }

        this.saveDirName = getUncollidingSaveDirName(this.mc.getSaveLoader(), this.saveDirName);
    }

    /**
     * Sets displayed GUI elements according to the current settings state
     */
    private void updateDisplayState()
    {
        this.btnGameMode.displayString = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode);
//        this.gameModeDesc1 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line1");
//        this.gameModeDesc2 = I18n.format("selectWorld.gameMode." + this.gameMode + ".line2");
        this.btnMapFeatures.displayString = I18n.format("selectWorld.mapFeatures") + " ";

        if (this.generateStructuresEnabled)
        {
            this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.on");
        }
        else
        {
            this.btnMapFeatures.displayString = this.btnMapFeatures.displayString + I18n.format("options.off");
        }

        this.btnBonusItems.displayString = I18n.format("selectWorld.bonusItems") + " ";

        if (this.bonusChestEnabled && !this.hardCoreMode)
        {
            this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.on");
        }
        else
        {
            this.btnBonusItems.displayString = this.btnBonusItems.displayString + I18n.format("options.off");
        }

        this.btnMapType.displayString = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslationKey());
        this.btnAllowCommands.displayString = I18n.format("selectWorld.allowCommands") + " ";

        if (this.allowCheats && !this.hardCoreMode)
        {
            this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.on");
        }
        else
        {
            this.btnAllowCommands.displayString = this.btnAllowCommands.displayString + I18n.format("options.off");
        }
    }

    /**
     * Ensures that a proposed directory name doesn't collide with existing names.
     * Returns the name, possibly modified to avoid collisions.
     */
    public static String getUncollidingSaveDirName(ISaveFormat saveLoader, String name)
    {
        name = name.replaceAll("[\\./\"]", "_");

        for (String s : DISALLOWED_FILENAMES)
        {
            if (name.equalsIgnoreCase(s))
            {
                name = "_" + name + "_";
            }
        }

        while (saveLoader.getWorldInfo(name) != null)
        {
            name = name + "-";
        }

        return name;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);

                if (this.alreadyGenerated)
                {
                    return;
                }

                this.alreadyGenerated = true;
                long i = (new Random()).nextLong();
                String s = this.worldSeedField.getText();

                if (!StringUtils.isEmpty(s))
                {
                    try
                    {
                        long j = Long.parseLong(s);

                        if (j != 0L)
                        {
                            i = j;
                        }
                    }
                    catch (NumberFormatException var7)
                    {
                        i = (long)s.hashCode();
                    }
                }

                WorldType.WORLD_TYPES[this.selectedIndex].onGUICreateWorldPress();

                WorldSettings worldsettings = new WorldSettings(i, GameType.getByName(this.gameMode), this.generateStructuresEnabled, this.hardCoreMode, WorldType.WORLD_TYPES[this.selectedIndex]);
                worldsettings.setGeneratorOptions(this.chunkProviderSettingsJson);

                if (this.bonusChestEnabled && !this.hardCoreMode)
                {
                    worldsettings.enableBonusChest();
                }

                if (this.allowCheats && !this.hardCoreMode)
                {
                    worldsettings.enableCommands();
                }

                this.mc.launchIntegratedServer(this.saveDirName, this.worldNameField.getText().trim(), worldsettings);
            }
            else if (button.id == 3)
            {
                this.toggleMoreWorldOptions();
            }
            else if (button.id == 2)
            {
                if ("survival".equals(this.gameMode))
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = false;
                    }

                    this.hardCoreMode = false;
                    this.gameMode = "hardcore";
                    this.hardCoreMode = true;
                    this.btnAllowCommands.enabled = false;
                    this.btnBonusItems.enabled = false;
                    this.updateDisplayState();
                }
                else if ("hardcore".equals(this.gameMode))
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = true;
                    }

                    this.hardCoreMode = false;
                    this.gameMode = "creative";
                    this.updateDisplayState();
                    this.hardCoreMode = false;
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                }
                else
                {
                    if (!this.allowCheatsWasSetByUser)
                    {
                        this.allowCheats = false;
                    }

                    this.gameMode = "survival";
                    this.updateDisplayState();
                    this.btnAllowCommands.enabled = true;
                    this.btnBonusItems.enabled = true;
                    this.hardCoreMode = false;
                }

                this.updateDisplayState();
            }
            else if (button.id == 4)
            {
                this.generateStructuresEnabled = !this.generateStructuresEnabled;
                this.updateDisplayState();
            }
            else if (button.id == 7)
            {
                this.bonusChestEnabled = !this.bonusChestEnabled;
                this.updateDisplayState();
            }
            else if (button.id == 5)
            {
                ++this.selectedIndex;

                if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
                {
                    this.selectedIndex = 0;
                }

                while (!this.canSelectCurWorldType())
                {
                    ++this.selectedIndex;

                    if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
                    {
                        this.selectedIndex = 0;
                    }
                }

                this.chunkProviderSettingsJson = "";
                this.updateDisplayState();
                this.showMoreWorldOptions(this.inMoreWorldOptionsDisplay);
                if (WorldType.WORLD_TYPES[this.selectedIndex].getName().equals("topography"))
            	{
                    this.onListSelected(this.presets.get(this.list.getIndex()));
            	}
            }
            else if (button.id == 6)
            {
                this.allowCheatsWasSetByUser = true;
                this.allowCheats = !this.allowCheats;
                this.updateDisplayState();
            }
            else if (button.id == 8)
            {
                WorldType.WORLD_TYPES[this.selectedIndex].onCustomizeButton(mc, this);
            }
        }
    }

    /**
     * Returns whether the currently-selected world type is actually acceptable for selection
     * Used to hide the "debug" world type unless the shift key is depressed.
     */
    private boolean canSelectCurWorldType()
    {
        WorldType worldtype = WorldType.WORLD_TYPES[this.selectedIndex];

        if (worldtype != null && worldtype.canBeCreated())
        {
            return worldtype == WorldType.DEBUG_ALL_BLOCK_STATES ? isShiftKeyDown() : true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Toggles between initial world-creation display, and "more options" display.
     * Called when user clicks "More World Options..." or "Done" (same button, different labels depending on current
     * display).
     */
    private void toggleMoreWorldOptions()
    {
        this.showMoreWorldOptions(!this.inMoreWorldOptionsDisplay);
    }

    /**
     * Shows additional world-creation options if toggle is true, otherwise shows main world-creation elements
     */
    private void showMoreWorldOptions(boolean toggle)
    {
        this.inMoreWorldOptionsDisplay = toggle;

        if (WorldType.WORLD_TYPES[this.selectedIndex] == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.btnGameMode.visible = true;
            this.btnGameMode.enabled = true;

            if (this.savedGameMode == null)
            {
                this.savedGameMode = this.gameMode;
            }

            this.gameMode = "spectator";
            this.btnMapFeatures.visible = true;
            this.btnBonusItems.visible = true;
            this.btnMapType.visible = true;
            this.btnAllowCommands.visible = true;
            this.btnCustomizeType.visible = true;
        }
        else
        {
            this.btnGameMode.visible = true;
            this.btnGameMode.enabled = true;

            if (this.savedGameMode != null)
            {
                this.gameMode = this.savedGameMode;
                this.savedGameMode = null;
            }

            this.btnMapFeatures.visible = true;
            this.btnBonusItems.visible = true;
            this.btnMapType.visible = true;
            this.btnAllowCommands.visible = true;
            this.btnCustomizeType.visible = WorldType.WORLD_TYPES[this.selectedIndex].isCustomizable();
        }

        this.updateDisplayState();

//        if (this.inMoreWorldOptionsDisplay)
//        {
//            this.btnMoreOptions.displayString = I18n.format("gui.done");
//        }
//        else
//        {
//            this.btnMoreOptions.displayString = I18n.format("selectWorld.moreWorldOptions");
//        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.worldNameField.isFocused())
        {
            this.worldNameField.textboxKeyTyped(typedChar, keyCode);
            this.worldName = this.worldNameField.getText();
        }
        else if (this.worldSeedField.isFocused())
        {
            this.worldSeedField.textboxKeyTyped(typedChar, keyCode);
            this.worldSeed = this.worldSeedField.getText();
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }

        (this.buttonList.get(0)).enabled = !this.worldNameField.getText().isEmpty();
        this.calcSaveDirName();
        
        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            this.actionPerformed(this.buttonList.get(1));
        }
        
        if (keyCode == Keyboard.KEY_UP)
        {
        	this.list.up();
        }
        if (keyCode == Keyboard.KEY_DOWN)
        {
        	this.list.down();
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

//        if (this.inMoreWorldOptionsDisplay)
//        {
            this.worldSeedField.mouseClicked(mouseX, mouseY, mouseButton);
//        }
//        else
//        {
            this.worldNameField.mouseClicked(mouseX, mouseY, mouseButton);
//        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	if (WorldType.WORLD_TYPES[this.selectedIndex].getName().equals("topography"))
    	{
        	if (this.texture != null)
            {
                this.texture.render(Minecraft.getMinecraft(), this.width, this.height);
            }
        	else
        	{
        		this.drawDefaultBackground();
        	}
    	}
    	else
    	{
    		this.drawDefaultBackground();
    	}
    	//Draw buttons
    	for (int i = 0; i < this.buttonList.size(); ++i)
        {
            ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (int j = 0; j < this.labelList.size(); ++j)
        {
            ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
        }
        
        if (WorldType.WORLD_TYPES[this.selectedIndex].getName().equals("topography"))
    	{
            this.list.drawScreen(mouseX, mouseY, partialTicks);
            
            if (!this.description.isEmpty())
            {
            	GuiUtils.drawGradientRect(0, 0, 149 + this.fontRenderer.FONT_HEIGHT - 2, 200, Math.min(this.height - 20, 149 + this.description.size() * this.fontRenderer.FONT_HEIGHT + this.fontRenderer.FONT_HEIGHT + 2), 0xC0101010, 0xD0101010);

                for (final GuiElementText text : this.description)
                {
                    text.render(this.fontRenderer, this.width, this.height);
                }
            }
    	}

            this.worldSeedField.drawTextBox();

            if (WorldType.WORLD_TYPES[this.selectedIndex].hasInfoNotice())
            {
                this.fontRenderer.drawSplitString(I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getInfoTranslationKey()), this.btnMapType.x + 2, this.btnMapType.y + 22, this.btnMapType.getButtonWidth(), 10526880);
            }
//        }
//        else
//        {
//            this.drawString(this.fontRenderer, I18n.format("selectWorld.enterName"), 1, 47, -6250336);
//            this.drawString(this.fontRenderer, "Folder:" + " " + this.saveDirName, 1, 25, -6250336);
            this.worldNameField.drawTextBox();
            this.drawString(this.fontRenderer, this.gameModeDesc1, this.width / 2 - 100, 137, -6250336);
            this.drawString(this.fontRenderer, this.gameModeDesc2, this.width / 2 - 100, 149, -6250336);
//        }

//        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void recreateFromExistingWorld(WorldInfo original)
    {
        this.worldName = I18n.format("selectWorld.newWorld.copyOf", original.getWorldName());
        this.worldSeed = original.getSeed() + "";
        this.worldSeedField.setText(this.worldSeed);
        this.selectedIndex = original.getTerrainType().getId();
        this.chunkProviderSettingsJson = original.getGeneratorOptions();
        if (WorldType.WORLD_TYPES[this.selectedIndex].getName().equals("topography"))
    	{
        	if (!chunkProviderSettingsJson.isEmpty())
            {
                final JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(chunkProviderSettingsJson);
                if (element.isJsonObject())
                {
                    JsonObject obj = (JsonObject) element;
                    JsonElement member = obj.get("Topography-Preset");
                    
                    if (member != null)
                    {
                    	String presetString = member.getAsString();

                    	for (int i = 0; i < this.presets.size(); i++)
                    	{
                    		final ConfigPreset preset = this.presets.get(i);
                    		
                    		if (preset.getName().equals(presetString))
                    		{
                    			this.list.elementClicked(i, false);
                    		}
                    	}
                    }
                }
            }
    	}
        this.generateStructuresEnabled = original.isMapFeaturesEnabled();
        this.allowCheats = original.areCommandsAllowed();

        if (original.isHardcoreModeEnabled())
        {
            this.gameMode = "hardcore";
        }
        else if (original.getGameType().isSurvivalOrAdventure())
        {
            this.gameMode = "survival";
        }
        else if (original.getGameType().isCreative())
        {
            this.gameMode = "creative";
        }
        this.updateDisplayState();
    }
    
    
    
    

    
    public void onListSelected(final ConfigPreset preset)
    {
        //Set description
        this.description.clear();
        final String description = preset.getDescription();
        
        if (description != null && !description.isEmpty())
        {
            List<String> list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(description, 196);
            int index = 0;
            int totalHeight = (list.size() + 1) * this.fontRenderer.FONT_HEIGHT;
            
            for (final String string : list)
            {
                index++;
                final GuiElementText text = new GuiElementText(EnumGuiLocation.TOP_LEFT, string);
//                text.setAbsYOffset(this.fontRenderer.FONT_HEIGHT * index - totalHeight);
//                text.setAbsXOffset(fontRenderer.getStringWidth(string) / 2 + 5);
                text.setAbsXOffset(2);
                text.setAbsYOffset(149 + index * this.fontRenderer.FONT_HEIGHT);
                
                this.description.add(text);
            }
        }
        
        //Set texture
        this.texture = null;
        final String imageName = preset.getImage();
        
        if (!imageName.isEmpty())
        {
            final BufferedImage image = IOHelper.loadImage(imageName);
            
            if (image != null)
            {
                this.texture = new GuiElementTextureStretch(EnumGuiLocation.TOP_LEFT, this.mc.getTextureManager().getDynamicTextureLocation("presetImage", new DynamicTexture(image)), image.getWidth(), image.getHeight());
                this.texture.setRelRender(1, 1);
            }
            else
            {
            	this.texture = null;
            }
        }
        else
        {
        	this.texture = null;
        }
        //Lock
        this.create.enabled = !preset.locked();
        this.chunkProviderSettingsJson = "{\"Topography-Preset\":\"" + presets.get(this.list.getIndex()).getName() + "\"}";
    }
    
    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    	this.list.handleMouseInput(Mouse.getEventX() * this.width / this.mc.displayWidth, this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1);
    }
}