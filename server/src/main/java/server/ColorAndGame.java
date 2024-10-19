package server;

import chess.ChessGame;

public record ColorAndGame(ChessGame.TeamColor color, String name){
}
