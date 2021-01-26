package com.bloodnbonesgaming.topography.client.gui;

import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;

public class PresetListWidget extends ExtendedList<PresetListWidget.PresetEntry>
{
    private final int listWidth;
    private FontRenderer fontRenderer;
    private GuiCreateWorld parent;

    public PresetListWidget(GuiCreateWorld parent, int listWidth, int top, int bottom, FontRenderer fontRenderer)
    {
        super(Minecraft.getInstance(), listWidth, parent.height, top, bottom, fontRenderer.FONT_HEIGHT * 2 + 8);
        int left = parent.width - listWidth;
        this.x0 = left;
        this.x1 = left + listWidth;
        this.parent = parent;
        this.listWidth = listWidth;
        this.fontRenderer = fontRenderer;
        this.refreshList();
        this.changeFocus(true);
        if (this.getSelected() != null) {
            this.parent.selected(this.getSelected().preset);
        }
    }

    @Override
    protected int getScrollbarPosition()
    {
        //return 100 + this.listWidth;
    	return this.x1;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        GlobalConfig global = ConfigurationManager.getGlobalConfig();
        
        if (global != null) {
        	for(Entry<String, Preset> entry : global.presets.entrySet()) {
                this.addEntry(new PresetEntry(entry.getValue(), this.fontRenderer));
        	}
        }
    }

