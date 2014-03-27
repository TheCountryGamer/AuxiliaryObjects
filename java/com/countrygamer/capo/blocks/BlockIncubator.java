package com.countrygamer.capo.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.core.Base.block.BlockContainerBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockIncubator extends BlockContainerBase {

	public BlockIncubator(Material mat, String modid, String name) {
		super(mat, modid, name, null);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float par7, float par8, float par9) {
		if (!player.isSneaking()) {
			player.openGui(Capo.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	private IIcon incuTop;
	@SideOnly(Side.CLIENT)
	private IIcon incuSide;
	@SideOnly(Side.CLIENT)
	private IIcon incuBottom;

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (side) {
		case 0:
			return this.incuBottom;
		case 1:
			return this.incuTop;
		default:
			return this.incuSide;
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.incuTop = iconRegister.registerIcon(this.modid
				+ ":"
				+ this.getUnlocalizedName().substring(
						this.getUnlocalizedName().indexOf(".") + 1) + "_top");
		this.incuBottom = iconRegister
				.registerIcon(this.modid
						+ ":"
						+ this.getUnlocalizedName().substring(
								this.getUnlocalizedName().indexOf(".") + 1)
								+ "_bottom");
		this.incuSide = iconRegister.registerIcon(this.modid
				+ ":"
				+ this.getUnlocalizedName().substring(
						this.getUnlocalizedName().indexOf(".") + 1) + "_side");
	}

}
