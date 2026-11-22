package io.github.emberbocor.placingdispensers.placing;

import org.jetbrains.annotations.Nullable;

import io.github.emberbocor.placingdispensers.PlacingDispensers;
import io.github.emberbocor.placingdispensers.config.ServerConfig;
import io.github.emberbocor.placingdispensers.mixin.DispenserBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.GameMasterBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;

public final class BlockPlacing {
    private BlockPlacing() {
    }

    /** True when this dispense belongs to the mod: a dispenser's plain default behavior with a placeable block item. */
    public static boolean handles(BlockSource source, ItemStack stack, DefaultDispenseItemBehavior behavior) {
        return blockItem(source, stack, behavior) != null;
    }

    /** Places the block like a dispensed shulker box; on success one item has been consumed. */
    public static boolean place(BlockSource source, ItemStack stack, DefaultDispenseItemBehavior behavior) {
        BlockItem item = blockItem(source, stack, behavior);
        if (item == null) {
            return false;
        }
        ServerLevel level = source.level();
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(direction);
        // Slabs and snow layers recurse forever in DirectionalPlaceContext when the same block is in front.
        if (level.getBlockState(pos).is(item.getBlock())) {
            return false;
        }
        Direction face = level.isEmptyBlock(pos.below()) ? direction : Direction.UP;
        try {
            return item.place(new DirectionalPlaceContext(level, pos, direction, stack, face)).consumesAction();
        } catch (Exception e) {
            PlacingDispensers.LOGGER.error("Error trying to place {} at {}", stack, pos, e);
            return false;
        }
    }

    @Nullable
    private static BlockItem blockItem(BlockSource source, ItemStack stack, DefaultDispenseItemBehavior behavior) {
        ServerConfig.Settings settings = ServerConfig.settings();
        if (!settings.enabled() || !source.state().is(Blocks.DISPENSER)
                || behavior != DispenserBlockAccessor.placingdispensers$getDefaultBehavior()) {
            return null;
        }
        if (stack.getItem() instanceof BlockItem item && !(item instanceof GameMasterBlockItem)
                && !settings.excludes(item.getBlock())) {
            return item;
        }
        return null;
    }
}
