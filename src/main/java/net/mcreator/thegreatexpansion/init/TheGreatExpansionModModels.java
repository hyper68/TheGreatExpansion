/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.thegreatexpansion.client.model.Modelsandcrab;
import net.mcreator.thegreatexpansion.client.model.Modelreinforcedchest;
import net.mcreator.thegreatexpansion.client.model.ModelStalker;

@EventBusSubscriber(Dist.CLIENT)
public class TheGreatExpansionModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelsandcrab.LAYER_LOCATION, Modelsandcrab::createBodyLayer);
		event.registerLayerDefinition(ModelStalker.LAYER_LOCATION, ModelStalker::createBodyLayer);
		event.registerLayerDefinition(Modelreinforcedchest.LAYER_LOCATION, Modelreinforcedchest::createBodyLayer);
	}
}