package raccoonman.reterraforged.data.worldgen.tags;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.world.level.levelgen.DensityFunction;
import raccoonman.reterraforged.data.worldgen.compat.terrablender.TBNoiseRouterData;
import raccoonman.reterraforged.data.worldgen.preset.PresetNoiseRouterData;
import raccoonman.reterraforged.tags.RTFDensityFunctionTags;

public class RTFDensityFunctionTagsProvider extends TagsProvider<DensityFunction> {

	public RTFDensityFunctionTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(packOutput, Registries.DENSITY_FUNCTION, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		TagBuilder builder = this.getOrCreateRawBuilder(RTFDensityFunctionTags.ADDITIONAL_NOISE_ROUTER_FUNCTIONS);
		addKey(builder, PresetNoiseRouterData.GRADIENT);
		addKey(builder, PresetNoiseRouterData.HEIGHT_EROSION);
		addKey(builder, PresetNoiseRouterData.SEDIMENT);
		addKey(builder, TBNoiseRouterData.UNIQUENESS);
	}

	private static void addKey(TagBuilder builder, ResourceKey<DensityFunction> key) {
		builder.addElement(key.identifier());
	}
}
