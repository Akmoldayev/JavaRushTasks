package com.javarush.games.minesweeper;

public class GameObject{
    public final int x;
    public final int y;
    public final boolean isMine;
    public int countMineNeighbors=0;
    public boolean isOpen=false;
    public boolean isFlag=false;
    public boolean isClicked=false;

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
