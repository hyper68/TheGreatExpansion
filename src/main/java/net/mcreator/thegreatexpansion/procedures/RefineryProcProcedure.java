package net.mcreator.thegreatexpansion.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModItems;
import net.mcreator.thegreatexpansion.init.TheGreatExpansionModBlocks;

public class RefineryProcProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 2).getCount() < 64 && (itemFromBlockInventory(world, BlockPos.containing(x, y, z), 0).copy()).getItem() == Items.COAL
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
				int _slotid = 0;
				ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
				_stk.shrink(1);
				_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
			}
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
}