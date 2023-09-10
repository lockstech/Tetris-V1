package tetris;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;


public class LeadersForm extends JFrame {

    private static String[] headers = {"Player", "Score"};
    private static String[][] leaderboardTable = read2DArrayFromFile("leaderboard");

    static private JPanel jPanel = new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            Color c1 = new Color(48,46,110);
            Color c2 = new Color(54,44,51);
            Color c3 = new Color(230,220,242);

            Font font = new Font("Unispace", Font.BOLD, 30);
            Font font2 = new Font("Unispace", Font.BOLD, 20);
            g.setFont(font2);

            g.setColor(c2);
            g.fillRect(0,0,700,919);
            g.setColor(c1);
            g.fillRect(150,200,400,440);

            g.setColor(c3);
            for (int y = 0; y < 11; y++) { // отрисовка сетки таблицы.
                for (int x = 0; x < 2; x++) {
                    g.drawRect(x * 200 + 150, y * 40 + 200, 200, 40);
                    while(y < 10){
                        g.drawString(leaderboardTable[y][x], x*200+155, y*40 + 275);
                        break;
                    }
                }
            }

            g.setFont(font);
            g.drawString(headers[0], 200, 232);
            g.drawString(headers[1], 400, 232);
        }
    };
    private JLabel Header = new JLabel("Leaderboard");
    private JButton btnMainMenu = new JButton("Main menu");

    private TableRowSorter<TableModel> sorter;

    public static void write2DArrayToFile(String[][] array, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] row : array) {
                for (String value : row) {
                    writer.write(value);
                    writer.write(" ");
                }
                writer.newLine();
            }
            System.out.println("Двумерный массив успешно записан в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static String[][] read2DArrayFromFile(String filename) {
        String[][] array = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int rowCount = 0;
            int colCount = 0;

            // Подсчет количества строк и столбцов в файле
            while ((line = reader.readLine()) != null) {
                rowCount++;
                String[] values = line.split(" ");
                colCount = Math.max(colCount, values.length);
            }

            // Создание двумерного массива
            array = new String[rowCount][colCount];

            // Считывание данных из файла
            Scanner scanner = new Scanner(new FileReader(filename));
            for (int i = 0; i < rowCount; i++) {
                if (scanner.hasNextLine()) {
                    String[] values = scanner.nextLine().split(" ");
                    for (int j = 0; j < values.length; j++) {
                        array[i][j] = values[j];
                    }
                }
            }

            System.out.println("Двумерный массив успешно считан из файла.");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return array;
    }

    private static void sortLeaderboardTable(String[][] array){
        int rows = array.length;
        boolean swapped;

        for (int i = 0; i < rows - 1; i++) {
            swapped = false;

            for (int j = 0; j < rows - i - 1; j++) {
                if (Integer.parseInt(array[j][1]) < Integer.parseInt(array[j + 1][1])) {
                    // Меняем местами строки
                    String[] temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }

            // Если внутренний цикл не выполнил ни одной замены,
            // значит массив уже отсортирован
            if (!swapped) {
                break;
            }
        }
    }

    private void drawJPanel(){
        jPanel.setVisible(true);
        jPanel.setBounds(0, 0, 700, 919); // Размер окна 700 X 919; Координаты от ЛВ ширина экрана/2 - 348, высота экрана/2 -460.
    }

    private void drawJLabel(){
        Font font = new Font("Unispace", Font.BOLD, 48);
        Header.setFont(font);

        Color c1 = new Color(230,220,242);
        Header.setForeground(c1);

        Header.setBounds(180,100,600,50);
        jPanel.add(Header);
    }

    private void drawJButton(){
        btnMainMenu.setBounds(200, 750, 280, 50);
        Font font = new Font("Unispace", Font.BOLD, 20);
        btnMainMenu.setFont(font);
        Color c1 = new Color(230,220,242);
        Color c2 = new Color(48,46,110);
        btnMainMenu.setForeground(c1);
        btnMainMenu.setBackground(c2);

        jPanel.add(btnMainMenu);
    }

    static JFrame leadersFrame = new JFrame();

    public LeadersForm(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit(); // Класс для работы с приложениями.
        Dimension dimension = toolkit.getScreenSize(); // Получение размеров окна.
        leadersFrame.setBounds(dimension.width / 2 - 348, dimension.height / 2 - 460, 696, 919); // Размер окна 696 X 919; Координаты от ЛВ ширина экрана/2 - 348, высота экрана/2 -460.

        jPanel.setLayout(null);

        drawJLabel();
        drawJButton();
        drawJPanel();
        leadersFrame.add(jPanel);

        leadersFrame.setTitle("Leaderboard");
        leadersFrame.setResizable(false);

        btnMainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leadersFrame.setVisible(false);
                Main.showMainMenu();
            }
        });
    }

    public void addPlayer (String playerName, int score){
        leaderboardTable[10][0] = playerName;
        leaderboardTable[10][1] = Integer.toString(score);
        sortLeaderboardTable(leaderboardTable); // Сортировка.

        write2DArrayToFile(leaderboardTable, "leaderboard"); // Сохранение результатов в файл.

        leadersFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new LeadersForm();
    }
}
