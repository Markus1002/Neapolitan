package com.minecraftabnormals.neapolitan.common.world.gen;

import com.minecraftabnormals.neapolitan.common.block.StrawberryBushBlock;
import com.minecraftabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.minecraftabnormals.neapolitan.core.registry.NeapolitanFeatures;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class NeapolitanBiomeFeatures {

	public static final BlockClusterFeatureConfig STRAWBERRY_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(NeapolitanBlocks.STRAWBERRY_BUSH.get().getDefaultState().with(StrawberryBushBlock.TYPE, StrawberryBushBlock.StrawberryType.RED)), SimpleBlockPlacer.field_236447_c_)).tries(512).build();
	public static final BlockClusterFeatureConfig VANILLA_VINE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(NeapolitanBlocks.VANILLA_VINE.get().getDefaultState()), SimpleBlockPlacer.field_236447_c_)).tries(64).build();

	public static void generateFeatures() {
		ForgeRegistries.BIOMES.getValues().forEach(NeapolitanBiomeFeatures::generate);
	}

	public static void generate(Biome biome) {
		if (biome.getCategory() == Biome.Category.PLAINS) {
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NeapolitanFeatures.STRAWBERRY_PATCH.get().withConfiguration(STRAWBERRY_PATCH_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(34))));
		}

		if (biome.getCategory() == Biome.Category.SAVANNA) {
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NeapolitanFeatures.VANILLA_PATCH.get().withConfiguration(VANILLA_VINE_PATCH_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(28))));
		}

		if (biome.getPrecipitation() == RainType.RAIN) {
			if (biome.getCategory() == Biome.Category.BEACH)
				addBananaPlants(biome, 0, 0.15F, 1); // Beach Biomes

			if (biome.getCategory() == Biome.Category.JUNGLE) {
				if (notJungleEdge(biome) && notBambooJungle(biome))
					addBananaPlants(biome, 1, 0.3F, 3); // Jungle Biomes
				else if (notBambooJungle(biome))
					addBananaPlants(biome, 1, 0.15F, 2); // Jungle Edge Biomes
			}

			if (biome.getRegistryName().getPath().contains("rainforest")) {
				if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST))
					addBananaPlants(biome, 1, 0.1F, 2); // Rainforest Biomes
				else
					addBananaPlants(biome, 0, 0.3F, 1); // Sparse Rainforest Plateau Biomes
			}
		}
	}

	private static void addBananaPlants(Biome biome, int count, float extraChanceIn, int extraCountIn) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, NeapolitanFeatures.BANANA_PLANT.get().withConfiguration(NoFeatureConfig.field_236559_b_).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, extraChanceIn, extraCountIn))));
	}

	private static boolean notJungleEdge(Biome biome) {
		return biome != Biomes.JUNGLE_EDGE && biome != Biomes.MODIFIED_JUNGLE_EDGE;
	}

	private static boolean notBambooJungle(Biome biome) {
		return biome != Biomes.BAMBOO_JUNGLE && biome != Biomes.BAMBOO_JUNGLE_HILLS;
	}
}
