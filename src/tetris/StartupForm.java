package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartupForm extends JFrame{
    private JButton btnStart;
    private JButton btnQuite;
    private JButton btnLeaders;
    private JPanel startupPanel;
    private JLabel jlHeader;

    public StartupForm(){
        setContentPane(startupPanel);
        setTitle("Main menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width / 2 - 348, dimension.height / 2 - 460, 696, 919);
        setVisible(true);
        setResizable(false);
        btnStart.addActionListener(new ActionListener() { // Нажатие "Старт".
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.start();
            }
        });
        btnLeaders.addActionListener(new ActionListener() { // Нажатие "Таблица лидеров".
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.showLeaderboard();
            }
        });
        btnQuite.addActionListener(new ActionListener() { // Нажатие "Выход".
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new StartupForm();
    }
}
