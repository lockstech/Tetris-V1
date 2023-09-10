package tetris;

import javax.swing.*;

import static tetris.GameForm.gameThreadFrame;
import static tetris.LeadersForm.leadersFrame;

public class Main {

    static GameForm gf;
    private static StartupForm sf;
    private static LeadersForm lf;

    public static void start(){ // Старт игры.
        gf.startGameForm();
    }

    public static void showLeaderboard(){
        leadersFrame.setVisible(true);
    }

    public static void showMainMenu(){
        sf.setVisible(true);
    }

    public static void gameOver(int score){ // Конец игры.
        int maxLength = 15; // Максимальная длина имени.
        String playerName = JOptionPane.showInputDialog("GAME OVER!\nPlease enter your name."); // Вызов диалога.
        if (playerName.isEmpty()){ // Если не ввели имя.
            playerName = "Anonim";
        }
        if (playerName.length() > maxLength) {
            playerName = playerName.substring(0, maxLength); // Обрезание имени до макс длины.
        }

        gameThreadFrame.setVisible(false);
        lf.addPlayer(playerName, score);
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() { // Отдельный поток.
            @Override
            public void run() {
                gf = new GameForm();
                sf = new StartupForm();
                lf = new LeadersForm();

                sf.setVisible(true);
            }
        });
    }
}