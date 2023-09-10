package tetris;



public class GameThread extends Thread{ // Игровой поток.
    private GameArea gameArea;

    private int score = 0; // Количество очков.
    public int level = 1; // Текущий уровень.
    private int lvlup = 0; // Прогресс поднятия уровня
    private int gameSpeed;
    private static boolean gameStart = false;

    public GameThread(GameArea gameArea){ // Конструктор для получения экземпляра.
        this.gameArea = gameArea;

        gameArea.updateScore(score);
        gameArea.updateLevel(level);
    }

    static void pauseBreak(){
        gameStart = true;
    }

    @Override
    public void run(){ // Запуск игры.

            while (true) {
                gameArea.spawnBlock(); // Вывод новой фигуры.
                gameSpeed = 500 / level;
                while (gameArea.moveBlockDown() == true) { // Пока фигура движется вниз, постоянное движение фигуры.
                    try {
                        Thread.sleep(gameSpeed); // Задержка между тиками.
                    }
                    catch (InterruptedException e) {
                        System.out.println("Поток прерван !!!");
                        return;
                    }
                }
                if (gameArea.isBlockOutside()) {
                    Main.gameOver(score);
                    break;
                }

                gameArea.moveBlockToFallenBlocks();
                switch (gameArea.clearLines()) {
                    case (1):
                        score += 100;
                        lvlup += 1;
                        break;
                    case (2):
                        score += 300;
                        lvlup += 2;
                        break;
                    case (3):
                        score += 700;
                        lvlup += 3;
                        break;
                    case (4):
                        score += 1500;
                        lvlup += 4;
                        break;
                }
                if (lvlup >= 8) {
                    level++;
                    int temp = lvlup - 8;
                    lvlup = temp;
                    gameArea.updateLevel(level);
                }
                gameArea.updateScore(score);
                gameArea.assignmentBlock();
            }
        }
    }

