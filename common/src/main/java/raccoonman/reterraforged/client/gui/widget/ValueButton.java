package raccoonman.reterraforged.client.gui.widget;

import java.util.function.Supplier;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ValueButton<T> extends Button {
	private Component name;
	private T value;
	
	public ValueButton(int i, int j, int k, int l, Component component, OnPress onPress, T initial) {
		super(i, j, k, l, component, onPress, Supplier::get);

		this.name = component;
		this.setValue(initial);
	}
	
	public ValueButton(int i, int j, int k, int l, Component component, OnPress onPress, CreateNarration createNarration, T initial) {
		super(i, j, k, l, component, onPress, createNarration);

		this.name = component;
		this.setValue(initial);
	}
	
	@Override
	public void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTicks) {
		this.extractDefaultSprite(guiGraphicsExtractor);
		ActiveTextCollector text = guiGraphicsExtractor.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE);
		this.extractDefaultLabel(text);
	}

	public void setValue(T value) {
		this.value = value;
		
		this.setMessage(CommonComponents.optionNameValue(this.name, Component.literal(this.value.toString())));
	}
	
	public T getValue() {
		return this.value;
	}
}
