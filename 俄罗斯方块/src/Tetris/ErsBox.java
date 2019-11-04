package Tetris;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * �����࣬����ɿ�Ļ���Ԫ�أ����Լ�����ɫ����ʾ������
 * �������Ƿ������ɫ
 */
public class ErsBox implements Cloneable {
	/**
     * ���ӿ�20
     */
    public final static int BOX_WIDTH = 20;
    /**
     * ���ӳ�20
     */
    public final static int BOX_HEIGHT = 20;
	static BufferedImage cellImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
	static BufferedImage bgImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
	
    private boolean isFront;

    /**
     * ������Ĺ��캯����
     *
     * @param isFront �ǲ�����ǰ��ɫ��Ϊ�˷�����ɫ trueǰ��ɫ��false �ñ���ɫ
     */
    public ErsBox(boolean isFront) {
        this.isFront = isFront;
    }
    
    /**
	* @Title: getImage 
	* @Description: TODO
	* @param @param isFront ��ʾ�Ƿ�ǰ�淽�飬�����Ǳ�������
	* @param @return  
	* @return BufferedImage   
	* @throws
	 */
	public static void drawImage() {
		//�������ͼ����ΪͼƬ
		Graphics g1=cellImage.getGraphics();
		g1.setColor(Color.BLACK);
		g1.fillRect(0, 0, 20, 20);
		g1.setColor(Color.WHITE);
		g1.fillRect(2, 2, 16, 16);
		g1.setColor(Color.BLACK);
		g1.fillRect(4, 4, 12, 12);
		
		Graphics g2=bgImage.getGraphics();
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, 20, 20);
		g2.setColor(Color.WHITE);
		g2.fillRect(2, 2, 16, 16);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(4, 4, 12, 12);
	}
	

    /**
     * �˷����ǲ�����ǰ��ɫ����
     *
     * @return boolean ��true��ǰ��ɫ���֣�false �ñ���ɫ����
     */
    public boolean isFront() {
        return isFront;
    }

    /**
     * ���÷������ɫ��
     *
     * @param isFront boolean ��true��ǰ��ɫ���֣�false �ñ���ɫ����
     */
    public void setFront(boolean isFront) {
        this.isFront = isFront;
    }


    /**
     * ����Object��Object clone(),ʵ�ֿ�¡
     *
     * @return Object����¡�Ľ��
             *   ����ֱ�Ӹ���Ҫ���ٺܶ�
     */
    @Override
    public Object clone() {
        Object cloned = null;
        try {
            cloned = super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cloned;
    }
}
