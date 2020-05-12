package com.chess.Engine.Alliance;

import com.chess.Engine.Player.BlackPlayer;
import com.chess.Engine.Player.Player;
import com.chess.Engine.Player.WhitePlayer;

public enum Alliance {

    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();

    public abstract boolean isWhite();

    public abstract boolean isBlack();

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);
}
