package net.mcreator.thegreatexpansion.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.thegreatexpansion.entity.SandCrabEntity;
import net.mcreator.thegreatexpansion.client.model.Modelsandcrab;

public class SandCrabRenderer extends MobRenderer<SandCrabEntity, Modelsandcrab<SandCrabEntity>> {
	public SandCrabRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelsandcrab<SandCrabEntity>(context.bakeLayer(Modelsandcrab.LAYER_LOCATION)), 0.1f);
	}

	@Override
	public ResourceLocation getTextureLocation(SandCrabEntity entity) {
		return ResourceLocation.parse("the_great_expansion:textures/entities/sandcrab.png");
	}
}