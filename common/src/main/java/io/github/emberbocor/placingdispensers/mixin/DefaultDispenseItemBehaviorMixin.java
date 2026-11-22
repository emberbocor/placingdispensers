package io.github.emberbocor.placingdispensers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.emberbocor.placingdispensers.placing.BlockPlacing;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

@Mixin(DefaultDispenseItemBehavior.class)
public abstract class DefaultDispenseItemBehaviorMixin {
    @Unique
    private boolean placingdispensers$kept;

    @Shadow
    protected abstract void playSound(BlockSource blockSource);

    @Shadow
    protected abstract void playAnimation(BlockSource blockSource, Direction direction);

    // dispense runs before execute, so placing comes ahead of every execute handler (e.g. Ranching Dispensers).
    @Inject(method = "dispense", at = @At("HEAD"), cancellable = true)
    private void placingdispensers$place(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        placingdispensers$kept = false;
        if (BlockPlacing.place(source, stack, (DefaultDispenseItemBehavior) (Object) this)) {
            playSound(source);
            playAnimation(source, source.state().getValue(DispenserBlock.FACING));
            cir.setReturnValue(stack);
        }
    }

    // Reached only when no execute handler took the item: keep it instead of throwing it out.
    @Inject(method = "execute", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private void placingdispensers$keep(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (BlockPlacing.handles(source, stack, (DefaultDispenseItemBehavior) (Object) this)) {
            placingdispensers$kept = true;
            cir.setReturnValue(stack);
        }
    }

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void placingdispensers$failSound(BlockSource source, CallbackInfo ci) {
        if (placingdispensers$kept) {
            placingdispensers$kept = false;
            source.level().levelEvent(1001, source.pos(), 0);
            ci.cancel();
        }
    }
}
