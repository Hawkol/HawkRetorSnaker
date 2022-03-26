package com.hawkonline.retorsnaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import org.omg.CORBA.INTERNAL;

public class Snake {
	private Random rand = new Random();
	private int to=0;//自动移动模式标记蛇移动的方向，1左，2右
	
	// 蛇图片相关显示属性
	private int step;// 步长
	private int offsetX;// 图块块显示的X位置偏移值-自动计算
	private int offsetY;// 图块块显示的X位置偏移值-自动计算
	private BufferedImage imgSnakeHead;// 蛇头图片
	private BufferedImage imgSnakeBody1;// 蛇身体奇数图片
	private BufferedImage imgSnakeBody2;// 蛇身体偶数图片
	private BufferedImage imgSnakeTait1;// 蛇尾巴图片
	private BufferedImage imgSnakeTait2;// 蛇尾巴图片
	private BufferedImage imgRotate;// 旋转后的图片

	// 蛇的属值
	private int energy;// 吃豆得到的能量
	private int snakeLength;// 蛇的长度--默认3节，根椐能量获得的多少自动生长

	// 蛇头
	private int snakeHeadX;// 蛇头X坐标
	private int snakeHeadY;// 蛇头Y坐标
	private int snakeHeadDirection;// 蛇头移动的方向
	private int snakeDisplayStyle;// 蛇显示风格
	private boolean snakeIsDeath;//蛇死亡标记   True-生存， false=死亡

	// 蛇身体
	private int[] bodyX;// 蛇身体X坐标
	private int[] bodyY;// 蛇身体Y坐标
	private int[] bodyDirection;// 蛇身体方向

