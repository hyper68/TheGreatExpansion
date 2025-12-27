/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.thegreatexpansion.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.thegreatexpansion.block.entity.*;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

@EventBusSubscriber
public class TheGreatExpansionModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheGreatExpansionMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrusherBlockEntity>> CRUSHER = register("crusher", TheGreatExpansionModBlocks.CRUSHER, CrusherBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RefineryBlockEntity>> REFINERY = register("refinery", TheGreatExpansionModBlocks.REFINERY, RefineryBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FillerBlockEntity>> FILLER = register("filler", TheGreatExpansionModBlocks.FILLER, FillerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ReinforcedChestBlockEntity>> REINFORCED_CHEST = register("reinforced_chest", TheGreatExpansionModBlocks.REINFORCED_CHEST, ReinforcedChestBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PressureCookerBlockEntity>> PRESSURE_COOKER = register("pressure_cooker", TheGreatExpansionModBlocks.PRESSURE_COOKER, PressureCookerBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PropaneGeneratorBlockEntity>> PROPANE_GENERATOR = register("propane_generator", TheGreatExpansionModBlocks.PROPANE_GENERATOR, PropaneGeneratorBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<JewleryWorkbenchBlockEntity>> JEWLERY_WORKBENCH = register("jewlery_workbench", TheGreatExpansionModBlocks.JEWLERY_WORKBENCH, JewleryWorkbenchBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SandVaultBlockEntity>> SAND_VAULT = register("sand_vault", TheGreatExpansionModBlocks.SAND_VAULT, SandVaultBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<Alpha2StatueBlockEntity>> ALPHA_2_STATUE = register("alpha_2_statue", TheGreatExpansionModBlocks.ALPHA_2_STATUE, Alpha2StatueBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IceCutterBlockEntity>> ICE_CUTTER = register("ice_cutter", TheGreatExpansionModBlocks.ICE_CUTTER, IceCutterBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GerminationTankBlockEntity>> GERMINATION_TANK = register("germination_tank", TheGreatExpansionModBlocks.GERMINATION_TANK, GerminationTankBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InductionFurnaceBlockEntity>> INDUCTION_FURNACE = register("induction_furnace", TheGreatExpansionModBlocks.INDUCTION_FURNACE, InductionFurnaceBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LavaleerBlockEntity>> LAVALEER = register("lavaleer", TheGreatExpansionModBlocks.LAVALEER, LavaleerBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CRUSHER.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, REFINERY.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FILLER.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, REINFORCED_CHEST.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PRESSURE_COOKER.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PRESSURE_COOKER.get(), (blockEntity, side) -> blockEntity.getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PROPANE_GENERATOR.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PROPANE_GENERATOR.get(), (blockEntity, side) -> blockEntity.getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, JEWLERY_WORKBENCH.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SAND_VAULT.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ALPHA_2_STATUE.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ICE_CUTTER.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GERMINATION_TANK.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GERMINATION_TANK.get(), (blockEntity, side) -> blockEntity.getFluidTank());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INDUCTION_FURNACE.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, INDUCTION_FURNACE.get(), (blockEntity, side) -> blockEntity.getEnergyStorage());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LAVALEER.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, LAVALEER.get(), (blockEntity, side) -> blockEntity.getEnergyStorage());
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, LAVALEER.get(), (blockEntity, side) -> blockEntity.getFluidTank());
	}
}