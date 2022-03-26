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
	
	private static BufferedImage bfMap= new BufferedImage(Tools.MAP_X, Tools.MAP_Y, BufferedImage.TYPE_3BYTE_BGR); // ����һ��1920��1080�ĵ�ͼԴͼ
	
    private static BufferedImage bfGameMap = new BufferedImage(Tools.SCREEN_X, Tools.SCREEN_Y, BufferedImage.TYPE_3BYTE_BGR); // ����һ��1920��1080�Ļ���ͼƬ
    private static Graphics bfGameMapGraphics =bfGameMap.getGraphics();// ��ȡ����ͼƬ�Ļ���
    private BufferedImage imgDisplayInformation=null;
    private BufferedImage imeGameOver=null;
    
    private Foods []foods=null;//СԲ��ʳ��
    
    private Snake []snake=null;//��
    //private Snake enemy=null;

	public SnakePlayer() {
		
		this.setTitle(Tools.GameName);
		// ���ÿ�ܵĴ�С
		this.setSize(Tools.SCREEN_X, Tools.SCREEN_Y);
		 // ���ÿ�ܴ�С���Ըı�
		this.setResizable(false);
		//���ô��������ʾ
		this.setLocationRelativeTo(null);
      // ���õ���رհ�ť �رս���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//����GUI�Ĵ���ͼ��
		ImageIcon snakeIcon = new ImageIcon("images\\snakeIcon.png");
		this.setIconImage(snakeIcon.getImage());
		// ������ʾ��Ϸ����
		this.setVisible(true); 
		// ��Ӽ��̼�����  
	    this.addKeyListener(this);
	    // ����������� 

        //�����ʽ
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    //����UI����
	    uiGameStart=new UIGameStart();
	    
	    
	    loadGameResources();//������Ϸ��Դ
	    bfMapDraw();//������ͼ���Ժ��ö�λ���
	    setFoods();
	    setSnake();
	    
	     Thread thread = new Thread(this); // �����߳�  
	     thread.start(); // �����߳�  
	}
	
	//������Ϸ��Դ
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

	//������
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

	// ����ʳ��
	private void setFoods() {

		foods=new Foods[Tools.FOODS_SUM];
		for(int i=0;i<Tools.FOODS_SUM;i++){

				int imgID=rand.nextInt(Tools.FOODS_TYPE_SUM);
				int drawX=rand.nextInt(Tools.MAP_X-40)+20;
				int drawY=rand.nextInt(Tools.MAP_Y-40)+20;
				int generateEnergy=100;//-------------����ƴ�XML�ļ�����
				foods[i]=new Foods(imgID, drawX, drawY, generateEnergy);
		}

	}
	// ������Ϸ��ͼ
	private void bfMapDraw() {
		Graphics bfMapGraphics =bfMap.getGraphics();// ��ȡ����ͼƬ�Ļ���
		
		bfMapGraphics.setColor(Color.BLACK); // ����map���Ϊ��ɫ
		bfMapGraphics.fillRect(0, 0, Tools.MAP_X, Tools.MAP_Y);

		bfMapGraphics.setColor(new Color(90, 90, 90)); // ����mapǽΪ��ɫ
		bfMapGraphics.fillRect(10, 10, Tools.MAP_X - 20, Tools.MAP_Y - 20);

		bfMapGraphics.setColor(new Color(208, 208, 224)); // ����map����ɫ
		bfMapGraphics.fillRect(20, 20, Tools.MAP_X - 40, Tools.MAP_Y - 40);
		// ��map��
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
		//�õ�����ͼ��ƫ��ֵ
		drawMapX=-snake[0].getSnakeHeadX()+Tools.SCREEN_X/2;
		drawMapY=-snake[0].getSnakeHeadY()+Tools.SCREEN_Y/2;
		
		//����ͼ����
		bfGameMapGraphics.setColor(Color.red);
		bfGameMapGraphics.fillRect(0, 0, Tools.SCREEN_X, Tools.SCREEN_Y);
		
		bfGameMapGraphics.drawImage(bfMap, drawMapX, drawMapY, null);//��ʾ��һ�㱳����ͼ
		
		for(int e=0;e<Tools.SNAKE__SUM;e++){
			
			//�ж����Ƿ���Գ�ʳ��
			isEatFoods(snake[e]);

			
			// ====================================
			if (snake[e].getSnakIsDeath()) {// ������ڻ���

				if (e==0) {
						snake[0].snakeMove();// ���ƶ�
				}else {
					snake[e].snakeAutoMove();// ���ƶ�
				}
				
				// �ж����Ƿ�ײǽ
				if (isLive(snake[e], 20)) {
					snake[e].snakeDeath();// ��������
				}
				
				//�ж����Ƿ���ײ==������ͷײ��ͷ����ͷײ������
				
				//88888888888888888888888
				//1.�ж���ͷײ��ͷ==============
				for(int j=e+1;j<Tools.SNAKE__SUM;j++){
					if(snake[e].getSnakIsDeath()&&snake[j].getSnakIsDeath()){//��������߶��ڻ��ţ����бȽϺ�����
						//�õ�������ͷ�ľ���=
						int ab=isAtoB(snake[e].getSnakeHeadX(), snake[e].getSnakeHeadY(),snake[j].getSnakeHeadX(), snake[j].getSnakeHeadY());
						if(ab<25){//�������ײֵ��
							if(snake[e].getSnakeLength()>snake[j].getSnakeLength()){//�����һ���ߴ� ���Ƚϵ�����
								snake[j].snakeDeath();
							}else if(snake[e].getSnakeLength()<snake[j].getSnakeLength()){//������Ƚϵ��ߴ� ��һ������
								snake[e].snakeDeath();
							}else{//�������ͬ�� ����
								snake[e].snakeDeath();
								snake[j].snakeDeath();
							}
						}
					}
				}
				
				
				//2.�ж���ͷײ������==============
				for (int x = e + 1; x < Tools.SNAKE__SUM; x++) {

					if (snake[e].getSnakIsDeath() && snake[x].getSnakIsDeath()) {// �����߶��ڻ���
																					// ���ж��Ƿ�ײ������

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
	
	
	//�õ�A�㵽B������֮��ľ���
	private int isAtoB(int x1, int y1, int x2, int y2) {
		return   (int) Math.sqrt(Math.abs((x1 - x2)*(x1 - x2))+Math.abs((y1 - y2)*(y1 - y2)));
	}
	
	//�ж����Ƿ�ײǽ 
	private boolean isLive(Snake snake,int distance) {
		if(snake.getSnakeHeadX()<=distance||snake.getSnakeHeadX()>=(Tools.MAP_X-distance)||snake.getSnakeHeadY()<=distance||snake.getSnakeHeadY()>=(Tools.MAP_Y-distance)){
			return true;
		}else {
			return false;
		}
	}

	//�ж����Ƿ���Գ�ʳ��
	private void isEatFoods(Snake snake) {
		
		for(int i=0;i<Tools.FOODS_SUM;i++){

			int distance = (int) Math.sqrt(Math.abs((foods[i].getDrawX() -  snake.getSnakeHeadX())*(foods[i].getDrawX() -  snake.getSnakeHeadX()))+Math.abs((foods[i].getDrawY() -  snake.getSnakeHeadY())*(foods[i].getDrawY() -  snake.getSnakeHeadY())));
			
			if (distance<25){//���̰������ʳ����20����Ե�ʳ��
				snake.snakGrow();//������
				snake.setEnergy(snake.getEnergy()+1);//������
				
				//�����µ�ʳ��

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
			gr.drawImage(bfGameMap,0,0, null); // ������map
			break;
		case 1:
			reDrawGame();
			//��ʾ�÷�
			showDisplayInformation();
			gr.drawImage(bfGameMap,0,0, null); // ������map
			
            break;
		case 2:
			reDrawGame();

			showDisplayInformation();
			bfGameMapGraphics.drawImage(imeGameOver,250,100, null);
			
			gr.drawImage(bfGameMap,0,0, null); // ������map
			break;
			
		default:
			break;
			
		}

 }  


		//��ʾ�÷�
		private void showDisplayInformation() {
			bfGameMapGraphics.drawImage(imgDisplayInformation,300,30, null); 
			bfGameMapGraphics.setColor(Color.RED);
			bfGameMapGraphics.setFont(new Font("arial",Font.BOLD, 30));
			bfGameMapGraphics.drawString(snake[0].getSnakeLength()+"", 460, 66);
			bfGameMapGraphics.drawString(snake[0].getEnergy()+"", 680, 66);
	}

		/** 
	     * �߳�ִ�з��� 
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
	     * ����ʱ���� 
	     */  
	    public void keyPressed(KeyEvent e) {  
	    	
	        int keyCode = e.getKeyCode();
	        
	        //	��Ϸ������ʱ�����������ʼ��Ϸ        
	        if (runGame==0){
	    		runGame=1;
	    	}
	        
	        if (keyCode ==KeyEvent.VK_LEFT&&runGame==1) { // �󰴼�  
	        	snake[0].setSnakeHeadDirection(snake[0].getSnakeHeadDirection()-5);//����ת
	        }  
	        if (keyCode == KeyEvent.VK_RIGHT&&runGame==1) { // �Ұ���  
	        	snake[0].setSnakeHeadDirection(snake[0].getSnakeHeadDirection()+5);//����ת
	        }  
	       if(keyCode==KeyEvent.VK_UP&&runGame==1){//����
	    	   seeping=20;
	       }
	       if(keyCode==KeyEvent.VK_L&&runGame==2){//����

	    	   snake[0].setSnakeHeadX(Tools.MAP_X/2);
	    	   snake[0].setSnakeHeadY(Tools.MAP_Y/2);
	    	   runGame=1;
	    	   snake[0].setSnakeIsDeath(true);
	       }
	       if(keyCode==KeyEvent.VK_Y&&runGame==2){//���¿�ʼ
	    	   snake[0].setSnakeHeadX(Tools.MAP_X/2);
	    	   snake[0].setSnakeHeadY(Tools.MAP_Y/2);
	    	   snake[0].setSnakeLength(3);
	    	   snake[0].setEnergy(0);
	    	   runGame=1;
	    	   snake[0].setSnakeIsDeath(true);
	       }
	    }  
	  
	    /** 
	     * �ͷŰ���ʱ���� 
	     */  
	    public void keyReleased(KeyEvent e) {  
	    	 int keyCode = e.getKeyCode(); 
	    	 if(keyCode==KeyEvent.VK_UP){
	    		 seeping=80;
		       }
	    }  
	  
	    /** 
	     * ������ 
	     */  
	    public void keyTyped(KeyEvent e) {  
	  
	    }  
	  
	    public static void main(String[] args) {  
	        new SnakePlayer();  
	    }

	}


