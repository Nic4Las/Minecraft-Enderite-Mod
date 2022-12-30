package net.enderitemc.enderitemod.tools;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;

// import java.util.List;

// import com.google.common.collect.Lists;

// import net.minecraft.block.BlockState;
// import net.minecraft.block.Material;
// import net.minecraft.client.item.TooltipContext;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.item.ItemStack;

// import net.minecraft.sound.SoundEvents;
// import net.minecraft.text.LiteralText;
// import net.minecraft.text.Text;
// import net.minecraft.text.TranslatableText;
// import net.minecraft.util.ActionResult;
// import net.minecraft.util.Hand;
// import net.minecraft.util.TypedActionResult;
// import net.minecraft.world.World;

public class PickaxeSubclass extends PickaxeItem {

    public int superAufladung;

    public PickaxeSubclass(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    // @Override
    // public TypedActionResult<ItemStack> use(World world, PlayerEntity
    // playerEntity, Hand hand) {
    // playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);

    // int temp = 0;
    // try {
    // temp =
    // Integer.parseInt(playerEntity.getStackInHand(hand).getTag().get("superAufladung").asString());
    // } catch (NullPointerException e) {
    // temp = 0;
    // }
    // if (playerEntity.isSneaking()) {
    // playerEntity.getStackInHand(hand).getTag().putInt("superAufladung", --temp >
    // -1 ? temp : 0);
    // } else {
    // playerEntity.getStackInHand(hand).getTag().putInt("superAufladung", ++temp);
    // }
    // playerEntity.sendMessage(new LiteralText(Integer.toString(temp)), false);

    // return new TypedActionResult<>(ActionResult.SUCCESS,
    // playerEntity.getStackInHand(hand));
    // }

    // @Override
    // public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
    // Material material = state.getMaterial();
    // int temp = 0;
    // try {
    // temp = Integer.parseInt(stack.getTag().get("superAufladung").asString());
    // } catch (NullPointerException e) {
    // temp = 1;
    // }
    // return material != Material.METAL && material != Material.REPAIR_STATION &&
    // material != Material.STONE
    // ? super.getMiningSpeedMultiplier(stack, state)
    // : this.miningSpeed * temp;
    // }

}