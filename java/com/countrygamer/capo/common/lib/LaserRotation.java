package com.countrygamer.capo.common.lib;

import java.util.HashMap;

import net.minecraft.client.renderer.Tessellator;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.core.common.lib.LogBlock;

public class LaserRotation {
	
	HashMap<String, double[]> points = new HashMap<String, double[]>();
	
	public LaserRotation() {
		this.points.put("Distance", new double[] {});
		this.points.put("BotLeftSrc", new double[] {});
		this.points.put("BotRightSrc", new double[] {});
		this.points.put("TopLeftSrc", new double[] {});
		this.points.put("TopRightSrc", new double[] {});
		this.points.put("BotLeftTarget", new double[] {});
		this.points.put("BotRightTarget", new double[] {});
		this.points.put("TopLeftTarget", new double[] {});
		this.points.put("TopRightTarget", new double[] {});
		
	}
	
	public void createPoints(double tileX, double tileY, double tileZ, double entX, double entY,
			double entZ) {
		LogBlock log = new LogBlock(Capo.log, "\n");
		double radius = 0.1;
		log.addWithLine("Radius:\t" + radius);
		double xDif = -this.getDif(tileX, entX);
		double yDif = -this.getDif(tileY, entY);
		double zDif = this.getDif(tileZ, entZ);
		log.addWithLine("Difference\n" + xDif + "\t" + yDif + "\t" + zDif);
		double xDis = Math.abs(xDif);
		double yDis = Math.abs(yDif);
		double zDis = Math.abs(zDif);
		log.addWithLine("Distance\n" + xDis + "\t" + yDis + "\t" + zDis);
		double distanceHorizontal = Math.sqrt(xDis * xDis + zDis * zDis);
		double fullDistance = Math.sqrt(yDif * yDif + distanceHorizontal * distanceHorizontal);
		this.points.put("Distance", new double[] {
				distanceHorizontal, fullDistance
		});
		log.addWithLine("Horiz:\t" + distanceHorizontal);
		log.addWithLine("Total:\t" + fullDistance);
		
		for (String key : this.points.keySet()) {
			if (key.endsWith("Src") || key.endsWith("Target")) {
				double xyz = 0.5, y = -0.2;
				if (key.endsWith("Src")) {
					this.points.put(key, new double[] {
							xyz, xyz + y, xyz
					});
				}
				else {
					this.points.put(key, new double[] {
							xDif + xyz, yDif + xyz + y, zDif + xyz
					});
				}
			}
		}
		
		double rotX = Math.atan(yDis / distanceHorizontal);
		double rotX_Deg = Math.toDegrees(rotX);
		log.addWithLine("Rot X:\t" + rotX_Deg);
		
		double rotX_Deg_Top = 0;
		rotX_Deg_Top = rotX + 90;
		if (rotX_Deg_Top >= 360) rotX_Deg_Top -= 360;
		double rotX_Top = Math.toRadians(rotX_Deg_Top);
		
		double rotX_Deg_Bot = 0;
		rotX_Deg_Bot = rotX - 90;
		if (rotX_Deg_Bot < 0) rotX_Deg_Bot += 360;
		double rotX_Bot = Math.toRadians(rotX_Deg_Bot);
		
		log.addWithLine("");
		log.addWithLine("RotX Top: " + rotX_Deg_Top);
		log.addWithLine("RotX Bot: " + rotX_Deg_Bot);
		
		double sinRotX_Top = Math.sin(rotX_Top);
		double cosRotX_Top = Math.cos(rotX_Top);
		double rotXy_Top = sinRotX_Top * radius;
		double rotXz_Top = cosRotX_Top * radius;
		if (rotX_Top > 90 && rotX_Top < 180) rotXy_Top = -rotXy_Top;
		if (rotX_Top > 270 && rotX_Top < 360) rotXz_Top = -rotXz_Top;
		log.addWithLine("Top Y: " + rotXy_Top);
		log.addWithLine("Top Z: " + rotXz_Top);
		
		double sinRotX_Bot = Math.sin(rotX_Bot);
		double cosRotX_Bot = Math.cos(rotX_Bot);
		double rotXy_Bot = sinRotX_Bot * radius;
		double rotXz_Bot = cosRotX_Bot * radius;
		if (rotX_Bot > 90 && rotX_Bot < 180) rotXy_Bot = -rotXy_Bot;
		if (rotX_Bot > 270 && rotX_Bot < 360) rotXz_Bot = -rotXz_Bot;
		log.addWithLine("Bot Y: " + rotXy_Bot);
		log.addWithLine("Bot Z: " + rotXz_Bot);
		
		log.addWithLine("");
		
		for (String key : this.points.keySet()) {
			if (key.endsWith("Src") || key.endsWith("Target")) {
				double[] point = this.points.get(key);
				if (key.startsWith("Top")) {
					point[1] += rotXy_Top;
					point[2] += rotXz_Top;
				}
				else if (key.startsWith("Bot")) {
					point[1] += rotXy_Bot;
					point[2] += rotXz_Bot;
				}
				this.points.put(key, point);
			}
		}
		
		double rotY = Math.atan(zDis / xDis);
		double rotY_Deg = Math.toDegrees(rotY);
		log.addWithLine("Rot Y:\t" + rotY_Deg);
		
		double rotY_Deg_Right = this.checkAngle(rotY_Deg + 90);
		double rotY_Right = Math.toRadians(rotY_Deg_Right);
		double rotY_Deg_Left = this.checkAngle(rotY_Deg - 90);
		double rotY_Left = Math.toRadians(rotY_Deg_Left);
		
		double sinRotY_Deg_R = Math.sin(rotY_Right);
		double cosRotY_Deg_R = Math.cos(rotY_Right);
		double rotYz_R = sinRotY_Deg_R * radius;
		double rotYx_R = cosRotY_Deg_R * radius;
		double sinRotY_Deg_L = Math.sin(rotY_Left);
		double cosRotY_Deg_L = Math.cos(rotY_Left);
		double rotYz_L = sinRotY_Deg_L * radius;
		double rotYx_L = cosRotY_Deg_L * radius;
		
		rotYz_R = this.checkSin(rotY_Deg_Right, rotYz_R);
		rotYx_R = this.checkCos(rotY_Deg_Right, rotYx_R);
		rotYz_L = this.checkSin(rotY_Deg_Left, rotYz_L);
		rotYx_L = this.checkCos(rotY_Deg_Left, rotYx_L);
		
		log.addWithLine("\n");
		log.addWithLine("Deg R: " + rotY_Deg_Right);
		log.addWithLine("Rad R: " + rotY_Right);
		log.addWithLine("Cos R: " + cosRotY_Deg_R);
		log.addWithLine("RotY X R: " + rotYx_R);
		log.addWithLine("Deg L: " + rotY_Deg_Left);
		log.addWithLine("Rad L: " + rotY_Left);
		log.addWithLine("Cos L: " + cosRotY_Deg_L);
		log.addWithLine("RotY X L: " + rotYx_L);
		log.addWithLine("\n");
		
		log.addWithLine("RotY R: " + rotY_Deg_Right);
		log.addWithLine("RotY R X: " + rotYx_R);
		log.addWithLine("RotY R Z: " + rotYz_R);
		log.addWithLine("RotY L: " + rotY_Deg_Left);
		log.addWithLine("RotY L X: " + rotYx_L);
		log.addWithLine("RotY L Z: " + rotYz_L);
		log.addWithLine("");
		
		for (String key : this.points.keySet()) {
			if (key.endsWith("Src") || key.endsWith("Target")) {
				double[] point = this.points.get(key);
				if (key.startsWith("Right", 3)) {
					point[2] += rotYz_R;
					point[0] += rotYx_R;
				}
				else if (key.startsWith("Left", 3)) {
					point[2] += rotYz_L;
					point[0] += rotYx_L;
				}
				this.points.put(key, point);
			}
		}
		
		/**
		 * Source Laser Center
		 */
		/*
		double[] srcLC = new double[] {0.5, 0.7, 0.5};
		
		this.points.put("BotLeftSrc",	new double[] {
				srcLC[0] - 0.1, srcLC[1] - 0.1, srcLC[2]
		});
		this.points.put("BotRightSrc",	new double[] {
				srcLC[0] + 0.1, srcLC[1] - 0.1, srcLC[2]
		});
		this.points.put("TopLeftSrc",		new double[] {
				srcLC[0] - 0.1, srcLC[1] + 0.1, srcLC[2]
		});
		this.points.put("TopRightSrc",		new double[] {
				srcLC[0] + 0.1, srcLC[1] + 0.1, srcLC[2]
		});
		
		double[] targetLC = new double[] {
				xDis + srcLC[0],
				yDis + srcLC[1],
				zDis + srcLC[2]
		};
		
		this.points.put("BotLeftTarget",		new double[] {
				targetLC[0] - 0.1, targetLC[1] - 0.1, targetLC[2]
		});
		this.points.put("BotRightTarget",	new double[] {
				targetLC[0] + 0.1, targetLC[1] - 0.1, targetLC[2]
		});
		this.points.put("TopLeftTarget",		new double[] {
				targetLC[0] - 0.1, targetLC[1] + 0.1, targetLC[2]
		});
		this.points.put("TopRightTarget",		new double[] {
				targetLC[0] + 0.1, targetLC[1] + 0.1, targetLC[2]
		});
		 */
		
		log.log();
	}
	
