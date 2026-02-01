package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;
import net.mcreator.thegreatexpansion.TheGreatExpansionMod;

public class ActivatorBenchOnBlockRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean found = false;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Blocks.OBSIDIAN.asItem()
				&& (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == TheGreatExpansionModBlocks.ACTIVATOR_BENCH.get()) {
			{
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockState _bs = TheGreatExpansionModBlocks.ACTIVATOR_BENCH_IGNITER.get().defaultBlockState();
				BlockState _bso = world.getBlockState(_bp);
				for (Property<?> _propertyOld : _bso.getProperties()) {
					Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
					if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
						try {
							_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
						} catch (Exception e) {
						}
				}
				BlockEntity _be = world.getBlockEntity(_bp);
				CompoundTag _bnbt = null;
				if (_be != null) {
					_bnbt = _be.saveWithFullMetadata(world.registryAccess());
					_be.setRemoved();
				}
				world.setBlock(_bp, _bs, 3);
				if (_bnbt != null) {
					_be = world.getBlockEntity(_bp);
					if (_be != null) {
						try {
							_be.loadWithComponents(_bnbt, world.registryAccess());
						} catch (Exception ignored) {
						}
					}
				}
			}
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(Blocks.OBSIDIAN);
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			assert Boolean.TRUE; //#dbg:ActivatorBenchOnBlockRightclicked:marker0
		}
		assert Boolean.TRUE; //#dbg:ActivatorBenchOnBlockRightclicked:marker1
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.FLINT_AND_STEEL
				&& (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == TheGreatExpansionModBlocks.ACTIVATOR_BENCH_IGNITER.get()) {
			if (((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_FLIGHT.get()
					|| (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_FLIGHT.get()
					|| (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_FLIGHT.get()
					|| (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_FLIGHT.get()) == true
					&& ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_SKY.get()
							|| (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_SKY.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_SKY.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_SKY.get()) == true
					&& ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_NATURE.get()
							|| (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_NATURE.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_NATURE.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_NATURE.get()) == true
					&& ((world.getBlockState(BlockPos.containing(x + 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_EARTH.get()
							|| (world.getBlockState(BlockPos.containing(x - 1, y, z))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_EARTH.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z + 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_EARTH.get()
							|| (world.getBlockState(BlockPos.containing(x, y, z - 1))).getBlock() == TheGreatExpansionModBlocks.TOME_EMITTER_EARTH.get()) == true) {
				assert Boolean.TRUE; //#dbg:ActivatorBenchOnBlockRightclicked:marker2
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("the_great_expansion:shine")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("the_great_expansion:shine")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				{
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockState _bs = TheGreatExpansionModBlocks.ACTIVATOR_BENCH_IGNITER_IN.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					BlockEntity _be = world.getBlockEntity(_bp);
					CompoundTag _bnbt = null;
					if (_be != null) {
						_bnbt = _be.saveWithFullMetadata(world.registryAccess());
						_be.setRemoved();
					}
					world.setBlock(_bp, _bs, 3);
					if (_bnbt != null) {
						_be = world.getBlockEntity(_bp);
						if (_be != null) {
							try {
								_be.loadWithComponents(_bnbt, world.registryAccess());
							} catch (Exception ignored) {
							}
						}
					}
				}
				if (entity instanceof Player _player) {
					ItemStack _stktoremove = new ItemStack(Items.FLINT_AND_STEEL);
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("SteelIn", true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				TheGreatExpansionMod.LOGGER.info("Worked");
				if (entity instanceof ServerPlayer _player) {
					AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("the_great_expansion:gosh_that_was_hard"));
					if (_adv != null) {
						AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
						if (!_ap.isDone()) {
							for (String criteria : _ap.getRemainingCriteria())
								_player.getAdvancements().award(_adv, criteria);
						}
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.WHITE_SMOKE, x, y, z, 10, 2, 2, 2, 2);
				{
					BlockPos _bp = BlockPos.containing(x + 1, y, z);
					BlockState _bs = TheGreatExpansionModBlocks.TOME_EMITTER.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
				{
					BlockPos _bp = BlockPos.containing(x - 1, y, z);
					BlockState _bs = TheGreatExpansionModBlocks.TOME_EMITTER.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
				{
					BlockPos _bp = BlockPos.containing(x, y, z + 1);
					BlockState _bs = TheGreatExpansionModBlocks.TOME_EMITTER.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
				{
					BlockPos _bp = BlockPos.containing(x, y, z - 1);
					BlockState _bs = TheGreatExpansionModBlocks.TOME_EMITTER.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Property<?> _propertyOld : _bso.getProperties()) {
						Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
						if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
							try {
								_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
			}
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem() && getBlockNBTLogic(world, BlockPos.containing(x, y, z), "SteelIn") == true) {
			assert Boolean.TRUE; //#dbg:ActivatorBenchOnBlockRightclicked:marker3
			{
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockState _bs = TheGreatExpansionModBlocks.ACTIVATOR_BENCH.get().defaultBlockState();
				BlockState _bso = world.getBlockState(_bp);
				for (Property<?> _propertyOld : _bso.getProperties()) {
					Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
					if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
						try {
							_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
						} catch (Exception e) {
						}
				}
				BlockEntity _be = world.getBlockEntity(_bp);
				CompoundTag _bnbt = null;
				if (_be != null) {
					_bnbt = _be.saveWithFullMetadata(world.registryAccess());
					_be.setRemoved();
				}
				world.setBlock(_bp, _bs, 3);
				if (_bnbt != null) {
					_be = world.getBlockEntity(_bp);
					if (_be != null) {
						try {
							_be.loadWithComponents(_bnbt, world.registryAccess());
						} catch (Exception ignored) {
						}
					}
				}
			}
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putBoolean("SteelIn", false);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (entity instanceof ServerPlayer _player) {
				AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("the_great_expansion:gosh_that_was_hard"));
				if (_adv != null) {
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.THE_SKYLANDS.get()).copy();
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBoolean(tag);
		return false;
	}
}