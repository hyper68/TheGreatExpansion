/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.mcreator.thegreatexpansion.client.renderer.SubZombieRenderer;
import net.mcreator.thegreatexpansion.client.renderer.StalkerRenderer;
import net.mcreator.thegreatexpansion.client.renderer.SandCrabRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class TheGreatExpansionModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(TheGreatExpansionModEntities.SAND_CRAB.get(), SandCrabRenderer::new);
		event.registerEntityRenderer(TheGreatExpansionModEntities.LEAD_ARROW_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(TheGreatExpansionModEntities.STALKER.get(), StalkerRenderer::new);
		event.registerEntityRenderer(TheGreatExpansionModEntities.SUB_ZOMBIE.get(), SubZombieRenderer::new);
	}
}