	private double getDif(double tileX, double entX) {
		double val = tileX - entX;
		if (val != 0) val = -val;
		return val;
	}
	
	private double checkAngle(double angle) {
		if (angle < 0)
			angle += 360;
		while (angle >= 360)
			angle -= 360;
		return angle;
	}
	
	private double checkSin(double angle, double val) {
		if (angle > 90 && angle < 180)
			return -val;
		return val;
	}
	
	private double checkCos(double angle, double val) {
		if (angle > 270 && angle < 360)
			return -val;
		return val;
	}
	
	public void setup(double srcX, double srcY, double srcZ, double targetX, double targetY,
			double targetZ) {
		
		this.points.put("BotLeftSrc", new double[] {
				srcX - 0.1D, srcY - 0.1D, srcZ + 0.0D
		});
		this.points.put("BotRightSrc", new double[] {
				srcX + 0.1D, srcY - 0.1D, srcZ + 0.0D
		});
		this.points.put("TopLeftSrc", new double[] {
				srcX - 0.1D, srcY + 0.1D, srcZ + 0.0D
		});
		this.points.put("TopRightSrc", new double[] {
				srcX + 0.1D, srcY + 0.1D, srcZ + 0.0D
		});
		this.points.put("BotLeftTarget", new double[] {
				targetX - 0.1D, targetY - 0.1D, targetZ + 0.0D
		});
		this.points.put("BotRightTarget", new double[] {
				targetX + 0.1D, targetY - 0.1D, targetZ + 0.0D
		});
		this.points.put("TopLeftTarget", new double[] {
				targetX - 0.1D, targetY + 0.1D, targetZ + 0.0D
		});
		this.points.put("TopRightTarget", new double[] {
				targetX + 0.1D, targetY + 0.1D, targetZ + 0.0D
		});
		
		/*
		this.points.put("BotLeftSrc",	new double[] { - 0.1D,  - 0.1D,  + 0.0D});
		this.points.put("BotRightSrc",	new double[] { + 0.1D,  - 0.1D,  + 0.0D});
		this.points.put("TopLeftSrc",		new double[] { - 0.1D,  + 0.1D,  + 0.0D});
		this.points.put("TopRightSrc",		new double[] { + 0.1D,  + 0.1D,  + 0.0D});
		this.points.put("BotLeftTarget", 	new double[] { - 0.1D,  - 0.1D,  + 0.0D});
		this.points.put("BotRightTarget",	new double[] { + 0.1D,  - 0.1D,  + 0.0D});
		this.points.put("TopLeftTarget",		new double[] { - 0.1D,  + 0.1D,  + 0.0D});
		this.points.put("TopRightTarget",		new double[] { + 0.1D,  + 0.1D,  + 0.0D});
		 */
		
		double tX = Math.abs(srcX - targetX);
		double tY = Math.abs(srcY - targetY);
		double tZ = Math.abs(srcZ - targetZ);
		double distance = Math.sqrt(tX * tX + tZ * tZ);
		double rotX = Math.floor(Math.atan(tY / distance) * 180 / Math.PI);
		double rotY = Math.floor(Math.atan(tZ / tX) * 180 / Math.PI);
		// Capo.log.info("\n\tTX: " + tX + "\n\tTY: " + tY + "\n\tTZ: " + tZ + "\n\tRotX: " + rotX +
		// "\n\tRotY: " + rotY);
		
		double TopNewTheta = 0.0D;
		double BottomNewTheta = 0.0D;
		if (rotX >= 0 && rotX <= 90) {
			TopNewTheta = rotX + 90;
			BottomNewTheta = 360 - Math.abs(rotX - 90);
		}
		else if (rotX >= 270 && rotX <= 360) {
			TopNewTheta = (rotX + 90) - 360;
			BottomNewTheta = rotX - 90;
		}
		else {
			Capo.log.info("Error with Theta Calculations");
		}
		
		Capo.log.info("\nTopTheta: " + TopNewTheta + "\nBottomTheta: " + BottomNewTheta);
		
		double newTopY = Math.sin(TopNewTheta) * 0.1;
		double newTopZ = Math.cos(TopNewTheta) * 0.1;
		double newBottomY = Math.sin(BottomNewTheta) * -0.1;
		double newBottomZ = Math.cos(BottomNewTheta) * -0.1;
		
		if (TopNewTheta > 90 && TopNewTheta <= 180) {
			newTopZ = -newTopZ;
		}
		if (BottomNewTheta > 270 && BottomNewTheta <= 360) {
			newBottomY = -newBottomY;
		}
		
		for (String key : this.points.keySet()) {
			double[] point = this.points.get(key);
			if (key.startsWith("Top")) {
				point[1] += newTopY;
				point[2] += newTopZ;
			}
			else if (key.startsWith("Bot")) {
				point[1] += newBottomY;
				point[2] += newBottomZ;
			}
			this.points.put(key, point);
		}
		
	}
	
