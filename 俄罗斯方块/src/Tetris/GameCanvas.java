package Tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 * 画布类，内有<行数>*<列数> 个方格类实例。 继承自JPanel类。 ErsBlock线程类动态改变画布类的方格颜色，画布类通过
 * 检查方格颜色来体现ErsBlock块的移动情况。
 */
public class GameCanvas extends JPanel {
   
	private static final long serialVersionUID = 6732901391026089276L;
	
	//score为当前分数，totalScore为总分数
    private int rows, cols,score,totalScore,level=Tetris.DEFAULT_LEVEL;
    //数组ErsBox[][]的用途是：用数组来保存方格，储存每个方格的颜色和大小。
    private ErsBox[][] canvasBox;
    //每个方格大小，之后会根据窗口大小确定方格大小
    private int boxWidth=20, boxHeight=20;

    /**
     * 画布类的构造函数
     *
     * @param rows int,画布的行数
     * @param cols int,画布的列数 行数和列数决定着画布拥有方格的数目
     */
    public GameCanvas(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        canvasBox = new ErsBox[rows][cols];
        for (int i = 0; i < canvasBox.length; i++) {
            for (int j = 0; j < canvasBox[i].length; j++) {
                canvasBox[i][j] = new ErsBox(false);
            }
        }
        Border border=BorderFactory.createMatteBorder(0,0,0,1,Color.BLACK);
        setBorder(border);
    }
 
    /**
     * 取得画布中方格的列数
     *
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * 取得画布中方格的行数
     *
     * @return int,方格的行数
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * 取得游戏成绩
     *
     * @return int, 分数
     */
    public int getPresentScore() {
    	if(score>=Tetris.PER_LEVEL_SCORE)
    		resetAfterLevelUPScore();
        return score;
    }
    public int getTotalScore() {
		return totalScore;
	}
    public void resetAfterLevelUPScore()
    {
    	if(level<Tetris.MAX_LEVEL)
    	{
    		level++;
    		score=score-Tetris.PER_LEVEL_SCORE;
    	}
    }
    
    public int getLevel() {
		return level;
	}
    
    public void setLevel(int level) {
		this.level=level;
	}

    /**
     * 得到某一行某一列的方格引用
     *
     * @return row int ,要引用的方格所在的行
     * @param col int, 要引用的方格所在的行
     * @return ErsBox,在row行col列的方格的引用
     */
    public ErsBox getBox(int row, int col) {
        if (row < 0 || row > canvasBox.length - 1 || col < 0 || col > canvasBox[0].length - 1) {
            return null;
        }
        return (canvasBox[row][col]);
    }

    /**
     * 覆盖JComponent类的函数，画组件。
     *
     * @param g 图形设备环境
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < canvasBox.length; i++) {
            for (int j = 0; j < canvasBox[i].length; j++) {
            	if(canvasBox[i][j].isFront())
            	{
            		g.drawImage(ErsBox.cellImage,j * (ErsBox.BOX_WIDTH+2)+10, i *(ErsBox.BOX_HEIGHT+2)+10, null);
            	}
            	else {
            		g.drawImage(ErsBox.bgImage,j * (ErsBox.BOX_WIDTH+2)+10, i *(ErsBox.BOX_HEIGHT+2)+10, null);
				}
            }
        }
    }

    /**
     * 当一行被游戏者叠满后，将此行清除，并为游戏者加分
     *
     * @param row int，要清除的行，是由ErscanvasBoxGame类计算的
     */
    public synchronized void removeLine(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < cols; j++) {
                canvasBox[i][j] = (ErsBox) canvasBox[i - 1][j].clone();                 //将上一行的方块颜色克隆下来，
            }                                                                   //即消去一行方块
        }
        score += Tetris.PER_LINE_SCORE;
        totalScore += Tetris.PER_LINE_SCORE;
        repaint();
    }
    
    

    /**
     * 重置画布
     */
    public void reset() {
    	score = 0;
        totalScore = 0;
        level=Tetris.DEFAULT_LEVEL;
        for (int i = 0; i < canvasBox.length; i++) {
            for (int j = 0; j < canvasBox[i].length; j++) {
                canvasBox[i][j].setFront(false);
            }
        }

        repaint();
    }
}