package com.countrygamer.capo.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.lib.Reference;

public class ItemRenderTeleCore implements IItemRenderer {
	
	public ItemRenderTeleCore() {
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if (type.equals(type.EQUIPPED)
				|| type.equals(type.EQUIPPED_FIRST_PERSON)
				|| type.equals(type.ENTITY)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;// helper.equals(helper.INVENTORY_BLOCK);
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack,
			Object... data) {
		if (type.equals(type.EQUIPPED)
				|| type.equals(type.EQUIPPED_FIRST_PERSON)
				|| type.equals(type.ENTITY)) {
			GL11.glPushMatrix();
			
			String resource = ".png";
			if (itemStack.hasTagCompound()
					&& itemStack.getTagCompound().getBoolean("isActive")) {
				resource = "_A.png";
			}
			Minecraft.getMinecraft().renderEngine
					.bindTexture(new ResourceLocation(
							Reference.MOD_ID,
							"textures/models/items/"
									+ itemStack.getItem().getUnlocalizedName()
											.substring(5) + resource));
			
			float scale = 2.0F;
			switch (type) {
				case EQUIPPED_FIRST_PERSON:
					// scale = 0.08F;
					// GL11.glScalef(scale, scale, scale);
					GL11.glTranslatef(1.0F, 0.2F, 0.0F);
					break;
				case EQUIPPED:
					// scale = 0.1F;
					// GL11.glScalef(scale, scale, scale);
					GL11.glTranslatef(0.0F, 0.25F, -0.3F);
					break;
				case ENTITY:
					// scale = 0.12F;
					// GL11.glScalef(scale, scale, scale);
					GL11.glTranslatef(0.0F, 0.3F, 0.0F);
					break;
				default:
					
					break;
			}
			GL11.glScalef(scale, scale, scale);
			
			float f5 = 0.0625F;
			ModelCore core = new ModelCore();
			this.rotation(core, itemStack);
			core.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5);
			
			GL11.glPopMatrix();
		}
	}
	
	private void rotation(ModelCore core, ItemStack itemStack) {
		NBTTagCompound tagCom = itemStack.getTagCompound();
		if (tagCom == null)
			tagCom = new NBTTagCompound();
		
		NBTTagCompound coreRots = (NBTTagCompound) tagCom
				.getCompoundTag("coreRots");
		float x = coreRots.getFloat("x");
		float y = coreRots.getFloat("y");
		float z = coreRots.getFloat("z");
		core.modelList.get(0).rotateAngleX = (x += 0.01F);
		core.modelList.get(0).rotateAngleY = (y += 0.05F);
		core.modelList.get(0).rotateAngleZ = (z += 0.01F);
		coreRots.setFloat("x", x);
		coreRots.setFloat("y", y);
		coreRots.setFloat("z", z);
		tagCom.setTag("coreRots", coreRots);
		
		itemStack.setTagCompound(tagCom);
		
	}
	
}
