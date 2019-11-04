package Tetris;

/**
 * ���࣬�̳����߳��ࣨThread�� ��4 �� 4�����飨ErsBox������һ�����飬 ���ƿ���ƶ������䡤���ε�
 */

class ErsBlock extends Thread {

    /**
     * һ����ռ��������4��
     */
    public final static int BLOCK_ROWS = 4;
    /**
     * һ����ռ��������4��
     */
    public final static int BLOCK_COLS = 4;
    /**
     * �������仯ƽ�������ӣ�������󼸼�֮����ٶ�����һ��
     */
    public final static int LEVEL_FLATNESS_GENE = 3;
    /**
     * ���������֮�䣬��ÿ����һ�е�ʱ����Ϊ���٣����룩
     */
    public final static int BETWEEN_LEVELS_DEGRESS_TIME = 100;
    /**
     * �������ʽ��ĿΪ7
     */
    public final static int BLOCK_KIND_NUMBER = 7;
    /**
     * ÿһ����ʽ�ķ���ķ�ת״̬����Ϊ4
     */
    public final static int BLOCK_STATUS_NUMBER = 4;
    /**
     * �ֱ��Ӧ7��ģ�͵�28��״̬
     */
    public final static int[][] STYLES = { //��28��״̬
    		{0xf000, 0x8888, 0xf000, 0x8888}, //�����͵�����״̬
            {0xe400, 0x4c40, 0x4e00, 0x8c80}, //T�͵�����״̬
            {0x8c40, 0x6c00, 0x8c40, 0x6c00}, //��Z�͵�����״̬
            {0x4c80, 0xc600, 0x4c80, 0xc600}, //Z�͵�����״̬
            {0xe800, 0xc440, 0x2e00, 0x88c0}, //7�͵�����״̬
            {0xe200, 0x44c0, 0x8e00, 0xc880}, //��7�͵�����״̬   
            {0xcc00, 0xcc00, 0xcc00, 0xcc00}, //���������״̬
    };
    private GameCanvas canvas;
    private ErsBox[][] blockBoxes = new ErsBox[BLOCK_ROWS][BLOCK_COLS];
    private int style, y, x;
    private boolean pausing = false, moving = true;
    
    //��ʼ��Ϸ
    public void startMove() {
        pausing = false;
        moving = true;
    }

    //��Ϸ��ͣ
    public void pauseMove() {
        pausing = true;
        moving = true;
    }

    //��Ϸ����
    public void resumeMove() {
        pausing = false;
        moving = true;
    }

    //��Ϸֹͣ
    public void stopMove() {
        pausing = false;
        moving = false;
    }

    /**
     * ���캯��������һ���ض��Ŀ�
     *
     * @param style �����ʽ����ӦSTYLES��28��ֵ�е�һ��
     * @param y ��ʼλ�ã����Ͻ���canvas�е�������
     * @param x ��ʼλ�ã����Ͻ���canvas�е�������
     * @param level ��Ϸ�ȼ������ƿ�������ٶ�
     * @param canvas ����
     */
    public ErsBlock(int style, int y, int x, GameCanvas canvas) {
        this.style = style;
        this.y = y;
        this.x = x;
        this.canvas = canvas;

        int key = 0x8000;
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                boolean isFront = ((style & key) != 0);
                if(isFront)
                	System.out.print("1 ");
                else
                	System.out.print("0 ");
                blockBoxes[i][j] = new ErsBox(isFront);
                //��16���Ƶ�1�����ƶ�һλ
                key >>= 1;
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

        display();
    }

