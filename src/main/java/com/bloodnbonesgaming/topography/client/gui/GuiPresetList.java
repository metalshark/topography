package com.bloodnbonesgaming.topography.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;

public class GuiPresetList extends ScrollPanel {
	
	FontRenderer fontRenderer;
	List<ITextProperties> lines = new ArrayList<ITextProperties>();
	GuiCreateWorld createWorldGui;

	public GuiPresetList(Minecraft client, int width, int height, int top, int left, FontRenderer fontRenderer, GuiCreateWorld createWorld) {
		super(Minecraft.getInstance(), width, height, top, left);
		this.createWorldGui = createWorld;
		
		this.fontRenderer = Minecraft.getInstance().fontRenderer;
		List<String> presets = new ArrayList<String>();
		presets.add("test");
		presets.add("test2");
		presets.add("test3");
		presets.add("test4");
		presets.add("test5");
		presets.add("test6");
		presets.add("test7");
		presets.add("test8");
		presets.add("test9");
		presets.add("test0");
		
		this.lines = this.resizeContent(presets);
	}

	@Override
	protected int getContentHeight() {
		int height = 0;
        height += (lines.size() * fontRenderer.FONT_HEIGHT);
        if (height < this.height - 50)
            height = this.height - 50;
        return height;
	}

	@Override
	protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
		int PADDING = 0;
		for (ITextProperties line : lines)
        {
            if (line != null)
            {
                RenderSystem.enableBlend();
                fontRenderer.func_238407_a_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(line)), left + PADDING, relativeY, 0xFFFFFF);
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();
            }
            relativeY += fontRenderer.FONT_HEIGHT;
        }

//        final Style component = findTextLine(mouseX, mouseY);
//        if (component!=null) {
//            this.createWorldGui.func_238653_a_(mStack, component, mouseX, mouseY);
//        }
	}

    private List<ITextProperties> resizeContent(List<String> lines)
    {
        List<ITextProperties> ret = new ArrayList<>();
        for (String line : lines)
        {
            if (line == null)
            {
                ret.add(null);
                continue;
            }

            ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
            int maxTextLength = this.width - 12;
            if (maxTextLength >= 0)
            {
                ret.addAll(fontRenderer.getCharacterManager().func_238362_b_(chat, maxTextLength, Style.EMPTY));
            }
        }
        return ret;
    }

    private Style findTextLine(final int mouseX, final int mouseY) {
        double offset = (mouseY - top) + border + scrollDistance + 1;
        if (offset <= 0)
            return null;

        int lineIdx = (int) (offset / fontRenderer.FONT_HEIGHT);
        if (lineIdx >= lines.size() || lineIdx < 1)
            return null;

        ITextProperties line = lines.get(lineIdx-1);
        if (line != null)
        {
            return fontRenderer.getCharacterManager().func_238357_a_(line, mouseX);
        }
        return null;
    }
}
