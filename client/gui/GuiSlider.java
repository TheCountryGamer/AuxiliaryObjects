package com.countrygamer.auxiliaryobjects.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlider extends GuiButton {
	
	public float	sliderValue		= 0.0F;
	public float	sliderMaxValue	= 1.0F;
	public int		actualValue		= 0;
	public boolean	dragging		= false;
	public String	label;
	
	public GuiSlider(int id, int x, int y, String label) {
		this(id, x, y, 0.0F, 1.0F, label);
	}
	
	public GuiSlider(int id, int x, int y, float starting, float maxValue, String label) {
		this(id, x, y, 150, 20, starting, maxValue, label);
	}
	
	public GuiSlider(int id, int x, int y, int width, int height, float starting, float maximum,
			String label) {
		super(id, x, y, width, height < 20 ? 20 : height, label);
		this.label = label;
		this.sliderValue = 0.0F;
		this.sliderMaxValue = 1.0F;
	}
	
	protected int getHoverState(boolean par1) {
		return 0;
	}
	
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.enabled) {
			if (this.dragging) {
				this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);
				/*
				 * currentVal : maxVal
				 * currentX : width
				 * 
				 * cVal			cX
				 * ----    =	---
				 * maxVal		width
				 * 
				 * maxVal(cX) = cVal(width)
				 * 
				 * cVal = (maxVal * cX) / width
				 * 
				 */
				this.actualValue = (int) (((this.sliderValue * this.sliderMaxValue) * 255) / this.sliderMaxValue);
				if (this.actualValue < 0) this.actualValue = 0;
				if (this.actualValue > 255) this.actualValue = 255;
				
				if (this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}
				
				if (this.sliderValue > this.sliderMaxValue) {
					this.sliderValue = this.sliderMaxValue;
				}
				
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition
					+ (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66,
					4, 20);
			this.drawTexturedModalRect(this.xPosition
					+ (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196,
					66, 4, 20);
		}
		//this.displayString = label + ": " + this.actualValue;
	}
	
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		boolean x = false;
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);
			
			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}
			
			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}
			
			x = true;
		}
		else {
			x = false;
		}
		
		this.dragging = !this.dragging;
		
		return x;
	}
	
	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}
}