	// 构造函数
	public Snake(int snakeHeadX, int snakeHeadY, int snakeHeadDirection, int snakeDisplayStyle) {
		super();
		this.snakeHeadX = snakeHeadX;
		this.snakeHeadY = snakeHeadY;
		this.snakeHeadDirection = snakeHeadDirection;
		this.snakeDisplayStyle = snakeDisplayStyle;
		this.snakeIsDeath = true;//蛇死亡标记   True-生存， false=死亡

		snakeLength = 3;

		// 加载图片
		try {
			imgSnakeHead = ImageIO.read(new FileInputStream("images\\snake\\s" + snakeDisplayStyle + "\\head.png"));
			imgSnakeBody1 = ImageIO.read(new FileInputStream("images\\snake\\s" + snakeDisplayStyle + "\\body1.png"));
			imgSnakeBody2 = ImageIO.read(new FileInputStream("images\\snake\\s" + snakeDisplayStyle + "\\body2.png"));
			imgSnakeTait1 = ImageIO.read(new FileInputStream("images\\snake\\s" + snakeDisplayStyle + "\\tait1.png"));
			imgSnakeTait2 = ImageIO.read(new FileInputStream("images\\snake\\s" + snakeDisplayStyle + "\\tait2.png"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.offsetX = imgSnakeHead.getWidth() / 2;
		this.offsetY = imgSnakeHead.getHeight() / 2;
		this.step = 8;

		// 蛇身体-构造时为3节
		bodyX = new int[snakeLength];
		bodyY = new int[snakeLength];
		bodyDirection = new int[snakeLength];

		for (int i = 0; i < snakeLength; i++) {
			bodyX[i] = snakeHeadX - (i + 1) * step;
			bodyY[i] = snakeHeadY;
			bodyDirection[i] = 0;
		}

	}

	// 画蛇
	public void snakeShow(Graphics cg,int x,int y) {

		// 画蛇身体
		for (int i = snakeLength - 1; i >= 0; i--) {
			if (i == snakeLength - 1) {// 画蛇尾
				if ((i % 2 == 0)) {
					imgRotate = Tools.rotateImage(imgSnakeTait1, bodyDirection[i]);
				} else {
					imgRotate = Tools.rotateImage(imgSnakeTait2, bodyDirection[i]);
				}

			} else {
				// 画蛇身体
				if ((i % 2) == 0) {
					imgRotate = Tools.rotateImage(imgSnakeBody1, bodyDirection[i]);
				} else {
					imgRotate = Tools.rotateImage(imgSnakeBody2, bodyDirection[i]);
				}

			}
			cg.drawImage(imgRotate, bodyX[i] - offsetX+x, bodyY[i] - offsetY+y, null);
			// 参照线-------------------
			cg.setColor(Color.WHITE);
			cg.fillRect(bodyX[i] - 1+x, bodyY[i] - 1+y, 2, 2);
		}

		// 画蛇头
		imgRotate = Tools.rotateImage(imgSnakeHead, snakeHeadDirection);
		cg.drawImage(imgRotate, snakeHeadX - offsetX+x, snakeHeadY - offsetY+y, null);

		// 参照线-------------------
		cg.setColor(Color.WHITE);
		cg.fillRect(snakeHeadX - 1+x, snakeHeadY - 1+y, 2, 2);
		
		
		
		cg.setColor(Color.BLUE);
		int sX = (int) (snakeHeadX + 150 * Math.cos(snakeHeadDirection * 3.14 / 180));
		int sY = (int) (snakeHeadY + 150 * Math.sin(snakeHeadDirection * 3.14 / 180));
		cg.fillRect(sX - 1+x, sY - 1+y, 2, 2);
		
		sX = (int) (snakeHeadX + 100 * Math.cos((snakeHeadDirection+45) * 3.14 / 180));
		sY = (int) (snakeHeadY + 100 * Math.sin((snakeHeadDirection+45) * 3.14 / 180));
		cg.fillRect(sX - 1+x, sY - 1+y, 2, 2);
		sX = (int) (snakeHeadX + 100 * Math.cos((snakeHeadDirection-45) * 3.14 / 180));
		sY = (int) (snakeHeadY + 100 * Math.sin((snakeHeadDirection-45) * 3.14 / 180));
		cg.fillRect(sX - 1+x, sY - 1+y, 2, 2);
		// 参照线-------------------
		cg.setColor(Color.WHITE);
		cg.fillRect(snakeHeadX - 1+x, snakeHeadY - 1+y, 2, 2);
		
		

	}
	public void snakeAutoMove (){

		// 蛇智能移动方案3
		//蛇下一步的位置
		int sX = (int) (snakeHeadX + 150 * Math.cos(snakeHeadDirection * 3.14 / 180));
		int sY = (int) (snakeHeadY + 150 * Math.sin(snakeHeadDirection * 3.14 / 180));
		
		//蛇死亡了
		if(sX<=20||sX>=(Tools.MAP_X-20)||sY<=20||sY>=(Tools.MAP_Y-20)){
			boolean isL=false,isR=false;
			//看下左边45度会不会死
			sX = (int) (snakeHeadX + 100 * Math.cos((snakeHeadDirection+45) * 3.14 / 180));
			sY = (int) (snakeHeadY + 100 * Math.sin((snakeHeadDirection+45) * 3.14 / 180));
			if(sX<=20||sX>=(Tools.MAP_X-20)||sY<=20||sY>=(Tools.MAP_Y-20)){
				isL=true;
			}
			//看下左边45度会不会死
			sX = (int) (snakeHeadX + 100 * Math.cos((snakeHeadDirection-45) * 3.14 / 180));
			sY = (int) (snakeHeadY + 100 * Math.sin((snakeHeadDirection-45) * 3.14 / 180));
			if(sX<=20||sX>=(Tools.MAP_X-15)||sY<=20||sY>=(Tools.MAP_Y-15)){
				isR=true;
			}
			
			//左右死
			if((isL)&&(isL)){
				if(rand.nextInt(1)==1){
					snakeHeadDirection+=(rand.nextInt(5)+20);//增大旋转角度
					to=1;
				}else{
					snakeHeadDirection-=(rand.nextInt(5)+20);//增大旋转角度
					to=2;
				}
			}else if(isL){
				snakeHeadDirection-=(rand.nextInt(5)+5);//增大旋转角度
				to=2;
			}else{
				snakeHeadDirection+=(rand.nextInt(5)+5);//增大旋转角度
				to=1;
			}
		}else{
			if(rand.nextInt(rand.nextInt(20)+10)==1){
				to=rand.nextInt(3);
			}
			if (to==1) snakeHeadDirection+=(rand.nextInt(5)+2);
			if (to==2) snakeHeadDirection-=(rand.nextInt(5)+2);
		}
		snakeMove();


		
	}
	// 蛇移动-按照设置的方向前行
	public void snakeMove() {

		if (snakeIsDeath) {
			// 蛇的后一节的值继承前一节的值
			for (int i = snakeLength - 1; i > 0; i--) {
				bodyX[i] = bodyX[i - 1];
				bodyY[i] = bodyY[i - 1];
				bodyDirection[i] = bodyDirection[i - 1];
			}
			// 第一节的值继承蛇头的值
			bodyX[0] = snakeHeadX;
			bodyY[0] = snakeHeadY;
			bodyDirection[0] = snakeHeadDirection;

			snakeHeadX = (int) (snakeHeadX + step * Math.cos(snakeHeadDirection * 3.14 / 180));
			snakeHeadY = (int) (snakeHeadY + step * Math.sin(snakeHeadDirection * 3.14 / 180));
		}

	}

	// 蛇生长--数组自动扩容1节
	public void snakGrow() {

		snakeLength = snakeLength + 1;

		if (snakeLength > bodyX.length) {
			bodyX = Arrays.copyOf(bodyX, snakeLength);
			bodyY = Arrays.copyOf(bodyY, snakeLength);
			bodyDirection = Arrays.copyOf(bodyDirection, snakeLength);
		}

	}

	// 蛇死亡
	public void snakeDeath() {
		this.snakeIsDeath = false;//蛇死亡标记   True-生存， false=死亡
		// 随机数--产生蛇块断裂
		Random rand = new Random();
		for (int i = 0; i < snakeLength; i++) {
			bodyX[i] = bodyX[i] + rand.nextInt(step * 2) - rand.nextInt(step * 2);
			bodyY[i] = bodyY[i] + rand.nextInt(step * 2) - rand.nextInt(step * 2);
			bodyDirection[i] = bodyDirection[i] + rand.nextInt(360);
		}
		//snakeHeadX = snakeHeadX + rand.nextInt(step) - rand.nextInt(step);
		//snakeHeadY = snakeHeadY + rand.nextInt(step) - rand.nextInt(step);
		snakeHeadX = (int) (snakeHeadX + step * Math.cos(snakeHeadDirection * 3.14 / 180));
		snakeHeadY = (int) (snakeHeadY + step * Math.sin(snakeHeadDirection * 3.14 / 180));
	}


	// get---- set-----
	public int getSnakeHeadDirection() {
		return snakeHeadDirection;
	}

	public void setSnakeHeadDirection(int snakeHeadDirection) {
		this.snakeHeadDirection = snakeHeadDirection;
	}

	public int getSnakeHeadX() {
		return snakeHeadX;
	}

	public void setSnakeHeadX(int snakeHeadX) {
		this.snakeHeadX = snakeHeadX;
	}

	public int getSnakeHeadY() {
		return snakeHeadY;
	}

	public void setSnakeHeadY(int snakeHeadY) {
		this.snakeHeadY = snakeHeadY;
	}

	public boolean getSnakIsDeath() {
		return snakeIsDeath;
	}

	public boolean isSnakeIsDeath() {
		return snakeIsDeath;
	}

	public void setSnakeIsDeath(boolean snakeIsDeath) {
		this.snakeIsDeath = snakeIsDeath;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getSnakeLength() {
		return snakeLength;
	}

	public void setSnakeLength(int snakeLength) {
		this.snakeLength = snakeLength;
	}

	public int getSnakeDisplayStyle() {
		return snakeDisplayStyle;
	}

	public void setSnakeDisplayStyle(int snakeDisplayStyle) {
		this.snakeDisplayStyle = snakeDisplayStyle;
	}
	public int getBodyX(int index) {
		return bodyX[index];
	}

	public int getBodyY(int index) {
		return bodyY[index];
	}

	
	
}
