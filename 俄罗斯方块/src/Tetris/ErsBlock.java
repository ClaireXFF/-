package Tetris;

/**
 * 块类，继承自线程类（Thread） 由4 × 4个方块（ErsBox）构成一个方块， 控制块的移动·下落·变形等
 */

class ErsBlock extends Thread {

    /**
     * 一个块占的行数是4行
     */
    public final static int BLOCK_ROWS = 4;
    /**
     * 一个块占的列数是4列
     */
    public final static int BLOCK_COLS = 4;
    /**
     * 让升级变化平滑的因子，避免最后几级之间的速度相差近一倍
     */
    public final static int LEVEL_FLATNESS_GENE = 3;
    /**
     * 相近的两级之间，块每下落一行的时间差别为多少（毫秒）
     */
    public final static int BETWEEN_LEVELS_DEGRESS_TIME = 100;
    /**
     * 方块的样式数目为7
     */
    public final static int BLOCK_KIND_NUMBER = 7;
    /**
     * 每一个样式的方块的反转状态种类为4
     */
    public final static int BLOCK_STATUS_NUMBER = 4;
    /**
     * 分别对应7种模型的28种状态
     */
    public final static int[][] STYLES = { //共28种状态
    		{0xf000, 0x8888, 0xf000, 0x8888}, //长条型的四种状态
            {0xe400, 0x4c40, 0x4e00, 0x8c80}, //T型的四种状态
            {0x8c40, 0x6c00, 0x8c40, 0x6c00}, //反Z型的四种状态
            {0x4c80, 0xc600, 0x4c80, 0xc600}, //Z型的四种状态
            {0xe800, 0xc440, 0x2e00, 0x88c0}, //7型的四种状态
            {0xe200, 0x44c0, 0x8e00, 0xc880}, //反7型的四种状态   
            {0xcc00, 0xcc00, 0xcc00, 0xcc00}, //方块的四种状态
    };
    private GameCanvas canvas;
    private ErsBox[][] blockBoxes = new ErsBox[BLOCK_ROWS][BLOCK_COLS];
    private int style, y, x;
    private boolean pausing = false, moving = true;
    
    //开始游戏
    public void startMove() {
        pausing = false;
        moving = true;
    }

    //游戏暂停
    public void pauseMove() {
        pausing = true;
        moving = true;
    }

    //游戏继续
    public void resumeMove() {
        pausing = false;
        moving = true;
    }

    //游戏停止
    public void stopMove() {
        pausing = false;
        moving = false;
    }

    /**
     * 构造函数，产生一个特定的块
     *
     * @param style 块的样式，对应STYLES的28个值中的一个
     * @param y 起始位置，左上角在canvas中的坐标行
     * @param x 起始位置，左上角在canvas中的坐标列
     * @param level 游戏等级，控制块的下落速度
     * @param canvas 画板
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
                //将16进制的1向右移动一位
                key >>= 1;
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

        display();
    }

    /**
     * 线程类的run()函数覆盖，下落块，直到块不能再下落
     * block.start()调用的函数
     */
    @Override
    public void run() {
        while (moving) {
            try {
                sleep(BETWEEN_LEVELS_DEGRESS_TIME*(Tetris.MAX_LEVEL-canvas.getLevel()+LEVEL_FLATNESS_GENE));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            //后边的moving是表示在等待的100毫秒间，moving没有被改变
            if (!pausing) {
                moving = (moveTo(y + 1, x) && moving);
            }
        }
    }

    /**
     * 将当前块从画布的对应位置移除，要等到下次重画画布时才能反映出来
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
     * 让当前块放置在画布的对应位置上，要等到下次重画画布时才能看见
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
     * 当前块能否移动到newRow/newCol 所指定的位置
     *
     * @param newRow int,目的地所在行
     * @param newCol int，目的地所在列
     * @return boolean，true-能移动，false-不能移动
     */
    public boolean isMoveAble(int newRow, int newCol) {
        erase();
        for (int i = 0; i < blockBoxes.length; i++) {
            for (int j = 0; j < blockBoxes[i].length; j++) {
                if (blockBoxes[i][j].isFront()) {
                    ErsBox box = canvas.getBox(i + newRow, j + newCol);
                    //到边界或者目标位置有块的时候 
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
     * 将当前块移动到newRow/newCol 所指定的位置
     *
     * @param newRow int,目的地所在行
     * @param newCol int，目的地所在列
     * @return boolean，true-移动成功，false-移动失败
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
     * 块向左移动一格
     */
    public void moveLeft() {
        moveTo(y, x - 1);
    }

    /**
     * 块向右移动一格
     */
    public void moveRight() {
        moveTo(y, x + 1);
    }

    /**
     * 块向下移动一格
     */
    public void moveDown() {
        moveTo(y + 1, x);
    }

    /**
     * 当前块能否变成newStyle所指定的块样式，主要是考虑 边界以及被其他块挡住，不能移动的情况
     *
     * @param newSytle int，希望改变的块样式，对应STYLES的28个值中的一个
     * @return boolean，true-能改变，false-不能改变
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
     * 将当前块变成newStyle所指定的块样式
     *
     * @param newStyle int，希望改变的块样式，对应STYLES的28个值中的一个
     * @return true-改变成功，false-改变失败
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
     * 块变型
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
