/*
 * Decompiled with CFR 0_123.
 *
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.init.Blocks
 */
package mchelio.wrapper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Blocks;

public abstract class W_Block
        extends Block {
    protected W_Block(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    public static Block getBlockFromName(String name) {
        return Block.getBlockFromName((String)name);
    }

    public static Block getSnowLayer() {
        return Blocks.snow_layer;
    }

    public static boolean isNull(Block block) {
        return block == null || block == Blocks.air;
    }

    public static boolean isEqual(int blockId, Block block) {
        return Block.isEqualTo((Block)Block.getBlockById((int)blockId), (Block)block);
    }

    public static boolean isEqual(Block block1, Block block2) {
        return Block.isEqualTo((Block)block1, (Block)block2);
    }

    public static Block getWater() {
        return Blocks.water;
    }

    public static Block getBlockById(int i) {
        return Block.getBlockById((int)i);
    }
}