	public Tessellator tesselate(Tessellator tess) {
		LogBlock log = new LogBlock(Capo.log, "\n");
		
		double distanceHorizontal = this.points.get("Distance")[0];
		// Capo.log.info(distanceHorizontal + " : " + (distanceHorizontal / 16));
		double maxV = (distanceHorizontal * 4);
		
		double[] point = null;
		
		/* +Y
			srcTopLeft	;	bottom left
			srcTopRight	;	bottom right
			tarTopLeft	;	top left
			tarTopRight	;	top right
		 */
		point = this.points.get("TopLeftSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopRightSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopRightTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopLeftTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		log.addWithLine("");
		
		/* -Y
			srcBottomLeft	;	bottom right
			srcBottomRight	;	bottom left
			tarBottomLeft	;	top right
			tarBottomRight	;	top left
		 */
		point = this.points.get("BotRightSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotLeftSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotLeftTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotRightTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		log.addWithLine("");
		
		/* +X
			srcTopLeft		;	bottom right
			srcBottomLeft	;	bottom left
			tarTopLeft		;	top right
			tarBottomLeft	;	top left
		 */
		point = this.points.get("BotLeftSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopLeftSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopLeftTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotLeftTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		log.addWithLine("");
		
		/* -X
			srcTopRight		;	bottom left
			srcBottomRight	;	bottom right
			tarTopRight		;	top left
			tarBottomRight	;	top right
		 */
		point = this.points.get("TopRightSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotRightSrc");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, 0);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("BotRightTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 1, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		point = this.points.get("TopRightTarget");
		tess.addVertexWithUV(point[0], point[1], point[2], 0, maxV);
		log.addWithLine(point[0] + "\t:\t" + point[1] + "\t:\t" + point[2]);
		log.addWithLine("");
		
		// log.log();
		/*
		tess.addVertexWithUV(0.0F, 0.0F, 0.0F, 0, 0);
		tess.addVertexWithUV(0.0F, 1.0F, 0.0F, 0, 1);
		tess.addVertexWithUV(1.0F, 1.0F, 0.0F, 1, 1);
		tess.addVertexWithUV(1.0F, 0.0F, 0.0F, 1, 0);
		 */
		return tess;
	}
	
}
