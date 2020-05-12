package com.chess;

import com.chess.Engine.Board.Board;
import com.chess.GUI.Table;

public class JavaChess {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();

        System.out.println(board);
        Table table = new Table();
    }
}
