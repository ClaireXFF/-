package Tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
/**
 * �����࣬����<����>*<����> ��������ʵ���� �̳���JPanel�ࡣ ErsBlock�߳��ද̬�ı仭����ķ�����ɫ��������ͨ��
 * ��鷽����ɫ������ErsBlock����ƶ������
 */
public class GameCanvas extends JPanel {
   
	private static final long serialVersionUID = 6732901391026089276L;
	
	//scoreΪ��ǰ������totalScoreΪ�ܷ���
    private int rows, cols,score,totalScore,level=Tetris.DEFAULT_LEVEL;
    //����ErsBox[][]����;�ǣ������������淽�񣬴���ÿ���������ɫ�ʹ�С��
    private ErsBox[][] canvasBox;
    //ÿ�������С��֮�����ݴ��ڴ�Сȷ�������С
    private int boxWidth=20, boxHeight=20;

    /**
     * ������Ĺ��캯��
     *
     * @param rows int,����������
     * @param cols int,���������� ���������������Ż���ӵ�з������Ŀ
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
     * ȡ�û����з��������
     *
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * ȡ�û����з��������
     *
     * @return int,���������
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * ȡ����Ϸ�ɼ�
     *
     * @return int, ����
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
     * �õ�ĳһ��ĳһ�еķ�������
     *
     * @return row int ,Ҫ���õķ������ڵ���
     * @param col int, Ҫ���õķ������ڵ���
     * @return ErsBox,��row��col�еķ��������
     */
    public ErsBox getBox(int row, int col) {
        if (row < 0 || row > canvasBox.length - 1 || col < 0 || col > canvasBox[0].length - 1) {
            return null;
        }
        return (canvasBox[row][col]);
    }

    /**
     * ����JComponent��ĺ������������
     *
     * @param g ͼ���豸����
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
     * ��һ�б���Ϸ�ߵ����󣬽������������Ϊ��Ϸ�߼ӷ�
     *
     * @param row int��Ҫ������У�����ErscanvasBoxGame������
     */
    public synchronized void removeLine(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < cols; j++) {
                canvasBox[i][j] = (ErsBox) canvasBox[i - 1][j].clone();                 //����һ�еķ�����ɫ��¡������
            }                                                                   //����ȥһ�з���
        }
        score += Tetris.PER_LINE_SCORE;
        totalScore += Tetris.PER_LINE_SCORE;
        repaint();
    }
    
    

    /**
     * ���û���
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