    /**
     * �߳����run()�������ǣ�����飬ֱ���鲻��������
     * block.start()���õĺ���
     */
    @Override
    public void run() {
        while (moving) {
            try {
                sleep(BETWEEN_LEVELS_DEGRESS_TIME*(Tetris.MAX_LEVEL-canvas.getLevel()+LEVEL_FLATNESS_GENE));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            //��ߵ�moving�Ǳ�ʾ�ڵȴ���100����䣬movingû�б��ı�
            if (!pausing) {
                moving = (moveTo(y + 1, x) && moving);
            }
        }
    }

    /**
     * ����ǰ��ӻ����Ķ�Ӧλ���Ƴ���Ҫ�ȵ��´��ػ�����ʱ���ܷ�ӳ����
     */
    private void erase() {
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                if (blockBoxes[i][j].isFront()) {
                    ErsBox box = canvas.getBox(i + y, j + x);
                    if (box == null) {
                        continue;
                    }
                    box.setFront(false);
                }
            }
        }
    }

    /**
     * �õ�ǰ������ڻ����Ķ�Ӧλ���ϣ�Ҫ�ȵ��´��ػ�����ʱ���ܿ���
     */
    private void display() {
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                if (blockBoxes[i][j].isFront()) {
                    ErsBox box = canvas.getBox(i + y, j + x);
                    if (box == null) {
                        continue;
                    }
                    box.setFront(true);
                }
            }
        }
    }
    
    /**
     * ��ǰ���ܷ��ƶ���newRow/newCol ��ָ����λ��
     *
     * @param newRow int,Ŀ�ĵ�������
     * @param newCol int��Ŀ�ĵ�������
     * @return boolean��true-���ƶ���false-�����ƶ�
     */
    public boolean isMoveAble(int newRow, int newCol) {
        erase();
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                if (blockBoxes[i][j].isFront()) {
                    ErsBox box = canvas.getBox(i + newRow, j + newCol);
                    //���߽����Ŀ��λ���п��ʱ�� 
                    if (box == null || (box.isFront())) {
                    	display();
                        return false;
                    }
                }
            }
        }
        display();
        return true;
    }

    /**
     * ����ǰ���ƶ���newRow/newCol ��ָ����λ��
     *
     * @param newRow int,Ŀ�ĵ�������
     * @param newCol int��Ŀ�ĵ�������
     * @return boolean��true-�ƶ��ɹ���false-�ƶ�ʧ��
     */
    private synchronized boolean moveTo(int newRow, int newCol) {
        if (!isMoveAble(newRow, newCol) || !moving) {
            return false;
        }
        
        erase();
        y = newRow;
        x = newCol;
        display();
        
        canvas.repaint();

        return true;
    }
    
    /**
     * �������ƶ�һ��
     */
    public void moveLeft() {
        moveTo(y, x - 1);
    }

    /**
     * �������ƶ�һ��
     */
    public void moveRight() {
        moveTo(y, x + 1);
    }

    /**
     * �������ƶ�һ��
     */
    public void moveDown() {
        moveTo(y + 1, x);
    }

    /**
     * ��ǰ���ܷ���newStyle��ָ���Ŀ���ʽ����Ҫ�ǿ��� �߽��Լ��������鵲ס�������ƶ������
     *
     * @param newSytle int��ϣ���ı�Ŀ���ʽ����ӦSTYLES��28��ֵ�е�һ��
     * @return boolean��true-�ܸı䣬false-���ܸı�
     */
    private boolean isTurnAble(int newStyle) {
        int key = 0x8000;
        erase();
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                if ((newStyle & key) != 0) {
                    ErsBox box = canvas.getBox(i + y, j + x);
                    if (box == null || (box.isFront())) {
                        display();
                        return false;
                    }
                }
                key >>= 1;
            }
        }
        display();
        return true;
    }

    /**
     * ����ǰ����newStyle��ָ���Ŀ���ʽ
     *
     * @param newStyle int��ϣ���ı�Ŀ���ʽ����ӦSTYLES��28��ֵ�е�һ��
     * @return true-�ı�ɹ���false-�ı�ʧ��
     */
    private boolean turnTo(int newStyle) {
        if (!isTurnAble(newStyle) || !moving) {
            return false;
        }

        erase();
        int key = 0x8000;
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                boolean isFront = ((newStyle & key) != 0);
                blockBoxes[i][j].setFront(isFront);
                key >>= 1;
            }
        }
        style = newStyle;

        display();
        canvas.repaint();

        return true;
    }
    
    /**
     * �����
     */
    public void turnNext() {
        for (int i = 0; i < BLOCK_KIND_NUMBER; i++) {
            for (int j = 0; j < BLOCK_STATUS_NUMBER; j++) {
                if (STYLES[i][j] == style) {
                    int newStyle = STYLES[i][(j + 1) % BLOCK_STATUS_NUMBER];
                    turnTo(newStyle);
                    return;
                }
            }
        }
    }

}
