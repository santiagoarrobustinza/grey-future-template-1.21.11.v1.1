package grey.future.world.gen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.block.BlockState;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Custom chunk generator for tall dimensions
 * Supports terrain up to thousands of blocks high
 */
public class CustomTallChunkGenerator extends ChunkGenerator {
    public static final MapCodec<CustomTallChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
                    MapCodec.assumeMapUnsafe(com.mojang.serialization.Codec.LONG).fieldOf("seed").forGetter(gen -> gen.seed)
            ).apply(instance, CustomTallChunkGenerator::new)
    );

    private final long seed;
    private final BiomeSource biomeSource;
    private static final int MAX_HEIGHT = 4064; // Thousands of blocks high

    public CustomTallChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource);
        this.biomeSource = biomeSource;
        this.seed = seed;
    }

    /**
     * Calculate terrain height using multiple noise layers
     * This creates varied, mountainous terrain up to 4000+ blocks high
     */
    private int calculateTerrainHeight(int x, int z) {
        // Base noise (very large features, 0-2000 blocks)
        double baseNoise = MathHelper.sin((x * 0.001f) + (z * 0.001f)) * 1000 + 1000;

        // Mid noise (large features, ±500 blocks)
        double midNoise = MathHelper.sin((x * 0.01f) + (z * 0.01f)) * 500;

        // Detail noise (medium features, ±200 blocks)
        double detailNoise = MathHelper.sin((x * 0.05f) + (z * 0.05f)) * 200;

        // Fine detail noise (small variations, ±100 blocks)
        double fineNoise = MathHelper.sin((x * 0.1f) + (z * 0.1f)) * 100;

        // Combine noises with weights
        double height = baseNoise * 0.5 + midNoise * 0.25 + detailNoise * 0.15 + fineNoise * 0.1;

        // Clamp to valid range (200-4000 blocks)
        height = Math.max(200, Math.min(MAX_HEIGHT, height));

        return (int) height;
    }

    @Override
    public MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
        // Custom carving logic (optional)
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        // Fill chunk with terrain
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                int terrainHeight = calculateTerrainHeight(worldX, worldZ);

                BlockPos.Mutable mutable = new BlockPos.Mutable(worldX, 0, worldZ);

                // Fill from bottom (-64) to terrain height
                for (int y = -64; y < terrainHeight; y++) {
                    mutable.setY(y);
                    if (y < terrainHeight - 5) {
                        chunk.setBlockState(mutable, Blocks.STONE.getDefaultState());
                    } else if (y < terrainHeight - 2) {
                        chunk.setBlockState(mutable, Blocks.DIRT.getDefaultState());
                    } else if (y == terrainHeight - 1) {
                        chunk.setBlockState(mutable, Blocks.GRASS_BLOCK.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        // Entity population (optional)
    }

    @Override
    public int getWorldHeight() {
        return MAX_HEIGHT;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 63; // Standard sea level
    }

    @Override
    public int getMinimumY() {
        return -64; // Standard minimum Y
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return calculateTerrainHeight(x, z);
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        int height = calculateTerrainHeight(x, z);
        BlockState[] states = new BlockState[world.getHeight()];

        for (int y = world.getBottomY(); y < world.getBottomY() + world.getHeight(); y++) {
            int index = y - world.getBottomY();
            if (index >= 0 && index < states.length) {
                if (y < height - 5) {
                    states[index] = Blocks.STONE.getDefaultState();
                } else if (y < height - 2) {
                    states[index] = Blocks.DIRT.getDefaultState();
                } else if (y == height - 1) {
                    states[index] = Blocks.GRASS_BLOCK.getDefaultState();
                } else {
                    states[index] = Blocks.AIR.getDefaultState();
                }
            }
        }

        return new VerticalBlockSample(world.getBottomY(), states);
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        text.add("Custom Tall Chunk Generator");
    }
}