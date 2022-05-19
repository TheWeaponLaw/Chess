package view;

import controller.GameController;
import model.ChessColor;
import model.ChessComponent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;


/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;
    JLabel statusLabel = new JLabel("The current player: White");
    String path = ".\\背景\\1.jpg";
    int counter = 1;
    JLabel background = new JLabel();
    JButton loadButton = new JButton("Load");
    JButton repentanceButton = new JButton("Repentance");


    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        playMusic(new File(".\\音效\\bgm.wav"));


        addChessboard();
        addLabel();
        resetBoard();
        addLoadButton();
        addSaveButton();
        changeButton();
        addMemoryButton();
        reviewingButton();
        addRefreshButton();
        addBackground();

    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        gameController.setChessGameFrame(this);
        chessboard.setLocation(HEIGHT / 10, HEIGHT / 10);
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        statusLabel.setLocation(HEIGHT - 50, HEIGHT / 10);
        statusLabel.setSize(300, 60);
        statusLabel.setFont(new Font("Rockwell", Font.LAYOUT_RIGHT_TO_LEFT, 20));
        statusLabel.setForeground(Color.red);
        add(statusLabel);
    }

    public void refresh() {
        if (gameController.getChessboard().getCurrentColor() == ChessColor.WHITE) {
            statusLabel.setText("The current player: WHITE");
        } else {
            statusLabel.setText("The current player: BLACK");
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会重置游戏。
     */


    private void resetBoard() {
        JButton button = new JButton("Reset the game.");
        button.addActionListener((e) -> {
            gameController.getChessboard().initChess();
            gameController.getChessboard().resetCurrentColor();
            refresh();
            repaint();
            gameController.getChessboard().memory = new ArrayList<>();
            gameController.getChessboard().count = 3;
            gameController.getChessboard().specialPawn1 = null;
            gameController.getChessboard().specialPawn2 = null;
            gameController.getChessboard().multiplyKillW = 0;
            gameController.getChessboard().multiplyKillB = 0;
            for (int i = 0; i < 6; i++) {
                gameController.getChessboard().blackSide[i] = String.valueOf(i);
                gameController.getChessboard().whiteSide[i] = String.valueOf(i);
            }
            gameController.getChessboard().countDraw = 0;
        });
        button.setLocation(HEIGHT - 25, HEIGHT / 10 + 60);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addLoadButton() {

        loadButton.setLocation(HEIGHT - 25, HEIGHT / 10 + 130);
        loadButton.setSize(200, 60);
        loadButton.setBackground(Color.WHITE);
        loadButton.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(loadButton);

        loadButton.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this, "Input Path here");
//            gameController.loadGameFromFile(path);
            JFileChooser jfc = new JFileChooser(".\\存储棋盘");
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jfc.showDialog(new JLabel(), "选择");
            File file = jfc.getSelectedFile();
            gameController.loadGameFromFile(file.getAbsolutePath());
            String path = file.getAbsolutePath();
            StringBuilder temp = new StringBuilder();
            temp.append(path);
            temp.replace(temp.indexOf("棋盘"), temp.indexOf("棋盘") + 2, "步骤");
            gameController.loadLocationFromFile(temp.toString());
            for (int i = 0; i < 6; i++) {
                gameController.getChessboard().blackSide[i] = String.valueOf(i);
                gameController.getChessboard().whiteSide[i] = String.valueOf(i);
            }
            gameController.getChessboard().countDraw = 0;
        });
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT - 25, HEIGHT / 10 + 200);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        button.addActionListener(e -> {
            JOptionPane jOptionPane = new JOptionPane();
            System.out.println("Click save");
            String path = jOptionPane.showInputDialog(this, "Input name here");
            int count = 0;
            if (path.length() != 0 || path == null) {
//                path = "E:\\武器法\\大学\\学习\\Java\\Project\\ChessDemo\\存储棋盘\\" + path + ".txt";
                path = ".\\存储棋盘\\" + path + ".txt";
            } else {
//                File file = new File("E:\\武器法\\大学\\学习\\Java\\Project\\ChessDemo\\存储棋盘\\" + "测试" + count + ".txt");
                File file = new File(".\\存储棋盘\\" + "测试" + count + ".txt");
                while (file.exists()) {
                    count++;
//                    file = new File("E:\\武器法\\大学\\学习\\Java\\Project\\ChessDemo\\存储步骤\\" + "测试" + count + ".txt");
                    file = new File(".\\存储步骤\\" + "测试" + count + ".txt");

                }
//                path = "E:\\武器法\\大学\\学习\\Java\\Project\\ChessDemo\\存储棋盘\\" + "测试" + count + ".txt";
                path = ".\\存储棋盘\\" + "测试" + count + ".txt";

            }
            gameController.saveGameFromFile(path);
            StringBuilder path2 = new StringBuilder();
            path2.append(path);
            path2.replace(path2.indexOf("棋盘"), path2.indexOf("棋盘") + 2, "步骤");
            gameController.saveLocationFromFile(path2.toString());
        });
    }

    public void reviewing() {
        JOptionPane jOptionPane = new JOptionPane();
        int userOption = JOptionPane.showConfirmDialog(this, "Do you want to review?", "Review", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (userOption == JOptionPane.OK_OPTION) {
            gameController.getChessboard().memory.add(gameController.getChessboard().getChessboardGraph1());
            Thread aThread = new Thread(new MyRunnable(this));
            aThread.start();
        } else {
            getGameController().getChessboard().initChess();
            getGameController().getChessboard().resetCurrentColor();
            refresh();
            repaint();
            getGameController().getChessboard().memory = new ArrayList<>();
            gameController.getChessboard().multiplyKillW = 0;
            for (int i = 0; i < 6; i++) {
                gameController.getChessboard().blackSide[i] = String.valueOf(i);
                gameController.getChessboard().whiteSide[i] = String.valueOf(i);
            }
            gameController.getChessboard().countDraw = 0;
        }

    }

    public void reviewingButton() {
        JButton button = new JButton("Reviewing");
        button.setLocation(HEIGHT - 25, HEIGHT / 10 + 340);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.CENTER_BASELINE, 20));
        add(button);
        button.addActionListener(e -> {
            reviewing();
        });
    }

    public void warning(String warning) {
        JOptionPane jOptionPane = new JOptionPane();
        jOptionPane.showMessageDialog(this, warning);
    }

    public void chosen(ChessComponent chessPawn) {
        JOptionPane jOptionPane = new JOptionPane();
        String[] options = {"皇后", "战车", "骑士", "主教"};
        int n = JOptionPane.showOptionDialog(this, "请选择升变类型：", "兵底线升变", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        int x = chessPawn.getChessboardPoint().getX();
        int y = chessPawn.getChessboardPoint().getY();
        ChessColor pawnColor = chessPawn.getChessColor();
        switch (n) {
            case 0:
                gameController.getChessboard().initQueenOnBoard(x, y, pawnColor);
                break;
            case 1:
                gameController.getChessboard().initRookOnBoard(x, y, pawnColor);
                break;
            case 2:
                gameController.getChessboard().initKnightOnBoard(x, y, pawnColor);
                break;
            case 3:
                gameController.getChessboard().initBishopOnBoard(x, y, pawnColor);
                break;
        }
        repaint();
        if (gameController.getChessboard().judgeKill(gameController.getChessboard().getChessComponents()[x][y])) {
            String color;
            if (gameController.getChessboard().getChessComponents()[x][y].getChessColor() == ChessColor.WHITE) {
                color = "BLACK";
            } else {
                color = "WHITE";
            }
            warning("The " + color + "'s king is in danger.");
        }
    }

    public void addMemoryButton() {
        repentanceButton.setLocation(HEIGHT - 25, HEIGHT / 10 + 270);
        repentanceButton.setSize(200, 60);
        repentanceButton.setBackground(Color.WHITE);
        repentanceButton.setFont(new Font("Rockwell", Font.CENTER_BASELINE, 20));
        add(repentanceButton);

        repentanceButton.addActionListener(e -> {
            //判断是否为零
            if (gameController.getChessboard().memory.size() != 0) {
                //获取最新一次的记录
                gameController.getChessboard().loadGame1(gameController.getChessboard().memory.get(gameController.getChessboard().memory.size() - 1));
                //移除最新一次的
                gameController.getChessboard().memory.remove(gameController.getChessboard().memory.size() - 1);
            } else {
                //若为零悔棋到头，提醒
                warning("You are at the first step.");
            }
        });
    }

    public void addBackground() {
        ImageIcon imageIcon = new ImageIcon(path);
        ;
        background.setIcon(imageIcon);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(this.WIDTH, this.HEIGHT, 5));
        background.setBounds(0, 0, this.WIDTH, this.HEIGHT);
        background.setVisible(true);
        add(background);
    }

    public void addRefreshButton() {
        JButton button = new JButton("Refresh style");
        button.setLocation(HEIGHT - 25, HEIGHT / 10 + 410);
        button.setSize(200, 60);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.setVisible(true);
        add(button);
        button.addActionListener(e -> {
            counter++;
            path = ".\\背景\\" + counter % 2 + ".jpg";
            ImageIcon imageIcon = new ImageIcon(path);

            imageIcon.setImage(imageIcon.getImage().getScaledInstance(this.WIDTH, this.HEIGHT, 5));
            background.setIcon(imageIcon);
            Color[][] temp = new Color[2][2];
            temp[1][0] = Color.WHITE;
            temp[1][1] = Color.BLACK;
            temp[0][0] = Color.WHITE;
            temp[0][1] = Color.cyan.darker();
            gameController.getChessboard().getChessComponents()[1][1].setBackgroundColors(temp[counter % 2]);
            repentanceButton.setBackground(temp[0][(counter + 1) % 2]);
            loadButton.setBackground(temp[0][(counter + 1) % 2]);
            button.setBackground(temp[0][(counter + 1) % 2]);
        });

    }

    public void changeButton() {
        JButton button = new JButton("Change mode");
        button.setLocation(HEIGHT - 25, HEIGHT / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.setVisible(true);
        add(button);

        button.addActionListener(e -> {
            this.getGameController().getChessboard().pve = this.gameController.getChessboard().pve == 1 ? 0 : 1;
        });
    }
    public static void playMusic(File file)
    {
        try
        {
            //创建相当于音乐播放器的对象
            Clip clip = AudioSystem.getClip();
            //将传入的文件转成可播放的文件
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            //播放器打开这个文件
            clip.open(audioInput);
            //clip.start();//只播放一次
            //循环播放
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        //死循环不让主程序结束（swing可不用）
    }
}
