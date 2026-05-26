package net.chixozhmix.dnmmod.blocks.custom;

import net.chixozhmix.dnmmod.Util.Utills;
import net.chixozhmix.dnmmod.blocks.entity.LeshyAltarEntity;
import net.chixozhmix.dnmmod.registers.ModEntityType;
import net.chixozhmix.dnmmod.entity.leshy.LeshyEntity;
import net.chixozhmix.dnmmod.registers.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class LeshyAltarBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    private static final List<Item> ITEMS = Arrays.asList(
            Items.GOLDEN_APPLE,
            Items.ENDER_PEARL,
            Items.APPLE
    );

    private static final List<Item> UNIQUE_ITEMS = Arrays.asList(
            Items.NETHERITE_INGOT,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_SWORD
    );

    private static final List<MobEffect> EFFECTS = Arrays.asList(
            MobEffects.NIGHT_VISION,
            MobEffects.DIG_SPEED,
            MobEffects.MOVEMENT_SPEED
    );

    private static final List<Item> USED_ITEM = Arrays.asList(
            Items.POTATO,
            Items.APPLE,
            Items.CHICKEN,
            Items.TROPICAL_FISH,
            Items.PUFFERFISH,
            Items.COD,
            Items.SALMON,
            Items.PORKCHOP,
            Items.MUTTON,
            Items.BEEF,
            Items.RABBIT,
            Items.SWEET_BERRIES,
            Items.CARROT,
            Items.BEETROOT
    );

    public LeshyAltarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LeshyAltarEntity(pPos, pState);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pLevel.isClientSide && !pState.is(pNewState.getBlock())) {
            destroyBlocksInRadius((ServerLevel) pLevel, pPos, 3);
            spawnCreature((ServerLevel) pLevel, pPos);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                 InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if(!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if(blockEntity instanceof LeshyAltarEntity altarEntity) {
                if(isUsedItem(itemStack) && altarEntity.canUse()) {
                    if(!pPlayer.isCreative()) {
                        itemStack.shrink(1);
                    }

                    altarEntity.incrementUseCount();
                    Utills.randomGiver(pPlayer, EFFECTS, ITEMS, UNIQUE_ITEMS);

                } else {
                    pPlayer.sendSystemMessage(Component.translatable("ui.dnmmod.leshy_altar_msg"));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private boolean isUsedItem(ItemStack item) {
        return USED_ITEM.contains(item.getItem());
    }

    //spaw
    private void spawnCreature(ServerLevel level, BlockPos pos) {
        LeshyEntity leshy = ModEntityType.LESHY.get().create(level);

        if(leshy != null) {
            leshy.setPos(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);
            level.addFreshEntity(leshy);
        }
    }

    //destroyBlock
    private void destroyBlocksInRadius(ServerLevel level, BlockPos centerPos, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(targetPos);

                    if (!blockState.isAir() && !targetPos.equals(centerPos) && y >= 0) {
                        level.destroyBlock(targetPos, false);
                    }
                }
            }
        }
    }
}
