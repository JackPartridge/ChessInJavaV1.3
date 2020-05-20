package com.chess.GUI;

import com.chess.Engine.Board.Board;
import com.chess.Engine.Board.BoardUtilities;
import com.chess.Engine.Board.Move;
import com.chess.Engine.Board.Tile;
import com.chess.Engine.Pieces.Piece;
import com.chess.Engine.Player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.*;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(594, 640);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static final String defaultPieceImagePath = "Images/Pieces/";

    private final Color lightTileColour = Color.decode("#efe4b0");
    private final Color darkTileColour = Color.decode("#585858");

    public Table() {

        this.gameFrame = new JFrame("Java Chess");

        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setResizable(false);
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }


    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel {

        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {

        private final int tileID;

        TilePanel(final BoardPanel boardPanel,
                  final int tileID) {

            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceImage(chessBoard);

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(final MouseEvent event) {

                    if (isRightMouseButton(event)) {
                        sourceTile = null;
                        humanMovedPiece = null;
                        destinationTile = null;
                    } else if (isLeftMouseButton(event)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                             }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(),
                                                                          destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatusCheck().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            validate();
        }

        public void drawTile(final Board board) {
            assignTileColour();
            assignTilePieceImage(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        private void assignTilePieceImage(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileID).isTileOccupied()) {
                try {
                    final BufferedImage image =
                            ImageIO.read(new File(defaultPieceImagePath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1) +
                                                  board.getTile(this.tileID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegalMoves(final Board board) {
            if (true) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileID) {
                        try {
                            add(new JLabel((new ImageIcon((ImageIO.read(new File("Images/Other/red_dot_legal.png")))))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() ==
                                           board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }


        private void assignTileColour() {
            if (BoardUtilities.FIRST_ROW[this.tileID] ||
                BoardUtilities.THIRD_ROW[this.tileID] ||
                BoardUtilities.FIFTH_ROW[this.tileID] ||
                BoardUtilities.SEVENTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTileColour : darkTileColour);
            } else if (BoardUtilities.SECOND_ROW[this.tileID] ||
                       BoardUtilities.FOURTH_ROW[this.tileID] ||
                       BoardUtilities.SIXTH_ROW[this.tileID] ||
                       BoardUtilities.EIGHTH_ROW[this.tileID]) {
                setBackground(this.tileID % 2 != 0 ? lightTileColour : darkTileColour);
            }

        }

    }
}
