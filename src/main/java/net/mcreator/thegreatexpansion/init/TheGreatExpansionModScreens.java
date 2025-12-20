/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.thegreatexpansion.client.gui.*;

@EventBusSubscriber(Dist.CLIENT)
public class TheGreatExpansionModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(TheGreatExpansionModMenus.CRUSHER_GUI.get(), CrusherGUIScreen::new);
		event.register(TheGreatExpansionModMenus.REFINERY_GUI.get(), RefineryGUIScreen::new);
		event.register(TheGreatExpansionModMenus.FILLER_GUI.get(), FillerGUIScreen::new);
		event.register(TheGreatExpansionModMenus.REINFORCED_CHEST_GUI.get(), ReinforcedChestGUIScreen::new);
		event.register(TheGreatExpansionModMenus.PRESSURE_COOKER_GUI.get(), PressureCookerGUIScreen::new);
		event.register(TheGreatExpansionModMenus.PROPANE_GENERATOR_GUI.get(), PropaneGeneratorGUIScreen::new);
		event.register(TheGreatExpansionModMenus.JEWLERY_WORKBENCH_GUI.get(), JewleryWorkbenchGUIScreen::new);
		event.register(TheGreatExpansionModMenus.ALPHA_2_STATUE_GUI.get(), Alpha2StatueGUIScreen::new);
		event.register(TheGreatExpansionModMenus.GERMINATION_TANK_GUI.get(), GerminationTankGUIScreen::new);
		event.register(TheGreatExpansionModMenus.INDUCTION_FURNACE_GUI.get(), InductionFurnaceGUIScreen::new);
		event.register(TheGreatExpansionModMenus.OMEGA_CRAFTING_TABLE_GUI.get(), OmegaCraftingTableGUIScreen::new);
		event.register(TheGreatExpansionModMenus.CAPSULATOR_GUI.get(), CapsulatorGUIScreen::new);
		event.register(TheGreatExpansionModMenus.ENRICHMENT_GUI.get(), EnrichmentGUIScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}