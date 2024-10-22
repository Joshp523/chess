package server;

import chess.ChessGame;

public record ColorAndGame(ChessGame.TeamColor playerColor, int gameID){
}
