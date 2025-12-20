/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

@EventBusSubscriber
public class TheGreatExpansionModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THE_GREAT_EXPANSION = REGISTRY.register("the_great_expansion",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.the_great_expansion.the_great_expansion")).icon(() -> new ItemStack(Blocks.DIAMOND_ORE)).displayItems((parameters, tabData) -> {
				tabData.accept(TheGreatExpansionModBlocks.BAUXITE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.CASSITERITE_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.GALENA_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ILMENITE_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.CINNABAR_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModItems.ALUMINUM_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.TIN_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.LEAD_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.MERCURY_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.STEEL_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.STEEL_POWDER.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_SWORD.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_AXE.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_HELMET.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_CHESTPLATE.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_LEGGINGS.get());
				tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_BOOTS.get());
				tabData.accept(TheGreatExpansionModItems.SILICON_PADDING_HELMET.get());
				tabData.accept(TheGreatExpansionModItems.SILICON_PADDING_CHESTPLATE.get());
				tabData.accept(TheGreatExpansionModItems.SILICON_PADDING_PANTS.get());
				tabData.accept(TheGreatExpansionModItems.SILICON_PADDING_BOOTS.get());
				tabData.accept(TheGreatExpansionModBlocks.CRUSHER.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PRESSURE_COOKER.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PROPANE_GENERATOR.get().asItem());
				tabData.accept(TheGreatExpansionModItems.STEEL_BEAMS.get());
				tabData.accept(TheGreatExpansionModItems.STEEL_FACE_PLATE.get());
				tabData.accept(TheGreatExpansionModBlocks.STEEL_FRAME.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.MACHINE_FRAME.get().asItem());
				tabData.accept(TheGreatExpansionModItems.SILICON_INGOT.get());
				tabData.accept(TheGreatExpansionModItems.CRUDE_OIL_BUCKET.get());
				tabData.accept(TheGreatExpansionModItems.PROPANE.get());
				tabData.accept(TheGreatExpansionModItems.EMPTY_PROPANE_TANK.get());
				tabData.accept(TheGreatExpansionModItems.FILLED_PROPANE_TANK.get());
				tabData.accept(TheGreatExpansionModItems.GASOLINE_1.get());
				tabData.accept(TheGreatExpansionModItems.DIESEL_1.get());
				tabData.accept(TheGreatExpansionModBlocks.ASPHALT.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.FILLER.get().asItem());
				tabData.accept(TheGreatExpansionModItems.SILICON_SHEETS.get());
				tabData.accept(TheGreatExpansionModBlocks.PACKED_SAND.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PACKED_SAND_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PACKED_SAND_BRICKS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PACKED_SAND_BRICK_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ASPHALT_SLABS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ASPHALT_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_LOG.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_WOOD.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_PLANKS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_LEAVES.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_SLAB.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_FENCE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_FENCE_GATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_PRESSURE_PLATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_BUTTON.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.REINFORCED_CHEST.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PALO_SAPLING.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.JEWLERY_WORKBENCH.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR_BASE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR_TOPPER.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR_T_SECTION.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR_3_WAY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_PILLAR_4_WAY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_SIDE_PILLAR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_SIDE_PILLAR_3_WAY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COBBLESTONE_SIDE_PILLAR_4_WAY.get().asItem());
				tabData.accept(TheGreatExpansionModItems.SNOWSCAPE_DISK.get());
				tabData.accept(TheGreatExpansionModItems.HOLLOW_BELLS_DISK.get());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICKS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_SLABS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_FENCE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_WALL.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_TRAPDOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_DOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_FENCEGATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_CUTTER.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.BIG_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.MEDIUM_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SMALL_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SNOW_LILY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_SLABS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_FENCE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_WALL.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_TRAPDOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_DOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_FENCEGATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SNOWDROP.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.HELLEBORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.WINTERBERRY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.GERMINATION_TANK.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PERMAFROST.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.REINFORCED_PERMAFROST.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.CRYONITE_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PERMAFROST_IRON_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModItems.LIQUID_GLASS.get());
				tabData.accept(TheGreatExpansionModBlocks.LEAD_BLOCK.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.COMPRESSED_LEAD_BLOCK.get().asItem());
				tabData.accept(TheGreatExpansionModItems.DIAMOND_ROD.get());
				tabData.accept(TheGreatExpansionModItems.EXPANSION_CONTROL_ROD.get());
				tabData.accept(TheGreatExpansionModItems.LIQUID_AQUALITH.get());
				tabData.accept(TheGreatExpansionModItems.AQUALITH_CONTROL_ROD.get());
				tabData.accept(TheGreatExpansionModItems.DIAMOND_SCREEN.get());
				tabData.accept(TheGreatExpansionModItems.CAPSULE.get());
				tabData.accept(TheGreatExpansionModItems.MERCURY_CAPSULE.get());
				tabData.accept(TheGreatExpansionModBlocks.CAPSULATOR.get().asItem());
				tabData.accept(TheGreatExpansionModItems.EXPANSIO_CONTROL_UNIT.get());
				tabData.accept(TheGreatExpansionModItems.IRON_POWDER.get());
				tabData.accept(TheGreatExpansionModItems.COPPER_POWDER.get());
				tabData.accept(TheGreatExpansionModItems.GOLD_POWDER.get());
				tabData.accept(TheGreatExpansionModItems.DIAMOND_POWDER.get());
				tabData.accept(TheGreatExpansionModItems.REDSTONE_POWDER.get());
				tabData.accept(TheGreatExpansionModBlocks.ENRICHMENT_FRAME.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.REINFORCED_GLASS.get().asItem());
			}).withSearchBar().build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WINTER_EXPANSION = REGISTRY.register("winter_expansion",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.the_great_expansion.winter_expansion")).icon(() -> new ItemStack(TheGreatExpansionModBlocks.SMALL_SNOWMAN.get())).displayItems((parameters, tabData) -> {
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICKS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_SLABS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_FENCE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_WALL.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_TRAPDOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_DOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BRICK_FENCEGATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_CUTTER.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.BIG_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.MEDIUM_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SMALL_SNOWMAN.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SNOW_LILY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_STAIRS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_SLABS.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_FENCE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_WALL.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_TRAPDOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_DOOR.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.ICE_BLOCK_FENCEGATE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.SNOWDROP.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.HELLEBORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.WINTERBERRY.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.GERMINATION_TANK.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PERMAFROST.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.PERMAFROST_IRON_ORE.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.REINFORCED_PERMAFROST.get().asItem());
				tabData.accept(TheGreatExpansionModBlocks.CRYONITE_ORE.get().asItem());
			}).withSearchBar().withTabsBefore(THE_GREAT_EXPANSION.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(TheGreatExpansionModItems.TITANIUM_SWORD.get());
			tabData.accept(TheGreatExpansionModItems.TITANIUM_AXE.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_PICKAXE.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_AXE.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_SHOVEL.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_HOE.get());
			tabData.accept(TheGreatExpansionModItems.STEEL_HAMMER.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_HELMET.get());
			tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_CHESTPLATE.get());
			tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_LEGGINGS.get());
			tabData.accept(TheGreatExpansionModItems.TITANIUM_ARMOUR_BOOTS.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_SWORD.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_ARMOR_HELMET.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_ARMOR_CHESTPLATE.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_ARMOR_LEGGINGS.get());
			tabData.accept(TheGreatExpansionModItems.AQUALITH_ARMOR_BOOTS.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(TheGreatExpansionModItems.AQUALITH.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(TheGreatExpansionModBlocks.AQUALITH_ORE.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.AQUALITH_BLOCK.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_LOG.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_WOOD.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_PLANKS.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_STAIRS.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_SLAB.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_FENCE.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_FENCE_GATE.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_PRESSURE_PLATE.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_BUTTON.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(TheGreatExpansionModBlocks.PALO_LEAVES.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.PALO_SAPLING.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.SNOW_LILY.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.SNOWDROP.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.HELLEBORE.get().asItem());
			tabData.accept(TheGreatExpansionModBlocks.WINTERBERRY.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(TheGreatExpansionModItems.SAND_CRAB_SPAWN_EGG.get());
			tabData.accept(TheGreatExpansionModItems.SUB_ZOMBIE_SPAWN_EGG.get());
		}
	}
}