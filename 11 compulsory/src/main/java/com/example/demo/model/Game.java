package com.example.demo.model;

public class Game {
    Player player1;
    Player player2;
    private boolean turn;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isTurn() {
        return turn;
    }
}