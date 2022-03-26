package com.hawkonline.retorsnaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Foods {
	
	private int imgID;//ʳ��ͼƬD ��foodsX 
	private int drawX;// ͼ����ʾ���ĵ�Xλ��
	private int drawY;// ͼ����ʾ���ĵ�Yλ��
	
	private int generateEnergy;//��������

	private  int offsetX;// ͼ�����ʾ��Xλ��ƫ��ֵ-�Զ�����
	private  int offsetY;// ͼ�����ʾ��Xλ��ƫ��ֵ-�Զ�����
	
	private BufferedImage imgFoods;//
	
	public Foods(int imgID, int drawX, int drawY, int generateEnergy) {
		super();

		this.imgID = imgID;
		this.drawX = drawX;
		this.drawY = drawY;
		this.generateEnergy = generateEnergy;
		
		//����ʳ��ͼƬ
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
	
	//��ʳ��
	public void drawFoods(Graphics cg,int x,int y ){
		cg.drawImage(imgFoods, drawX-offsetX+x, drawY-offsetY+y, null);
		cg.setColor(Color.WHITE);
		//������-------------------
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
