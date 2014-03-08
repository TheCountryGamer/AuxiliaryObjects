package com.countrygamer.auxiliaryobjects.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;
import com.countrygamer.auxiliaryobjects.lib.EnumPartition;
import com.countrygamer.core.Core;
import com.countrygamer.core.Items.ItemBase;
import com.countrygamer.core.lib.CoreUtil;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * An Item class which allows the Item's tag compound to store up to X amount of items,
 * be able to be partitioned, and have an advanced setting.
 * 
 * @author Country_Gamer
 * 
 */
public class ItemMultiItem extends ItemBase {
	
	public static final NBTTagCompound	emptyTagCom	= new NBTTagCompound();
	public static final int				maxItemNum	= 5;
	public static final int				maxDamage	= 100;
	public static final boolean			takeStack	= Core.DEBUG;
	
	public ItemMultiItem(String modid, String name) {
		super(modid, name);
		this.maxStackSize = 1;
		this.setMaxDamage(maxDamage);
		// this.efficiencyOnProperMaterial =
		// AuxiliaryObjects.basicMat.getEfficiencyOnProperMaterial();
		// this.damageVsEntity = 2.0F + AuxiliaryObjects.basicMat.getDamageVsEntity();
		
	}
	
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}
	
	//
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		if (itemStack != null && !itemStack.hasTagCompound()) {
			NBTTagCompound tagCom = new NBTTagCompound();
			tagCom.setTag("multiTagCom", this.newMultiTagCompound());
			itemStack.setTagCompound(tagCom);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(itemStack);
		list.add(multiTagCom.getInteger("damage") + "/" + ItemMultiItem.maxDamage);
		
		// AuxiliaryObjects.log.info("P;" + ItemMultiItem.getPartition(itemStack));
		list.add("Partition:   " + ItemMultiItem.getPartition(itemStack).getName());
		
		List<ItemStack> stacks = new ArrayList();
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(itemStack, i);
			if (stackInSlot != null) stacks.add(stackInSlot);
		}
		
		if (!stacks.isEmpty()) {
			list.add("Items:");
			for (ItemStack stackInSlot : stacks) {
				if (stackInSlot != null) {
					String str = "    " + stackInSlot.getDisplayName() + " - "
							+ (stackInSlot.getMaxDamage() - stackInSlot.getItemDamage()) + "/"
							+ stackInSlot.getMaxDamage();
					list.add(str);
				}
			}
		}
		
	}
	
	//
	/**
	 * Returns a new NBTTagCompound. This holds data for:
	 * |+| "partition"; EnumPartition ID
	 * |+| keys "0" through "5" (the maxItemNum) store ItemStack data
	 * |+| "advanced"; boolean specifying if this item is an advanced item
	 * 
	 * @return
	 */
	public static NBTTagCompound newMultiTagCompound() {
		NBTTagCompound multiTagCom = new NBTTagCompound();
		
		multiTagCom.setInteger("partition", EnumPartition.NONE.getID());
		multiTagCom.setBoolean("advanced", false);
		multiTagCom.setInteger("damage", ItemMultiItem.maxDamage);
		multiTagCom
				.setFloat("efficency", AuxiliaryObjects.basicMat.getEfficiencyOnProperMaterial());
		multiTagCom
				.setFloat("damageVsEntity", 2.0F + AuxiliaryObjects.basicMat.getDamageVsEntity());
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			multiTagCom.setTag(i + "", ItemMultiItem.emptyTagCom);
		}
		
		return multiTagCom;
	}
	
	/**
	 * Returns the tag compound stored in the item stack under the key "multiTagCom".
	 * If the passed ItemStack has no tag compound, returns a new multi tag compound.
	 * 
	 * @param itemStack
	 * @return
	 */
	public static NBTTagCompound getMultiTagCompound(ItemStack itemStack) {
		if (itemStack.hasTagCompound()
				&& itemStack.getTagCompound().getCompoundTag("multiTagCom") != ItemMultiItem.emptyTagCom)
			return itemStack.getTagCompound().getCompoundTag("multiTagCom");
		else
			return ItemMultiItem.newMultiTagCompound();
	}
	
	/**
	 * Set the NBTTagCompound at key "multiTagCom" in the ItemStack's tag compound to parameter
	 * multiTagCom. If parameter multiTagCom is null, and paramenter generateNew is true, a fresh
	 * multi tag compound will be generated. Parameter generateNew should only be false if setting
	 * the tag compound to null.
	 * 
	 * @param itemStack
	 * @param multiTagCom
	 * @param generateNew
	 */
	public static ItemStack setMultiTagCompound(ItemStack itemStack, NBTTagCompound multiTagCom,
			boolean generateNew) {
		ItemStack newItemStack = itemStack.copy();
		
		if (!newItemStack.hasTagCompound()) newItemStack.setTagCompound(new NBTTagCompound());
		
		NBTTagCompound tagCom = newItemStack.getTagCompound();
		if (multiTagCom == null) {
			AuxiliaryObjects.log.info("Null input tagcom");
			if (generateNew) multiTagCom = ItemMultiItem.newMultiTagCompound();
		}
		tagCom.setTag("multiTagCom", multiTagCom);
		newItemStack.setTagCompound(tagCom);
		
		return newItemStack;
	}
	
	/**
	 * Returns an ItemStack. This stack is generated from the data stored in the
	 * multi tag compound at key 'slot' (passed 'slot' is converted to string).
	 * 
	 * @param sourceStack
	 * @param slot
	 * @return
	 */
	public static ItemStack getStackInSlot(ItemStack sourceStack, int slot) {
		ItemStack stackInSlot = null;
		if (sourceStack != null && (slot > 0 && slot <= ItemMultiItem.maxItemNum)) {
			NBTTagCompound multiTagCom = (NBTTagCompound) ItemMultiItem.getMultiTagCompound(
					sourceStack).copy();
			
			NBTTagCompound itemStackTagCom = multiTagCom.getCompoundTag(slot + "");
			if (!itemStackTagCom.hasNoTags()) {
				String name = itemStackTagCom.getString("name");
				// AuxiliaryObjects.log.info(name);
				int size = itemStackTagCom.getInteger("size");
				int damage = itemStackTagCom.getInteger("damage");
				
				stackInSlot = new ItemStack((Item) Item.itemRegistry.getObject(name), size, damage);
				
				if (itemStackTagCom.getBoolean("hasTagCom")) {
					NBTTagCompound stackTagCom = itemStackTagCom.getCompoundTag("tagcom");
					stackInSlot.setTagCompound(stackTagCom);
					// AuxiliaryObjects.log.info("returnin stack has tag com");
				}
			}
			// else
			// AuxiliaryObjects.log.info("Is empty compound");
		}
		else {
			// AuxiliaryObjects.log.info("invalid slot or stacks");
		}
		return stackInSlot;
	}
	
	public static int getSlotOfStack(ItemStack sourceStack, ItemStack stackInSlot) {
		int slot = -1;
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack slotStack = ItemMultiItem.getStackInSlot(sourceStack, i);
			if (ItemStack.areItemStacksEqual(slotStack, stackInSlot)
					&& ItemStack.areItemStackTagsEqual(slotStack, stackInSlot)) return i;
		}
		
		return slot;
	}
	
	/**
	 * Returns false if either itemstack is null, or the slot is invalid. Otherwise, will store the
	 * itemStack in he tag compound of sourceStack.
	 * 
	 * @param sourceStack
	 * @param slot
	 * @param itemStack
	 * @return
	 */
	public static ItemStack setStackInSlot(ItemStack sourceStack, int slot, ItemStack itemStack) {
		if (sourceStack != null && (slot > 0 && slot <= ItemMultiItem.maxItemNum)) {
			NBTTagCompound multiTagCom = (NBTTagCompound) ItemMultiItem.getMultiTagCompound(
					sourceStack).copy();
			
			if (itemStack == null) {
				multiTagCom.removeTag(slot + "");
				return ItemMultiItem.setMultiTagCompound(sourceStack, multiTagCom, true);
			}
			
			NBTTagCompound itemstackTagCom = new NBTTagCompound();
			Item item = itemStack.getItem();
			String name = Item.itemRegistry.getNameForObject(item);
			// AuxiliaryObjects.log.info(name);
			itemstackTagCom.setString("name", name);
			itemstackTagCom.setInteger("size", itemStack.stackSize);
			itemstackTagCom.setInteger("damage", itemStack.getItemDamage());
			itemstackTagCom.setBoolean("hasTagCom", itemStack.hasTagCompound());
			if (itemStack.hasTagCompound()) AuxiliaryObjects.log.info("Has tag com");
			itemstackTagCom.setTag("tagcom",
					itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound());
			multiTagCom.setTag(slot + "", itemstackTagCom);
			
			return ItemMultiItem.setMultiTagCompound(sourceStack, multiTagCom, true);
		}
		else {
			// AuxiliaryObjects.log.info("Invalid slot or stacks");
		}
		return sourceStack.copy();
	}
	
	/**
	 * Returns the total number of items this Multi Item stores.
	 * 
	 * @param sourceStack
	 * @return
	 */
	public static int getCurrentNumberOfItems(ItemStack sourceStack) {
		int items = 0;
		
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			if (!multiTagCom.getCompoundTag(i + "").hasNoTags()) {
				items++;
			}
		}
		
		return items;
	}
	
	/**
	 * Returns the next available slot index. Returns -1 if all slots full.
	 * 
	 * @param sourceStack
	 * @return
	 */
	public static int getNextOpenSlot(ItemStack sourceStack) {
		int slot = -1;
		
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			if (multiTagCom.getCompoundTag(i + "").hasNoTags()) {
				slot = i;
				break;
			}
			else {
				// AuxiliaryObjects.log.info(multiTagCom.getCompoundTag(i + "").getString("name"));
			}
		}
		
		return (slot > 0 && slot <= ItemMultiItem.maxItemNum) ? slot : -1;
	}
	
	/**
	 * Returns true if the parameter 'inflixStack' is already present in
	 * the parameter 'sourceStack'. 'sourceStack' should be an ItemStack
	 * with its item being a MultiItem
	 * 
	 * @param sourceStack
	 * @param inflixStack
	 * @return
	 */
	public static boolean hasItemInflixed(ItemStack sourceStack, ItemStack inflixStack) {
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(sourceStack, i);
			if (stackInSlot != null) {
				if (ItemStack.areItemStacksEqual(stackInSlot, inflixStack)
						&& ItemStack.areItemStackTagsEqual(stackInSlot, inflixStack)) {
					return true;
				}
				else {
					boolean instanceofStack = inflixStack.getItem().getClass()
							.isInstance(stackInSlot.getItem())
							|| stackInSlot.getItem().getClass().isInstance(inflixStack.getItem());
					boolean sameDamage = true;
					if (inflixStack.isStackable() && stackInSlot.isStackable()) {
						sameDamage = inflixStack.getItemDamage() == stackInSlot.getItemDamage();
					}
					boolean sameName = inflixStack.getUnlocalizedName() == stackInSlot
							.getUnlocalizedName();
					// AuxiliaryObjects.log.info(instanceofStack + "");
					if (instanceofStack && sameDamage && sameName) return true;
				}
			}
		}
		return false;
	}
	
	public static boolean hasPotionItemStack(ItemStack sourceStack) {
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(sourceStack, i);
			if (stackInSlot != null && stackInSlot.getItem() == Items.potionitem) return true;
		}
		return false;
	}
	
	public static ItemStack[] getPotionItemStacks(ItemStack sourceStack) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(sourceStack, i);
			if (stackInSlot != null && stackInSlot.getItem() == Items.potionitem)
				stacks.add(stackInSlot);
		}
		return ItemMultiItem.getStackArray(stacks.toArray());
	}
	
	/**
	 * Returns true if the 'sourceStack' can hold 'inflixStack'.
	 * 
	 * @param sourceStack
	 * @param inflixStack
	 * @return
	 */
	public static boolean canInflixItemStack(ItemStack sourceStack, ItemStack inflixStack) {
		boolean valid = false;
		int slot = ItemMultiItem.getNextOpenSlot(sourceStack);
		if (slot != -1 && slot <= ItemMultiItem.maxItemNum) {
			// has availible slot
			if (!ItemMultiItem.hasItemInflixed(sourceStack, inflixStack)) {
				// is not already inflixed
				valid = true;
			}
		}
		return valid;
	}
	
	/**
	 * Infixes 'inflixStack' into the 'sourceStack'.
	 * 
	 * @param sourceStack
	 * @param inflixStack
	 * @return
	 */
	public static ItemStack inflixItemStack(ItemStack sourceStack, ItemStack inflixStack) {
		ItemStack newSource = sourceStack.copy();
		if (ItemMultiItem.canInflixItemStack(sourceStack, inflixStack)) {
			int slot = ItemMultiItem.getNextOpenSlot(newSource);
			// AuxiliaryObjects.log.info("Slot: " + slot);
			if (slot > 0) {
				newSource = ItemMultiItem.setStackInSlot(newSource, slot, inflixStack);
				if (inflixStack.getItem() instanceof ItemSword) {
					Item multiItem = (ItemMultiItem) sourceStack.getItem();
				}
			}
		}
		return newSource;
	}
	
	/**
	 * Returns the EnumPartition stored in the multi tag compound.
	 * 
	 * @param sourceStack
	 * @return
	 */
	public static EnumPartition getPartition(ItemStack sourceStack) {
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		int partID = multiTagCom.getInteger("partition");
		EnumPartition partition = EnumPartition.getEnumForID(partID);
		// AuxiliaryObjects.log.info(partID + ":" + partition);
		return partition;
	}
	
	/**
	 * Sets the 'sourceStack''s partition to the passed parition
	 * 
	 * @param sourceStack
	 * @param partition
	 * @return
	 */
	public static ItemStack setPartition(ItemStack sourceStack, EnumPartition partition) {
		ItemStack newSource = sourceStack.copy();
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(newSource);
		multiTagCom.setInteger("partition", partition.getID());
		newSource = ItemMultiItem.setMultiTagCompound(newSource, multiTagCom, true);
		return newSource;
	}
	
	//
	public static ItemStack[] getStackArray(Object[] ar) {
		ItemStack[] isAr = new ItemStack[ar.length];
		for (int i = 0; i < ar.length; i++) {
			isAr[i] = (ItemStack) ar[i];
		}
		return isAr;
	}
	
	//
	/**
	 * Return the itemDamage represented by this ItemStack. Defaults to the itemDamage field on
	 * ItemStack, but can be overridden here for other sources such as NBT.
	 * 
	 * @param stack
	 *            The itemstack that is damaged
	 * @return the damage value
	 */
	public int getDamage(ItemStack stack) {
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(stack);
		return multiTagCom.getInteger("damage");
	}
	
	public void setDamage(ItemStack stack, int damage) {
		if (damage < 0) damage = 0;
		
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(stack);
		multiTagCom.setInteger("damage", damage);
		ItemMultiItem.setMultiTagCompound(stack, multiTagCom, true);
		
	}
	
	/**
	 * Return the itemDamage display value represented by this itemstack.
	 * 
	 * @param stack
	 *            the stack
	 * @return the damage value
	 */
	public int getDisplayDamage(ItemStack stack) {
		return this.getDamage(stack);
	}
	
	//
	/**
	 * Tool Types:
	 * 
	 * @param sourceStack
	 * @param toolType
	 * @return
	 */
	public static ItemStack[] getTools(ItemStack sourceStack, String toolType) {
		List<ItemStack> tools = new ArrayList();
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(sourceStack);
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(sourceStack, i);
			if (stackInSlot != null) {
				if (stackInSlot.getItem().isItemTool(stackInSlot)
						&& stackInSlot.getItem() instanceof ItemTool) {
					if (!toolType.equals("")) {
						if (stackInSlot.getItem().getToolClasses(stackInSlot).contains(toolType)) {
							tools.add(stackInSlot);
						}
					}
					else
						tools.add(stackInSlot);
				}
			}
		}
		return ItemMultiItem.getStackArray(tools.toArray());
	}
	
	//
	//
	protected float	efficiencyOnProperMaterial	= 4.0F;
	
	// private float damageVsEntity;
	
	@Override
	public float func_150893_a(ItemStack itemstack, Block block) {
		return block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil
				&& block.getMaterial() != Material.rock ? super.func_150893_a(itemstack, block)
				: this.efficiencyOnProperMaterial;
	}
	
	public static String toolClassFromBlock(Block block) {
		String toolClass = "";
		if (Sets.newHashSet(
				new Block[] {
						Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab,
						Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore,
						Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore,
						Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack,
						Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore,
						Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail,
						Blocks.golden_rail, Blocks.activator_rail
				}).contains(block)) {
			toolClass = "pickaxe";
		}
		if (Sets.newHashSet(
				new Block[] {
						Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer,
						Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand,
						Blocks.mycelium
				}).contains(block)) {
			toolClass = "shovel";
		}
		if (Sets.newHashSet(
				new Block[] {
						Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest,
						Blocks.pumpkin, Blocks.lit_pumpkin
				}).contains(block)) {
			toolClass = "axe";
		}
		return toolClass;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		String toolClass = ItemMultiItem.toolClassFromBlock(block);
		
		ItemStack[] tools = ItemMultiItem.getTools(stack, toolClass);
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(stack);
		if (tools != null && tools[0] != null)
			return ToolMaterial.valueOf(((ItemTool) tools[0].getItem()).getToolMaterialName())
					.getEfficiencyOnProperMaterial();
		else
			return AuxiliaryObjects.basicMat.getEfficiencyOnProperMaterial();
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
		ItemStack[] tools = ItemMultiItem.getTools(itemStack, "pickaxe");
		List<ItemStack> validTools = new ArrayList();
		for (ItemStack toolStack : tools) {
			if (toolStack.getItem().canHarvestBlock(block, itemStack)) validTools.add(toolStack);
		}
		return !validTools.isEmpty();
	}
	
	//
	
	//
	
	//
	
	@Override
	public boolean isValidArmor(ItemStack itemStack, int armorType, Entity entity) {
		// return true if contains a piece of armor
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(itemStack);
		boolean hasArmor = false;
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(itemStack, i);
			if (stackInSlot != null) {
				hasArmor = stackInSlot.getItem().isValidArmor(stackInSlot, armorType, entity);
				if (hasArmor) return hasArmor;
			}
		}
		
		return hasArmor;
	}
	
	@Override
	public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
		AuxiliaryObjects.log.info("armor tex");
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(itemStack, i);
			if (stackInSlot != null)
				AuxiliaryObjects.log.info(stackInSlot.getDisplayName());
			else
				AuxiliaryObjects.log.info("Slot Null");
			if (stackInSlot != null
					&& stackInSlot.getItem().isValidArmor(stackInSlot, slot, entity)) {
				AuxiliaryObjects.log.info(slot + ":" + type);
				String str = stackInSlot.getItem().getArmorTexture(stackInSlot, entity, slot, type);
				AuxiliaryObjects.log.info(str);
				if (str != null) return str;
			}
		}
		return null;
	}
	
	@Override
	public boolean isItemTool(ItemStack itemStack) {
		// return true if contains a tool
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(itemStack);
		boolean hasTool = false;
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(itemStack, i);
			if (stackInSlot != null) {
				hasTool = stackInSlot.getItem().isItemTool(stackInSlot)
						&& stackInSlot.getItem() instanceof ItemTool;
				if (hasTool) return hasTool;
			}
		}
		return hasTool;
	}
	
	// Used to damage item
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ItemStack multiStack = stack.copy();
		
		ItemStack swordStack = null;
		NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(multiStack);
		for (int i = 0; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
			if (stackInSlot != null && stackInSlot.getItem() instanceof ItemSword) {
				swordStack = stackInSlot;
				break;
			}
		}
		
		if (swordStack != null) {
			ItemStack swordCopy = swordStack.copy();
			float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
					.getAttributeValue();
			// this is the default sword value
			// wood = 4
			// stone = 5
			// iron = 6
			// gold = 4
			// diamond = 7
			float newDamage = 4.0F + ((ItemSword) swordCopy.getItem()).func_150931_i();
			// AuxiliaryObjects.log.info(damage + ":" + newDamage);
			this.defaultHitAction(swordCopy, player, entity, newDamage);
			
			if (takeStack) {
				multiStack = ItemMultiItem.setStackInSlot(multiStack,
						ItemMultiItem.getSlotOfStack(multiStack, swordStack), swordCopy);
			}
			player.setCurrentItemOrArmor(0, multiStack);
			
			return true; // cancel any other things that occur
		}
		return false; // continue. has no sword
	}
	
	private void defaultHitAction(ItemStack stack, EntityPlayer player, Entity entity, float damage) {
		if (entity.canAttackWithItem()) {
			if (!entity.hitByEntity(player)) {
				float f = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
						.getAttributeValue();
				f = damage;
				int i = 0;
				float f1 = 0.0F;
				
				if (entity instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getEnchantmentModifierLiving(player,
							(EntityLivingBase) entity);
					i += EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) entity);
				}
				
				if (player.isSprinting()) {
					++i;
				}
				
				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = player.fallDistance > 0.0F && !player.onGround
							&& !player.isOnLadder() && !player.isInWater()
							&& !player.isPotionActive(Potion.blindness)
							&& player.ridingEntity == null && entity instanceof EntityLivingBase;
					
					if (flag && f > 0.0F) {
						f *= 1.5F;
					}
					
					f += f1;
					boolean flag1 = false;
					int j = EnchantmentHelper.getFireAspectModifier(player);
					
					if (entity instanceof EntityLivingBase && j > 0 && !entity.isBurning()) {
						flag1 = true;
						entity.setFire(1);
					}
					
					boolean flag2 = entity.attackEntityFrom(DamageSource.causePlayerDamage(player),
							f);
					
					if (flag2) {
						if (i > 0) {
							entity.addVelocity(
									(double) (-MathHelper.sin(player.rotationYaw * (float) Math.PI
											/ 180.0F)
											* (float) i * 0.5F),
									0.1D,
									(double) (MathHelper.cos(player.rotationYaw * (float) Math.PI
											/ 180.0F)
											* (float) i * 0.5F));
							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}
						
						if (flag) {
							player.onCriticalHit(entity);
						}
						
						if (f1 > 0.0F) {
							player.onEnchantmentCritical(entity);
						}
						
						if (f >= 18.0F) {
							player.triggerAchievement(AchievementList.overkill);
						}
						
						player.setLastAttacker(entity);
						
						if (entity instanceof EntityLivingBase) {
							EnchantmentHelper.func_151384_a((EntityLivingBase) entity, player);
						}
						
						EnchantmentHelper.func_151385_b(player, entity);
						ItemStack itemstack = player.getCurrentEquippedItem();
						Object object = entity;
						
						if (entity instanceof EntityDragonPart) {
							IEntityMultiPart ientitymultipart = ((EntityDragonPart) entity).entityDragonObj;
							
							if (ientitymultipart != null
									&& ientitymultipart instanceof EntityLivingBase) {
								object = (EntityLivingBase) ientitymultipart;
							}
						}
						
						if (itemstack != null && object instanceof EntityLivingBase) {
							itemstack.hitEntity((EntityLivingBase) object, player);
							
							if (itemstack.stackSize <= 0) {
								player.destroyCurrentEquippedItem();
							}
						}
						
						if (entity instanceof EntityLivingBase) {
							player.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
							
							if (j > 0) {
								entity.setFire(j * 4);
							}
						}
						
						player.addExhaustion(0.3F);
					}
					else if (flag1) {
						entity.extinguish();
					}
				}
			}
		}
	}
	
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase thisEntity,
			EntityLivingBase thatEntity) {
		// boolean supered = super.hitEntity(itemStack, thisEntity, thatEntity);
		itemStack.damageItem(1, thatEntity);
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		
		ItemStack multiStack = itemStack.copy();
		if (!world.isRemote) {
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Checks dyes
			int count = 0;
			for (int i = 0; i <= ItemMultiItem.maxItemNum; i++) {
				ItemStack stackInSlot = ItemMultiItem.getStackInSlot(itemStack, i);
				if (stackInSlot != null) {
					if (stackInSlot.getItem() instanceof ItemDye) count++;
				}
			}
			// AuxiliaryObjects.log.info(count + ":" + ItemMultiItem.maxItemNum);
			if (count == ItemMultiItem.maxItemNum) {
				multiStack = new ItemStack(AuxiliaryObjects.multidye);
				player.setCurrentItemOrArmor(0, multiStack);
				return multiStack;
			}
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(itemStack);
			ItemStack usageStack = null;
			
			HashMap<ItemStack, Boolean> stackBeenUsed = new HashMap<ItemStack, Boolean>();
			
			multiStack = this.bowCheckAndAction(multiStack, world, player);
			
			// Uses water bucket. If not partitioned, set to empty bucket
			usageStack = new ItemStack(Items.water_bucket);
			if (ItemMultiItem.hasItemInflixed(multiStack, usageStack)) {
				// AuxiliaryObjects.log.info("Using water bucket");
				if (player.isBurning()) {
					player.extinguish();
					if (takeStack
							&& multiTagCom.getInteger("partition") != EnumPartition.BUCKET.getID()) {
						multiStack = ItemMultiItem.setStackInSlot(multiStack,
								ItemMultiItem.getSlotOfStack(multiStack, usageStack),
								new ItemStack(Items.bucket));
					}
					stackBeenUsed.put(usageStack, true);
				}
			}
			usageStack = null;
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Uses an Potions
			if (ItemMultiItem.hasPotionItemStack(multiStack)) {
				// AuxiliaryObjects.log.info("Has potion");
				ItemStack[] potionStacks = ItemMultiItem.getPotionItemStacks(multiStack);
				for (ItemStack potionStack : potionStacks) {
					List list = ((ItemPotion) potionStack.getItem()).getEffects(potionStack);
					
					if (list != null) {
						Iterator iterator = list.iterator();
						
						while (iterator.hasNext()) {
							PotionEffect potioneffect = (PotionEffect) iterator.next();
							player.addPotionEffect(new PotionEffect(potioneffect));
						}
					}
					
					if (takeStack
							&& multiTagCom.getInteger("partition") != EnumPartition.POTION.getID()) {
						multiStack = ItemMultiItem.setStackInSlot(multiStack,
								ItemMultiItem.getSlotOfStack(multiStack, potionStack),
								new ItemStack(Items.glass_bottle));
					}
					stackBeenUsed.put(potionStack, true);
				}
				
			}
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Uses milk bucket. If not partitioned, set to empty bucket
			usageStack = new ItemStack(Items.milk_bucket);
			if (ItemMultiItem.hasItemInflixed(multiStack, usageStack)) {
				// AuxiliaryObjects.log.info("Using milk bucket");
				if (!player.getActivePotionEffects().isEmpty()) {
					player.curePotionEffects(usageStack);
					if (takeStack
							&& multiTagCom.getInteger("partition") != EnumPartition.BUCKET.getID()) {
						multiStack = ItemMultiItem.setStackInSlot(multiStack,
								ItemMultiItem.getSlotOfStack(multiStack, usageStack),
								new ItemStack(Items.bucket));
					}
					stackBeenUsed.put(usageStack, true);
				}
			}
			usageStack = null;
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Uses bucket
			usageStack = new ItemStack(Items.bucket);
			if (ItemMultiItem.hasItemInflixed(multiStack, usageStack)) {
				AuxiliaryObjects.log.info("Using bucket");
				ItemStack newBucket = this.rightClickBucket(usageStack, world, player);
				multiStack = ItemMultiItem.setStackInSlot(multiStack,
						ItemMultiItem.getSlotOfStack(multiStack, usageStack), newBucket);
				stackBeenUsed.put(usageStack, true);
			}
			usageStack = null;
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
				ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
				if (stackInSlot != null && stackBeenUsed.get(stackInSlot) != null
						&& !stackBeenUsed.get(stackInSlot)) {
					ItemStack newStack = stackInSlot.getItem().onItemRightClick(stackInSlot, world,
							player);
					if (takeStack) {
						multiStack = ItemMultiItem.setStackInSlot(multiStack,
								ItemMultiItem.getSlotOfStack(multiStack, stackInSlot), newStack);
					}
					stackBeenUsed.put(stackInSlot, true);
				}
			}
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			player.setCurrentItemOrArmor(0, multiStack);
			stackBeenUsed.clear();
		}
		return multiStack;
	}
	
	public ItemStack rightClickBucket(ItemStack bucketStack, World world, EntityPlayer player) {
		Block isFull = Blocks.air;
		boolean flag = isFull == Blocks.air;
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world,
				player, flag);
		
		if (movingobjectposition == null) {
			return bucketStack;
		}
		else {
			FillBucketEvent event = new FillBucketEvent(player, bucketStack, world,
					movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return bucketStack;
			}
			
			if (event.getResult() == Event.Result.ALLOW) {
				if (player.capabilities.isCreativeMode) {
					return bucketStack;
				}
				
				if (--bucketStack.stackSize <= 0) {
					return event.result;
				}
				
				if (!player.inventory.addItemStackToInventory(event.result)) {
					player.dropPlayerItemWithRandomChoice(event.result, false);
				}
				
				return bucketStack;
			}
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;
				
				if (!world.canMineBlock(player, i, j, k)) {
					return bucketStack;
				}
				
				if (flag) {
					if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, bucketStack)) {
						return bucketStack;
					}
					
					Material material = world.getBlock(i, j, k).getMaterial();
					int l = world.getBlockMetadata(i, j, k);
					
					if (material == Material.water && l == 0) {
						world.setBlockToAir(i, j, k);
						return new ItemStack(Items.water_bucket);
					}
					
					if (material == Material.lava && l == 0) {
						world.setBlockToAir(i, j, k);
						return new ItemStack(Items.lava_bucket);
					}
				}
				else {
					if (isFull == Blocks.air) {
						return new ItemStack(Items.bucket);
					}
					
					if (movingobjectposition.sideHit == 0) {
						--j;
					}
					
					if (movingobjectposition.sideHit == 1) {
						++j;
					}
					
					if (movingobjectposition.sideHit == 2) {
						--k;
					}
					
					if (movingobjectposition.sideHit == 3) {
						++k;
					}
					
					if (movingobjectposition.sideHit == 4) {
						--i;
					}
					
					if (movingobjectposition.sideHit == 5) {
						++i;
					}
					
					if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, bucketStack)) {
						return bucketStack;
					}
					
					/*s
					if (this.tryPlaceContainedLiquid(player, i, j, k)
							&& !player.capabilities.isCreativeMode) {
						return new ItemStack(Items.bucket);
					}
					 */
				}
			}
			
			return bucketStack;
		}
	}
	
	public ItemStack bowCheckAndAction(ItemStack multiStack, World world, EntityPlayer player) {
		int slot = 0;
		ItemStack bowStack = null;
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
			if (stackInSlot != null && stackInSlot.getItem() == Items.bow) {
				slot = i;
				bowStack = stackInSlot;
				break;
			}
		}
		
		if (bowStack != null) {
			ArrowNockEvent event = new ArrowNockEvent(player, bowStack);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.isCanceled()) {
				return event.result;
			}
			
			if (player.capabilities.isCreativeMode || player.inventory.hasItem(Items.arrow)) {
				int j = 72000;
				ArrowLooseEvent event2 = new ArrowLooseEvent(player, bowStack, j);
				MinecraftForge.EVENT_BUS.post(event2);
				if (event2.isCanceled()) {
					return multiStack;
				}
				j = event2.charge;
				
				boolean flag = player.capabilities.isCreativeMode
						|| EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId,
								bowStack) > 0;
				
				if (flag || player.inventory.hasItem(Items.arrow)) {
					float f = (float) j / 20.0F;
					f = (f * f + f * 2.0F) / 3.0F;
					
					if ((double) f < 0.1D) {
						return multiStack;
					}
					
					if (f > 1.0F) {
						f = 1.0F;
					}
					
					EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);
					
					if (f == 1.0F) {
						entityarrow.setIsCritical(true);
					}
					
					int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
							bowStack);
					
					if (k > 0) {
						entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);
					}
					
					int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
							bowStack);
					
					if (l > 0) {
						entityarrow.setKnockbackStrength(l);
					}
					
					if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bowStack) > 0) {
						entityarrow.setFire(100);
					}
					
					bowStack.damageItem(1, player);
					world.playSoundAtEntity(player, "random.bow", 1.0F,
							1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					
					if (flag) {
						entityarrow.canBePickedUp = 2;
					}
					else {
						player.inventory.consumeInventoryItem(Items.arrow);
					}
					
					if (!world.isRemote) {
						world.spawnEntityInWorld(entityarrow);
					}
					
					multiStack = ItemMultiItem.setStackInSlot(multiStack, slot, bowStack);
				}
			}
		}
		
		return multiStack;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
			int z, int blockSide, float xFloat, float yFloat, float zFloat) {
		boolean takeStack = !Core.DEBUG;
		
		boolean used = false;
		ItemStack multiStack = itemStack.copy();
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z);
			
			NBTTagCompound multiTagCom = ItemMultiItem.getMultiTagCompound(itemStack);
			ItemStack usageStack = null;
			
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// Uses water bucket. If not partitioned, set to empty bucket
			usageStack = new ItemStack(Items.lava_bucket);
			if (ItemMultiItem.hasItemInflixed(multiStack, usageStack)) {
				// AuxiliaryObjects.log.info("Using lava bucket");
				ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(
						new ItemStack(block));
				if (itemstack != null) {
					// AuxiliaryObjects.log.info("Valid smelt stack");
					Block blockSmelt = Block.getBlockFromItem(itemstack.getItem());
					if (!blockSmelt.getUnlocalizedName().equals("tile.air")) {
						// is a block
						// AuxiliaryObjects.log.info("Valid block");
						ItemStack blockStack = itemstack.copy();
						world.setBlock(x, y, z, blockSmelt);
						
						blockStack.stackSize -= 1;
						// AuxiliaryObjects.log.info(blockStack.stackSize + "");
						if (blockStack.stackSize > 0)
							CoreUtil.dropItemStack(world, blockStack, x, y, z);
						
						used = used || true;
					}
					else {
						// is a item
						
					}
				}
			}
			usageStack = null;
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			List<ItemStack> hoeStacks = new ArrayList<ItemStack>();
			for (int i = 1; i < ItemMultiItem.maxItemNum; i++) {
				ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
				if (stackInSlot != null && (ItemHoe.class).isInstance(stackInSlot.getItem())) {
					hoeStacks.add(stackInSlot);
				}
			}
			
			if (!hoeStacks.isEmpty()) {
				ItemHoe item = (ItemHoe) hoeStacks.get(0).getItem();
				used = used
						|| item.onItemUse(hoeStacks.get(0), player, world, x, y, z, blockSide,
								xFloat, yFloat, zFloat);
			}
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			player.setCurrentItemOrArmor(0, multiStack);
		}
		return used;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer player) {
		boolean used = false;
		
		ItemStack multiStack = itemStack.copy();
		
		for (int i = 1; i < ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
			if (stackInSlot != null) {
				ItemStack stackInSlot1 = stackInSlot.copy();
				used = used
						|| stackInSlot1.getItem().onBlockStartBreak(stackInSlot1, x, y, z, player);
				if (takeStack) {
					multiStack = ItemMultiItem.setStackInSlot(multiStack,
							ItemMultiItem.getSlotOfStack(multiStack, stackInSlot), stackInSlot1);
				}
			}
		}
		
		player.setCurrentItemOrArmor(0, multiStack);
		
		return used;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y,
			int z, EntityLivingBase thisEntity) {
		boolean used = false;
		
		ItemStack multiStack = itemStack.copy();
		
		ItemStack stackInSlot = ItemMultiItem.getTools(multiStack,
				ItemMultiItem.toolClassFromBlock(block))[0];
		if (stackInSlot != null) {
			ItemStack stackInSlot1 = stackInSlot.copy();
			used = used
					|| stackInSlot1.getItem().onBlockDestroyed(stackInSlot1, world, block, x, y, z,
							thisEntity);
			if (takeStack) {
				multiStack = ItemMultiItem.setStackInSlot(multiStack,
						ItemMultiItem.getSlotOfStack(multiStack, stackInSlot), stackInSlot1);
			}
			
		}
		
		if (thisEntity instanceof EntityPlayer)
			((EntityPlayer) thisEntity).setCurrentItemOrArmor(0, multiStack);
		else
			itemStack = multiStack;
		
		return used;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player,
			EntityLivingBase entity) {
		boolean used = false;
		
		ItemStack multiStack = itemstack.copy();
		
		for (int i = 1; i < ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
			if (stackInSlot != null) {
				ItemStack stackInSlot1 = stackInSlot.copy();
				used = used
						|| stackInSlot1.getItem().itemInteractionForEntity(stackInSlot1, player,
								entity);
				if (takeStack) {
					multiStack = ItemMultiItem.setStackInSlot(multiStack,
							ItemMultiItem.getSlotOfStack(multiStack, stackInSlot), stackInSlot1);
				}
			}
		}
		
		player.setCurrentItemOrArmor(0, multiStack);
		
		return used;
	}
	
}
