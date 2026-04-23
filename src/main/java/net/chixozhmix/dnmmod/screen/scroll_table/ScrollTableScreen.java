package net.chixozhmix.dnmmod.screen.scroll_table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.chixozhmix.dnmmod.DnMmod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScrollTableScreen extends AbstractContainerScreen<ScrolltableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(DnMmod.MOD_ID, "textures/gui/scroll_table_gui.png");

    public ScrollTableScreen(ScrolltableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.pose().pushPose();
        float scale = 0.7f;

        pGuiGraphics.pose().scale(scale, scale, 1f);

        int x = (int)(this.titleLabelX / scale);
        int y = (int)(this.titleLabelY / scale);

        pGuiGraphics.drawString(this.font, this.title, x, y, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        pGuiGraphics.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
