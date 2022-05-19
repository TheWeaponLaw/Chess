package controller;

import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Chessboard chessboard;

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        chessboard.setChessGameFrame(chessGameFrame);
    }

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public List<String> loadGameFromFile(String path) {
        try {
            if (!path.endsWith(".txt")) {
                chessboard.setSignal(1);
            }
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadLocationFromFile(String path) {
        try {
            File try1 = new File(path);
            if (try1.exists()) {
                List<String> chessData = Files.readAllLines(Path.of(path));
                getChessboard().memory = new ArrayList<>();
                getChessboard().count = 3;
                getChessboard().specialPawn1 = null;
                getChessboard().specialPawn2 = null;
                getChessboard().specialPawnKill = null;
                for (int i = 0; i < chessData.size(); i++) {
                    chessboard.memory.add(chessData.get(i));
                }
                chessboard.count = chessboard.memory.get(chessData.size() - 1).charAt(65) - '0';
                chessboard.specialPawn1 = new ChessboardPoint(chessboard.memory.get(chessData.size() - 1).charAt(66) - '0', chessboard.memory.get(chessData.size() - 1).charAt(67) - '0');
                chessboard.specialPawn2 = new ChessboardPoint(chessboard.memory.get(chessData.size() - 1).charAt(68) - '0', chessboard.memory.get(chessData.size() - 1).charAt(69) - '0');
                chessboard.specialPawnKill = new ChessboardPoint(chessboard.memory.get(chessData.size() - 1).charAt(70) - '0', chessboard.memory.get(chessData.size() - 1).charAt(71) - '0');
            } else {
                getChessboard().memory = new ArrayList<>();
                getChessboard().count = 3;
                getChessboard().specialPawn1 = null;
                getChessboard().specialPawn2 = null;
                getChessboard().specialPawnKill = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGameFromFile(String path) {
        try {
            FileWriter fw = new FileWriter(path, false);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(chessboard.getChessboardGraph());
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLocationFromFile(String name) {
        try {
            FileWriter fw = new FileWriter(name, false);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < chessboard.memory.size(); i++) {
                pw.print(chessboard.memory.get(i) + "\n");
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
