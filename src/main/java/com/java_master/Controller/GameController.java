package com.java_master.Controller;

import com.java_master.Controller.Dto.ConnectRequest;
import com.java_master.Excption.InvalidGameException;
import com.java_master.Excption.InvalidParamException;
import com.java_master.Excption.NotFoundException;
import com.java_master.Excption.NotFoundIException;
import com.java_master.Model.Game;
import com.java_master.Model.GamePlay;
import com.java_master.Model.Player;
import com.java_master.Service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestBody Player player) {
        log.info("Starting game with player: " + player);
        return ResponseEntity.ok(gameService.createGame(player));

    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(@RequestBody ConnectRequest request) throws InvalidParamException, InvalidGameException {
        log.info("Connecting request: " + request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }


    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectToRandomGame(@RequestBody Player player) throws NotFoundIException {
        log.info("Connecting random game with player: " + player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));

    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException, NotFoundIException {
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }}

