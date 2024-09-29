package com.java_master.Service;
import com.java_master.Excption.InvalidGameException;
import com.java_master.Excption.InvalidParamException;
import com.java_master.Excption.NotFoundIException;
import com.java_master.Model.Game;
import com.java_master.Model.GamePlay;
import com.java_master.Model.Player;
import com.java_master.Model.TicToe;
import com.java_master.Storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.java_master.Model.GameStatus.*;

@AllArgsConstructor
@Service
public class GameService {
    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setGameStatus(NEW);
        GameStorage.getInstance().setGames(game);


        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if (GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Game with this id doesn't exist");

        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) {
            throw new InvalidGameException("Game with this id already exists");
        }
        game.setPlayer2(player2);
        game.setGameStatus(IN_PROGRESS);
        GameStorage.getInstance().setGames(game);
        return game;
    }

    public Game connectToRandomGame(Player player2) throws NotFoundIException {
        Game game = GameStorage.getInstance().getGames().values().stream().filter(it -> it.getGameStatus().equals(NEW)).findFirst()
                .orElseThrow(() -> new NotFoundIException("Game not found"));
        game.setPlayer2(player2);
        game.setGameStatus(IN_PROGRESS);
        GameStorage.getInstance().setGames(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundIException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundIException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getGameStatus().equals(FINISHED)) {
            throw new NotFoundIException("Game finished");
        }

        int[][] board = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
        Boolean oWinner = checkWinner(game.getBoard(), TicToe.O);

        if (xWinner) {
            game.setWinner(TicToe.X);
        } else if (oWinner) {
            game.setWinner(TicToe.O);
        }

        GameStorage.getInstance().setGames(game);
        return game;
    }

    private boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArray = new int[9];
        int counterIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex] = board[i][j];
                counterIndex++;
            }
        }
        int[][] winCombinations = {
                {0, 1, 2}, // Row 1
                {3, 4, 5}, // Row 2
                {6, 7, 8}, // Row 3
                {0, 3, 6}, // Column 1
                {1, 4, 7}, // Column 2
                {2, 5, 8}, // Column 3
                {0, 4, 8}, // Diagonal from top-left to bottom-right
                {2, 4, 6}  // Diagonal from top-right to bottom-left
        };
        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                // Check if the board value at the winning combination index matches the player's value
                if (boardArray[winCombinations[i][j]] == ticToe.getValue()) {
                    counter++;
                }
            }
            // If all three values in the combination match, return true
            if (counter == 3) {
                return true;
            }
        }
// Return false if no winning combination is found
        return false;
    }}