package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static tetris.GameThread.pauseBreak;
import static tetris.Main.gf;

public class GameForm extends JFrame implements KeyListener {

        static GameArea gameArea;
        static GameThread gt;

        public GameForm(){
            gameArea = new GameArea();
            this.add(gameArea);
        }

    static JFrame getFrame() {
        JFrame jFrame = new JFrame("Tetris V1") {};
        jFrame.setResizable(false);
        jFrame.addKeyListener(gf);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Операция закрытия окна.
        Toolkit toolkit = Toolkit.getDefaultToolkit(); // Класс для работы с приложениями.
        Dimension dimension = toolkit.getScreenSize(); // Получение размеров окна.
        jFrame.setBounds(dimension.width / 2 - 350, dimension.height / 2 - 460, 700, 919); // Размер окна 696 X 919; Координаты от ЛВ ширина экрана/2 - 348, высота экрана/2 -460.
        return jFrame;
    }
    public void startGame(GameArea gameArea) {
        gameArea.initBackgroundArray();
        gt = new GameThread(gameArea);
        gt.start();
    }

    static JFrame gameThreadFrame = gf.getFrame();
    public static void startGameForm() {
        JLabel label = new JLabel("SCORE: 999");
        //JFrame gameThreadFrame = gf.getFrame();
        gameThreadFrame.setVisible(true);
        gameThreadFrame.add(label);
        gameThreadFrame.add(gameArea); // Запуск игровой зоны.
        gameThreadFrame.removeKeyListener(gf); // При повторном создании потока необходимо удалить предыдущий keyListener. :)
        gameThreadFrame.addKeyListener(gf); // Добавление управления.
        gf.startGame(gameArea); // Запуск игры.
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) { // Управление
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){ // Перемещение фигуры вправо.

            System.out.println("Вправо");
            gameArea.moveBlockRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){ // Перемещение фигуры влево.
            System.out.println("Влево");
            gameArea.moveBlockLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){ // Поворот фигуры на 90 градусов.
            System.out.println("Вверх");
            gameArea.rotateBlock();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){ // Резкое падение фигуры вниз.
            System.out.println("Вниз");
            gameArea.dropBlockDown();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){ // Резкое падение фигуры вниз.
            pauseBreak();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
