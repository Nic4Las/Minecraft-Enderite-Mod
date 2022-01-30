package net.enderitemc.enderitemod.tools;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EnderiteShears extends ShearsItem {

    public EnderiteShears(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BeehiveBlock beehiveBlock;
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockstate = world.getBlockState(blockPos = context.getBlockPos());
        Block block = blockstate.getBlock();
        if (block instanceof BeehiveBlock && blockstate.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            beehiveBlock = ((BeehiveBlock)block);
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }
            beehiveBlock.dropHoneycomb(world, blockPos);
            beehiveBlock.takeHoney(world, blockstate, blockPos);
            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            world.emitGameEvent((Entity)playerEntity, GameEvent.SHEAR, blockPos);
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, player -> player.sendToolBreakStatus(context.getHand()));
            }
            return ActionResult.success(world.isClient);
        }
        return super.useOnBlock(context);
    }
    
}
