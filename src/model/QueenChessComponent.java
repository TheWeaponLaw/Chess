package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueenChessComponent extends ChessComponent{
    private static Image QUEEN_BLACK;
    private static Image QUEEN_WHITE;
    private Image queenImage;

    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/queen-black.png"));
        }
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateQueenImage(color);
        if(color==ChessColor.WHITE){
            this.name = 'q';
        }
        else {
            this.name = 'Q';
        }
    }

    @Override
    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        List<ChessboardPoint> all = new ArrayList<>();
        all.addAll(canMoveToPartR(1, 0,chessboard));
        all.addAll(canMoveToPartR(-1, 0,chessboard));
        all.addAll(canMoveToPartR(0, 1,chessboard));
        all.addAll(canMoveToPartR(0, -1,chessboard));
        all.addAll(canMoveToPart(-1,-1,chessboard));
        all.addAll(canMoveToPart(1,1,chessboard));
        all.addAll(canMoveToPart(-1,1,chessboard));
        all.addAll(canMoveToPart(1,-1,chessboard));
        return all;
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        if (ChessboardPoint.contain(moveTo(chessboard), destination)) {
            return true;
        } else {
            return false;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//     g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }

    public List<ChessboardPoint> canMoveToPartR(int a, int b,ChessComponent[][] chessboard) {
        List<ChessboardPoint> canMoveTo = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            int xOriginal = this.getChessboardPoint().getX();
            int yOriginal = this.getChessboardPoint().getY();
            int x = xOriginal + a * i;
            int y = yOriginal + b * i;
            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }
            if (chessboard[x][y].name != '_') {
                if (chessboard[x][y].getChessColor() == this.getChessColor()) {
                    break;
                }
                if (chessboard[x][y].getChessColor() != this.getChessColor()) {
                    canMoveTo.add(this.getChessboardPoint().offset(a * i, b * i));
                    break;
                }
            } else {
                canMoveTo.add(this.getChessboardPoint().offset(a * i, b * i));
            }
        }
        while (canMoveTo.contains(null)) {
            canMoveTo.remove(null);
        }
        return canMoveTo;
    }
    public List<ChessboardPoint> canMoveToPart(int a, int b,ChessComponent[][] chessboard) {
        List<ChessboardPoint> canMoveTo = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            int xOriginal = this.getChessboardPoint().getX();
            int yOriginal = this.getChessboardPoint().getY();
            int x = xOriginal + a * i;
            int y = yOriginal + b * i;
            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }
            if (chessboard[x][y].name != '_') {
                if (chessboard[x][y].getChessColor() == this.getChessColor()) {
                    break;
                } else {
                    canMoveTo.add(this.getChessboardPoint().offset(a * i, b * i));
                    break;
                }
            } else {
                canMoveTo.add(this.getChessboardPoint().offset(a * i, b * i));
            }
        }
        while (canMoveTo.contains(null)) {
            canMoveTo.remove(null);
        }
        return canMoveTo;
    }
}
