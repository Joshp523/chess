package model;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.Serializable;

public record BoardAndMessage(ChessBoard board, String message, ChessGame.TeamColor color) implements Serializable {

}
