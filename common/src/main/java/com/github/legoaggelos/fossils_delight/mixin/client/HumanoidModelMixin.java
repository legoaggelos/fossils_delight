package com.github.legoaggelos.fossils_delight.mixin.client;

import com.github.legoaggelos.fossils_delight.registry.TagRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

    @SuppressWarnings("unchecked")
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public <T extends LivingEntity> void setupAnimTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        HumanoidModel<T> model = ((HumanoidModel<T>) (Object) this);
        if (entity.getMainHandItem().is(TagRegistry.HOLD_OVER_HEAD)) {
            model.rightArm.xRot = -2.9f;
            model.leftArm.xRot = model.rightArm.xRot;
        } else if (entity.getOffhandItem().is(TagRegistry.HOLD_OVER_HEAD)) {
            model.leftArm.xRot = -2.9f;
            model.rightArm.xRot = model.leftArm.xRot;
        }
    }
}
