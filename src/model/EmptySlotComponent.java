package model;

import view.ChessboardPoint;
import controller.ClickController;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * 这个类表示棋盘上的空位置
 */
public class EmptySlotComponent extends ChessComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size);
        this.name = '_';

    }

    @Override
    public List<ChessboardPoint> moveTo(ChessComponent[][] chessboard) {
        return null;
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        return false;
    }

    @Override
    public void loadResource() throws IOException {
        //No resource!
    }

}
