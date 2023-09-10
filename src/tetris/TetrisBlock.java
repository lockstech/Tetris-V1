package tetris;

import java.awt.*;
import java.util.Random;

public class TetrisBlock {
    private int[][] shape; // Форма фигуры.
    private Color color; // Цвет фигуры.
    private int x,y; // Координаты фигуры.
    private int[][][] shapes; // Поворотные формы фигур.
    private int currentRotation; // Номер поворотной формы.


    private GameThread gameThread;

    private Color[] availableColors = {Color.white, Color.red, Color.blue};

    public TetrisBlock(int[][] shape){ // Конструктор.
        this.shape = shape;

        initShapes();
    }


    public void initShapes(){ // Определение поворотной формы фигуры.
        shapes = new int[4][][];
        for (int i = 0; i < 4; i++){
            int r = shape[0].length; // Число столбцов.
            int c = shape.length; // Число строк.
            shapes[i] = new int[r][c];

            for (int y = 0; y < r; y++){
                for (int x = 0; x < c; x++){
                    shapes[i][y][x] = shape[c-x-1][y]; // Поворот фигуры по часовой на 90 градусов.
                }
            }
            shape = shapes[i]; // Выбираем поворотную форму фигуры.
        }
    }

    public void colorBlock(){
        Random r = new Random();

        currentRotation = r.nextInt(4);
        shape = shapes[currentRotation];
        color = availableColors[ r.nextInt( availableColors.length)];
    }

    public void coordinates(int gridWidth){ // Появление фигуры за границами поля.
        Random r = new Random();
        y = -getHeight();
        x = r.nextInt(gridWidth - getWidth() - 1); // Случайное положение по x
    }

    public int[][] getShape(){
        return shape;
    }
    public Color getColor(){
        return color;
    }

    public int getHeight(){
        return shape.length;
    }
    public int getWidth(){
        return shape[0].length;
    }
    public int getX(){ // Получение координат.
        return x + 1;
    }

    public void setX(int newX){
        x = newX;
    }
    public int getY(){
        return y + 1;
    }

    public void setY(int newY){
        y = newY;
    }

    public void moveDown(){ // Движение фигуры вниз.
        y++;
    }
    public void moveRight(){ // Движение фигуры вправо.
        x++;
        System.out.println("y = " + y);
        System.out.println("x = " + x);
    }
    public void moveLeft(){ // Движение фигуры влево.

        x--;
        System.out.println("y = " + y);
        System.out.println("x = " + x);
    }

    public void rotate(){ // Поворот фигуры.
        currentRotation++;
        if (currentRotation > 3) {
            currentRotation = 0;
        }
        shape = shapes[currentRotation];

    }

    public int getBottomEdge(){
        return y + getHeight();
    }

    public int getLeftEdge(){
        return x;
    }

    public int getRightEdge(){
        return x + getWidth();
    }
}
