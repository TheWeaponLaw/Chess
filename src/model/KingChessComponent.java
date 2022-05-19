package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KingChessComponent extends ChessComponent {

    private static Image KING_WHITE;
    private static Image KING_BLACK;

    private Image kingImage;

    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./images/king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKingImage(color);
        if (color == ChessColor.WHITE) {
            this.name = 'k';
        } else {
            this.name = 'K';
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
        int x = this.getChessboardPoint().getX();
        int y = this.getChessboardPoint().getY();
        ChessboardPoint temp = new ChessboardPoint(x, y);
        canMoveTo.add(temp.offset(1, 0));
        canMoveTo.add(temp.offset(1, 1));
        canMoveTo.add(temp.offset(1, -1));
        canMoveTo.add(temp.offset(0, 1));
        canMoveTo.add(temp.offset(0, -1));
        canMoveTo.add(temp.offset(-1, 1));
        canMoveTo.add(temp.offset(-1, 0));
        canMoveTo.add(temp.offset(-1, -1));
        while (canMoveTo.contains(null)) {
            canMoveTo.remove(null);
        }
        for (int i = 0; i < canMoveTo.size(); i++) {
            if (chessboard[canMoveTo.get(i).getX()][canMoveTo.get(i).getY()].getChessColor() == this.getChessColor()) {
                canMoveTo.remove(i);
                i--;
            }
        }
        return canMoveTo;

    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//     g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
}
