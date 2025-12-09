package net.mcreator.thegreatexpansion.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class BigSnowmanBlock extends Block {
	public static final DirectionProperty FACING = DirectionalBlock.FACING;

	public BigSnowmanBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SNOW).strength(1f, 10f).noCollission().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
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
			default -> Shapes.or(box(0, -2, 0, 16, 8, 16), box(1, 8, 1, 15, 20, 15), box(2, 20, 2, 14, 32, 14));
			case NORTH -> Shapes.or(box(0, -2, 0, 16, 8, 16), box(1, 8, 1, 15, 20, 15), box(2, 20, 2, 14, 32, 14));
			case EAST -> Shapes.or(box(0, -2, 0, 16, 8, 16), box(1, 8, 1, 15, 20, 15), box(2, 20, 2, 14, 32, 14));
			case WEST -> Shapes.or(box(0, -2, 0, 16, 8, 16), box(1, 8, 1, 15, 20, 15), box(2, 20, 2, 14, 32, 14));
			case UP -> Shapes.or(box(0, 0, -2, 16, 16, 8), box(1, 1, 8, 15, 15, 20), box(2, 2, 20, 14, 14, 32));
			case DOWN -> Shapes.or(box(0, 0, 8, 16, 16, 18), box(1, 1, -4, 15, 15, 8), box(2, 2, -16, 14, 14, -4));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
}