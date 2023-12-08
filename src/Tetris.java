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
    final int FIELD_DX = 7;
    final int FIELD_DY = 26;
    final int LEFT = 37;
    final int UP = 38;
    final int RIGHT = 39;
    final int DOWN = 40;
    final int SHOW_DELAY = 350; // определяет задержку анимации

    final int[][][] SHAPES = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {4, 0x00f0f0}}, // I
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {4, 0xf0f000}}, // O
            {{1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x0000f0}}, // J
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf0a000}}, // L
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0x00f000}}, // S
            {{1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xa000f0}}, // T
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0xf00000}} // Z
    };
    final int[] SCORES = {100, 300, 700, 1500}; // количество очков за заполнение строк в игре.
    int gameScores = 0; // для хранение очков
    int[][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH]; // игравое поле

    JFrame frame;
    Canvas canvas = new Canvas();
    Random random = new Random();
    Figure figure = new Figure();
    boolean gameOver = false;

    public static void main(String[] args) {
        new Tetris().go();
    }

    void go() {

    }
}