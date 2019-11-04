package Tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * 游戏主类，继承自JFrame类，负责游戏的全�?控制�? 内含�? 1.�?个GameCanvas画布类的实例对象�?
 * 2.�?个保存当前活动块（RussiaBlock）实例的对象�? 3.�?个保存当前控制面板（ControlPanel）实例的对象�?
 */
public class Tetris extends JFrame {
	/**
     * 每填满一行计多少�?
     */
    public final static int PER_LINE_SCORE = 100;
    /**
     * 积多少分以后能升�?
     */
    public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 2;
    /**
     * �?大级数是10�?
     */
    public final static int MAX_LEVEL = 10;
    /**
     * 默认级数�?2
     */
    public final static int DEFAULT_LEVEL = 2;

	private static final long serialVersionUID = -7332245439279674749L;
    private GameCanvas canvas;
    private ErsBlock block;
    //判断是否暂停
    private boolean playing = false;
    private ControlPanel ctrlPanel;
   
    /**
     * 主游戏类的构造方�?
     *
     * @param title String ,窗口标题
     */
    public Tetris(String title) {
        super(title);                                          //设置标题
        setSize(370, 500);                                     //设置窗口大小                  
        setLocationRelativeTo(null);                             //设置窗口居中
        setResizable(false);
        
        setLayout(new BorderLayout(6, 0));              //设置窗口的布�?管理�?,6指的水平距离
        canvas = new GameCanvas(20, 10);         				//新建游戏画布
        //canvas.setBorder(border);
        ctrlPanel = new ControlPanel(this);                        //新建控制面板
        add(canvas, BorderLayout.CENTER);                //左边加上画布
        add(ctrlPanel, BorderLayout.EAST);               //右边加上控制面板
        
        ErsBox.drawImage();

        //注册窗口事件。当点击关闭按钮时，结束游戏，系统�??出�??
        addWindowListener(new WindowAdapter() {                    
            @Override
            public void windowClosing(WindowEvent we) {
                stopGame();
                System.exit(0);
            }
        });
       
        //放于�?后，就不会出现显示间断的感觉�?
        setVisible(true);
    }

    /**
     * 让游戏复�?
     */
    public void reset() {                            //画布复位，控制面板复�?
        ctrlPanel.setPlayButtonEnable(true);
        ctrlPanel.setPauseButtonEnable(false);
        ctrlPanel.setPauseButtonLabel(true);
        ctrlPanel.setStopButtonEnable(false);
        ctrlPanel.reset();
        canvas.reset();
    }

    /**
     * 判断游戏是否还在进行
     *
     * @return boolean,true -还在运行，false-已经停止
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * 得到当前活动的块
     *
     * @return ErsBlock,当前活动块的引用
     */
    public ErsBlock getCurBlock() {
        return block;
    }

    /**
     * 得到当前画布
     *
     * @return GameCanvas,当前画布的引�?
     */
    public GameCanvas getCanvas() {
        return canvas;
    }
    
    /** 
    * @Description: 获取总分数和当前分数
    * @param @return  
    * @return int   
    * @throws
     */
    public int getPresentScore() {
    	if (canvas != null) {
            return canvas.getPresentScore();
        }
        return 0;
	}
    
    public int getTotalScore() {
    	if (canvas != null) {
            return canvas.getTotalScore();
        }
        return 0;
	}
    
    public int getLevel()
    {
    	return canvas.getLevel();
    }

    /**
     * �?始游�?
     */
    public void playGame() {
    	reset();
        playing = true;
        Thread thread = new Thread(new Game());//启动游戏线程
        thread.start();
        
        ctrlPanel.setPlayButtonEnable(false);
        ctrlPanel.setPauseButtonEnable(true);
        ctrlPanel.setPauseButtonLabel(true);
        ctrlPanel.setStopButtonEnable(true);
        ctrlPanel.setLevelUpButtonEnable(true);
        ctrlPanel.setLevelDownButtonEnable(true);
        ctrlPanel.requestFocus();              //设置焦点
    }

    /**
     * 游戏暂停
     */
    public void pauseGame() {
        if (block != null) {
            block.pauseMove();
        }
        ctrlPanel.setPlayButtonEnable(false);
        ctrlPanel.setPauseButtonLabel(false);
        ctrlPanel.setStopButtonEnable(true);
    }

    /**
     * 让暂停中的游戏继�?
     */
    public void resumeGame() {
        if (block != null) {
            block.resumeMove();
        }
        ctrlPanel.setPlayButtonEnable(false);
        ctrlPanel.setPauseButtonEnable(true);
        ctrlPanel.setPauseButtonLabel(true);
        
        ctrlPanel.requestFocus();
    }

