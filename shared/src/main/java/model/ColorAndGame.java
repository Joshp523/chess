package model;

import chess.ChessGame;

public record ColorAndGame(ChessGame.TeamColor playerColor, int gameID){
}
