package com.javarush.games.minesweeper;

public class GameObject{
    public int x;
    public int y;
    public boolean isMine;
    public int countMineNeighbors=0;
    public boolean isOpen=false;
    public boolean isFlag=false;

//    public GameObject(int x, int y, boolean isMine, int countMineNeighbors, boolean isOpen, boolean isFlag) {
//        this.x = x;
//        this.y = y;
//        this.isMine = isMine;
//        this.countMineNeighbors = countMineNeighbors;
//        this.isOpen = isOpen;
//        this.isFlag = isFlag;
//    }

    public GameObject(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine=isMine;
    }
}
