package raccoonman.reterraforged.client.gui.widget;

import java.util.function.Supplier;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
	public void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
		this.extractDefaultSprite(guiGraphicsExtractor);
		ActiveTextCollector text = guiGraphicsExtractor.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE);
		this.extractDefaultLabel(text);
	}
}
