/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import net.mcreator.thegreatexpansion.world.inventory.*;
import net.mcreator.thegreatexpansion.network.MenuStateUpdateMessage;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

import java.util.Map;

public class TheGreatExpansionModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<CrusherGUIMenu>> CRUSHER_GUI = REGISTRY.register("crusher_gui", () -> IMenuTypeExtension.create(CrusherGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<RefineryGUIMenu>> REFINERY_GUI = REGISTRY.register("refinery_gui", () -> IMenuTypeExtension.create(RefineryGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<FillerGUIMenu>> FILLER_GUI = REGISTRY.register("filler_gui", () -> IMenuTypeExtension.create(FillerGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ReinforcedChestGUIMenu>> REINFORCED_CHEST_GUI = REGISTRY.register("reinforced_chest_gui", () -> IMenuTypeExtension.create(ReinforcedChestGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<PressureCookerGUIMenu>> PRESSURE_COOKER_GUI = REGISTRY.register("pressure_cooker_gui", () -> IMenuTypeExtension.create(PressureCookerGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<PropaneGeneratorGUIMenu>> PROPANE_GENERATOR_GUI = REGISTRY.register("propane_generator_gui", () -> IMenuTypeExtension.create(PropaneGeneratorGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<JewleryWorkbenchGUIMenu>> JEWLERY_WORKBENCH_GUI = REGISTRY.register("jewlery_workbench_gui", () -> IMenuTypeExtension.create(JewleryWorkbenchGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<Alpha2StatueGUIMenu>> ALPHA_2_STATUE_GUI = REGISTRY.register("alpha_2_statue_gui", () -> IMenuTypeExtension.create(Alpha2StatueGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<GerminationTankGUIMenu>> GERMINATION_TANK_GUI = REGISTRY.register("germination_tank_gui", () -> IMenuTypeExtension.create(GerminationTankGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<InductionFurnaceGUIMenu>> INDUCTION_FURNACE_GUI = REGISTRY.register("induction_furnace_gui", () -> IMenuTypeExtension.create(InductionFurnaceGUIMenu::new));

	public interface MenuAccessor {
		Map<String, Object> getMenuState();

		Map<Integer, Slot> getSlots();

		default void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
			getMenuState().put(elementType + ":" + name, elementState);
			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new MenuStateUpdateMessage(elementType, name, elementState));
			} else if (player.level().isClientSide) {
				if (Minecraft.getInstance().screen instanceof TheGreatExpansionModScreens.ScreenAccessor accessor && needClientUpdate)
					accessor.updateMenuState(elementType, name, elementState);
				PacketDistributor.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
			}
		}

		default <T> T getMenuState(int elementType, String name, T defaultValue) {
			try {
				return (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
			} catch (ClassCastException e) {
				return defaultValue;
			}
		}
	}
}