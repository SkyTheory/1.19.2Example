package skytheory.example.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import skytheory.example.ExampleMod;
import skytheory.example.gui.DisplayStandMenu;

public class DisplayStandScreen extends AbstractContainerScreen<DisplayStandMenu> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExampleMod.MODID, "textures/gui/display_stand.png");

	public DisplayStandScreen(DisplayStandMenu menu, Inventory playerInv, Component component) {
		super(menu, playerInv, component);
		// デフォルト値だが、明示的に宣言しておけばコードを見返したときに理解しやすい
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	/**
	 * 背景を描画し、内容を描画し、マウスオーバー時の説明を描画する
	 */
	public void render(PoseStack poseStack, int x, int y, float ticks) {
		this.renderBackground(poseStack);
		super.render(poseStack, x, y, ticks);
		this.renderTooltip(poseStack, x, y);
	}

	/**
	 * renderBackgroundから呼ばれてGUIの背景画像を描画する
	 */
	@Override
	protected void renderBg(PoseStack poseStack, float ticks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		int fixedX = (this.width - this.imageWidth) / 2;
		int fixedY = (this.height - this.imageHeight) / 2;
		this.blit(poseStack, fixedX, fixedY, 0, 0, this.imageWidth, this.imageHeight);
	}

}
