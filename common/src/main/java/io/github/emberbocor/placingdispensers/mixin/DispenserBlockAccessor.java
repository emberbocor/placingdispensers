package io.github.emberbocor.placingdispensers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;

@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor {
    @Accessor("DEFAULT_BEHAVIOR")
    static DefaultDispenseItemBehavior placingdispensers$getDefaultBehavior() {
        throw new AssertionError();
    }
}
