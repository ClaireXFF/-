package Tetris;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * 控制面板类，继承自JPanel。 上边安放预显窗口，等级，得分等信息。
 */
class ControlPanel extends JPanel {

	private static final long serialVersionUID = 3900659640646175724L;
	//分数信息
	private JTextField tfScore = new JTextField(" 0"),
            tfTotalScore=new JTextField(" 0"),
            tfTime = new JTextField(" "),
			tfLevel = new JTextField("0" + Tetris.DEFAULT_LEVEL);
	private JPanel plInfo = new JPanel(new GridLayout(6, 1));
	
	//下一个
	private JPanel plTip = new JPanel(new BorderLayout());
    private TipPanel plTipBlock = new TipPanel();
    
    //按键
    private JPanel plButton = new JPanel(new GridLayout(6, 1));
    private JButton btPlay = new JButton(" 开始"),
            btPause = new JButton(" 暂停"),
            btStop = new JButton("终止游戏"),
            btLevelUp=new JButton("增加难度"),
            btLevelDown=new JButton("降低难度");
    private Timer timer;
    

    /**
     * 控制面板类的构造函数
     *
     * @param game ErsBlocksGame,ErsBlocksGame 类的一个实例引用 方便直接控制ErsBlocksGame类的行为。
     */
    public ControlPanel(final Tetris game) {
        setLayout(new GridLayout(3, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        plInfo.add(new JLabel("       SCORE"));
        tfScore.setEditable(false);
        tfScore.setHorizontalAlignment(JTextField.CENTER);
        tfScore.setFont(new Font("微软雅黑", Font.BOLD, 15));
        tfScore.setBorder(null);
        plInfo.add(tfScore);
        plInfo.add(new JLabel("     Hi-SCORE"));
        tfTotalScore.setEditable(false);
        tfTotalScore.setHorizontalAlignment(JTextField.CENTER);
        tfTotalScore.setFont(new Font("微软雅黑", Font.BOLD, 15));
        tfTotalScore.setBorder(null);
        plInfo.add(tfTotalScore);
        plInfo.add(new JLabel("        Speed"));
        tfLevel.setEditable(false);
        tfLevel.setHorizontalAlignment(JTextField.CENTER);
        tfLevel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        tfLevel.setBorder(null);
        plInfo.add(tfLevel);
        
        plTip.add(new JLabel("  NEXT BLOCK"), BorderLayout.NORTH);
        plTip.add(plTipBlock);
        
        plButton.add(btPlay);
        btPlay.setEnabled(true);
        plButton.add(btPause);
        btPause.setEnabled(false);
        plButton.add(btStop);
        btStop.setEnabled(false);
        plButton.add(btLevelUp);
        btLevelUp.setEnabled(false);
        plButton.add(btLevelDown);
        btLevelDown.setEnabled(false);
        tfTime.setHorizontalAlignment(JTextField.CENTER);
        tfTime.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        tfTime.setMargin(new Insets(0, 0, 0, 0));
        tfTime.setBorder(null);
        tfTime.setEditable(false);
        plButton.add(tfTime);
        
        add(plInfo);
        add(plTip);
        add(plButton);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (!game.isPlaying()) {
                    return;
                }

                ErsBlock block = game.getCurBlock();
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        block.moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        block.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        block.moveRight();
                        break;
                    case KeyEvent.VK_UP:
                        block.turnNext();
                        break;
                    default:
                        break;
                }
            }
        });

        btPlay.addActionListener(new ActionListener() {                         //开始游戏
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.playGame();
            }
        });
        btPause.addActionListener(new ActionListener() {                        //暂停游戏
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (btPause.getText().equals(" 暂停")) {
                    game.pauseGame();
                } else {
                    game.resumeGame();
                }
            }
        });
        
        btStop.addActionListener(new ActionListener() {                         //停止游戏
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.stopGame();
            }
        });
        
        btLevelUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.levelUp();
			}
		});
        
        btLevelDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				game.levelDown();
			}
		});
        
        //可以随着时间的变化刷新分数和级别
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DateFormat format = new SimpleDateFormat("时间:HH:mm:ss");      //系统获得时间
                Date date = new Date();
                tfTime.setText(format.format(date));
                tfScore.setText("" + game.getPresentScore());
                tfTotalScore.setText(" "+game.getTotalScore()); 
                tfLevel.setText("0"+game.getLevel());
                
              //实时监控，判断按钮是否可用
                if(game.getLevel()<Tetris.MAX_LEVEL)
                	btLevelUp.setEnabled(true);
                else
                	btLevelUp.setEnabled(false);
                if(game.getLevel()>Tetris.DEFAULT_LEVEL)
                	btLevelDown.setEnabled(true);
                else
                	btLevelDown.setEnabled(false);
            }
        });
        timer.start();
    }
    /**
     * 设置预显窗口的样式
     *
     * @param style int,对应ErsBlock类的STYLES中的28个值
     */
    public void setTipStyle(int style) {
        plTipBlock.setStyle(style);
    }

    /**
     * 设置按钮的状态。
     */
    public void setPlayButtonEnable(boolean enable) {
        btPlay.setEnabled(enable);
    }

    public void setPauseButtonEnable(boolean enable) {
        btPause.setEnabled(enable);
    }

    public void setPauseButtonLabel(boolean pause) {
        btPause.setText(pause ? " 暂停" : " 继续");
    }

    public void setStopButtonEnable(boolean enable) {
        btStop.setEnabled(enable);
    }
    
    public void setLevelUpButtonEnable(boolean enable) {
		btLevelUp.setEnabled(enable);
	}
    
    public void setLevelDownButtonEnable(boolean enable) {
		btLevelDown.setEnabled(enable);
	}


    /**
     * 重置控制面板,重置分数
     */
    public void reset() {
    	tfScore.setText(" 0");
    	tfTotalScore.setText(" 0");
        plTipBlock.setStyle(0);
    }

    /**
     * 预显窗口的实现细节类
     */
	public class TipPanel extends JPanel {                                    //TipPanel用来显示下一个将要出现方块的形状

		private static final long serialVersionUID = 5160553671436997616L;
        private ErsBox[][] boxes = new ErsBox[ErsBlock.BLOCK_ROWS][ErsBlock.BLOCK_COLS];
        private int style, boxWidth=20, boxHeight=20;

        /**
         * 预显示窗口类构造函数
         */
        public TipPanel() {
            for (int i = 0; i < boxes.length; i++) {
                for (int j = 0; j < boxes[i].length; j++) {
                    boxes[i][j] = new ErsBox(false);
                }
            }
        }

        /**
         * 设置预显示窗口的方块样式
         *
         * @param style int，对应ErsBlock类的STYLES中的28个值
         */
        public void setStyle(int style) {
            this.style = style;
            repaint();
        }

        /**
         * 覆盖JComponent类的函数，画组件。
         *
         * @param g 图形设备环境
         */
        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	
            int key = 0x8000;
            for (int i = 0; i < boxes.length; i++) {
                for (int j = 0; j < boxes[i].length; j++) {
                	if((key & style) != 0)
                	{
                		g.drawImage(ErsBox.cellImage,j * (ErsBox.BOX_WIDTH+2), i *(ErsBox.BOX_HEIGHT+2), null);
                	}
                	else {
                		g.drawImage(ErsBox.bgImage,j * (ErsBox.BOX_WIDTH+2), i *(ErsBox.BOX_HEIGHT+2), null);
    				}
                    key >>= 1;
                }
            }
        }
    }
}

