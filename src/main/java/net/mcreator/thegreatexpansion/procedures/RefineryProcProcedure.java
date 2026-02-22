package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;

public class RefineryProcProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() < 64 && getEnergyStored(world, BlockPos.containing(x, y, z), null) <= 0
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == TheGreatExpansionModItems.CRUDE_OIL_BUCKET.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() <= 63 && itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount() <= 63
						&& itemFromBlockInventory(world, BlockPos.containing(x, y, z), 4).getCount() <= 63 && itemFromBlockInventory(world, BlockPos.containing(x, y, z), 5).getCount() <= 63
						&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == TheGreatExpansionModItems.PROPANE.get()
						&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == TheGreatExpansionModItems.GASOLINE_1.get()
						&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 4).copy()).getItem() == TheGreatExpansionModItems.DIESEL_1.get()
						&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 5).copy()).getItem() == TheGreatExpansionModBlocks.ASPHALT.get().asItem()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == Blocks.AIR.asItem() || (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).copy()).getItem() == Blocks.AIR.asItem()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 4).copy()).getItem() == Blocks.AIR.asItem() || (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 5).copy()).getItem() == Blocks.AIR.asItem())) {
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				int _slotid = 1;
				ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
				_stk.shrink(1);
				_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.PROPANE.get()).copy();
				_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() + 1);
				_itemHandlerModifiable.setStackInSlot(2, _setstack);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.GASOLINE_1.get()).copy();
				_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 3).getCount() + 1);
				_itemHandlerModifiable.setStackInSlot(3, _setstack);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.DIESEL_1.get()).copy();
				_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 4).getCount() + 1);
				_itemHandlerModifiable.setStackInSlot(4, _setstack);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModBlocks.ASPHALT.get()).copy();
				_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 5).getCount() + 1);
				_itemHandlerModifiable.setStackInSlot(5, _setstack);
			}
			if (entity instanceof ServerPlayer _player) {
				AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("the_great_expansion:fuelish_dreams"));
				if (_adv != null) {
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
		}
	}

	private static ItemStack itemFromBlockInventory(LevelAccessor world, BlockPos pos, int slot) {
		if (world instanceof ILevelExtension ext) {
			IItemHandler itemHandler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null)
				return itemHandler.getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	public static int getEnergyStored(LevelAccessor level, BlockPos pos, Direction direction) {
		if (level instanceof ILevelExtension levelExtension) {
			IEnergyStorage energyStorage = levelExtension.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction);
			if (energyStorage != null)
				return energyStorage.getEnergyStored();
		}
		return 0;
	}
}