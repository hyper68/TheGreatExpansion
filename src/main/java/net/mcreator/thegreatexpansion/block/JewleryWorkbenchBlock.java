package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.thegreatexpansion.world.inventory.JewleryWorkbenchGUIMenu;
import net.mcreator.thegreatexpansion.block.entity.JewleryWorkbenchBlockEntity;

import io.netty.buffer.Unpooled;

public class JewleryWorkbenchBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public JewleryWorkbenchBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			default -> Shapes.or(box(14, 0, 14, 16, 16, 16), box(14, 0, 0, 16, 16, 2), box(-16, 0, 0, -14, 14, 2), box(-16, 12, 2, -14, 14, 14), box(-16, 6, 2, -14, 7, 14), box(-14, 6, 0, 14, 8, 2), box(-14, 9, 14, 14, 11, 16),
					box(12, 9, 16, 13, 10, 18), box(12, 10, 17, 13, 11, 18), box(-16, 0, 14, -14, 14, 16), box(14, 13, 2, 16, 14, 14), box(-14, 13, 0, 14, 14, 16), box(-16, 14, 0, -5, 19, 7), box(0, 14, 5, 11, 16, 12));
			case NORTH -> Shapes.or(box(0, 0, 0, 2, 16, 2), box(0, 0, 14, 2, 16, 16), box(30, 0, 14, 32, 14, 16), box(30, 12, 2, 32, 14, 14), box(30, 6, 2, 32, 7, 14), box(2, 6, 14, 30, 8, 16), box(2, 9, 0, 30, 11, 2), box(3, 9, -2, 4, 10, 0),
					box(3, 10, -2, 4, 11, -1), box(30, 0, 0, 32, 14, 2), box(0, 13, 2, 2, 14, 14), box(2, 13, 0, 30, 14, 16), box(21, 14, 9, 32, 19, 16), box(5, 14, 4, 16, 16, 11));
			case EAST -> Shapes.or(box(14, 0, 0, 16, 16, 2), box(0, 0, 0, 2, 16, 2), box(0, 0, 30, 2, 14, 32), box(2, 12, 30, 14, 14, 32), box(2, 6, 30, 14, 7, 32), box(0, 6, 2, 2, 8, 30), box(14, 9, 2, 16, 11, 30), box(16, 9, 3, 18, 10, 4),
					box(17, 10, 3, 18, 11, 4), box(14, 0, 30, 16, 14, 32), box(2, 13, 0, 14, 14, 2), box(0, 13, 2, 16, 14, 30), box(0, 14, 21, 7, 19, 32), box(5, 14, 5, 12, 16, 16));
			case WEST -> Shapes.or(box(0, 0, 14, 2, 16, 16), box(14, 0, 14, 16, 16, 16), box(14, 0, -16, 16, 14, -14), box(2, 12, -16, 14, 14, -14), box(2, 6, -16, 14, 7, -14), box(14, 6, -14, 16, 8, 14), box(0, 9, -14, 2, 11, 14),
					box(-2, 9, 12, 0, 10, 13), box(-2, 10, 12, -1, 11, 13), box(0, 0, -16, 2, 14, -14), box(2, 13, 14, 14, 14, 16), box(0, 13, -14, 16, 14, 14), box(9, 14, -16, 16, 19, -5), box(4, 14, 0, 11, 16, 11));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		if (entity instanceof ServerPlayer player) {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("Jewlery Workbench");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new JewleryWorkbenchGUIMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
				}
			}, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new JewleryWorkbenchBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof JewleryWorkbenchBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof JewleryWorkbenchBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}