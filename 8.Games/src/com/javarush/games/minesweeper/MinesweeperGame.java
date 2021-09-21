package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final String  MINE = "\uD83D\uDCA3";
    private static final String FLAG="\uD83D\uDEA9";
    private static final int SIDE = 20;
    private final GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countFlags;
    private boolean isGameStopped=false;
    private int countClosedTiles=SIDE*SIDE;
    private int score;
    @Override
    public void initialize() {//---------------------------------------------------------------
        setScreenSize(SIDE, SIDE);
        createGame();
        setScore(score);
        showGrid(true);
        System.out.println(countMinesOnField);
    }//----------------------------------------------------------------------------------------
    private  void openTile(int x, int y){
        GameObject object = gameField[x][y];
        if (!gameField[x][y].isFlag && !isGameStopped) {
            countClosedTiles--;
            object.isOpen = true;
            if (object.isMine) {
                setCellValueEx(x, y, Color.RED, MINE, Color.BLACK);
                gameOver();
            }
            if (!object.isMine) {
                if (!object.isClicked) {
                    score += 5;
                    if (!object.isOpen) {
                        setScore(score);
                    }
                    object.isClicked = true;
                }
                if (object.countMineNeighbors > 0) {
                    setCellNumber(x, y, object.countMineNeighbors);
                    setCellColor(x, y, Color.HOTPINK);
                }
                if (object.countMineNeighbors == 0) {
                    setCellValue(x, y, "");
                    for (GameObject g : getNeighbors(object)) {
                        if (!g.isOpen) {
                            g.isOpen = true;
                            openTile(g.y, g.x);
                        }
                    }
                    setCellColor(x, y, Color.HOTPINK);
                }
            }
            if (countMinesOnField==countClosedTiles&& !isGameStopped){
                win();
            }
        } else if (object.isFlag){
        }
    }
    private void createGame() {
        if (!isGameStopped) {
            for (int y = 0; y < SIDE; y++) {
                for (int x = 0; x < SIDE; x++) {
                    setCellValue(x,y,"");
                    boolean isMine = getRandomNumber(10) < 1;
                    if (isMine) {
                        countMinesOnField++;
                    }
                    gameField[y][x] = new GameObject(x, y, isMine);
                    setCellColor(x, y, Color.ORANGE);
                }
            }
            countFlags = countMinesOnField;
            countMineNeighbors();
        }
    }
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
    private void countMineNeighbors() {
        List<GameObject> list ;//= new ArrayList<>();
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (!gameField[y][x].isMine) {
                    list = getNeighbors(gameField[y][x]);
                    for (GameObject object : list) {
                        if (object.isMine) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
        }
        else{
            openTile(x, y);
        }
    }
    public void onMouseRightClick(int x, int y){
        markTile(x,y);
    }
    private void markTile(int x, int y){
        if (!gameField[x][y].isOpen) {//do not remove
            if (!gameField[x][y].isFlag &&countFlags==0|| isGameStopped){
                return;
            }
            gameField[x][y].isFlag = !gameField[x][y].isFlag;
            if (gameField[x][y].isFlag) {
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.BLUE);
                setScore(score);
            }
            if (!gameField[x][y].isFlag) {
                countFlags++;
                setCellValue(x, y, "");
                setScore(score);
                setCellColor(x, y, Color.ORANGE);
            }

        } else {
        }
    }
    private void gameOver(){
        showMessageDialog(Color.ALICEBLUE,"Game Over",Color.BLACK,50);
        isGameStopped=true;
    }
    private void win() {
        isGameStopped=true;
        showMessageDialog(Color.ALICEBLUE,"YOU WIN, YAHOOOO!",Color.GREEN,50);
    }
    private void restart(){
        isGameStopped=false;
        score=0;
        setScore(score);
        countClosedTiles=SIDE*SIDE;
        countMinesOnField=0;
        /*установка исходных значений полей countClosedTiles, score и countMinesOnField.*/
        createGame();
    }
}