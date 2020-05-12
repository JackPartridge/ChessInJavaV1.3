package com.chess.Engine.Player;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Move;

public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatusCheck moveStatusCheck;

    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatusCheck moveStatusCheck) {

        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatusCheck = moveStatusCheck;
    }

    public MoveStatusCheck getMoveStatusCheck() {
        return this.moveStatusCheck;
    }

    public Board getTransitionBoard(){
        return this.transitionBoard;
    }

}
