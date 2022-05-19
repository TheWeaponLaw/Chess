package view;

import java.util.List;

/**
 * 这个类表示棋盘上的位置，如(0, 0), (0, 7)等等
 * 其中，左上角是(0, 0)，左下角是(7, 0)，右上角是(0, 7)，右下角是(7, 7)
 */
public class ChessboardPoint {
    private int x, y;

    public ChessboardPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ") " + "on the chessboard is clicked!";
    }
    public static boolean contain(List<ChessboardPoint> canMoveTo,ChessboardPoint chessboardPoint2) {
       for(int i=0;i<canMoveTo.size();i++){
           if(canMoveTo.get(i).getX()==chessboardPoint2.getX()&&canMoveTo.get(i).getY()==chessboardPoint2.getY()){
               return true;
           }
       }
       return false;
    }

    public ChessboardPoint offset(int dx, int dy) {
        if (this.x + dx > 7 || this.y + dy > 7 || this.x + dx < 0 || this.y + dy < 0) {
            return null;
        } else {
            return new ChessboardPoint(this.x + dx, this.y + dy);
        }
    }

    public boolean equals(ChessboardPoint a){
        if(a!=null){
        if(a.getX()== this.getX()&&a.getY()== this.getY()) {
            return true;
        }
        else {
            return false;
        }
    }
    return false;
    }

}