    @Override
    protected void renderBackground(MatrixStack mStack)
    {
        //this.parent.func_230446_a_(mStack);
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
       this.renderBackground(p_230430_1_);
       int i = this.getScrollbarPosition();
       int j = i + 6;
       Tessellator tessellator = Tessellator.getInstance();
       BufferBuilder bufferbuilder = tessellator.getBuffer();
       //this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
       RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
       float f = 32.0F;
       if (this.selected != null) {
           RenderSystem.enableBlend();
           bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
           bufferbuilder.pos((double)this.x0, (double)this.y1, 0.0D).color(32, 32, 32, 125).endVertex();
           bufferbuilder.pos((double)this.x1, (double)this.y1, 0.0D).color(32, 32, 32, 125).endVertex();
           bufferbuilder.pos((double)this.x1, (double)this.y0, 0.0D).color(32, 32, 32, 125).endVertex();
           bufferbuilder.pos((double)this.x0, (double)this.y0, 0.0D).color(32, 32, 32, 125).endVertex();
           tessellator.draw();
           RenderSystem.disableBlend();
       }
       int k = this.getRowLeft();
       int l = this.y0 + 4 - (int)this.getScrollAmount();
//       if (this.field_230680_q_) {
//          this.func_230448_a_(p_230430_1_, k, l, tessellator);
//       }

       this.renderList(p_230430_1_, k, l, p_230430_2_, p_230430_3_, p_230430_4_);
//       this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
//       RenderSystem.enableDepthTest();
//       RenderSystem.depthFunc(519);
//       float f1 = 32.0F;
//       int i1 = -100;
//       bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//       bufferbuilder.pos((double)this.x0, (double)this.y0, -100.0D).tex(0.0F, (float)this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)(this.x0 + this.width), (double)this.y0, -100.0D).tex((float)this.width / 32.0F, (float)this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)(this.x0 + this.width), 0.0D, -100.0D).tex((float)this.width / 32.0F, 0.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)this.x0, 0.0D, -100.0D).tex(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)this.x0, (double)this.height, -100.0D).tex(0.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)(this.x0 + this.width), (double)this.height, -100.0D).tex((float)this.width / 32.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)(this.x0 + this.width), (double)this.y1, -100.0D).tex((float)this.width / 32.0F, (float)this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
//       bufferbuilder.pos((double)this.x0, (double)this.y1, -100.0D).tex(0.0F, (float)this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
//       tessellator.draw();
//       RenderSystem.depthFunc(515);
//       RenderSystem.disableDepthTest();
       RenderSystem.enableBlend();
       RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
       RenderSystem.disableAlphaTest();
       RenderSystem.shadeModel(7425);
       RenderSystem.disableTexture();
       int j1 = 4;
       bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
       bufferbuilder.pos((double)this.x0, (double)(this.y0 + 4), 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 0).endVertex();
       bufferbuilder.pos((double)this.x1, (double)(this.y0 + 4), 0.0D).tex(1.0F, 1.0F).color(0, 0, 0, 0).endVertex();
       bufferbuilder.pos((double)this.x1, (double)this.y0, 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
       bufferbuilder.pos((double)this.x0, (double)this.y0, 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
       bufferbuilder.pos((double)this.x0, (double)this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
       bufferbuilder.pos((double)this.x1, (double)this.y1, 0.0D).tex(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
       bufferbuilder.pos((double)this.x1, (double)(this.y1 - 4), 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 0).endVertex();
       bufferbuilder.pos((double)this.x0, (double)(this.y1 - 4), 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 0).endVertex();
       tessellator.draw();
       int k1 = Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
       if (k1 > 0) {
          int l1 = (int)((float)((this.y1 - this.y0) * (this.y1 - this.y0)) / (float)this.getMaxPosition());
          l1 = MathHelper.clamp(l1, 32, this.y1 - this.y0 - 8);
          int i2 = (int)this.getScrollAmount() * (this.y1 - this.y0 - l1) / k1 + this.y0;
          if (i2 < this.y0) {
             i2 = this.y0;
          }

          bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
          bufferbuilder.pos((double)i, (double)this.y1, 0.0D).tex(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
          bufferbuilder.pos((double)j, (double)this.y1, 0.0D).tex(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
          bufferbuilder.pos((double)j, (double)this.y0, 0.0D).tex(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
          bufferbuilder.pos((double)i, (double)this.y0, 0.0D).tex(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
          bufferbuilder.pos((double)i, (double)(i2 + l1), 0.0D).tex(0.0F, 1.0F).color(128, 128, 128, 255).endVertex();
          bufferbuilder.pos((double)j, (double)(i2 + l1), 0.0D).tex(1.0F, 1.0F).color(128, 128, 128, 255).endVertex();
          bufferbuilder.pos((double)j, (double)i2, 0.0D).tex(1.0F, 0.0F).color(128, 128, 128, 255).endVertex();
          bufferbuilder.pos((double)i, (double)i2, 0.0D).tex(0.0F, 0.0F).color(128, 128, 128, 255).endVertex();
          bufferbuilder.pos((double)i, (double)(i2 + l1 - 1), 0.0D).tex(0.0F, 1.0F).color(192, 192, 192, 255).endVertex();
          bufferbuilder.pos((double)(j - 1), (double)(i2 + l1 - 1), 0.0D).tex(1.0F, 1.0F).color(192, 192, 192, 255).endVertex();
          bufferbuilder.pos((double)(j - 1), (double)i2, 0.0D).tex(1.0F, 0.0F).color(192, 192, 192, 255).endVertex();
          bufferbuilder.pos((double)i, (double)i2, 0.0D).tex(0.0F, 0.0F).color(192, 192, 192, 255).endVertex();
          tessellator.draw();
       }

       this.renderDecorations(p_230430_1_, p_230430_2_, p_230430_3_);
       RenderSystem.enableTexture();
       RenderSystem.shadeModel(7424);
       RenderSystem.enableAlphaTest();
       RenderSystem.disableBlend();
    }

//    @Override
//    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
//       this.func_230947_b_(p_231044_1_, p_231044_3_, p_231044_5_);
//       if (!this.func_231047_b_(p_231044_1_, p_231044_3_)) {
//          return false;
//       } else {
//          PresetEntry e = this.getAtPos(p_231044_1_, p_231044_3_);
//          if (e != null) {
//             if (e.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
//                this.func_231035_a_(e);
//                this.func_231037_b__(true);
//                return true;
//             }
//          } else if (p_231044_5_ == 0) {
//             this.func_230938_a_((int)(p_231044_1_ - (double)(this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(p_231044_3_ - (double)this.y0) + (int)this.getScrollAmount() - 4);
//             return true;
//          }
//
//          return this.field_230681_r_;
//       }
//    }

//    @Override
//    protected void renderList(MatrixStack p_238478_1_, int p_238478_2_, int p_238478_3_, int p_238478_4_, int p_238478_5_, float p_238478_6_) {
//       int i = this.func_230965_k_();
//       Tessellator tessellator = Tessellator.getInstance();
//       BufferBuilder bufferbuilder = tessellator.getBuffer();
//
//       for(int j = 0; j < i; ++j) {
//          int k = this.func_230962_i_(j);
//          int l = this.func_230962_i_(j) + this.field_230669_c_;
//          if (l >= this.y0 && k <= this.y1) {
//             int i1 = p_238478_3_ + j * this.field_230669_c_ + this.field_230677_n_;
//             int j1 = this.field_230669_c_ - 4;
//             PresetEntry e = this.func_230953_d_(j);
//             int k1 = this.getRowWidth();
//             if (this.func_230957_f_(j)) {
//                int l1 = this.x0 + this.width / 2 - k1 / 2;
//                int i2 = this.x0 + this.width / 2 + k1 / 2;
//                RenderSystem.disableTexture();
//                float f = this.func_230971_aw__() ? 1.0F : 0.5F;
//                RenderSystem.color4f(f, f, f, 1.0F);
//                bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
//                bufferbuilder.pos((double)l1, (double)(i1 + j1 + 2), 0.0D).endVertex();
//                bufferbuilder.pos((double)i2, (double)(i1 + j1 + 2), 0.0D).endVertex();
//                bufferbuilder.pos((double)i2, (double)(i1 - 2), 0.0D).endVertex();
//                bufferbuilder.pos((double)l1, (double)(i1 - 2), 0.0D).endVertex();
//                tessellator.draw();
//                RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//                bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
//                bufferbuilder.pos((double)(l1 + 1), (double)(i1 + j1 + 1), 0.0D).endVertex();
//                bufferbuilder.pos((double)(i2 - 1), (double)(i1 + j1 + 1), 0.0D).endVertex();
//                bufferbuilder.pos((double)(i2 - 1), (double)(i1 - 1), 0.0D).endVertex();
//                bufferbuilder.pos((double)(l1 + 1), (double)(i1 - 1), 0.0D).endVertex();
//                tessellator.draw();
//                RenderSystem.enableTexture();
//             }
//
//             int j2 = this.func_230968_n_();
//             e.render(p_238478_1_, j, k, j2, k1, j1, p_238478_4_, p_238478_5_, this.func_231047_b_((double)p_238478_4_, (double)p_238478_5_) && Objects.equals(this.getAtPos((double)p_238478_4_, (double)p_238478_5_), e), p_238478_6_);
//          }
//       }
//
//    }

//    @Nullable
//    protected final PresetEntry getAtPos(double p_230933_1_, double p_230933_3_) {
//       int i = this.getRowWidth() / 2;
//       int j = this.x0 + this.width / 2;
//       int k = j - i;
//       int l = j + i;
//       int i1 = MathHelper.floor(p_230933_3_ - (double)this.y0) - this.field_230677_n_ + (int)this.getScrollAmount() - 4;
//       int j1 = i1 / this.field_230669_c_;
//       return (PresetEntry)(p_230933_1_ < (double)this.getScrollbarPosition() && p_230933_1_ >= (double)k && p_230933_1_ <= (double)l && j1 >= 0 && i1 >= 0 && j1 < this.func_230965_k_() ? this.func_231039_at__().get(j1) : null);
//    }

    public class PresetEntry extends ExtendedList.AbstractListEntry<PresetEntry> {
        private final FontRenderer fontRenderer;
        public final Preset preset;

        PresetEntry(Preset preset, FontRenderer fontRenderer) {
            this.fontRenderer = fontRenderer;
            this.preset = preset;
        }

        @Override
        public void render(MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks)
        {
            ITextComponent name = new StringTextComponent(preset.displayName);
            FontRenderer font = fontRenderer;
            font.func_238422_b_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(font.func_238417_a_(name, listWidth))),left + 3, top + 2, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
        {
        	Topography.getLog().info("Selected: " + this.preset);
            PresetListWidget.this.parent.selected(preset);
            PresetListWidget.this.setSelected(this);
            return false;
        }
        
        @Override
        public boolean changeFocus(boolean focus) {
        	Topography.getLog().info("Selected: " + this.preset);
            PresetListWidget.this.parent.selected(preset);
            PresetListWidget.this.setSelected(this);
        	return true;
        }
        
//        @Override
//        public boolean func_231047_b_(double p_231047_1_, double p_231047_3_) {
//            return Objects.equals(PresetListWidget.this.getAtPos(p_231047_1_, p_231047_3_), this);
//         }
    }
}
