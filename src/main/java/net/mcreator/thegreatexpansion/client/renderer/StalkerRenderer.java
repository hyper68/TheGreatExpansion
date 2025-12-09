package net.mcreator.thegreatexpansion.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.thegreatexpansion.entity.StalkerEntity;
import net.mcreator.thegreatexpansion.client.model.ModelStalker;

public class StalkerRenderer extends MobRenderer<StalkerEntity, ModelStalker<StalkerEntity>> {
	public StalkerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelStalker<StalkerEntity>(context.bakeLayer(ModelStalker.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(StalkerEntity entity) {
		return ResourceLocation.parse("the_great_expansion:textures/entities/texture.png");
	}
}