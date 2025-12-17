package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;

public class CapsulatorProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double previousRecipe = 0;
		if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() < 64 && getEnergyStored(world, BlockPos.containing(x, y, z), null) >= 65000
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == TheGreatExpansionModItems.MERCURY_INGOT.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 1).copy()).getItem() == TheGreatExpansionModItems.CAPSULE.get()
				&& (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() <= 63 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == TheGreatExpansionModItems.MERCURY_CAPSULE.get()
						|| (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).copy()).getItem() == Blocks.AIR.asItem())) {
			if (world instanceof ILevelExtension _ext) {
				IEnergyStorage _entityStorage = _ext.getCapability(Capabilities.EnergyStorage.BLOCK, BlockPos.containing(x, y, z), null);
				if (_entityStorage != null)
					_entityStorage.extractEnergy(65000, false);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				int _slotid = 0;
				ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
				_stk.shrink(1);
				_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
			}
			if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
				ItemStack _setstack = new ItemStack(TheGreatExpansionModItems.MERCURY_CAPSULE.get()).copy();
				_setstack.setCount(itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() + 1);
				_itemHandlerModifiable.setStackInSlot(2, _setstack);
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