package com.example.colisiongame.Model;

public class Character {

    private int positionX;
    private int positionY;


    public Character(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public Character setPositionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public int getPositionY() {
        return positionY;
    }

    public Character setPositionY(int positionY) {
        this.positionY = positionY;
        return this;
    }
}
