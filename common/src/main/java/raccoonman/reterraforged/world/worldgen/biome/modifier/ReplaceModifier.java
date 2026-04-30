package raccoonman.reterraforged.world.worldgen.biome.modifier;

import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

record ReplaceModifier(GenerationStep.Decoration step, Optional<HolderSet<Biome>> biomes, Map<ResourceKey<PlacedFeature>, Holder<PlacedFeature>> replacements) implements FabricBiomeModifier {
	public static final MapCodec<ReplaceModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(ReplaceModifier::step),
		Biome.LIST_CODEC.optionalFieldOf("biomes").forGetter(ReplaceModifier::biomes),
		Codec.unboundedMap(ResourceKey.codec(Registries.PLACED_FEATURE), PlacedFeature.CODEC).fieldOf("replacements").forGetter(ReplaceModifier::replacements)
	).apply(instance, ReplaceModifier::new));

	@Override
	public void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext) {
		if(this.biomes.isPresent() && !this.biomes.get().contains(selectionContext.getBiomeHolder())) {
			return;
		}
		
		BiomeModificationContext.GenerationSettingsContext genSettings = modificationContext.getGenerationSettings();
		
		for (Map.Entry<ResourceKey<PlacedFeature>, Holder<PlacedFeature>> entry : this.replacements.entrySet()) {
			ResourceKey<PlacedFeature> oldKey = entry.getKey();
			Holder<PlacedFeature> newHolder = entry.getValue();
		
			genSettings.removeFeature(this.step, oldKey);
			
			Optional<ResourceKey<PlacedFeature>> newKey = newHolder.unwrapKey();
			if (newKey.isPresent()) {
				genSettings.addFeature(this.step, newKey.get());
			}
		}
	}

	@Override
	public MapCodec<ReplaceModifier> codec() {
		return CODEC;
	}
}
