package grey.future.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
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
import grey.future.block.ModBlocks;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Custom chunk generator for tall dimensions with random cubic structures
 */
public class CustomTallChunkGenerator extends ChunkGenerator {
    public static final MapCodec<CustomTallChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
                    Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed)
            ).apply(instance, CustomTallChunkGenerator::new)
    );

    public final long seed;
    private final BiomeSource biomeSource;
    private static final int MAX_HEIGHT = 2096;
    private static final int MIN_Y = -64;

    public CustomTallChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource);
        this.biomeSource = biomeSource;
        this.seed = seed;
    }

    private Random getRandomForBlock(int x, int y, int z) {
        Random random = new Random(seed);
        random.setSeed(seed ^ ((long)x * 73856093L ^ (long)y * 19349663L ^ (long)z * 83492791L));
        return random;
    }

    @Override
    public MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
        // No carving
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        // Fill chunk with random cubic structures
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = MIN_Y; y < MIN_Y + MAX_HEIGHT; y++) {
                    int worldX = chunkX * 16 + x;
                    int worldZ = chunkZ * 16 + z;

                    // Snap to structure origin
                    int structureX = worldX / 4; // Assuming size 4 as base
                    int structureY = y / 4;
                    int structureZ = worldZ / 4;

                    // Get random for this structure (ONCE per structure)
                    Random structureRandom = new Random(seed);
                    structureRandom.setSeed(seed ^ ((long)structureX * 73856093L ^ (long)structureY * 19349663L ^ (long)structureZ * 83492791L));

                    // Calculate size once per structure
                    int roll = structureRandom.nextInt(100);
                    int exponent;
                    if (roll < 30) exponent = 0;           // 5% chance: 2^0 = 1
                    else if (roll < 55) exponent = 1;     // 15% chance: 2^1 = 2
                    else if (roll < 70) exponent = 2;     // 15% chance: 2^2 = 4 (peak around 3)
                    else if (roll < 75) exponent = 3;     // 13% chance: 2^3 = 8
                    else if (roll < 79) exponent = 4;     // 10% chance: 2^4 = 16
                    else if (roll < 83) exponent = 5;     // 8% chance: 2^5 = 32
                    else if (roll < 85) exponent = 6;     // 6% chance: 2^6 = 64
                    else if (roll < 87) exponent = 7;     // 5% chance: 2^7 = 128
                    else if (roll < 90) exponent = 8;     // 4% chance: 2^8 = 256
                    else exponent = 9 + structureRandom.nextInt(5);

                    int structureSize = (int) Math.pow(2, exponent);

                    // Re-snap with actual size
                    int actualStructureX = (worldX / structureSize) * structureSize;
                    int actualStructureY = (y / structureSize) * structureSize;
                    int actualStructureZ = (worldZ / structureSize) * structureSize;

                    // Fill or air
                    boolean fillBlock = structureRandom.nextInt(10) < 3;

                    BlockPos pos = new BlockPos(worldX, y, worldZ);
                    BlockState state = fillBlock ? ModBlocks.GREY_GOO.getDefaultState() : Blocks.AIR.getDefaultState();
                    chunk.setBlockState(pos, state);
                }
            }
        }
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        // No entity population
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
        return MIN_Y;
    }

    @Override
    public int getMinimumY() {
        return MIN_Y;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return MIN_Y + 64;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        BlockState[] states = new BlockState[world.getHeight()];
        for (int i = 0; i < states.length; i++) {
            states[i] = Blocks.AIR.getDefaultState();
        }
        return new VerticalBlockSample(world.getBottomY(), states);
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        text.add("Random Cubic Structure Generator");
    }
}