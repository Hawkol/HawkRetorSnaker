package com.hawkonline.retorsnaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class UIGameStart {

	private BufferedImage gameStart=null;
    private BufferedImage snakeImage=null;
    private BufferedImage snakeTxt=null;
    private BufferedImage imgRotate;// 旋转后的图片
    private int retate=0;
    private boolean left=true;
    
    public UIGameStart() {
    	try {
    		gameStart = ImageIO.read(new FileInputStream("images\\gameStart.png"));
    		snakeImage = ImageIO.read(new FileInputStream("images\\snakeImage.png"));
    		snakeTxt = ImageIO.read(new FileInputStream("images\\snakeTxt.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
	}
    public void drawUIGameStart(Graphics cg){
    	cg.setColor(Color.BLACK);
    	cg.fillRect(0, 0, Tools.SCREEN_X, Tools.SCREEN_Y);
    	//画背景
    	cg.drawImage(gameStart,0, 0, Tools.SCREEN_X, Tools.SCREEN_Y,null);
    	
    	//蛇头动画参数
    	if (left){
    		retate+=2;
    		if (retate==20) left=false;
    		
    	}else{
    		retate-=2;
    		if (retate==-20) left=true;
    	}
    	
    	imgRotate = Tools.rotateImage(snakeImage, retate);
    	
    	//居中显示
    	cg.drawImage(imgRotate, (Tools.SCREEN_X-snakeImage.getWidth())/2, (Tools.SCREEN_Y-snakeImage.getHeight())/2, null);
    	cg.drawImage(snakeTxt, (Tools.SCREEN_X-snakeTxt.getWidth())/2, (Tools.SCREEN_Y-snakeTxt.getHeight())/2, null);
    	
    	cg.setColor(Color.RED);
    	cg.setFont(new Font("arial",Font.BOLD, 30));	
    }
}
