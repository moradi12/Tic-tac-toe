package com.java_master.Controller.Dto;

import com.java_master.Model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;

}
