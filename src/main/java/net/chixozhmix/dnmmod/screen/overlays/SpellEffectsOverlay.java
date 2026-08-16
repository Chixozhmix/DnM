package net.chixozhmix.dnmmod.screen.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.chixozhmix.dnmmod.DnMmod;
import net.chixozhmix.dnmmod.registers.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SpellEffectsOverlay implements IGuiOverlay {
    public static final SpellEffectsOverlay instance = new SpellEffectsOverlay();

    public final static ResourceLocation MANA_SHIELD_TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mana_shield/frame_0.png");
    public final static ResourceLocation MAGE_ARMOR_TEXTURE = ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mage_armor_overlay.png");

    public static final ResourceLocation[] MANA_SHIELD_FRAMES = {
            ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mana_shield/frame_0.png"),
            ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mana_shield/frame_1.png"),
            ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mana_shield/frame_2.png"),
            ResourceLocation.fromNamespaceAndPath(DnMmod.MOD_ID, "textures/gui/overlays/mana_shield/frame_3.png")
    };

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (Minecraft.getInstance().options.hideGui || Minecraft.getInstance().player.isSpectator())
            return;

        Player player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(player.hasEffect(ModEffects.MANA_SHIELD.get())) {
            renderOverlayAdditive(guiGraphics, MANA_SHIELD_TEXTURE, 0.129f, 0.431f, 0.929f, 0.25f, screenWidth, screenHeight);
            //renderAnimatedOverlay(guiGraphics, MANA_SHIELD_FRAMES, 0.129f, 0.431f, 0.929f, 0.25f, screenWidth, screenHeight, 5);
        }
        if (player.hasEffect(ModEffects.MAGE_ARMOR.get()) && !player.hasEffect(ModEffects.MANA_SHIELD.get())) {
            renderOverlayAdditive(guiGraphics, MAGE_ARMOR_TEXTURE, 0.129f, 0.431f, 0.929f, 0.25f, screenWidth, screenHeight);
        }
    }

    private static void renderOverlayAdditive(GuiGraphics gui, ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE
        );
        gui.setColor(r, g, b, a);
        gui.blit(texture, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    private static void renderOverlay(GuiGraphics gui, ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        gui.setColor(r, g, b, a);
        gui.blit(texture, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderAnimatedOverlay(GuiGraphics gui, ResourceLocation[] frames, float r, float g, float b, float a, int screenWidth, int screenHeight, int ticksPerFrame) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null)
            return;

        long gameTime = minecraft.level.getGameTime();

        int frame = (int) ((gameTime / ticksPerFrame) % frames.length);

        renderOverlayAdditive(
                gui,
                frames[frame],
                r, g, b, a,
                screenWidth,
                screenHeight
        );
    }
}
