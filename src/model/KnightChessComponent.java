package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnightChessComponent extends ChessComponent{
    private static Image KNIGHT_BLACK;
    private static Image KNIGHT_WHITE;
    private Image knightImage;

    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateKnightImage(color);
        if(color==ChessColor.WHITE){
            this.name = 'n';
        }
        else {
            this.name = 'N';
        }
    }

    @Override
    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        ArrayList<ChessboardPoint> canMoveTo = new ArrayList<>();
        int x = this.getChessboardPoint().getX();
        int y = this.getChessboardPoint().getY();
        ChessboardPoint temp = new ChessboardPoint(x,y);
        canMoveTo.add(temp.offset(2,1));
        canMoveTo.add(temp.offset(2,-1));
        canMoveTo.add(temp.offset(-2,1));
        canMoveTo.add(temp.offset(-2,-1));
        canMoveTo.add(temp.offset(1,2));
        canMoveTo.add(temp.offset(1,-2));
        canMoveTo.add(temp.offset(-1,2));
        canMoveTo.add(temp.offset(-1,-2));
        while (canMoveTo.contains(null)){
            canMoveTo.remove(null);
        }
        for(int i=0;i<canMoveTo.size();i++){
            if(chessboard[canMoveTo.get(i).getX()][canMoveTo.get(i).getY()].getChessColor()==this.getChessColor()){
                canMoveTo.remove(i);
                i--;
            }
        }
        return canMoveTo;

    }

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./images/knight-white.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./images/knight-black.png"));
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//     g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
