package com.java_master.Model;

import lombok.Data;

@Data
public class Game {

    private String gameId;
    private Player player1;
    private Player player2;
    private GameStatus gameStatus;
    private int[][] board;
    private TicToe winner;

}
