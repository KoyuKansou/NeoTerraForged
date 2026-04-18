package raccoonman.reterraforged.world.worldgen.biome.modifier.fabric;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import raccoonman.reterraforged.world.worldgen.biome.modifier.Filter;
import raccoonman.reterraforged.world.worldgen.biome.modifier.Order;

record AddModifier(Order order, GenerationStep.Decoration step, Optional<Filter> biomes, HolderSet<PlacedFeature> features) implements FabricBiomeModifier {
	public static final MapCodec<AddModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Order.CODEC.fieldOf("order").forGetter(AddModifier::order),
		GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddModifier::step),
		Filter.CODEC.optionalFieldOf("biomes").forGetter(AddModifier::biomes),
		PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddModifier::features)
	).apply(instance, AddModifier::new));

	@Override
	public MapCodec<AddModifier> codec() {
		return CODEC;
	}

	@Override
	public void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext) {
		if(this.biomes.isPresent() && !this.biomes.get().test(selectionContext.getBiomeRegistryEntry())) {
			return;
		}

		BiomeModificationContext.GenerationSettingsContext genSettings = modificationContext.getGenerationSettings();
		
		for (Holder<PlacedFeature> holder : this.features) {
			Optional<ResourceKey<PlacedFeature>> key = holder.unwrapKey();
			if (key.isPresent()) {
				genSettings.addFeature(this.step, key.get());
			}
		}
	}
}
