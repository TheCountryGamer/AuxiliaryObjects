package com.countrygamer.capo.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityWall;
import com.countrygamer.core.Base.block.BlockContainerBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWall extends BlockContainerBase {
	
	public BlockWall(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
		this.setBlockUnbreakable();
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return false;
	}
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z,
			int metadata) {
		return false;
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
	}
	
	@Override
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return false;
	}
	
	@Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return false;
	}
	
	@Override
	public void onFallenUpon(World world, int x, int y, int z, Entity entity, float unknown) {
		
	}
	
	//
	
	@Override
	public int getRenderType() {
		return 3000;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public int getLightOpacity() {
		// TODO refer to location aware version below
		return 0;
	}
	
	/**
	 * Location aware and overrideable version of the lightOpacity array,
	 * return the number to subtract from the light value when it passes through this block.
	 * 
	 * This is not guaranteed to have the tile entity in place before this is called, so it is
	 * Recommended that you have your tile entity call relight after being placed if you
	 * rely on it for light info.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z position
	 * @return The amount of light to block, 0 for air, 255 for fully opaque.
	 */
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		return 255;
		/*
		int opacity = 0;
		
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if (tileEnt != null && tileEnt instanceof TileEntityWall) {
			TileEntityWall tileEntWall = (TileEntityWall)tileEnt;
			// TODO Find a way to get the light opacity of a block with metadata
			// Currently, cannot pass meta data into function
			Block camoBlock = tileEntWall.block;
			//Capo.log.info((camoBlock == null) + "");
			if (camoBlock == null) {
				opacity = 0;
			}
			else {
				opacity = 255;
			}
			/*
			else if (camoBlock.isOpaqueCube()) {
				opacity = 255;
			}
			else {
				opacity = camoBlock.getLightOpacity(world, x, y, z);
			}
			/
		}
		Capo.log.info("" + opacity);
		return opacity;
		*/
	}
	
	private Block getBlock(IBlockAccess world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileEntityWall) {
			return ((TileEntityWall) tile).block;
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileEntityWall) {
			TileEntityWall teWall = (TileEntityWall) tile;
			Block camoBlock = teWall.block;
			int camoMeta = teWall.blockMeta;
			if (teWall.hasCamo) {
				return camoBlock.getIcon(side, camoMeta);
			}
		}
		return this.blockIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		Block block = this.getBlock(world, x, y, z);
		return block == null ? null : Item.getItemFromBlock(block);
	}
	
	boolean particles = false;
	
	/**
	 * Spawn a digging particle effect in the world, this is a wrapper
	 * around EffectRenderer.addBlockHitEffects to allow the block more
	 * control over the particles. Useful when you have entirely different
	 * texture sheets for different sides/locations in the world.
	 * 
	 * @param world
	 *            The current world
	 * @param target
	 *            The target the player is looking at {x/y/z/side/sub}
	 * @param effectRenderer
	 *            A reference to the current effect renderer.
	 * @return True to prevent vanilla digging particles form spawning.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target,
			EffectRenderer effectRenderer) {
		return !this.particles;
	}
	
	/**
	 * Spawn particles for when the block is destroyed. Due to the nature
	 * of how this is invoked, the x/y/z locations are not always guaranteed
	 * to host your block. So be sure to do proper sanity checks before assuming
	 * that the location is this block.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X position to spawn the particle
	 * @param y
	 *            Y position to spawn the particle
	 * @param z
	 *            Z position to spawn the particle
	 * @param meta
	 *            The metadata for the block before it was destroyed.
	 * @param effectRenderer
	 *            A reference to the current effect renderer.
	 * @return True to prevent vanilla break particles from spawning.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta,
			EffectRenderer effectRenderer) {
		return !this.particles;
	}
	
}
