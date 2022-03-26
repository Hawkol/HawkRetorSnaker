package com.hawkonline.retorsnaker;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Tools {
	public static final int MAP_X = 1920;
	public static final int MAP_Y = 1080;
	public static final int SCREEN_X = 800;
	public static final int SCREEN_Y = 600;
	public static final String GameName = "����̰����1.0.0";

	public final static int FOODS_TYPE_SUM = 11;// ʳ������
	public final static int FOODS_SUM = 50;// СԲʳ������
	public final static int SNAKE_STYLE_SUM = 5;// ��Ƥ������
	public final static int SNAKE__SUM=10;//�ߵ�����
	
	/**
	 * ��תͼƬΪָ���Ƕ�
	 * 
	 * @param bufferedimage
	 *            Ŀ��ͼ��
	 * @param degree
	 *            ��ת�Ƕ�
	 * @return
	 */
	public final static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();// �õ�ͼƬ��ȡ�
		int h = bufferedimage.getHeight();// �õ�ͼƬ�߶ȡ�
		int type = bufferedimage.getColorModel().getTransparency();// �õ�ͼƬ͸���ȡ�
		BufferedImage img;// �յ�ͼƬ��
		Graphics2D graphics2d;// �յĻ��ʡ�
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
				.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);// ��ת��degree�����ͣ����������紹ֱ90�ȡ�
		graphics2d.drawImage(bufferedimage, 0, 0, null);// ��bufferedimagecopyͼƬ��img��0,0��img�����ꡣ
		graphics2d.dispose();
		return img;// ���ظ��ƺõ�ͼƬ��ԭͼƬ��Ȼû�б䣬û����ת���´λ�����ʹ�á�
	}

}
