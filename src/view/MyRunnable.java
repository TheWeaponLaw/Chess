package view;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MyRunnable implements Runnable {
    public ChessGameFrame chessGameFrame;
    public MyRunnable(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    @Override
    public void run() {
        for (int i = 0; i < chessGameFrame.getGameController().getChessboard().memory.size(); i++) {
            chessGameFrame.getGameController().getChessboard().loadGamePlus(chessGameFrame.getGameController().getChessboard().memory.get(i));
            chessGameFrame.repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
        chessGameFrame.warning("You are at the last step.");
        chessGameFrame.getGameController().getChessboard().initChess();
        chessGameFrame.getGameController().getChessboard().resetCurrentColor();
        chessGameFrame.refresh();
        chessGameFrame.repaint();
        chessGameFrame.getGameController().getChessboard().memory = new ArrayList<>();
        chessGameFrame.getGameController().getChessboard().multiplyKill=0;
    }
}
