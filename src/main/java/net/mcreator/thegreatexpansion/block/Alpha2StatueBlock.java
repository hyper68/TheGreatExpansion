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

import net.mcreator.thegreatexpansion.world.inventory.Alpha2StatueGUIMenu;
import net.mcreator.thegreatexpansion.block.entity.Alpha2StatueBlockEntity;

import io.netty.buffer.Unpooled;

public class Alpha2StatueBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public Alpha2StatueBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
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
			default -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(1, 2, 1, 15, 4, 15), box(3, 4, 3, 13, 6, 13), box(8, 17, 7, 10, 19, 9), box(6, 17, 7, 8, 19, 9), box(4, 15, 7, 6, 17, 9), box(10, 15, 7, 12, 17, 9), box(8, 13, 7, 10, 15, 9),
					box(6, 11, 7, 8, 13, 9), box(4, 7, 7, 6, 9, 9), box(6, 7, 7, 8, 9, 9), box(7.5, 6, 7.5, 8.5, 7, 8.5), box(8, 7, 7, 10, 9, 9), box(10, 7, 7, 12, 9, 9), box(4, 9, 7, 6, 11, 9));
			case NORTH -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(1, 2, 1, 15, 4, 15), box(3, 4, 3, 13, 6, 13), box(6, 17, 7, 8, 19, 9), box(8, 17, 7, 10, 19, 9), box(10, 15, 7, 12, 17, 9), box(4, 15, 7, 6, 17, 9), box(6, 13, 7, 8, 15, 9),
					box(8, 11, 7, 10, 13, 9), box(10, 7, 7, 12, 9, 9), box(8, 7, 7, 10, 9, 9), box(7.5, 6, 7.5, 8.5, 7, 8.5), box(6, 7, 7, 8, 9, 9), box(4, 7, 7, 6, 9, 9), box(10, 9, 7, 12, 11, 9));
			case EAST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(1, 2, 1, 15, 4, 15), box(3, 4, 3, 13, 6, 13), box(7, 17, 6, 9, 19, 8), box(7, 17, 8, 9, 19, 10), box(7, 15, 10, 9, 17, 12), box(7, 15, 4, 9, 17, 6), box(7, 13, 6, 9, 15, 8),
					box(7, 11, 8, 9, 13, 10), box(7, 7, 10, 9, 9, 12), box(7, 7, 8, 9, 9, 10), box(7.5, 6, 7.5, 8.5, 7, 8.5), box(7, 7, 6, 9, 9, 8), box(7, 7, 4, 9, 9, 6), box(7, 9, 10, 9, 11, 12));
			case WEST -> Shapes.or(box(0, 0, 0, 16, 2, 16), box(1, 2, 1, 15, 4, 15), box(3, 4, 3, 13, 6, 13), box(7, 17, 8, 9, 19, 10), box(7, 17, 6, 9, 19, 8), box(7, 15, 4, 9, 17, 6), box(7, 15, 10, 9, 17, 12), box(7, 13, 8, 9, 15, 10),
					box(7, 11, 6, 9, 13, 8), box(7, 7, 4, 9, 9, 6), box(7, 7, 6, 9, 9, 8), box(7.5, 6, 7.5, 8.5, 7, 8.5), box(7, 7, 8, 9, 9, 10), box(7, 7, 10, 9, 9, 12), box(7, 9, 4, 9, 11, 6));
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
					return Component.literal("Alpha 2 Statue");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new Alpha2StatueGUIMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
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
		return new Alpha2StatueBlockEntity(pos, state);
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
			if (blockEntity instanceof Alpha2StatueBlockEntity be) {
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
		if (tileentity instanceof Alpha2StatueBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}