package raccoonman.reterraforged.client.gui.widget;

import java.util.function.Supplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class Label extends Button {
    
	public Label(int x, int y, int width, int height, Component component) {
    	this(x, y, width, height, (b) -> {}, component);
	}
	
	public Label(int x, int y, int width, int height, OnPress onPress, Component component) {
    	super(x, y, width, height, component, onPress, Supplier::get);
	}

	@Override
    public void playDownSound(SoundManager soundManager) {
    }

	@Override
	public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		// 1.21.11: renderWidget() no longer draws the background; renderContents must do it
		this.renderDefaultSprite(guiGraphics);
		this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
	}
}
