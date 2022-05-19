package view;


import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    private ChessGameFrame chessGameFrame;
    private int signal = 0;
    private ChessboardPoint warningPoint;
    public ArrayList<String> memory = new ArrayList<>();
    public ChessboardPoint specialPawn1;
    public ChessboardPoint specialPawn2;
    public ChessboardPoint specialPawnKill;
    public int count = 3;
    public int pve = 0;
    public int multiplyKill = 0;


    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);
        // FIXME: Initialize chessboard for testing only.
        initChess();
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }

    public void judgePawn(ChessComponent chess1) {
        ChessboardPoint tempPoint = chess1.getChessboardPoint();
        if (count == 0 && ((tempPoint.getX() == specialPawn1.getX() && tempPoint.getY() == specialPawn1.getY()) ||
                (tempPoint.getX() == specialPawn2.getX() && tempPoint.getY() == specialPawn2.getY()))) {
            chess1.symbol = 1;
            if (chess1.getChessColor() == ChessColor.WHITE) {
                chess1.specialPawnPoint = new ChessboardPoint(specialPawn1.getX() - 1, (specialPawn1.getY() + specialPawn2.getY()) / 2);
            } else {
                chess1.specialPawnPoint = new ChessboardPoint(specialPawn1.getX() + 1, (specialPawn1.getY() + specialPawn2.getY()) / 2);
            }
            specialPawnKill = chess1.specialPawnPoint;
        }
    }


    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        count++;
        if (chess1 instanceof PawnChessComponent && Math.abs(chess1.getChessboardPoint().getX() - chess2.getChessboardPoint().getX()) == 2) {
            if (chess2.getChessboardPoint().getY() - 1 >= 0) {
                specialPawn1 = new ChessboardPoint(chess2.getChessboardPoint().getX(), chess2.getChessboardPoint().getY() - 1);
            } else {
                specialPawn1 = new ChessboardPoint(-1, -1);
            }
            if (chess2.getChessboardPoint().getY() + 1 <= 7) {
                specialPawn2 = new ChessboardPoint(chess2.getChessboardPoint().getX(), chess2.getChessboardPoint().getY() + 1);
            } else {
                specialPawn2 = new ChessboardPoint(-1, -1);
            }
            count = 0;
        }
        boolean try1 = chess2.getChessboardPoint().equals(specialPawnKill);
        if (chess1 instanceof PawnChessComponent && chess2.getChessboardPoint().equals(specialPawnKill) && count == 1) {
            int x = specialPawn2.getX();
            int y = (specialPawn1.getY() + specialPawn2.getY()) / 2;
            ChessComponent chessSpecial = chessComponents[x][y];
            remove(chessSpecial);
            add(chessSpecial = new EmptySlotComponent(new ChessboardPoint(x, y), chessSpecial.getLocation(), clickController, CHESS_SIZE));
            chessComponents[x][y] = chessSpecial;
            chessComponents[x][y].repaint();
        }

        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        if (chess1 instanceof PawnChessComponent) {
            if (chess1.getChessColor() == ChessColor.BLACK && chess1.getChessboardPoint().getX() == 7) {
                chessGameFrame.chosen(chess1);
            }
            if (chess1.getChessColor() == ChessColor.WHITE && chess1.getChessboardPoint().getX() == 0) {
                chessGameFrame.chosen(chess1);
            }
        }
        if(!judgeKill(chess1)){
            multiplyKill=0;
        }
        if (judgeKillFinal(chess1)) {
            String temp;
            if (chess1.getChessColor() == ChessColor.WHITE) {
                temp = "WHITE";
            } else {
                temp = "BLACK";
            }
            chessGameFrame.warning(temp + " win");
            chessGameFrame.reviewing();
//            initChess();
//            resetCurrentColor();
//            chessGameFrame.refresh();
//            repaint();
//            memory = new ArrayList<>();
            return;
        }
        if (judgeDraw(chess1)) {
            chessGameFrame.warning("Draw.");
            chessGameFrame.reviewing();
//            initChess();
//            resetCurrentColor();
//            chessGameFrame.refresh();
//            repaint();
//            memory = new ArrayList<>();
            return;
        }


        chess1.repaint();
        chess2.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
        this.getChessGameFrame().refresh();
    }

    public void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    public void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    public void initKnightOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    public void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    public void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    public void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
        chessComponent.chessboard = this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        if (testLoad(chessData) == null) {
            initiateEmptyChessboard();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessColor tempColor = null;
                    if (chessData.get(i).charAt(j) > 'a' && chessData.get(i).charAt(j) < 'z') {
                        tempColor = ChessColor.WHITE;
                    } else if (chessData.get(i).charAt(j) > 'A' && chessData.get(i).charAt(j) < 'Z') {
                        tempColor = ChessColor.BLACK;
                    } else if (chessData.get(i).charAt(j) == '_') {
                        tempColor = ChessColor.NONE;
                    }
                    if ((chessData.get(i).charAt(j) == 'r') || (chessData.get(i).charAt(j) == 'R')) {
                        initRookOnBoard(i, j, tempColor);
                    }
                    if ((chessData.get(i).charAt(j) == 'k') || (chessData.get(i).charAt(j) == 'K')) {
                        initKingOnBoard(i, j, tempColor);
                    }
                    if ((chessData.get(i).charAt(j) == 'b') || (chessData.get(i).charAt(j) == 'B')) {
                        initBishopOnBoard(i, j, tempColor);
                    }
                    if ((chessData.get(i).charAt(j) == 'Q') || (chessData.get(i).charAt(j) == 'q')) {
                        initQueenOnBoard(i, j, tempColor);
                    }
                    if ((chessData.get(i).charAt(j) == 'n') || (chessData.get(i).charAt(j) == 'N')) {
                        initKnightOnBoard(i, j, tempColor);
                    }
                    if ((chessData.get(i).charAt(j) == 'p') || (chessData.get(i).charAt(j) == 'P')) {
                        initPawnOnBoard(i, j, tempColor);
                    }
                }
            }
            if (chessData.get(8).charAt(0) == 'b') {
                currentColor = ChessColor.BLACK;
            } else {
                currentColor = ChessColor.WHITE;
            }
            chessGameFrame.refresh();
            repaint();
        } else {
            String temp = testLoad(chessData);
            chessGameFrame.warning(testLoad(chessData));
            signal = 0;
        }
    }

    public void initChess() {
        initiateEmptyChessboard();
        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);
        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);
        initBishopOnBoard(0, 2, ChessColor.BLACK);
        initBishopOnBoard(0, CHESSBOARD_SIZE - 3, ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, 2, ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 3, ChessColor.WHITE);
        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, 3, ChessColor.WHITE);
        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, 4, ChessColor.WHITE);
        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1, i, ChessColor.BLACK);
            initPawnOnBoard(CHESSBOARD_SIZE - 2, i, ChessColor.WHITE);
        }
    }

    public void resetCurrentColor() {
        this.currentColor = ChessColor.WHITE;
    }

    public String getChessboardGraph() {
        StringBuilder getChessboardGraph = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                getChessboardGraph.append((chessComponents[i][j].getChessName()));
            }
            getChessboardGraph.append("\n");
        }
        return getChessboardGraph.append(currentColor.toString()).toString();
    }

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    public ChessGameFrame getChessGameFrame() {
        return chessGameFrame;
    }

    public String testLoad(List<String> chessData) {
        if (signal == 1) {
//            signal = 0;
            return "错误编码：104";
        }
        if (chessData.size() != 9) {
            return "错误编码： 101";
        }
        for (int i = 0; i < 8; i++) {
            if (chessData.get(i).length() != 8) {
                return "错误编码： 101";
            }
            for (int j = 0; j < 8; j++) {
                char in = chessData.get(i).charAt(j);
                if (in != '_' && in != 'b' && in != 'B' && in != 'r' && in != 'R' && in != ' ' &&
                        in != 'K' && in != 'k' && in != 'P' && in != 'p' && in != 'Q' && in != 'q' && in != 'N' && in != 'n') {
                    return "错误编码：102";
                }
            }
        }
        if (chessData.get(8).charAt(0) != 'w' && chessData.get(8).charAt(0) != 'b') {
            return "错误编码：103";
        }

        return null;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public boolean judgeKill(ChessComponent chess1) {
        for (int i = 0; i < chess1.moveTo(chessComponents).size(); i++) {
            if (chessComponents[chess1.moveTo(chessComponents).get(i).getX()][chess1.moveTo(chessComponents).get(i).getY()]
                    instanceof KingChessComponent) {
                warningPoint = new ChessboardPoint(chess1.moveTo(chessComponents).get(i).getX(), chess1.moveTo(chessComponents).get(i).getY());
                return true;
            }
        }
        warningPoint = null;
        return false;
    }

    //跑将，消将，挡将
    public boolean judgeKillFinal(ChessComponent chess1) {
        int signal1 = 0;
        int signal2 = 1;
        int signal3 = 1;

        if (judgeKill(chess1)) {
            multiplyKill++;
            //可能有bug
            ChessColor enemyColor = currentColor == ChessColor.BLACK ? ChessColor.BLACK : ChessColor.WHITE;
            String name;
            if (enemyColor == ChessColor.WHITE) {
                name = "WHITE";
            } else {
                name = "BLACK";
            }
            this.chessGameFrame.warning("The " + name + "'s king is in danger");
            //后续代码极有可能出现bug
            //判断王能不能跑，不能跑signal1=1
            List<ChessboardPoint> temp = chessComponents[warningPoint.getX()][warningPoint.getY()].moveTo(chessComponents);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponents[i][j].getChessColor() == chess1.getChessColor()) {
                        for (int m = 0; m < chessComponents[i][j].moveTo(chessComponents).size(); m++) {
                            for (int k = 0; k < temp.size(); k++) {
                                if (temp != null && chessComponents[i][j].moveTo(chessComponents).get(m).getX() == temp.get(k).getX() &&
                                        chessComponents[i][j].moveTo(chessComponents).get(m).getY() == temp.get(k).getY()) {
                                    temp.remove(k);
                                    if (temp.size() == 0) {
                                        break;
                                    }
                                    k--;
                                }
                            }
                            if (temp.size() == 0) {
                                break;
                            }
                        }
                    }
                    if (temp.size() == 0) {
                        break;
                    }
                }
                if (temp.size() == 0) {
                    break;
                }
            }
            if (temp.size() == 0) {
                signal1 = 1;
            }
            //判断能不能挡将，不能挡signal2=1
            List<ChessboardPoint> dangerPathWay = new ArrayList<>();
            if (chess1 instanceof RookChessComponent || chess1 instanceof QueenChessComponent) {
                if (warningPoint.getX() == chess1.getChessboardPoint().getX()) {
                    for (int i = Math.min(warningPoint.getY(), chess1.getChessboardPoint().getY()) + 1;
                         i < Math.max(warningPoint.getY(), chess1.getChessboardPoint().getY()); i++) {
                        dangerPathWay.add(new ChessboardPoint(warningPoint.getX() + 1, i));
                    }
                }
                if (warningPoint.getY() == chess1.getChessboardPoint().getY()) {
                    for (int i = Math.min(warningPoint.getX(), chess1.getChessboardPoint().getX() + 1);
                         i < Math.max(warningPoint.getX(), chess1.getChessboardPoint().getX()); i++) {
                        dangerPathWay.add(new ChessboardPoint(warningPoint.getY(), i));
                    }
                }
            }
            if (chess1 instanceof BishopChessComponent || chess1 instanceof QueenChessComponent) {
                for (int i = Math.min(warningPoint.getX(), chess1.getChessboardPoint().getX()) + 1;
                     i < Math.max(warningPoint.getX(), chess1.getChessboardPoint().getX()); i++) {
                    int path;
                    if ((warningPoint.getX() - chess1.getChessboardPoint().getX()) * (warningPoint.getY() - chess1.getChessboardPoint().getY()) > 0) {
                        path = 1;
                    } else {
                        path = -1;
                    }
                    if (path == -1) {
                        dangerPathWay.add(new ChessboardPoint(i, Math.max(warningPoint.getY(),
                                chess1.getChessboardPoint().getY()) + path * (i - Math.min(warningPoint.getX(), chess1.getChessboardPoint().getX()))));
                    } else {
                        dangerPathWay.add(new ChessboardPoint(i, Math.min(warningPoint.getY(),
                                chess1.getChessboardPoint().getY()) + path * (i - Math.min(warningPoint.getX(), chess1.getChessboardPoint().getX()))));
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponents[i][j].getChessColor() != chess1.getChessColor()) {
                        if (chessComponents[i][j].moveTo(chessComponents) == null) {
                            continue;
                        } else {
                            List<ChessboardPoint> temp1 = chessComponents[i][j].moveTo(chessComponents);
                            for (int k = 0; k < chessComponents[i][j].moveTo(chessComponents).size(); k++) {
                                for (int t = 0; t < dangerPathWay.size(); t++) {
                                    if (chessComponents[i][j].canMoveTo(chessComponents, dangerPathWay.get(t)) &&
                                            !killSelf(chessComponents[i][j], chessComponents[dangerPathWay.get(t).getX()][dangerPathWay.get(t).getY()])) {
                                        signal2 = 0;
                                        break;
                                    }
                                }
                                if (signal2 == 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (signal2 == 0) {
                        break;
                    }
                }
                if (signal2 == 0) {
                    break;
                }
            }

            //判断能不能消除将军,无法消掉为1
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponents[i][j].getChessColor() != chess1.getChessColor()) {
                        if (chessComponents[i][j].moveTo(chessComponents) != null &&
                                chessComponents[i][j].canMoveTo(chessComponents, chess1.getChessboardPoint()) &&
                                !killSelf(chessComponents[i][j], chess1)) {
                            signal3 = 0;
                            break;
                        }
                    }
                }
                if (signal3 == 0) {
                    break;
                }
            }
        }


        if (signal1 == 1 && signal2 == 1 && signal3 == 1) {
            return true;
        } else {
            return false;
        }


    }

    public boolean judgeDraw(ChessComponent chess1) {
        if(multiplyKill==5){
            return true;
        }
        if (!judgeKill(chess1)) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (chessComponents[i][j].getChessColor() != chess1.getChessColor()) {
                        if (chessComponents[i][j].moveTo(chessComponents) == null) {
                            continue;
                        }
                        for (int k = 0; k < chessComponents[i][j].moveTo(chessComponents).size(); k++) {
                            ChessboardPoint temp2 = chessComponents[i][j].moveTo(chessComponents).get(k);
                            if (!killSelf(chessComponents[i][j], chessComponents[temp2.getX()][temp2.getY()])) {
                                return false;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    //判断将要移动子是否会把王暴露于敌方攻击范围,是为true
    public boolean killSelf(ChessComponent chess1, ChessComponent chess2) {
        ChessComponent[][] chessComponentsModel = new ChessComponent[8][8];
        //创建一个虚拟的棋盘用作模拟
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessComponentsModel[i][j] = chessComponents[i][j];
            }
        }
        chessComponentsModel[chess1.getChessboardPoint().getX()][chess1.getChessboardPoint().getY()] = new EmptySlotComponent(chess1.getChessboardPoint(), calculatePoint(-1, -1), clickController, CHESS_SIZE);
        chessComponentsModel[chess2.getChessboardPoint().getX()][chess2.getChessboardPoint().getY()] = chess1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessComponentsModel[i][j].getChessColor() != chess1.getChessColor()) {
                    if (chessComponentsModel[i][j].moveTo(chessComponentsModel) == null) {
                        continue;
                    }
                    ChessboardPoint temp;
                    for (int k = 0; k < chessComponentsModel[i][j].moveTo(chessComponentsModel).size(); k++) {
                        temp = chessComponentsModel[i][j].moveTo(chessComponentsModel).get(k);
                        if (chessComponentsModel[temp.getX()][temp.getY()] instanceof KingChessComponent) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getChessboardGraph1() {
        StringBuilder getChessboardGraph = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                getChessboardGraph.append((chessComponents[i][j].getChessName()));
            }
        }
        getChessboardGraph.append(currentColor);
        getChessboardGraph.append(count);
        if (specialPawn1 != null) {
            getChessboardGraph.append(specialPawn1.getX());
            getChessboardGraph.append(specialPawn1.getY());
        } else {
            getChessboardGraph.append(-1);
            getChessboardGraph.append(-1);
        }
        if (specialPawn2 != null) {
            getChessboardGraph.append(specialPawn2.getX());
            getChessboardGraph.append(specialPawn2.getY());
        } else {
            getChessboardGraph.append(-1);
            getChessboardGraph.append(-1);
        }
        if (specialPawnKill != null) {
            getChessboardGraph.append(specialPawnKill.getX());
            getChessboardGraph.append(specialPawnKill.getY());
        } else {
            getChessboardGraph.append(-1);
            getChessboardGraph.append(-1);
        }
        return getChessboardGraph.toString();
    }

    public void loadGame1(String memory) {
        loadGamePlus(memory);
        repaint();
    }

    public void loadGamePlus(String memory) {
        initiateEmptyChessboard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessColor tempColor = null;
                char temp = memory.charAt(8 * i + j);
                if (temp > 'a' && temp < 'z') {
                    tempColor = ChessColor.WHITE;
                } else if (temp > 'A' && temp < 'Z') {
                    tempColor = ChessColor.BLACK;
                } else if (temp == '_') {
                    tempColor = ChessColor.NONE;
                }
                if ((temp == 'r') || (temp == 'R')) {
                    initRookOnBoard(i, j, tempColor);
                }
                if ((temp == 'k') || (temp == 'K')) {
                    initKingOnBoard(i, j, tempColor);
                }
                if ((temp == 'b') || (temp == 'B')) {
                    initBishopOnBoard(i, j, tempColor);
                }
                if ((temp == 'Q') || (temp == 'q')) {
                    initQueenOnBoard(i, j, tempColor);
                }
                if ((temp == 'n') || (temp == 'N')) {
                    initKnightOnBoard(i, j, tempColor);
                }
                if ((temp == 'p') || (temp == 'P')) {
                    initPawnOnBoard(i, j, tempColor);
                }
            }
        }
        if (memory.charAt(64) == 'b') {
            currentColor = ChessColor.BLACK;
        } else {
            currentColor = ChessColor.WHITE;
        }
        chessGameFrame.refresh();
        count = memory.charAt(65) - '0';
        specialPawn1 = new ChessboardPoint(memory.charAt(66) - '0', memory.charAt(67) - '0');
        specialPawn2 = new ChessboardPoint(memory.charAt(68) - '0', memory.charAt(69) - '0');
        specialPawnKill = new ChessboardPoint(memory.charAt(70) - '0', memory.charAt(71) - '0');
    }

    public ArrayList<ChessComponent> randomChessComponent(ChessColor chessColor) {
        ArrayList<ChessComponent> randomOne = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int count = 0;
                if (chessComponents[i][j].getChessColor() == chessColor) {
                    if (chessComponents[i][j].moveTo(chessComponents)!=null&&chessComponents[i][j].moveTo(chessComponents).size() != 0) {
                            for (int k = 0; k < chessComponents[i][j].moveTo(chessComponents).size(); k++) {
                                if (!killSelf(chessComponents[i][j], chessComponents[chessComponents[i][j].moveTo(chessComponents).get(k).getX()]
                                        [chessComponents[i][j].moveTo(chessComponents).get(k).getY()])) {
                                    count++;
                                }
                            }

                        if (count != 0) {
                            randomOne.add(chessComponents[i][j]);
                        }
                    }
                }
            }
        }
        return randomOne;
    }


}
