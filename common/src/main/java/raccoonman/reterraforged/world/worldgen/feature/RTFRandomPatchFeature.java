package raccoonman.reterraforged.world.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class RTFRandomPatchFeature extends Feature<RTFRandomPatchFeature.Config> {
	public RTFRandomPatchFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> ctx) {
		Config config = ctx.config();
		RandomSource random = ctx.random();
		BlockPos origin = ctx.origin();
		WorldGenLevel level = ctx.level();
		int placed = 0;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int xzSpread = config.xzSpread() + 1;
		int ySpread = config.ySpread() + 1;

		for(int i = 0; i < config.tries(); i++) {
			pos.setWithOffset(origin,
				random.nextInt(xzSpread) - random.nextInt(xzSpread),
				random.nextInt(ySpread) - random.nextInt(ySpread),
				random.nextInt(xzSpread) - random.nextInt(xzSpread)
			);

			if(config.feature().value().place(level, ctx.chunkGenerator(), random, pos)) {
				placed++;
			}
		}

		return placed > 0;
	}

	public record Config(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("tries").forGetter(Config::tries),
			Codec.INT.fieldOf("xz_spread").orElse(7).forGetter(Config::xzSpread),
			Codec.INT.fieldOf("y_spread").orElse(3).forGetter(Config::ySpread),
			PlacedFeature.CODEC.fieldOf("feature").forGetter(Config::feature)
		).apply(instance, Config::new));
	}
}
