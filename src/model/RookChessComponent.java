package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示国际象棋里面的车
 */
public class RookChessComponent extends ChessComponent {
    /**
     * 黑车和白车的图片，static使得其可以被所有车对象共享
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image ROOK_WHITE;
    private static Image ROOK_BLACK;

    /**
     * 车棋子对象自身的图片，是上面两种中的一种
     */
    private Image rockImage;

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (ROOK_WHITE == null) {
            ROOK_WHITE = ImageIO.read(new File("./images/rook-white.png"));
        }

        if (ROOK_BLACK == null) {
            ROOK_BLACK = ImageIO.read(new File("./images/rook-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateRookImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                rockImage = ROOK_WHITE;
            } else if (color == ChessColor.BLACK) {
                rockImage = ROOK_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RookChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        initiateRookImage(color);
        if(color==ChessColor.WHITE){
            this.name = 'r';
        }
        else {
            this.name = 'R';
        }
    }

    @Override
    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        List<ChessboardPoint> all = new ArrayList<>();
        all.addAll(canMoveToPartR(1,0,chessboard));
        all.addAll(canMoveToPartR(-1,0,chessboard));
        all.addAll(canMoveToPartR(0,1,chessboard));
        all.addAll(canMoveToPartR(0,-1,chessboard));
        return all;
    }

    /**
     * 车棋子的移动规则
     *
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else { // Not on the same row or the same column.
            return false;
        }
        return true;
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//     g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(rockImage, 0, 0, getWidth() , getHeight(), this);
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
            if(x<0||x>7||y<0||y>7){
                break;
            }
            if (chessboard[x][y].name != '_') {
                if (chessboard[x][y].getChessColor() == this.getChessColor()) {
                    break;
                }
                if (chessboard[x][y].getChessColor() != this.getChessColor()){
                    canMoveTo.add(this.getChessboardPoint().offset(a*i,b*i));
                    break;
                }
            }
            else{
                canMoveTo.add(this.getChessboardPoint().offset(a*i,b*i));
            }
        }
        while(canMoveTo.contains(null)){
            canMoveTo.remove(null);
        }
        return canMoveTo;
    }
}
