package com.example.colisiongame.Logic;
import com.example.colisiongame.Model.Obstacle;
import com.example.colisiongame.Model.mainCharacter;

import java.util.HashSet;
import java.util.Set;


public class GameManager {

    private int life;
    private int numOfCollosions = 0;
    private int numberOfLanes = 3;
    private int numberOfRows = 8;

    private mainCharacter mainCharacter;
    private Set<Obstacle> obstacles;
    private int numberOfObstacles;
    private int maxNumberOfObstacles = 4;

    public GameManager() {

        life = 3;
        numberOfObstacles = 0;
        obstacles = new HashSet<>();
        mainCharacter = new mainCharacter();
    }

    public mainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public int getLife() {
        return life;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }

    public void addObstacle (Obstacle obstacle){
        if(numberOfObstacles < maxNumberOfObstacles ) {
            obstacles.add(obstacle);
        }
    }

    public Set<Obstacle> getObstacles() {
        return obstacles;
    }

    public GameManager setObstacles(Set<Obstacle> obstacles) {
        this.obstacles = obstacles;
        return this;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public GameManager setNumberOfLanes(int numberOfLanes) {
        this.numberOfLanes = numberOfLanes;
        return this;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumOfCollosions() {
        return numOfCollosions;
    }

    public GameManager setNumOfCollosions(int numOfCollosions) {
        this.numOfCollosions = numOfCollosions;
        return this;
    }

    public void removeObstacle (Obstacle obstacle){
        obstacles.remove(obstacle);
    }

    public boolean checkCollosionInGameManager(Obstacle obstacle){
        if(this.mainCharacter.getPositionX() == obstacle.getPositionX()){
            this.numOfCollosions++;
            return true;
        }

        return false;
    }

}





