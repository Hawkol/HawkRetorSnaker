package com.hawkonline.retorsnaker;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Tools {
	public static final int MAP_X = 1920;
	public static final int MAP_Y = 1080;
	public static final int SCREEN_X = 800;
	public static final int SCREEN_Y = 600;
	public static final String GameName = "美卡贪吃蛇1.0.0";

	public final static int FOODS_TYPE_SUM = 11;// 食物种类
	public final static int FOODS_SUM = 50;// 小圆食物总数
	public final static int SNAKE_STYLE_SUM = 5;// 蛇皮肤总数
	public final static int SNAKE__SUM=10;//蛇的数量
	
	/**
	 * 旋转图片为指定角度
	 * 
	 * @param bufferedimage
	 *            目标图像
	 * @param degree
	 *            旋转角度
	 * @return
	 */
	public final static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();// 得到图片宽度。
		int h = bufferedimage.getHeight();// 得到图片高度。
		int type = bufferedimage.getColorModel().getTransparency();// 得到图片透明度。
		BufferedImage img;// 空的图片。
		Graphics2D graphics2d;// 空的画笔。
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
				.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// 旋转，degree是整型，度数，比如垂直90度。
		graphics2d.drawImage(bufferedimage, 0, 0, null);// 从bufferedimagecopy图片至img，0,0是img的坐标。
		graphics2d.dispose();
		return img;// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
	}

}
