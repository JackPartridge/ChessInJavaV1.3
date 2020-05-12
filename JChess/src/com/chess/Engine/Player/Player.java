package com.chess.Engine.Player;

import com.chess.Engine.Alliance.Alliance;
import com.chess.Engine.Pieces.King;
import com.chess.Engine.Pieces.Piece;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.Move;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,
                                                                calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calcAttacksOnTile(this.playerKing.getPiecePosition(),
                                                   opponentMoves).isEmpty();
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    protected static Collection<Move> calcAttacksOnTile(int piecePosition,
                                                        Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here. Invalid board");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(legalMoves);
    }

    //all end game possibilities
    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    //method to see if king is able to move at any given time.
    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);

            if (transition.getMoveStatusCheck().isDone()) {
                return true;
            }
        }
        return false;
    }

    //
    //Game strategy possibilities
    public boolean isCastled() {
        return false;
    }

    //
    public MoveTransition makeMove(final Move move) {

        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatusCheck.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calcAttacksOnTile(transitionBoard.currentPlayer()
                                                                              .getOpponent().getPlayerKing().getPiecePosition(),
                                                                      transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatusCheck.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatusCheck.DONE);
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move> opponentsLegals);

}
