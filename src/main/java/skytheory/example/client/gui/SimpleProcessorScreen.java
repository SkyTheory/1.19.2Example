package skytheory.example.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import skytheory.example.ExampleMod;
import skytheory.example.block.entity.SimpleProcessorEntity;
import skytheory.example.gui.SimpleProcessorMenu;

public class SimpleProcessorScreen extends AbstractContainerScreen<SimpleProcessorMenu> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(ExampleMod.MODID, "textures/gui/simple_processor.png");

	public SimpleProcessorScreen(SimpleProcessorMenu menu, Inventory playerInv, Component component) {
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
		// レンダーの設定を行い
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		// 描画位置を取得して
		int fixedX = (this.width - this.imageWidth) / 2;
		int fixedY = (this.height - this.imageHeight) / 2;
		// 実際の描画を行う
		/**
		 * PoseStack：描画の位置やサイズなどを定義するオブジェクト
		 * int：横軸開始位置
		 * int：縦軸開始位置
		 * int：テクスチャのX（U）開始位置
		 * int：テクスチャのY（V）開始位置
		 * int：テクスチャの幅
		 * int：テクスチャの高さ
		 */
		this.blit(poseStack, fixedX, fixedY, 0, 0, this.imageWidth, this.imageHeight);
		// プログレスバーの描画
		int progressX = fixedX + 79;
		int progressY = fixedY + 35;
		int progressImageU = 176;
		int progressImageV = 0;
		int progressWidth = this.getProgressBarWidth();
		int progressHeight = 17;
		this.blit(poseStack, progressX, progressY, progressImageU, progressImageV, progressWidth, progressHeight);
	}

	private int getProgressBarWidth() {
		// 加工の進捗の割合を取得して
		int progress = this.menu.progressData.get();
		float ratio = (float) progress / (float) SimpleProcessorEntity.PROCESSING_TIME;
		// プログレスバーのテクスチャのサイズを掛ける
		return (int) (ratio * 24.0f);
	}

}
