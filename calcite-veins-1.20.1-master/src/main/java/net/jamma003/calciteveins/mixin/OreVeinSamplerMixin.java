package net.jamma003.calciteveins.mixin;

import net.jamma003.calciteveins.CalciteVeins;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.gen.OreVeinSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import com.hugman.universal_ores.registry.content.UniversalOresBlocks;

@Mixin(OreVeinSampler.class)
abstract class OreVeinSamplerMixin {
    /**
     * @author Jamma003
     * @reason I have no fucking idea what Im doing
     */
    @Overwrite
    public static ChunkNoiseSampler.BlockStateSampler create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, RandomSplitter randomDeriver) {
        // lord forgive me for what I am about to do
        // Index 0: ore, 1: rawOreBlock, 2: stone, 3: minY, 4: maxY
        Object[][] VEIN_TYPES = {
                {Blocks.STONE.getDefaultState(),
                        Blocks.STONE.getDefaultState(),
                        Blocks.STONE.getDefaultState(),
                        0, 50},  // Copper

                {Blocks.DEEPSLATE.getDefaultState(),
                        Blocks.DEEPSLATE.getDefaultState(),
                        Blocks.DEEPSLATE.getDefaultState(),
                        -60, -8}  // Iron
        };
        BlockState blockState = null;
        return (pos) -> {
            double d = veinToggle.sample(pos);
            int i = pos.blockY();
            Object[] veinType = d > (double) 0.0F ? VEIN_TYPES[0] : VEIN_TYPES[1];
            double e = Math.abs(d);
            int j = (int) veinType[4] - i;
            int k = i - (int) veinType[3];
            if (k >= 0 && j >= 0) {
                int l = Math.min(j, k);
                double f = MathHelper.clampedMap((double) l, (double) 0.0F, (double) 20.0F, -0.2, (double) 0.0F);
                if (e + f < (double) 0.4F) {
                    return blockState;
                } else {
                    Random random = randomDeriver.split(pos.blockX(), i, pos.blockZ());
                    if (random.nextFloat() > 0.7F) {
                        return blockState;
                    } else if (veinRidged.sample(pos) >= (double) 0.0F) {
                        return blockState;
                    } else {
                        double g = MathHelper.clampedMap(e, (double) 0.4F, (double) 0.6F, (double) 0.1F, (double) 0.3F);
                        if ((double) random.nextFloat() < g && veinGap.sample(pos) > (double) -0.3F) {
                            return random.nextFloat() < 0.02F ? (BlockState) veinType[1] : (BlockState) veinType[0];
                        } else {
                            return (BlockState) veinType[2];
                        }
                    }
                }
            } else {
                return blockState;
            }
        };
    }
}