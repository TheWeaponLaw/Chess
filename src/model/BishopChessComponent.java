package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BishopChessComponent extends ChessComponent {
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private Image bishopImage;

    @Override
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }

    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateBishopImage(color);
        if (getChessColor() == ChessColor.WHITE) {
            this.name = 'b';
        } else {
            this.name = 'B';
        }
    }

    @Override
    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        List<ChessboardPoint> all = new ArrayList<>();
        all.addAll(canMoveToPart(-1, -1, chessboard));
        all.addAll(canMoveToPart(1, 1, chessboard));
        all.addAll(canMoveToPart(-1, 1, chessboard));
        all.addAll(canMoveToPart(1, -1, chessboard));
        return all;

    }

    public List<ChessboardPoint> canMoveToPart(int a, int b, ChessComponent[][] chessboard) {
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

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                bishopImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                bishopImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        if (ChessboardPoint.contain(moveTo(chessboard), destination)) {
            return true;
        }
        return false;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bishopImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
}
