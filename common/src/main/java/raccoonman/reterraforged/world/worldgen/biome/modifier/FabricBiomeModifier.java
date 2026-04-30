package raccoonman.reterraforged.world.worldgen.biome.modifier;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import raccoonman.reterraforged.mixin.MixinBiomeGenerationSettings;

public interface FabricBiomeModifier extends BiomeModifier {
	void apply(BiomeSelectionContext selectionContext, BiomeModificationContext modificationContext);
	
	default void rebuildFlowerFeatures(BiomeGenerationSettings generationSettings) {
		if(generationSettings instanceof MixinBiomeGenerationSettings biomeGenerationSettings) {
			Supplier<List<ConfiguredFeature<?, ?>>> supplier = () -> {
				return biomeGenerationSettings.getFeatures().stream()
					.flatMap(HolderSet::stream)
					.map(Holder::value)
					.flatMap(PlacedFeature::getFeatures)
					.map(Holder::value)
					.filter((cf) -> cf.feature().getClass().getSimpleName().toLowerCase().contains("flower"))
					.collect(ImmutableList.toImmutableList());
			};
			biomeGenerationSettings.setBoneMealFeatures(Suppliers.memoize(supplier::get));
		}
	}
}
