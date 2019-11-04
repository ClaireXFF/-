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
 * ��������࣬�̳���JPanel�� �ϱ߰���Ԥ�Դ��ڣ��ȼ����÷ֵ���Ϣ��
 */
class ControlPanel extends JPanel {

	private static final long serialVersionUID = 3900659640646175724L;
	//������Ϣ
	private JTextField tfScore = new JTextField(" 0"),
            tfTotalScore=new JTextField(" 0"),
            tfTime = new JTextField(" "),
			tfLevel = new JTextField("0" + Tetris.DEFAULT_LEVEL);
	private JPanel plInfo = new JPanel(new GridLayout(6, 1));
	
	//��һ��
	private JPanel plTip = new JPanel(new BorderLayout());
    private TipPanel plTipBlock = new TipPanel();
    
    //����
    private JPanel plButton = new JPanel(new GridLayout(6, 1));
    private JButton btPlay = new JButton(" ��ʼ"),
            btPause = new JButton(" ��ͣ"),
            btStop = new JButton("��ֹ��Ϸ"),
            btLevelUp=new JButton("�����Ѷ�"),
            btLevelDown=new JButton("�����Ѷ�");
    private Timer timer;
    

    /**
     * ���������Ĺ��캯��
     *
     * @param game ErsBlocksGame,ErsBlocksGame ���һ��ʵ������ ����ֱ�ӿ���ErsBlocksGame�����Ϊ��
     */
    public ControlPanel(final Tetris game) {
        setLayout(new GridLayout(3, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        plInfo.add(new JLabel("       SCORE"));
        tfScore.setEditable(false);
        tfScore.setHorizontalAlignment(JTextField.CENTER);
        tfScore.setFont(new Font("΢���ź�", Font.BOLD, 15));
        tfScore.setBorder(null);
        plInfo.add(tfScore);
        plInfo.add(new JLabel("     Hi-SCORE"));
        tfTotalScore.setEditable(false);
        tfTotalScore.setHorizontalAlignment(JTextField.CENTER);
        tfTotalScore.setFont(new Font("΢���ź�", Font.BOLD, 15));
        tfTotalScore.setBorder(null);
        plInfo.add(tfTotalScore);
        plInfo.add(new JLabel("        Speed"));
        tfLevel.setEditable(false);
        tfLevel.setHorizontalAlignment(JTextField.CENTER);
        tfLevel.setFont(new Font("΢���ź�", Font.BOLD, 15));
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
        tfTime.setFont(new Font("΢���ź�", Font.PLAIN, 13));
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

        btPlay.addActionListener(new ActionListener() {                         //��ʼ��Ϸ
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.playGame();
            }
        });
        btPause.addActionListener(new ActionListener() {                        //��ͣ��Ϸ
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (btPause.getText().equals(" ��ͣ")) {
                    game.pauseGame();
                } else {
                    game.resumeGame();
                }
            }
        });
        
        btStop.addActionListener(new ActionListener() {                         //ֹͣ��Ϸ
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
        
        //��������ʱ��ı仯ˢ�·����ͼ���
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DateFormat format = new SimpleDateFormat("ʱ��:HH:mm:ss");      //ϵͳ���ʱ��
                Date date = new Date();
                tfTime.setText(format.format(date));
                tfScore.setText("" + game.getPresentScore());
                tfTotalScore.setText(" "+game.getTotalScore()); 
                tfLevel.setText("0"+game.getLevel());
                
              //ʵʱ��أ��жϰ�ť�Ƿ����
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
     * ����Ԥ�Դ��ڵ���ʽ
     *
     * @param style int,��ӦErsBlock���STYLES�е�28��ֵ
     */
    public void setTipStyle(int style) {
        plTipBlock.setStyle(style);
    }

    /**
     * ���ð�ť��״̬��
     */
    public void setPlayButtonEnable(boolean enable) {
        btPlay.setEnabled(enable);
    }

    public void setPauseButtonEnable(boolean enable) {
        btPause.setEnabled(enable);
    }

    public void setPauseButtonLabel(boolean pause) {
        btPause.setText(pause ? " ��ͣ" : " ����");
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
     * ���ÿ������,���÷���
     */
    public void reset() {
    	tfScore.setText(" 0");
    	tfTotalScore.setText(" 0");
        plTipBlock.setStyle(0);
    }

    /**
     * Ԥ�Դ��ڵ�ʵ��ϸ����
     */
	public class TipPanel extends JPanel {                                    //TipPanel������ʾ��һ����Ҫ���ַ������״

		private static final long serialVersionUID = 5160553671436997616L;
        private ErsBox[][] boxes = new ErsBox[ErsBlock.BLOCK_ROWS][ErsBlock.BLOCK_COLS];
        private int style, boxWidth=20, boxHeight=20;

        /**
         * Ԥ��ʾ�����๹�캯��
         */
        public TipPanel() {
            for (int i = 0; i < boxes.length; i++) {
                for (int j = 0; j < boxes[i].length; j++) {
                    boxes[i][j] = new ErsBox(false);
                }
            }
        }

        /**
         * ����Ԥ��ʾ���ڵķ�����ʽ
         *
         * @param style int����ӦErsBlock���STYLES�е�28��ֵ
         */
        public void setStyle(int style) {
            this.style = style;
            repaint();
        }

        /**
         * ����JComponent��ĺ������������
         *
         * @param g ͼ���豸����
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

