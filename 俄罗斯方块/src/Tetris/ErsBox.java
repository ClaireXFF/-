package Tetris;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * 方格类，是组成块的基本元素，用自己的颜色来表示块的外观
 * 变量：是方格的颜色
 */
public class ErsBox implements Cloneable {
	/**
     * 盒子宽20
     */
    public final static int BOX_WIDTH = 20;
    /**
     * 盒子长20
     */
    public final static int BOX_HEIGHT = 20;
	static BufferedImage cellImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
	static BufferedImage bgImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
	
    private boolean isFront;

    /**
     * 方格类的构造函数，
     *
     * @param isFront 是不是用前景色来为此方格着色 true前景色，false 用背景色
     */
    public ErsBox(boolean isFront) {
        this.isFront = isFront;
    }
    
    /**
	* @Title: getImage 
	* @Description: TODO
	* @param @param isFront 表示是否前面方块，否则是背景方块
	* @param @return  
	* @return BufferedImage   
	* @throws
	 */
	public static void drawImage() {
		//将画完的图保存为图片
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
     * 此方格是不是用前景色表现
     *
     * @return boolean ，true用前景色表现，false 用背景色表现
     */
    public boolean isFront() {
        return isFront;
    }

    /**
     * 设置方格的颜色，
     *
     * @param isFront boolean ，true用前景色表现，false 用背景色表现
     */
    public void setFront(boolean isFront) {
        this.isFront = isFront;
    }


    /**
     * 覆盖Object的Object clone(),实现克隆
     *
     * @return Object，克隆的结果
             *   比起直接复制要快速很多
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
