package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

import java.util.Arrays;
import java.util.Random;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField  = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private boolean isWinnerFound = false;
    private int score = 0;

    private static int EMPTY_SCREEN_COLOR = 15;
    private static int CELL2_COLOR = 4;

    @Override
    public void initialize() {
        System.out.println("The game started");
        createGame();
        //  задали размер поля 4x4 клеток
        setScreenSize(SIDE, SIDE);
        drawScene();
    }

    private void createGame() {
        System.out.println("The creating the field");
        score = 0;
        setScore(score);
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                gameField[i][j] = 0;
            }
        }
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
//        System.out.println("Drawing the scene");

        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
        /*int[] rows = new int[SIDE];
        for (int i = 0; i < SIDE; ++i) {
            rows[i] = new Random().nextInt(2);
        }*/
//        int[] rows = new int[]{4, 0, 4, 0};

        /*System.out.println(Arrays.toString(rows));
        boolean res = mergeRow(rows);
        System.out.println(Arrays.toString(rows));
        System.out.println(res);*/
        /*for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                gameField[i][j] = new Random().nextInt(10);
            }
        }
        for (int i = 0; i < SIDE; ++i) {
            System.out.println(Arrays.toString(gameField[i]));
        }
        System.out.println();
        System.out.println(gameField[1][2]);
        System.out.println(gameField[0][3]);
        rotateClockwise();
        System.out.println();
        for (int i = 0; i < SIDE; ++i) {
            System.out.println(Arrays.toString(gameField[i]));
        }*/
    }
    
    private void win() {
        isGameStopped = true;
        isWinnerFound = true;
        showMessageDialog(Color.WHITE, "Сіз жеңдіңіз", Color.GREEN, 10);
    }
    
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "Ойын бітті", Color.GREEN, 10);
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
            } else if (key == Key.ENTER)  {
                isGameStopped = false;
            }
            return;
        }
        if (!canUserMove()) {
            gameOver();
            return;
        }
        switch (key) {
            case LEFT: {
                moveLeft();
                drawScene();
                break;
            }
            case RIGHT: {
                moveRight();
                drawScene();
                break;
            }
            case UP : {
                moveUp();
                drawScene();
                break;
            }
            case DOWN : {
                moveDown();
                drawScene();
                break;
            }
            default :
                break;
        }
    }

    private void moveLeft() {
        boolean isChanged = false;
        for (int i = 0; i < SIDE; ++i) {
            if (compressRow(gameField[i])) isChanged = true;
            if (mergeRow(gameField[i])) {
                isChanged = true;
                compressRow(gameField[i]);
            }
        }
        if (isChanged) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private boolean canUserMove() {
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                if (gameField[i][j] == 0) return true;;
            }
        }
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                if (i + 1 < SIDE && gameField[i][j] == gameField[i+1][j]) return true;
                if (i - 1 >= 0 && gameField[i][j] == gameField[i-1][j]) return true;
                if (j + 1 < SIDE && gameField[i][j] == gameField[i][j+1]) return true;
                if (j - 1 >= 0 && gameField[i][j] == gameField[i][j-1]) return true;
            }
        }
        return false;
    }
    
    private int getMaxTileValue() {
        int maxValue = 0;
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                maxValue = Math.max(maxValue, gameField[i][j]);
            }
        }
        return maxValue;
    }

    private void rotateClockwise() {
        int[][] tmp = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                tmp[i][j] = gameField[i][j];
            }
        }
        for (int i = 0; i < SIDE; ++i) {
            for (int j = 0; j < SIDE; ++j) {
                gameField[i][j] = tmp[SIDE - 1 - j][i];
            }
        }
    }

    private boolean mergeRow(int[] row) {
        int length = row.length;
        boolean result = false;
        int left = 0, right = 0;
        while (left < length-1) {
            if (row[left] > 0 && row[left] == row[left + 1]) {
                row[left] *= 2;
                row[left + 1] = 0;
                result = true;
                score += row[left];
                ++left;
                setScore(score);
            }
            ++left;
        }
        return result;
    }

    private boolean compressRow(int[] row) {
        int length = row.length;
        int left = 0, right = 0;
        boolean result = false;
        while (left < length && right < length) {
            if (left < length && row[left] != 0) {
                while (left < length && row[left] != 0) ++left;
            }
            right = left;
            if (right < length && row[right] == 0) {
                while (right < length && row[right] == 0) ++right;
            }
//            System.out.println(left + " " + right);
            if (left < length && right < length && left < right) {
                row[left] = row[right];
                row[right] = 0;
                result = true;
            }
        }
        return result;
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048 && !isWinnerFound) {
            win();
        }
        int x = 0, y = 0;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);
        int chooseValue = getRandomNumber(10);
        if (chooseValue == 9) gameField[x][y] = 4;
        else gameField[x][y] = 2;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value != 0) {
            setCellValueEx(x, y, color, String.valueOf(value));
        } else {
            setCellValueEx(x, y, color, "");
        }
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0:
                return Color.WHITE;
            case 2:
                return Color.BLUE;
            case 4:
                return Color.ORANGE;
            case 8:
                return Color.BROWN;
            case 16:
                return Color.RED;
            case 32:
                return Color.YELLOW;
            case 64:
                return Color.GRAY;
            case 128:
                return Color.VIOLET;
            case 256:
                return Color.PINK;
            case 512:
                return Color.BEIGE;
            case 1024:
                return Color.PURPLE;
            case 2048:
                return Color.GREEN;
        }
        return Color.WHITE;
    }
}