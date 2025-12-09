package net.mcreator.thegreatexpansion.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.thegreatexpansion.entity.SubZombieEntity;

public class SubZombieRenderer extends HumanoidMobRenderer<SubZombieEntity, HumanoidModel<SubZombieEntity>> {
	public SubZombieRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<SubZombieEntity>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(SubZombieEntity entity) {
		return ResourceLocation.parse("the_great_expansion:textures/entities/eskimo.png");
	}
}