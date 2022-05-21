package controller;


import model.ChessColor;
import model.ChessComponent;
import view.Chessboard;
import view.ChessboardPoint;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClickController {
    private final Chessboard chessboard;
    private ChessComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                first = chessComponent;
                chessboard.judgePawn(first);
                for (int i = 0; i < chessComponent.moveTo(chessboard.getChessComponents()).size(); i++) {
                    ChessboardPoint temp = chessComponent.moveTo(chessboard.getChessComponents()).get(i);
                    if (temp.getX() >= 0 && temp.getY() <= 7 && temp.getX() <= 7 && temp.getY() >= 0) {
                        if (!chessboard.killSelf(first, chessboard.getChessComponents()[temp.getX()][temp.getY()])) {
                            chessboard.getChessComponents()[temp.getX()][temp.getY()].setChosen(true);
                            chessboard.getChessComponents()[temp.getX()][temp.getY()].chosenColor = first.getChessColor();
                            chessboard.getChessComponents()[temp.getX()][temp.getY()].repaint();
                        }
                    }
                }
                playOnce(new File(".\\音效\\moveSound.wav"));
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                for (int i = 0; i < chessComponent.moveTo(chessboard.getChessComponents()).size(); i++) {
                    ChessboardPoint temp = chessComponent.moveTo(chessboard.getChessComponents()).get(i);
                    if (temp.getX() >= 0 && temp.getY() <= 7 && temp.getX() <= 7 && temp.getY() >= 0) {
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].setChosen(false);
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].chosenColor = null;
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].repaint();
                    }
                }
                playOnce(new File(".\\音效\\moveSound.wav"));
                ChessComponent recordFirst = first;
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                chessboard.memory.add(chessboard.getChessboardGraph1());
                //repaint in swap chess method.
                chessboard.swapColor();
                for (int i = 0; i < first.moveTo(chessboard.getChessComponents()).size(); i++) {
                    ChessboardPoint temp = first.moveTo(chessboard.getChessComponents()).get(i);
                    if (temp.getX() >= 0 && temp.getY() <= 7 && temp.getX() <= 7 && temp.getY() >= 0) {
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].setChosen(false);
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].chosenColor = null;
                        chessboard.getChessComponents()[temp.getX()][temp.getY()].repaint();
                    }
                }
                playOnce(new File(".\\音效\\moveSound.wav"));
                chessboard.swapChessComponents(first, chessComponent);
                first.setSelected(false);
                first = null;
            }
        }
        if (chessboard.getCurrentColor() != ChessColor.WHITE && chessboard.pve == 1) {
            Random random = new Random();
            int t = random.nextInt(chessboard.randomChessComponent(ChessColor.BLACK).size());
            ChessComponent chess = chessboard.randomChessComponent(ChessColor.BLACK).get(t);
            int k = random.nextInt(chess.moveTo(chessboard.getChessComponents()).size());
            ChessComponent chess2 = chessboard.getChessComponents()[chess.moveTo(chessboard.getChessComponents()).get(k).getX()][chess.moveTo(chessboard.getChessComponents()).get(k).getY()];
            while ((chessboard.killSelf(chess, chess2) &&
                    chess.canMoveTo(chessboard.getChessComponents(), chess2.getChessboardPoint()))) {
                k = random.nextInt(chess.moveTo(chessboard.getChessComponents()).size());
                chess2 = chessboard.getChessComponents()[chess.moveTo(chessboard.getChessComponents()).get(k).getX()][chess.moveTo(chessboard.getChessComponents()).get(k).getY()];
            }
            chessboard.memory.add(chessboard.getChessboardGraph1());
            chessboard.swapColor();
            chessboard.swapChessComponents(chess, chess2);
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        return !chessboard.killSelf(first, chessComponent) && chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }

    public static void playOnce(File file) {
        try {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            //播放器打开这个文件
            clip.open(audioInput);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //死循环不让主程序结束（swing可不用）
    }
}
