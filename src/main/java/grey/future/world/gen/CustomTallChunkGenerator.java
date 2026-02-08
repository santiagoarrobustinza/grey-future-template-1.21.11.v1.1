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

                    // Get random for this position to determine structure size
                    Random random = getRandomForBlock(worldX, y, worldZ);
                    int structureSize = 1 + random.nextInt(1000); // 1-1000 block sizes

                    // Snap to structure origin
                    int structureX = (worldX / structureSize) * structureSize;
                    int structureY = (y / structureSize) * structureSize;
                    int structureZ = (worldZ / structureSize) * structureSize;

                    // Get random for this structure
                    Random structureRandom = new Random(seed);
                    structureRandom.setSeed(seed ^ ((long)structureX * 73856093L ^ (long)structureY * 19349663L ^ (long)structureZ * 83492791L));

                    // Bias towards air (70% air, 30% grey_goo)
                    boolean fillBlock = structureRandom.nextInt(10) < 3; // 30% chance for grey_goo

                    BlockPos pos = new BlockPos(worldX, y, worldZ);
                    BlockState state = fillBlock ? ModBlocks.GREY_GOO.getDefaultState() : Blocks.AIR.getDefaultState();
                    chunk.setBlockState(pos, state);
                }
            }
        }

        // Remove geodes
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = MIN_Y; y < MIN_Y + MAX_HEIGHT; y++) {
                    BlockPos pos = new BlockPos(chunk.getPos().getStartX() + x, y, chunk.getPos().getStartZ() + z);
                    BlockState state = chunk.getBlockState(pos);
                    if (state.getBlock() == Blocks.AMETHYST_BLOCK || state.getBlock() == Blocks.BUDDING_AMETHYST) {
                        chunk.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
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