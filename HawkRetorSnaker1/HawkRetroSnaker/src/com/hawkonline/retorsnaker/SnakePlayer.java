package com.hawkonline.retorsnaker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SnakePlayer  extends JFrame implements Runnable, KeyListener{
	Random rand = new Random();
	private int runGame=0;
	private int seeping=80;
	//UI
	private UIGameStart uiGameStart=null;
	

	private long s=0;
	private int drawMapX,drawMapY;
	
	private static BufferedImage bfMap= new BufferedImage(Tools.MAP_X, Tools.MAP_Y, BufferedImage.TYPE_3BYTE_BGR); // 创建一张1920＊1080的地图源图
	
    private static BufferedImage bfGameMap = new BufferedImage(Tools.SCREEN_X, Tools.SCREEN_Y, BufferedImage.TYPE_3BYTE_BGR); // 创建一张1920＊1080的缓冲图片
    private static Graphics bfGameMapGraphics =bfGameMap.getGraphics();// 获取缓冲图片的画笔
    private BufferedImage imgDisplayInformation=null;
    private BufferedImage imeGameOver=null;
    
    private Foods []foods=null;//小圆点食物
    
    private Snake []snake=null;//蛇
    //private Snake enemy=null;

	public SnakePlayer() {
		
		this.setTitle(Tools.GameName);
		// 设置框架的大小
		this.setSize(Tools.SCREEN_X, Tools.SCREEN_Y);
		 // 设置框架大小可以改变
		this.setResizable(false);
		//设置窗体居中显示
		this.setLocationRelativeTo(null);
      // 设置点击关闭按钮 关闭界面
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置GUI的窗口图标
		ImageIcon snakeIcon = new ImageIcon("images\\snakeIcon.png");
		this.setIconImage(snakeIcon.getImage());
		// 允许显示游戏界面
		this.setVisible(true); 
		// 添加键盘监听器  
	    this.addKeyListener(this);
	    // 添加鼠标监听器 

        //鼠标样式
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    //创建UI界面
	    uiGameStart=new UIGameStart();
	    
	    
	    loadGameResources();//加载游戏资源
	    bfMapDraw();//画背地图，以后不用多次画了
	    setFoods();
	    setSnake();
	    
	     Thread thread = new Thread(this); // 创建线程  
	     thread.start(); // 启动线程  
	}
	
	//加载游戏资源
	private void loadGameResources() {
	    try {
			imgDisplayInformation = ImageIO.read(new FileInputStream("images\\DisplayInformation.png"));
			imeGameOver = ImageIO.read(new FileInputStream("images\\GameOver.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//设置蛇
	private void setSnake() {
		
		snake=new Snake[Tools.SNAKE__SUM];
		snake[0]=new Snake(Tools.MAP_X/2, Tools.MAP_Y/2, 0,rand.nextInt(Tools.SNAKE_STYLE_SUM));
		
		for(int i=1;i<Tools.SNAKE__SUM;i++){
			int sx=rand.nextInt(Tools.MAP_X-40)+20;
			int sy=rand.nextInt(Tools.MAP_Y-40)+20;
			int sd=rand.nextInt(360);
			int ss=rand.nextInt(Tools.SNAKE_STYLE_SUM);

			snake[i]=new Snake(sx, sy, sd, ss);
		}
	
	}

	// 设置食物
	private void setFoods() {

		foods=new Foods[Tools.FOODS_SUM];
		for(int i=0;i<Tools.FOODS_SUM;i++){

				int imgID=rand.nextInt(Tools.FOODS_TYPE_SUM);
				int drawX=rand.nextInt(Tools.MAP_X-40)+20;
				int drawY=rand.nextInt(Tools.MAP_Y-40)+20;
				int generateEnergy=100;//-------------可设计从XML文件读入
				foods[i]=new Foods(imgID, drawX, drawY, generateEnergy);
		}

	}
	// 设置游戏地图
	private void bfMapDraw() {
		Graphics bfMapGraphics =bfMap.getGraphics();// 获取缓冲图片的画笔
		
		bfMapGraphics.setColor(Color.BLACK); // 设置map外框为黑色
		bfMapGraphics.fillRect(0, 0, Tools.MAP_X, Tools.MAP_Y);

		bfMapGraphics.setColor(new Color(90, 90, 90)); // 设置map墙为黑色
		bfMapGraphics.fillRect(10, 10, Tools.MAP_X - 20, Tools.MAP_Y - 20);

		bfMapGraphics.setColor(new Color(208, 208, 224)); // 设置map背景色
		bfMapGraphics.fillRect(20, 20, Tools.MAP_X - 40, Tools.MAP_Y - 40);
		// 画map格
		bfMapGraphics.setColor(new Color(241, 241, 240));
		for (int i = 20; i < Tools.MAP_Y; i += 20) {
			bfMapGraphics.drawLine(0 + 12, i, Tools.MAP_X -12, i);
		}

		for (int j = 20; j < Tools.MAP_X; j += 20) {
			bfMapGraphics.drawLine(j, 0 + 12, j, Tools.MAP_Y - 12);
		}
	}
	
	public void reDrawGame(){
		s++;
		//得到画地图的偏移值
		drawMapX=-snake[0].getSnakeHeadX()+Tools.SCREEN_X/2;
		drawMapY=-snake[0].getSnakeHeadY()+Tools.SCREEN_Y/2;
		
		//画地图背景
		bfGameMapGraphics.setColor(Color.red);
		bfGameMapGraphics.fillRect(0, 0, Tools.SCREEN_X, Tools.SCREEN_Y);
		
		bfGameMapGraphics.drawImage(bfMap, drawMapX, drawMapY, null);//显示第一层背景地图
		
		for(int e=0;e<Tools.SNAKE__SUM;e++){
			
			//判断蛇是否可以吃食物
			isEatFoods(snake[e]);

			
			// ====================================
			if (snake[e].getSnakIsDeath()) {// 如果蛇在活着

				if (e==0) {
						snake[0].snakeMove();// 蛇移动
				}else {
					snake[e].snakeAutoMove();// 蛇移动
				}
				
				// 判断蛇是否撞墙
				if (isLive(snake[e], 20)) {
					snake[e].snakeDeath();// 蛇死亡了
				}
				
				//判断蛇是否相撞==包含蛇头撞蛇头，蛇头撞蛇身体
				
				//88888888888888888888888
				//1.判断蛇头撞蛇头==============
				for(int j=e+1;j<Tools.SNAKE__SUM;j++){
					if(snake[e].getSnakIsDeath()&&snake[j].getSnakIsDeath()){//如果两条蛇都在活着，才有比较胡意义
						//得到两个蛇头的距离=
						int ab=isAtoB(snake[e].getSnakeHeadX(), snake[e].getSnakeHeadY(),snake[j].getSnakeHeadX(), snake[j].getSnakeHeadY());
						if(ab<25){//如果在碰撞值内
							if(snake[e].getSnakeLength()>snake[j].getSnakeLength()){//如果第一个蛇大 被比较的蛇死
								snake[j].snakeDeath();
							}else if(snake[e].getSnakeLength()<snake[j].getSnakeLength()){//如果被比较的蛇大 第一个蛇死
								snake[e].snakeDeath();
							}else{//如果蛇相同大 都死
								snake[e].snakeDeath();
								snake[j].snakeDeath();
							}
						}
					}
				}
				
				
				//2.判断蛇头撞蛇身体==============
				for (int x = e + 1; x < Tools.SNAKE__SUM; x++) {

					if (snake[e].getSnakIsDeath() && snake[x].getSnakIsDeath()) {// 两条蛇都在活着
																					// 才判断是否撞到身体

						for (int b = 0; b < snake[x].getSnakeLength(); b++) {
							int xb = isAtoB(snake[e].getSnakeHeadX(), snake[e].getSnakeHeadY(), snake[x].getBodyX(b),
									snake[x].getBodyY(b));
							if (xb < 25) {
								snake[e].snakeDeath();
							}

						}
						
						
						for (int b = 0; b < snake[e].getSnakeLength(); b++) {
							int xb = isAtoB(snake[x].getSnakeHeadX(), snake[x].getSnakeHeadY(), snake[e].getBodyX(b),
									snake[e].getBodyY(b));
							if (xb < 25) {
								snake[x].snakeDeath();
							}

						}
						
						

					}

				}
			
			
				
				
				
				
			
			}
			
			
			snake[e].snakeShow(bfGameMapGraphics,drawMapX,drawMapY);
	
			}
		
		if(!snake[0].getSnakIsDeath()){
			runGame=2;
		}
	}
	
	
	//得到A点到B点两点之间的距离
	private int isAtoB(int x1, int y1, int x2, int y2) {
		return   (int) Math.sqrt(Math.abs((x1 - x2)*(x1 - x2))+Math.abs((y1 - y2)*(y1 - y2)));
	}
	
	//判断蛇是否撞墙 
	private boolean isLive(Snake snake,int distance) {
		if(snake.getSnakeHeadX()<=distance||snake.getSnakeHeadX()>=(Tools.MAP_X-distance)||snake.getSnakeHeadY()<=distance||snake.getSnakeHeadY()>=(Tools.MAP_Y-distance)){
			return true;
		}else {
			return false;
		}
	}

	//判断蛇是否可以吃食物
	private void isEatFoods(Snake snake) {
		
		for(int i=0;i<Tools.FOODS_SUM;i++){

			int distance = (int) Math.sqrt(Math.abs((foods[i].getDrawX() -  snake.getSnakeHeadX())*(foods[i].getDrawX() -  snake.getSnakeHeadX()))+Math.abs((foods[i].getDrawY() -  snake.getSnakeHeadY())*(foods[i].getDrawY() -  snake.getSnakeHeadY())));
			
			if (distance<25){//如果贪吃蛇离食物有20，测吃掉食物
				snake.snakGrow();//蛇生长
				snake.setEnergy(snake.getEnergy()+1);//能量加
				
				//产生新的食物

				foods[i].setDrawX(rand.nextInt(Tools.MAP_X-40)+20);
				foods[i].setDrawY(rand.nextInt(Tools.MAP_Y-40)+20);
			}
			foods[i].drawFoods(bfGameMapGraphics, drawMapX,drawMapY);

	}
	}

	public void paint(Graphics gr) {
		switch (runGame){
		case 0:
			uiGameStart.drawUIGameStart(bfGameMapGraphics);
			gr.drawImage(bfGameMap,0,0, null); // 将缓冲map
			break;
		case 1:
			reDrawGame();
			//显示得分
			showDisplayInformation();
			gr.drawImage(bfGameMap,0,0, null); // 将缓冲map
			
            break;
		case 2:
			reDrawGame();

			showDisplayInformation();
			bfGameMapGraphics.drawImage(imeGameOver,250,100, null);
			
			gr.drawImage(bfGameMap,0,0, null); // 将缓冲map
			break;
			
		default:
			break;
			
		}

 }  


		//显示得分
		private void showDisplayInformation() {
			bfGameMapGraphics.drawImage(imgDisplayInformation,300,30, null); 
			bfGameMapGraphics.setColor(Color.RED);
			bfGameMapGraphics.setFont(new Font("arial",Font.BOLD, 30));
			bfGameMapGraphics.drawString(snake[0].getSnakeLength()+"", 460, 66);
			bfGameMapGraphics.drawString(snake[0].getEnergy()+"", 680, 66);
	}

		/** 
	     * 线程执行方法 
	     */  
	    public void run() {  
	        try {  
	            while (true) {  
	                this.repaint();  
	                Thread.sleep(seeping);  
	            }  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    /** 
	     * 按下时调用 
	     */  
	    public void keyPressed(KeyEvent e) {  
	    	
	        int keyCode = e.getKeyCode();
	        
	        //	游戏刚启动时，按任意键开始游戏        
	        if (runGame==0){
	    		runGame=1;
	    	}
	        
	        if (keyCode ==KeyEvent.VK_LEFT&&runGame==1) { // 左按键  
	        	snake[0].setSnakeHeadDirection(snake[0].getSnakeHeadDirection()-5);//蛇左转
	        }  
	        if (keyCode == KeyEvent.VK_RIGHT&&runGame==1) { // 右按键  
	        	snake[0].setSnakeHeadDirection(snake[0].getSnakeHeadDirection()+5);//蛇右转
	        }  
	       if(keyCode==KeyEvent.VK_UP&&runGame==1){//加速
	    	   seeping=20;
	       }
	       if(keyCode==KeyEvent.VK_L&&runGame==2){//复活

	    	   snake[0].setSnakeHeadX(Tools.MAP_X/2);
	    	   snake[0].setSnakeHeadY(Tools.MAP_Y/2);
	    	   runGame=1;
	    	   snake[0].setSnakeIsDeath(true);
	       }
	       if(keyCode==KeyEvent.VK_Y&&runGame==2){//重新开始
	    	   snake[0].setSnakeHeadX(Tools.MAP_X/2);
	    	   snake[0].setSnakeHeadY(Tools.MAP_Y/2);
	    	   snake[0].setSnakeLength(3);
	    	   snake[0].setEnergy(0);
	    	   runGame=1;
	    	   snake[0].setSnakeIsDeath(true);
	       }
	    }  
	  
	    /** 
	     * 释放按键时调用 
	     */  
	    public void keyReleased(KeyEvent e) {  
	    	 int keyCode = e.getKeyCode(); 
	    	 if(keyCode==KeyEvent.VK_UP){
	    		 seeping=80;
		       }
	    }  
	  
	    /** 
	     * 不解释 
	     */  
	    public void keyTyped(KeyEvent e) {  
	  
	    }  
	  
	    public static void main(String[] args) {  
	        new SnakePlayer();  
	    }

	}


