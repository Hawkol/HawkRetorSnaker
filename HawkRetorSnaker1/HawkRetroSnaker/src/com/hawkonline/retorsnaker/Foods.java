package com.hawkonline.retorsnaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Foods {
	
	private int imgID;//食物图片D 以foodsX 
	private int drawX;// 图块显示中心的X位置
	private int drawY;// 图块显示中心的Y位置
	
	private int generateEnergy;//产生能量

	private  int offsetX;// 图块块显示的X位置偏移值-自动计算
	private  int offsetY;// 图块块显示的X位置偏移值-自动计算
	
	private BufferedImage imgFoods;//
	
	public Foods(int imgID, int drawX, int drawY, int generateEnergy) {
		super();

		this.imgID = imgID;
		this.drawX = drawX;
		this.drawY = drawY;
		this.generateEnergy = generateEnergy;
		
		//加载食物图片
		try {
			imgFoods= ImageIO.read(new FileInputStream("images\\foods\\foods"+imgID+".png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.offsetX = imgFoods.getWidth()/2;
		this.offsetY = imgFoods.getHeight()/2;
	}
	
	//画食物
	public void drawFoods(Graphics cg,int x,int y ){
		cg.drawImage(imgFoods, drawX-offsetX+x, drawY-offsetY+y, null);
		cg.setColor(Color.WHITE);
		//参照线-------------------
		cg.fillRect(drawX-1+x, drawY-1+y, 2, 2);
	}

	public Foods() {
		super();
	}

	public int getImgID() {
		return imgID;
	}

	public void setImgID(int imgID) {
		this.imgID = imgID;
	}

	public int getDrawX() {
		return drawX;
	}

	public void setDrawX(int drawX) {
		this.drawX = drawX;
	}

	public int getDrawY() {
		return drawY;
	}

	public void setDrawY(int drawY) {
		this.drawY = drawY;
	}

	public int getGenerateEnergy() {
		return generateEnergy;
	}

	public void setGenerateEnergy(int generateEnergy) {
		this.generateEnergy = generateEnergy;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
}
