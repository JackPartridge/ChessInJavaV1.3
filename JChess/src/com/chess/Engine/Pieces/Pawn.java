package com.chess.Engine.Pieces;

import com.chess.Engine.Alliance.Alliance;
import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.BoardUtilities;
import com.chess.Engine.Board.Move;
import com.chess.Engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { 8, 16, 7, 9 };

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {

        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {

        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection()
                                                                             * currentCandidateOffset);

            if (!BoardUtilities.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work to do here (deal with promotions)
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                       ((BoardUtilities.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                       (BoardUtilities.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))) {

                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }

            } else if (currentCandidateOffset == 7 &&
                       !(BoardUtilities.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                         BoardUtilities.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            } else if (currentCandidateOffset == 9 &&
                       !(BoardUtilities.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                         BoardUtilities.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {

                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());

    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
