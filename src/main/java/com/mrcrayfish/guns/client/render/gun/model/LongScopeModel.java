package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.RenderTypes;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

/**
 * Author: MrCrayfish
 */
public class LongScopeModel implements IOverrideModel
{
    private static final ResourceLocation RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/effect/long_scope_reticle.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);

        if(this.isFirstPerson(transformType) && entity.equals(Minecraft.getInstance().player))
        {
            if(entity.getPrimaryHand() == HandSide.LEFT)
            {
                matrixStack.scale(-1, 1, 1);
            }

            float size = 1.1F / 16.0F;
            float crop = 0.4F;
            Minecraft mc = Minecraft.getInstance();
            MainWindow window = mc.getMainWindow();
            int kickAmount = (int) ClientHandler.getGunRenderer().getRecoilAngle();
            float offset = (float) (ClientHandler.getGunRenderer().getRecoilNormal() * kickAmount * (1.0 / window.getHeight()));
            float texU = ((window.getWidth() - window.getHeight() + window.getHeight() * crop * 2.0F) / 2.0F) / window.getWidth();

            matrixStack.push();
            {
                Matrix4f matrix = matrixStack.getLast().getMatrix();
                Matrix3f normal = matrixStack.getLast().getNormal();

                matrixStack.translate(-size / 2, 0.85 * 0.0625, 2.45 * 0.0625);

                float color = (float) ClientHandler.getGunRenderer().getNormalisedAimProgress() * 0.8F + 0.2F;

                IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderTypes.getScreen());
                builder.pos(matrix, 0, size, 0).color(color, color, color, 1.0F).tex(texU, 1.0F - crop + offset).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, 0, 0).color(color, color, color, 1.0F).tex(texU, crop + offset).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, 0, 0).color(color, color, color, 1.0F).tex(1.0F - texU, crop + offset).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, size, 0).color(color, color, color, 1.0F).tex(1.0F - texU, 1.0F - crop + offset).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

                matrixStack.translate(0, 0, 0.0001);

                float alpha = (float) (ClientHandler.getGunRenderer().getNormalisedAimProgress() * 0.8F + 0.2F);

                builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(RETICLE));
                builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.9921875F, 0.9921875F).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0.9921875F).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0, 0).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, alpha).tex(0.9921875F, 0).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

                matrixStack.translate(0, 0, 0.0001);

                builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(VIGNETTE));
                builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0.984375F, 0.984375F).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0.984375F).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0.984375F, 0).overlay(overlay).lightmap(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            matrixStack.pop();
        }
    }

    private boolean isFirstPerson(ItemCameraTransforms.TransformType transformType)
    {
        return transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
    }
}
