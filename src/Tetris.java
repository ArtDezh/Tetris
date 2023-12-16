import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Tetris {

    final String TITLE_OF_PROGRAM = "Tetris";
    final int BLOCK_SIZE = 25; //размер блока, из которых состоит фигура
    final int ARC_RADIUS = 6; // величина закругления углов фигурки
    final int FIELD_WIDTH = 10; // ширина поля (в блоках)
    final int FIELD_HEIGHT = 18; // высота поля (в блоках)
    final int START_LOCATION = 180; // координата, где появляется окно программы
    final int FIELD_DX = 7; // для корректной установки границ игрового поля
    final int FIELD_DY = 37;// для корректной установки границ игрового поля
    final int LEFT = 37;
    final int UP = 38;
    final int RIGHT = 39;
    final int DOWN = 40;
    final int SHOW_DELAY = 400; // определяет задержку анимации

    // Массив фигур. Каждая буква лат. алфавита отображает схожую фигуру.
    final int[][][] SHAPES = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {4, 0x00f0f0}}, // I
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {4, 0xf0f000}}, // O
            {{1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x0000f0}}, // J
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf0a000}}, // L
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x00f000}}, // S
            {{1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xa000f0}}, // T
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf00000}}  // Z
    };
    final int[] SCORES = {100, 300, 700, 1500}; // количество очков за заполнение строк в игре.
    int gameScores = 0; // для хранение очков
    int[][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH]; // игравое поле
    JFrame frame;
    Canvas canvas = new Canvas();
    Random random = new Random();
    Figure figure = new Figure();
    boolean gameOver = false;
    // Отображение фразы "GameOver"
    final int[][] GAME_OVER = {
            {0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0},
            {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0},
            {0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0},
    };

    public static void main(String[] args) {
        new Tetris().go();
    }

    public Tetris() {
        frame = new JFrame(TITLE_OF_PROGRAM);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FIELD_WIDTH * BLOCK_SIZE + FIELD_DX, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY);
        frame.setLocation(START_LOCATION, START_LOCATION);
        frame.setResizable(false);
        canvas.setBackground(Color.BLACK);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == DOWN) figure.drop();
                    if (e.getKeyCode() == UP) figure.rotate();
                    if (e.getKeyCode() == LEFT || e.getKeyCode() == RIGHT)
                        figure.move(e.getKeyCode());
                }
                canvas.repaint();
            }
        });
        frame.getContentPane().add(BorderLayout.CENTER, canvas);
        frame.setVisible(true);

        Arrays.fill(mine[FIELD_HEIGHT], 1); // Нижняя часть игрового поля, на которой располаются первые фигурки
    }

    void go() {
        while (!gameOver) {
            try {
                Thread.sleep(SHOW_DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvas.repaint();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                checkFilling();
                figure = new Figure();
                gameOver = figure.isCrossGround();
            } else figure.stepDown();
        }
    }

    // проверка на заполнение строк и начисление очков
    void checkFilling() {
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++) {
                filled *= Integer.signum(mine[row][col]);
            }
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(mine[i - 1], 0, mine[i], 0, FIELD_WIDTH);
            } else {
                row--;
            }
        }

        if (countFillRows > 0) {
            gameScores += SCORES[countFillRows - 1];
            frame.setTitle(TITLE_OF_PROGRAM + " : " + gameScores);
        }
    }

    // Отрисовка содержимого
    class Canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < FIELD_WIDTH; x++) {
                for (int y = 0; y < FIELD_HEIGHT; y++) {
                    if (mine[y][x] > 0) {
                        g.setColor(new Color(mine[y][x]));
                        g.fill3DRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1, true);
                    }
                }
            }
            if (gameOver) {
                g.setColor(Color.WHITE);
                for (int y = 0; y < GAME_OVER.length; y++) {
                    for (int x = 0; x < GAME_OVER[y].length; x++) {
                        if (GAME_OVER[y][x] == 1) g.fill3DRect(x * 11 + 10, y * 11 + 160, 10, 10, true);
                    }
                }
            } else figure.paint(g);
        }
    }

    class Figure {
        private ArrayList<Block> figure = new ArrayList<>(); // Список фигур
        private int[][] shape = new int[4][4]; // фигура размером 4х4
        private int type, size, color; // тип, размер. цвет фигуры соответственно
        private int x = 3, y = 0; // координаты фигуры

        public Figure() {
            type = random.nextInt(SHAPES.length);
            size = SHAPES[type][4][0];
            color = SHAPES[type][4][1];
            if (size == 4) y = -1;
            for (int i = 0; i < size; i++)
                System.arraycopy(SHAPES[type][i], 0, shape[i], 0, SHAPES[type][i].length);
            createFromShape();
        }

        private void createFromShape() {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (shape[y][x] == 1) figure.add(new Block(x + this.x, y + this.y));
                }
            }
        }

        // Метод вращения фигуры
        public void rotate() {
            for (int i = 0; i < size / 2; i++) {
                for (int j = i; j < size - 1 - i; j++) {
                    int tmp = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[i][j];
                    shape[i][j] = tmp;
                }
            }

            if (!isWrongPosition()) {
                figure.clear();
                createFromShape();
            }
        }

        // Метод не дает вылезть за пределы игравого поля, накладываться на фигуры при вращении
        public boolean isWrongPosition() {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (shape[y][x] == 1) {
                        if (y + this.y < 0) return true;
                        if (x + this.x < 0 || x + this.x > FIELD_WIDTH - 1) return true;
                        if (mine[y + this.y][x + this.x] > 0) return true;
                    }
                }
            }
            return false;
        }

        // Метод опускания фигуры
        public void drop() {
            while (!isTouchGround()) stepDown();
        }

        // Метод движения фигуры (фигура падает вниз)
        public void move(int direction) {
            if (!isTouchWall(direction)) {
                int dx = direction - 38;
                for (Block block : figure) block.setX(block.getX() + dx);
                x += dx;
            }
        }

        // Метод проверки касания стенки игрового поля фигурой
        public boolean isTouchWall(int direction) {
            for (Block block : figure) {
                if (direction == LEFT && (block.getX() == 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
                if (direction == RIGHT && (block.getX() == FIELD_WIDTH - 1 || mine[block.getY()][block.getX() + 1] > 0))
                    return true;
            }
            return false;
        }

        // Метод проверяет, касается ли фигура нижней части игрового поля или другой фигуры
        public boolean isTouchGround() {
            for (Block block : figure) if (mine[block.getY() + 1][block.getX()] > 0) return true;
            return false;
        }

        // Метод оставляет фигуру на месте, куда она упала
        public void leaveOnTheGround() {
            for (Block block : figure) mine[block.getY()][block.getX()] = color;
        }

        // Метод прверяет, на переполнение нашего игрового поля
        public boolean isCrossGround() {
            for (Block block : figure) if (mine[block.getY()][block.getX()] > 0) return true;
            return false;
        }

        // Метод опускает фигуру на один уровень
        public void stepDown() {
            for (Block block : figure) block.setY(block.getY() + 1);
            y++;
        }

        // Метод отрисовки фигуры на полотне
        public void paint(Graphics g) {
            for (Block block : figure) block.paint(g, color);
        }
    }

    class Block {
        private int x, y;

        public Block(int x, int y) {
            setX(x);
            setY(y);
        }

        // Метод отрисовки блока на полотоне
        public void paint(Graphics g, int color) {
            g.setColor(new Color(color));
            g.drawRoundRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2, ARC_RADIUS, ARC_RADIUS);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}