    /**
     * 用户停止游戏
     */
    public void stopGame() {
        playing = false;
        if (block != null) {
            block.stopMove();
        }
        ctrlPanel.setPlayButtonEnable(true);
        ctrlPanel.setPauseButtonEnable(false);
        ctrlPanel.setPauseButtonLabel(true);
        ctrlPanel.setStopButtonEnable(false);
        ctrlPanel.setLevelUpButtonEnable(false);
        ctrlPanel.setLevelDownButtonEnable(false);
        reset();//重置画布和控制面�?
    }

    public void levelUp() {
    	int level=canvas.getLevel()+1;
    	if(level<=Tetris.MAX_LEVEL)
    		canvas.setLevel(level);
	}
    
    public void levelDown() {
    	int level=canvas.getLevel()-1;
    	if(level>=DEFAULT_LEVEL)
    		canvas.setLevel(level);
	}

    /**
     * 报告游戏结束�?
     * 只有主类才能调用其私有类，故要另外写�?个函数调�?
     */
    private void reportGameOver() {
        new gameOverDialog(this, "俄罗斯方�?", "游戏结束!");
    }


    /**
     * 主类的私有类
     * �?轮游戏过程，实现了Runnable接口 �?轮游戏是�?个大循环，在这个循环中，每隔100毫秒�? �?查游戏中的当前块是否已经到底了，如果没有�?
     * 就继续等待�?�如果到底了，就看有没有全填满的行， 如果有就删除它，并为游戏者加分，同时随机产生�?个新的当前块并让它自动落下�??
     * 当新产生�?个块时，先检查画布最顶上的一行是否已经被占了，如果是，可以判断Game Over 了�??
     */
private class Game implements Runnable {
       @Override
        public void run() {
            int col = (int) (Math.random() * (canvas.getCols() - 3));
            int style = ErsBlock.STYLES[ (int) (Math.random() * 7)][(int) (Math.random() * 4)];

            while (playing) {
                if (block != null) {   //第二次循环之后，暂停500ms
                    if (block.isAlive()) {	//用来判断当前线程是否处于活动状�?�，是否在执行某个函数，可以用来判断是否在下�?
                        try {
    						Thread.sleep(500);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                        continue;
                    }
                }
                
                //第一次循环，block为空
                checkFullLine();    //�?查是否有全填满的�?

                if (isGameOver()) {
                    reportGameOver();
                    ctrlPanel.setPlayButtonEnable(true);
                    ctrlPanel.setPauseButtonLabel(false);
                    ctrlPanel.setStopButtonEnable(false);
                    return;
                }
                //块的起始图案和位�?
                block = new ErsBlock(style, -1, col, canvas);
                block.start();

                col = (int) (Math.random() * (canvas.getCols() - 3));
                style = ErsBlock.STYLES[ (int) (Math.random() * 7)][(int) (Math.random() * 4)];

                ctrlPanel.setTipStyle(style);
            }
        }

        //�?查画布中是否有全填满的行，如果有就删�?
        public void checkFullLine() {
            for (int i = 0; i < canvas.getRows(); i++) {
                int row = -1;
                boolean fullLineColorBox = true;
                for (int j = 0; j < canvas.getCols(); j++) {
                    if (!canvas.getBox(i, j).isFront()) {
                        fullLineColorBox = false;
                        break;
                    }
                }
                if (fullLineColorBox) {
                    row = i--;
                    canvas.removeLine(row);
                }
            }
        }

        //根据�?顶行是否被占，判断游戏是否已经结束了
        //@return boolean ，true-游戏结束了，false-游戏未结�?
        private boolean isGameOver() {
            for (int i = 0; i < canvas.getCols(); i++) {
                ErsBox box = canvas.getBox(0, i);
                if (box.isFront()) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *  主类的私有类
     * 定义GameOver对话框�??
     */
    @SuppressWarnings("serial")
    private class gameOverDialog extends JDialog implements ActionListener {

        private JButton againButton, exitButton;
        private Border border = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));

        public gameOverDialog(JFrame parent, String title, String message) {
            super(parent, title, true);
            if (parent != null) {
                setSize(240, 120);
                this.setLocationRelativeTo(parent);
                JPanel messagePanel = new JPanel();
                messagePanel.add(new JLabel(message));
                messagePanel.setBorder(border);
                Container container = this.getContentPane();
                container.setLayout(new GridLayout(2, 0, 0, 10));
                container.add(messagePanel);
                JPanel choosePanel = new JPanel();
                choosePanel.setLayout(new GridLayout(0, 2, 4, 0));
                container.add(choosePanel);
                againButton = new JButton("再玩�?�?");
                exitButton = new JButton("�?出游�?");
                choosePanel.add(new JPanel().add(againButton));
                choosePanel.add(new JPanel().add(exitButton));
                choosePanel.setBorder(border);
            }
            againButton.addActionListener(this);
            exitButton.addActionListener(this);
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == againButton) {
                this.setVisible(false);
                reset();
            } else if (e.getSource() == exitButton) {
                stopGame();
                System.exit(0);

            }
        }
    }
}

