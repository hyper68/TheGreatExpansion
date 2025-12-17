package net.mcreator.thegreatexpansion.world.inventory;

import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.thegreatexpansion.init.TheGreatExpansionModMenus;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class OmegaCraftingTableGUIMenu extends AbstractContainerMenu implements TheGreatExpansionModMenus.MenuAccessor {
	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 122)
				return null;
			return super.put(key, value);
		}
	};
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private IItemHandler internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;

	public OmegaCraftingTableGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(TheGreatExpansionModMenus.OMEGA_CRAFTING_TABLE_GUI.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new ItemStackHandler(122);
		BlockPos pos = null;
		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
		if (pos != null) {
			if (extraData.readableBytes() == 1) { // bound to item
				byte hand = extraData.readByte();
				ItemStack itemstack = hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
				this.boundItemMatcher = () -> itemstack == (hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem());
				IItemHandler cap = itemstack.getCapability(Capabilities.ItemHandler.ITEM);
				if (cap != null) {
					this.internal = cap;
					this.bound = true;
				}
			} else if (extraData.readableBytes() > 1) { // bound to entity
				extraData.readByte(); // drop padding
				boundEntity = world.getEntity(extraData.readVarInt());
				if (boundEntity != null) {
					IItemHandler cap = boundEntity.getCapability(Capabilities.ItemHandler.ENTITY);
					if (cap != null) {
						this.internal = cap;
						this.bound = true;
					}
				}
			} else { // might be bound to block
				boundBlockEntity = this.world.getBlockEntity(pos);
				if (boundBlockEntity instanceof BaseContainerBlockEntity baseContainerBlockEntity) {
					this.internal = new InvWrapper(baseContainerBlockEntity);
					this.bound = true;
				}
			}
		}
		this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 14, 17) {
			private final int slot = 0;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(1, this.addSlot(new SlotItemHandler(internal, 1, 30, 17) {
			private final int slot = 1;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(2, this.addSlot(new SlotItemHandler(internal, 2, 47, 17) {
			private final int slot = 2;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(3, this.addSlot(new SlotItemHandler(internal, 3, 63, 17) {
			private final int slot = 3;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(4, this.addSlot(new SlotItemHandler(internal, 4, 80, 17) {
			private final int slot = 4;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(5, this.addSlot(new SlotItemHandler(internal, 5, 96, 17) {
			private final int slot = 5;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(6, this.addSlot(new SlotItemHandler(internal, 6, 113, 17) {
			private final int slot = 6;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(7, this.addSlot(new SlotItemHandler(internal, 7, 129, 17) {
			private final int slot = 7;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(8, this.addSlot(new SlotItemHandler(internal, 8, 146, 17) {
			private final int slot = 8;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(9, this.addSlot(new SlotItemHandler(internal, 9, 162, 17) {
			private final int slot = 9;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(10, this.addSlot(new SlotItemHandler(internal, 10, 179, 17) {
			private final int slot = 10;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(11, this.addSlot(new SlotItemHandler(internal, 11, 14, 35) {
			private final int slot = 11;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(12, this.addSlot(new SlotItemHandler(internal, 12, 30, 35) {
			private final int slot = 12;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(13, this.addSlot(new SlotItemHandler(internal, 13, 47, 35) {
			private final int slot = 13;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(14, this.addSlot(new SlotItemHandler(internal, 14, 63, 35) {
			private final int slot = 14;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(15, this.addSlot(new SlotItemHandler(internal, 15, 80, 35) {
			private final int slot = 15;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(16, this.addSlot(new SlotItemHandler(internal, 16, 96, 35) {
			private final int slot = 16;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(17, this.addSlot(new SlotItemHandler(internal, 17, 113, 35) {
			private final int slot = 17;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(18, this.addSlot(new SlotItemHandler(internal, 18, 129, 35) {
			private final int slot = 18;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(19, this.addSlot(new SlotItemHandler(internal, 19, 146, 35) {
			private final int slot = 19;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(20, this.addSlot(new SlotItemHandler(internal, 20, 162, 35) {
			private final int slot = 20;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(21, this.addSlot(new SlotItemHandler(internal, 21, 179, 35) {
			private final int slot = 21;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(22, this.addSlot(new SlotItemHandler(internal, 22, 14, 53) {
			private final int slot = 22;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(23, this.addSlot(new SlotItemHandler(internal, 23, 30, 53) {
			private final int slot = 23;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(24, this.addSlot(new SlotItemHandler(internal, 24, 47, 53) {
			private final int slot = 24;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(25, this.addSlot(new SlotItemHandler(internal, 25, 63, 53) {
			private final int slot = 25;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(26, this.addSlot(new SlotItemHandler(internal, 26, 80, 53) {
			private final int slot = 26;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(27, this.addSlot(new SlotItemHandler(internal, 27, 96, 53) {
			private final int slot = 27;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(28, this.addSlot(new SlotItemHandler(internal, 28, 113, 53) {
			private final int slot = 28;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(29, this.addSlot(new SlotItemHandler(internal, 29, 129, 53) {
			private final int slot = 29;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(30, this.addSlot(new SlotItemHandler(internal, 30, 146, 53) {
			private final int slot = 30;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(31, this.addSlot(new SlotItemHandler(internal, 31, 162, 53) {
			private final int slot = 31;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(32, this.addSlot(new SlotItemHandler(internal, 32, 179, 53) {
			private final int slot = 32;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(33, this.addSlot(new SlotItemHandler(internal, 33, 14, 71) {
			private final int slot = 33;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(34, this.addSlot(new SlotItemHandler(internal, 34, 30, 71) {
			private final int slot = 34;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(35, this.addSlot(new SlotItemHandler(internal, 35, 47, 71) {
			private final int slot = 35;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(36, this.addSlot(new SlotItemHandler(internal, 36, 63, 71) {
			private final int slot = 36;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(37, this.addSlot(new SlotItemHandler(internal, 37, 80, 71) {
			private final int slot = 37;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(38, this.addSlot(new SlotItemHandler(internal, 38, 96, 71) {
			private final int slot = 38;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(39, this.addSlot(new SlotItemHandler(internal, 39, 113, 71) {
			private final int slot = 39;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(40, this.addSlot(new SlotItemHandler(internal, 40, 129, 71) {
			private final int slot = 40;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(41, this.addSlot(new SlotItemHandler(internal, 41, 146, 71) {
			private final int slot = 41;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(42, this.addSlot(new SlotItemHandler(internal, 42, 162, 71) {
			private final int slot = 42;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(43, this.addSlot(new SlotItemHandler(internal, 43, 179, 71) {
			private final int slot = 43;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(44, this.addSlot(new SlotItemHandler(internal, 44, 14, 89) {
			private final int slot = 44;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(45, this.addSlot(new SlotItemHandler(internal, 45, 30, 89) {
			private final int slot = 45;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(46, this.addSlot(new SlotItemHandler(internal, 46, 47, 89) {
			private final int slot = 46;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(47, this.addSlot(new SlotItemHandler(internal, 47, 63, 89) {
			private final int slot = 47;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(48, this.addSlot(new SlotItemHandler(internal, 48, 80, 89) {
			private final int slot = 48;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(49, this.addSlot(new SlotItemHandler(internal, 49, 96, 89) {
			private final int slot = 49;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(50, this.addSlot(new SlotItemHandler(internal, 50, 113, 89) {
			private final int slot = 50;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(51, this.addSlot(new SlotItemHandler(internal, 51, 129, 89) {
			private final int slot = 51;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(52, this.addSlot(new SlotItemHandler(internal, 52, 146, 89) {
			private final int slot = 52;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(53, this.addSlot(new SlotItemHandler(internal, 53, 162, 89) {
			private final int slot = 53;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(54, this.addSlot(new SlotItemHandler(internal, 54, 179, 89) {
			private final int slot = 54;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(55, this.addSlot(new SlotItemHandler(internal, 55, 14, 107) {
			private final int slot = 55;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(56, this.addSlot(new SlotItemHandler(internal, 56, 30, 107) {
			private final int slot = 56;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(57, this.addSlot(new SlotItemHandler(internal, 57, 47, 107) {
			private final int slot = 57;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(58, this.addSlot(new SlotItemHandler(internal, 58, 63, 107) {
			private final int slot = 58;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(59, this.addSlot(new SlotItemHandler(internal, 59, 80, 107) {
			private final int slot = 59;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(60, this.addSlot(new SlotItemHandler(internal, 60, 96, 107) {
			private final int slot = 60;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(61, this.addSlot(new SlotItemHandler(internal, 61, 113, 107) {
			private final int slot = 61;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(62, this.addSlot(new SlotItemHandler(internal, 62, 129, 107) {
			private final int slot = 62;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(63, this.addSlot(new SlotItemHandler(internal, 63, 146, 107) {
			private final int slot = 63;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(64, this.addSlot(new SlotItemHandler(internal, 64, 162, 107) {
			private final int slot = 64;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(65, this.addSlot(new SlotItemHandler(internal, 65, 179, 107) {
			private final int slot = 65;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(66, this.addSlot(new SlotItemHandler(internal, 66, 14, 125) {
			private final int slot = 66;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(67, this.addSlot(new SlotItemHandler(internal, 67, 30, 125) {
			private final int slot = 67;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(68, this.addSlot(new SlotItemHandler(internal, 68, 47, 125) {
			private final int slot = 68;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(69, this.addSlot(new SlotItemHandler(internal, 69, 63, 125) {
			private final int slot = 69;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(70, this.addSlot(new SlotItemHandler(internal, 70, 80, 125) {
			private final int slot = 70;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(71, this.addSlot(new SlotItemHandler(internal, 71, 96, 125) {
			private final int slot = 71;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(72, this.addSlot(new SlotItemHandler(internal, 72, 113, 125) {
			private final int slot = 72;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(73, this.addSlot(new SlotItemHandler(internal, 73, 129, 125) {
			private final int slot = 73;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(74, this.addSlot(new SlotItemHandler(internal, 74, 146, 125) {
			private final int slot = 74;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(75, this.addSlot(new SlotItemHandler(internal, 75, 162, 125) {
			private final int slot = 75;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(76, this.addSlot(new SlotItemHandler(internal, 76, 179, 125) {
			private final int slot = 76;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(77, this.addSlot(new SlotItemHandler(internal, 77, 14, 143) {
			private final int slot = 77;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(78, this.addSlot(new SlotItemHandler(internal, 78, 30, 143) {
			private final int slot = 78;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(79, this.addSlot(new SlotItemHandler(internal, 79, 47, 143) {
			private final int slot = 79;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(80, this.addSlot(new SlotItemHandler(internal, 80, 63, 143) {
			private final int slot = 80;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(81, this.addSlot(new SlotItemHandler(internal, 81, 80, 143) {
			private final int slot = 81;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(82, this.addSlot(new SlotItemHandler(internal, 82, 96, 143) {
			private final int slot = 82;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(83, this.addSlot(new SlotItemHandler(internal, 83, 113, 143) {
			private final int slot = 83;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(84, this.addSlot(new SlotItemHandler(internal, 84, 129, 143) {
			private final int slot = 84;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(85, this.addSlot(new SlotItemHandler(internal, 85, 146, 143) {
			private final int slot = 85;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(86, this.addSlot(new SlotItemHandler(internal, 86, 162, 143) {
			private final int slot = 86;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(87, this.addSlot(new SlotItemHandler(internal, 87, 179, 143) {
			private final int slot = 87;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(88, this.addSlot(new SlotItemHandler(internal, 88, 14, 161) {
			private final int slot = 88;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(89, this.addSlot(new SlotItemHandler(internal, 89, 30, 161) {
			private final int slot = 89;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(90, this.addSlot(new SlotItemHandler(internal, 90, 47, 161) {
			private final int slot = 90;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(91, this.addSlot(new SlotItemHandler(internal, 91, 63, 161) {
			private final int slot = 91;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(92, this.addSlot(new SlotItemHandler(internal, 92, 80, 161) {
			private final int slot = 92;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(93, this.addSlot(new SlotItemHandler(internal, 93, 96, 161) {
			private final int slot = 93;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(94, this.addSlot(new SlotItemHandler(internal, 94, 113, 161) {
			private final int slot = 94;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(95, this.addSlot(new SlotItemHandler(internal, 95, 129, 161) {
			private final int slot = 95;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(96, this.addSlot(new SlotItemHandler(internal, 96, 146, 161) {
			private final int slot = 96;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(97, this.addSlot(new SlotItemHandler(internal, 97, 162, 161) {
			private final int slot = 97;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(98, this.addSlot(new SlotItemHandler(internal, 98, 179, 161) {
			private final int slot = 98;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(99, this.addSlot(new SlotItemHandler(internal, 99, 14, 179) {
			private final int slot = 99;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(100, this.addSlot(new SlotItemHandler(internal, 100, 30, 179) {
			private final int slot = 100;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(101, this.addSlot(new SlotItemHandler(internal, 101, 47, 179) {
			private final int slot = 101;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(102, this.addSlot(new SlotItemHandler(internal, 102, 63, 179) {
			private final int slot = 102;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(103, this.addSlot(new SlotItemHandler(internal, 103, 80, 179) {
			private final int slot = 103;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(104, this.addSlot(new SlotItemHandler(internal, 104, 96, 179) {
			private final int slot = 104;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(105, this.addSlot(new SlotItemHandler(internal, 105, 113, 179) {
			private final int slot = 105;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(106, this.addSlot(new SlotItemHandler(internal, 106, 129, 179) {
			private final int slot = 106;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(107, this.addSlot(new SlotItemHandler(internal, 107, 146, 179) {
			private final int slot = 107;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(108, this.addSlot(new SlotItemHandler(internal, 108, 162, 179) {
			private final int slot = 108;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(109, this.addSlot(new SlotItemHandler(internal, 109, 179, 179) {
			private final int slot = 109;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(110, this.addSlot(new SlotItemHandler(internal, 110, 14, 197) {
			private final int slot = 110;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(111, this.addSlot(new SlotItemHandler(internal, 111, 30, 197) {
			private final int slot = 111;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(112, this.addSlot(new SlotItemHandler(internal, 112, 47, 197) {
			private final int slot = 112;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(113, this.addSlot(new SlotItemHandler(internal, 113, 63, 197) {
			private final int slot = 113;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(114, this.addSlot(new SlotItemHandler(internal, 114, 80, 197) {
			private final int slot = 114;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(115, this.addSlot(new SlotItemHandler(internal, 115, 96, 197) {
			private final int slot = 115;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(116, this.addSlot(new SlotItemHandler(internal, 116, 113, 197) {
			private final int slot = 116;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(117, this.addSlot(new SlotItemHandler(internal, 117, 129, 197) {
			private final int slot = 117;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(118, this.addSlot(new SlotItemHandler(internal, 118, 146, 197) {
			private final int slot = 118;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(119, this.addSlot(new SlotItemHandler(internal, 119, 162, 197) {
			private final int slot = 119;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(120, this.addSlot(new SlotItemHandler(internal, 120, 179, 197) {
			private final int slot = 120;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;
		}));
		this.customSlots.put(121, this.addSlot(new SlotItemHandler(internal, 121, 283, 53) {
			private final int slot = 121;
			private int x = OmegaCraftingTableGUIMenu.this.x;
			private int y = OmegaCraftingTableGUIMenu.this.y;

			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		}));
		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 204 + 8 + sj * 18, 51 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 204 + 8 + si * 18, 51 + 142));
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 122) {
				if (!this.moveItemStackTo(itemstack1, 122, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 122, false)) {
				if (index < 122 + 27) {
					if (!this.moveItemStackTo(itemstack1, 122 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemstack1, 122, 122 + 27, false))
						return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected boolean moveItemStackTo(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
		boolean flag = false;
		int i = p_38905_;
		if (p_38907_) {
			i = p_38906_ - 1;
		}
		if (p_38904_.isStackable()) {
			while (!p_38904_.isEmpty() && (p_38907_ ? i >= p_38905_ : i < p_38906_)) {
				Slot slot = this.slots.get(i);
				ItemStack itemstack = slot.getItem();
				if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameComponents(p_38904_, itemstack)) {
					int j = itemstack.getCount() + p_38904_.getCount();
					int k = slot.getMaxStackSize(itemstack);
					if (j <= k) {
						p_38904_.setCount(0);
						itemstack.setCount(j);
						slot.set(itemstack);
						flag = true;
					} else if (itemstack.getCount() < k) {
						p_38904_.shrink(k - itemstack.getCount());
						itemstack.setCount(k);
						slot.set(itemstack);
						flag = true;
					}
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		if (!p_38904_.isEmpty()) {
			if (p_38907_) {
				i = p_38906_ - 1;
			} else {
				i = p_38905_;
			}
			while (p_38907_ ? i >= p_38905_ : i < p_38906_) {
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(p_38904_)) {
					int l = slot1.getMaxStackSize(p_38904_);
					slot1.setByPlayer(p_38904_.split(Math.min(p_38904_.getCount(), l)));
					slot1.setChanged();
					flag = true;
					break;
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		return flag;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
				for (int j = 0; j < internal.getSlots(); ++j) {
					playerIn.drop(internal.getStackInSlot(j), false);
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(j, ItemStack.EMPTY);
				}
			} else {
				for (int i = 0; i < internal.getSlots(); ++i) {
					playerIn.getInventory().placeItemBackInInventory(internal.getStackInSlot(i));
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public Map<Integer, Slot> getSlots() {
		return Collections.unmodifiableMap(customSlots);
	}

	@Override
	public Map<String, Object> getMenuState() {
		return menuState;
	}
}