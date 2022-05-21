package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PawnChessComponent extends ChessComponent {

    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;

    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiatePawnImage(color);
        if (color == ChessColor.WHITE) {
            this.name = 'p';
        } else {
            this.name = 'P';
        }
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        if (ChessboardPoint.contain(moveTo(chessboard), destination)) {
            return true;
        } else {
            return false;
        }
    }

    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        List<ChessboardPoint> canMoveTo = new ArrayList<>();
        int a = 0;
        if (this.getChessColor() == ChessColor.WHITE) {
            a = -1;
        } else {
            a = 1;
        }
        if (this.getChessboardPoint().getX() + a >= 0 && this.getChessboardPoint().getX() + a <= 7 && chessboard[this.getChessboardPoint().getX() + a][this.getChessboardPoint().getY()].name == '_') {
            canMoveTo.add(this.getChessboardPoint().offset(a, 0));
            if (this.getChessboardPoint().getX() == 3.5 - a * 2.5 && chessboard[this.getChessboardPoint().getX() + a * 2][this.getChessboardPoint().getY()].name == '_') {
                canMoveTo.add(this.getChessboardPoint().offset(a * 2, 0));
            }
        }

        if (this.getChessboardPoint().getY() != 7) {
            if (this.getChessboardPoint().getX() + a >= 0 && this.getChessboardPoint().getX() + a <= 7 && chessboard[this.getChessboardPoint().getX() + a][this.getChessboardPoint().getY() + 1].name != '_' &&
                    chessboard[this.getChessboardPoint().getX() + a][this.getChessboardPoint().getY() + 1].getChessColor() != this.getChessColor()) {
                canMoveTo.add(this.getChessboardPoint().offset(a, 1));
            }
        }
        if (this.getChessboardPoint().getY() != 0) {
            if (this.getChessboardPoint().getX() + a >= 0 && this.getChessboardPoint().getX() + a <= 7 && chessboard[this.getChessboardPoint().getX() + a][this.getChessboardPoint().getY() - 1].name != '_' &&
                    chessboard[this.getChessboardPoint().getX() + a][this.getChessboardPoint().getY() - 1].getChessColor() != this.getChessColor()) {
                canMoveTo.add(this.getChessboardPoint().offset(a, -1));
            }
        }
//        if (symbol == 1  && specialPawnPoint.getX() >= 0 && specialPawnPoint.getX() < 8 && specialPawnPoint.getY() - a < 8 && specialPawnPoint.getY() - a >= 0) {
//            if (symbol == 1 && this.chessColor != chessboard[specialPawnPoint.getX()][specialPawnPoint.getY() - a].chessColor) {
            if(symbol==1){
                canMoveTo.add(specialPawnPoint);
        }
        while (canMoveTo.contains(null)) {
            canMoveTo.remove(null);
        }
        return canMoveTo;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//     g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(pawnImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }

}

