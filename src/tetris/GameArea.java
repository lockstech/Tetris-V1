package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import tetrisBlocks.*;

import static tetris.GameForm.gameThreadFrame;
import static tetris.GameForm.gt;
import static tetris.Main.gf;

class GameArea extends JPanel {

    private int gridRows; // Строки.
    private int gridColumns; // Столбцы.
    private int gridCellSize; // Размер клетки.
    private Color[][] fallenBlocks; // Массив для сохранения упавших блоков.

    JLabel scoreDisplay = new JLabel("Score: " + 0);
    JLabel levelDisplay = new JLabel("Level: " + 1);

    JButton btnMainMenu = new JButton("Main menu");

    private TetrisBlock block; // Текущая фигура.

    private TetrisBlock nextBlock; // Следующая фигура.

    private TetrisBlock[] blocks;

    public GameArea(){
        gridRows = 21; // 20 + 1 из-за границ.
        gridColumns = 11;
        gridCellSize = 400 / 10;

        blocks = new TetrisBlock[]{ // Разные фигуры.
            new IShape(),
            new JShape(),
            new LShape(),
            new OShape(),
            new SShape(),
            new TShape(),
            new ZShape()
        };


        btnMainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameThreadFrame.setVisible(false);
                block = null;
                gt.interrupt();
                Main.showMainMenu();
            }
        });
    }

    public void initBackgroundArray(){
        fallenBlocks = new Color[gridRows][gridColumns];

    }

    public void spawnBlock(){ // Появление фигуры.
        Random r = new Random();
        if(block == null){ // Создание самой первой фигуры.
            block = blocks[r.nextInt(blocks.length)]; // Рандом формы фигуры.
            block.colorBlock();
        }
        nextBlock = blocks[r.nextInt(blocks.length)]; // Рандом следующей фигуры.
        nextBlock.colorBlock(); // Закрашиваем и поворачиваем фигуру.
        block.coordinates(gridColumns); // Задаём координаты.
    }

    public void assignmentBlock(){
        block = nextBlock;
    }

    public boolean isBlockOutside(){ // Окончание игры.
        if(block.getY() < 1){ // Если блок на границе игровой зоны.
            block = null;
            return true;
        }

        return false;
    }

    public  boolean moveBlockDown(){ // Движение фигуры вниз.
        if(checkingBottom() == false){ // Если фигура коснулась дна, то падение останавливается.

            return false; // Фигура не двигается вниз.
        }
        block.moveDown();
        repaint(); // Перерисовка компонента.
        return true; // Фигура двигается вниз.
    }

    public void moveBlockRight(){ // Движение фигуры вправо.
        if (block == null){ // Если игра окончена, то нельзя двигать фигуры.
            return;
        }
       if(!checkingRight()){ // Если справа граница.
           return;
       }
        block.moveRight();
        repaint();
    }

    public void moveBlockLeft(){ // Движение фигуры влево.
        if (block == null){ // Если игра окончена, то нельзя двигать фигуры.
            return;
        }
        if(!checkingLeft()){ // Если слева граница.
            return;
        }
        block.moveLeft();
        repaint();
    }

    public void dropBlockDown(){ // Резкое падение фигуры вниз.
        while(checkingBottom()){
            block.moveDown();
        }
        repaint();
    }

    public void rotateBlock(){
        boolean rotat = false;
        if (block == null){ // Если игра окончена, то нельзя поворачивать фигуру.
            return;
        }

        if(block.getLeftEdge() < 0){ // Проверка на границы.
            block.setX(0);
        }
        if(block.getRightEdge() >= gridColumns - 1) { //мб -1
            if(block.getHeight()==4){
                return;
            }
            if (checkingLeft()){
                if (block.getWidth() == 2){
                    block.setX(gridColumns - block.getWidth() - 2);
                }
                else {
                    block.setX(gridColumns - block.getWidth() - 2);
                    block.rotate();
                    block.setX(8);
                    rotat = true;
                }
                                                                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! БАГ // Почти исправлено.
            }                                                  // При повороте фигура может переместиться в другую фигуру => сделать проверку
            else{
                return;
            }
        }
        if(block.getRightEdge() >= gridColumns - 3) { // Для палки
            if(block.getHeight()==4){
                return;
            }
        }
        if(block.getBottomEdge() >= gridRows ){
            block.setY(gridRows - block.getHeight());

        }

        if(!checkingBottomForI()){ // Если снизу граница.
            return;
        }

        if(!checkingBottom()){ // Если снизу граница.
            return;
        }

        if(!checkingRightForI()){ // Если справа граница.
            return;
        }

        if(!checkingRight()){ // Если справа граница.
            return;
        }

        if(!checkingUp()){ // Если сверху граница.
            return;
        }
        if (rotat == false){
            block.rotate();
        }


        repaint();
    }

    private boolean checkingBottom(){ // Проверка на касание дна фигурой.
        if((block.getBottomEdge() == gridRows - 1)){ // Координата y + высота блока = строке. ( - 1 из-за границ)
            return false;
        }

        int[][]shape = block.getShape(); // Получение данных о фигуре.
        int w = block.getWidth();
        int h = block.getHeight();

        for(int col = 0; col < w; col++){ // Проверка на уже упавшие блоки, которые сформировали новое дно.
            for(int row = h - 1; row >= 0; row--){
                if(shape[row][col] != 0){

                    int x = col + block.getX();
                    int y = row + block.getY()+ 1;

                    if(y < 0){ // Если фигура за границей игрового поля.
                        break;
                    }
                    if(fallenBlocks[y][x] != null){
                        return false;
                    }
                    break;
                }
            }
        }
    return true;
    }

    private boolean checkingBottomForI(){ // Проверка на касание дна палкой.
        if (block.getWidth() == 4){
            if((block.getBottomEdge() == gridRows - 1)){ // Координата y + высота блока = строке. ( - 1 из-за границ)
                return false;
            }

            int[][]shape = block.getShape(); // Получение данных о фигуре.
            int w = block.getWidth();
            int h = block.getHeight();

            for(int col = 0; col < w; col++){ // Проверка на уже упавшие блоки, которые сформировали новое дно.
                for(int row = h - 1; row >= 0; row--){
                    if(shape[row][col] != 0){

                        int x = col + block.getX();
                        int y = block.getY() + 4;

                        if(y < 0){ // Если фигура за границей игрового поля.
                            break;
                        }
                        if(fallenBlocks[y][x] != null){
                            return false;
                        }
                        break;
                    }
                }
            }
            return true;
        }
        return true;
    }


    private boolean checkingUp(){ // Проверка на касание сверху.
        int[][]shape = block.getShape(); // Получение данных о фигуре.
        int w = block.getWidth();
        int h = block.getHeight();

        for(int col = 0; col < w; col++){ // Проверка на уже упавшие блоки, которые сформировали новое дно.
            for(int row = h - 1; row >= 0; row--){
                if(shape[row][col] != 0){

                    int x = col + block.getX();
                    int y = row + block.getY() - 1;

                    if(y < 0){ // Если фигура за границей игрового поля.
                        break;
                    }
                    if(fallenBlocks[y][x] != null){
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkingLeft(){ // Проверка на касание фигурой упора слева.
        if(block.getLeftEdge() == 0){
            return false;
        }

        int[][]shape = block.getShape(); // Получение данных о фигуре.
        int w = block.getWidth();
        int h = block.getHeight();

        for(int row = 0; row < h; row++){ // Проверка на уже упавшие блоки, которые сформировали новое дно.
            for(int col = 0; col < w; col++){ // Проход по строчкам, затем по столбам, чтобы проверить боковую сторону.
                if(shape[row][col] != 0){ // Проход слева направо (от 0).

                    int x = col + block.getX() - 1; // Проверка на координату меньше.
                    int y = row + block.getY();

                    if(y < 0){ // Если фигура за границей игрового поля.
                        break;
                    }
                    if(fallenBlocks[y][x] != null){
                        return false;
                    }
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkingRight(){ // Проверка на касание фигурой упора справа.
        if(block.getRightEdge() == gridColumns - 1){ // - 1 из-за границ
            return false;
        }

        int[][]shape = block.getShape(); // Получение данных о фигуре.
        int w = block.getWidth();
        int h = block.getHeight();

        for(int row = 0; row < h; row++){ // Проверка на уже упавшие блоки, которые сформировали новое дно.
            for(int col = w - 1; col >= 0; col--){ // Проход по строчкам, затем по столбам, чтобы проверить боковую сторону.
                if(shape[row][col] != 0){ // Проход справа налево (от w - 1).

                    int x = col + block.getX() + 1; // Проверка на координату больше.
                    int y = row + block.getY();

                    if(y < 0){ // Если фигура за границей игрового поля.
                        break;
                    }
                    if(fallenBlocks[y][x] != null){
                        return false;
                    }
                    break;
                }
            }
        }

        return true;
    }

    private boolean checkingRightForI(){ // Проверка на касание фигурой упора справа.
        if(block.getHeight() == 4) {
            if (block.getRightEdge() == gridColumns - 1) { // - 1 из-за границ
                return false;
            }

            int[][] shape = block.getShape(); // Получение данных о фигуре.
            int w = block.getWidth();
            int h = block.getHeight();

            for (int row = 0; row < h; row++) { // Проверка на уже упавшие блоки, которые сформировали новое дно.
                for (int col = w - 1; col >= 0; col--) { // Проход по строчкам, затем по столбам, чтобы проверить боковую сторону.
                    if (shape[row][col] != 0) { // Проход справа налево (от w - 1).

                        int x = col + block.getX() + 4; // Проверка на координату больше.
                        int y = block.getY();

                        if (y < 0) { // Если фигура за границей игрового поля.
                            break;
                        }
                        if (fallenBlocks[y][x] != null) {
                            return false;
                        }
                        if (fallenBlocks[y][x - 1] != null) {
                            return false;
                        }
                        if (fallenBlocks[y][x - 2] != null) {
                            return false;
                        }
                        if (fallenBlocks[y][x - 3] != null) {
                            return false;
                        }
                        break;
                    }
                }
            }

            return true;
        }
        return true;
    }

    public int clearLines(){ // Очистка строки.
        boolean lineFilled; // Заполненность строки.
        int linesCleared = 0;

        for(int r = gridRows - 1; r>= 0; r--){
            lineFilled = true; // Строка заполнена.

            for(int c = 1; c < gridColumns ; c++){ // Начинается с 1 из-за границы
                if(fallenBlocks[r][c] == null){
                    lineFilled = false; // Строка не заполнена.
                    break;
                }
            }
            if(lineFilled){ // Если строка заполнена.
                linesCleared++;
                clearLine(r); // Заполнение строки пустотой.
                shiftDown(r); // Перемещение строк вниз.
                clearLine(0); // Очистка нулевой Мб с 1.

                r++; // Увеличение строки на 1 для очистки самой нижней.

                repaint();
            }
        }
        return linesCleared;
    }

    private void clearLine(int r){ // Заполнение строки пустотой.
        for(int i = 1; i < gridColumns ; i++) { // Начинается с 1 из-за границы
            fallenBlocks[r][i] = null;
        }
    }

    private void shiftDown(int r){
        for(int row = r; row > 0; row--){
            for(int col = 0; col < gridColumns; col++){ // Мб начало с 1.
                fallenBlocks[row][col] = fallenBlocks[row - 1][col];
            }
        }
    }

    public void moveBlockToFallenBlocks(){ // Перемещение фигуры в массив упавших.
        int[][] shape = block.getShape(); // Получение всех данных о фигуре.
        int h = block.getHeight();
        int w = block.getWidth();

        int xPos = block.getX() ;
        int yPos = block.getY() ;

        Color color = block.getColor();

        for(int r = 0; r < h; r++){ // Заполняем соответствующие элементы массива цветов.
            for(int c = 0; c < w; c++){
                if(shape[r][c] == 1){
                    fallenBlocks[r + yPos][c + xPos] = color;
                }
            }
        }
    }

    private void drawBlock(Graphics g){ // Отрисовка фигуры.
        Graphics2D g2 = (Graphics2D)g;

        int h = block.getHeight();
        int w = block.getWidth();
        Color c = block.getColor();
        int[][] shape = block.getShape();

        for(int row = 0; row < h; row++){
            for(int column = 0; column < w; column++){
                if(shape[row][column] == 1){
                    int x = (block.getX() + (column  )) * gridCellSize;
                    int y = (block.getY() + (row )) * gridCellSize;

                    drawGridSquare(g2, c, x, y);
                }
            }
        }
    }

    private void drawNextBlock(Graphics g){ // Отрисовка следующей фигуры.
        Graphics2D g2 = (Graphics2D)g;

        int h = nextBlock.getHeight();
        int w = nextBlock.getWidth();
        Color c = nextBlock.getColor();
        int[][] shape = nextBlock.getShape();

        for(int row = 0; row < h; row++){
            for(int column = 0; column < w; column++){
                if(shape[row][column] == 1){
                    int x = 520 + column * gridCellSize;
                    int y = 40 + row  * gridCellSize;

                    drawGridSquare(g2, c, x, y);
                    repaint();
                }
            }
        }
    }

    public void drawFallenBlocks(Graphics g){ // Отрисовка упавших блоков.
        Graphics2D g2 = (Graphics2D)g;
        Color color;
        for (int r = 0; r < gridRows; r++){
            for(int c = 0; c < gridColumns; c++){
                color = fallenBlocks[r][c];

                if(color != null){ // Если цвет не пустой.
                    int x = c * gridCellSize; // Задаём координаты.
                    int y = r * gridCellSize;

                    drawGridSquare(g2, color, x, y); // Отрисовка фигур.
                }
            }
        }
    }


    public void drawGridSquare(Graphics2D g2, Color color, int x, int y){ // Отрисовка фигур.
        g2.setColor(color);
        g2.fillRect(x , y , gridCellSize, gridCellSize);
        g2.setColor(Color.black);
        g2.drawRect(x , y , gridCellSize, gridCellSize);
    }

    private void drawLabel(){ // Вывод текста.
        scoreDisplay.setBounds(500,150, 160, 120);
        levelDisplay.setBounds(500,200, 160, 120);
        Font font = new Font("Unispace", Font.BOLD, 20);
        scoreDisplay.setFont(font);
        levelDisplay.setFont(font);
        Color c1 = new Color(230,220,242);
        scoreDisplay.setForeground(c1);
        levelDisplay.setForeground(c1);
        add(scoreDisplay);
        add(levelDisplay);
        scoreDisplay.repaint();
        levelDisplay.repaint();
    }

    public void drawButton(){
        btnMainMenu.setBounds(500, 750, 160, 80);
        Font font = new Font("Unispace", Font.BOLD, 20);
        btnMainMenu.setFont(font);
        Color c1 = new Color(230,220,242);
        Color c2 = new Color(48,46,110);
        btnMainMenu.setForeground(c1);
        btnMainMenu.setBackground(c2);
        add(btnMainMenu);
        btnMainMenu.repaint();
        btnMainMenu.setFocusable(false);
    }

    public void updateScore(int score){ // Обновление данных.
        scoreDisplay.setText("Score: " + score);
    }

    public void updateLevel(int level){ // Обновление данных.
        levelDisplay.setText("Level: " + level);
    }

    @Override
    protected void paintComponent(Graphics g) { // Вывод изображения.
        Graphics2D g2 = (Graphics2D)g; // Улучшенная версия графических компонентов.
        ImageIcon backGround = new ImageIcon("images/tetrisBackground.jpg");
        g2.drawImage(backGround.getImage(), 0,0, null);
        for (int y = 0; y < gridRows + 1; y++) { // отрисовка сетки.
            for (int x = 1; x < gridColumns + 1; x++) {
                g2.drawRect(x * gridCellSize, y * gridCellSize, gridCellSize, gridCellSize);
            }
        }
        drawFallenBlocks(g2); // Отрисовка упавших фигур.
        drawBlock(g2); // Отрисовка фигуры.
        drawNextBlock(g2); // Отрисовка следующей фигуры.
        drawLabel();
        drawButton();
    }
}