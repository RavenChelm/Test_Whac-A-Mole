import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

public class Game extends JPanel implements ActionListener, MouseListener {
    private static int positionX = 190;
    private static int positionY = 60;
    private int moleX;
    private int moleY;
    private int probability;

    private Image mole_hide;
    private Image mole_visible;
    private Image mole_delite;
    private Image del_score;
    private Image BackGroundMenu;
    private Image mole_hit;

    private String TimeStr;
    private String ScoreStr;
    private int Time;
    private int Score;
    private int MaxScore;

    private boolean helpTime = false;
    private boolean moleHith = false;
    private boolean inGame = false;
    private boolean AfterMenu = false;
    private boolean menu = true;

    private Timer timer;

    static JPanel GamePanel = new JPanel();
    JLabel MouseCheker = new JLabel(new ColorIcon(new Color(0, 0, 0), 1080, 920));

    public Game() {
        loadImages();
        timer = new Timer(500, (ActionListener) this);
        timer.start();

        GamePanel.add(MouseCheker);
        MouseCheker.addMouseListener(this);
        MouseCheker.setBounds(0, 0, 1080, 920);
        Time = 30;
        Score = 0;
        MaxScore = 0;
    }

    public void WriteRecord() throws Exception {
        FileWriter writer = new FileWriter("Record.txt");
        String MaxScoreStr = Integer.toString(MaxScore);
        writer.write(MaxScoreStr);
        writer.close();
    }

    public void ReadRecord() throws Exception {
        FileReader reader = new FileReader("Record.txt");
        String MaxScoreStr = "";
        while (reader.ready()) {
            int w = reader.read() - 48;
            String Ch = Integer.toString(w);
            MaxScoreStr += Ch;
            System.out.println(w);
        }
        MaxScore = Integer.parseInt(MaxScoreStr);
        reader.close();
    }

    // graphics
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {// Окно с геймплеем
            g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
            for (int x = 0; x < 3; x++) { // Отрисовка всех кротов
                for (int y = 0; y < 3; y++) {
                    g.drawImage(mole_hide, positionX + x * 240, positionY + y * 300, this);
                }
            }
            g.drawImage(mole_delite, positionX + moleX * 240, positionY + moleY * 300, this);
            g.drawImage(mole_hide, positionX + moleX * 240, positionY + moleY * 300, this);
            randomMole();
            if (probability >= 3) {
                g.drawImage(mole_visible, positionX + moleX * 240, positionY + moleY * 300, this);
            }
            g.drawImage(del_score, 0, 0, this);
            g.drawString(TimeStr, 0, 30);
            g.drawString(ScoreStr, 20, 60);
            if (moleHith) {
                g.drawImage(mole_hit, positionX + moleX * 240, positionY + moleY * 300, this);
                moleHith = false;
            }
        }
        if (AfterMenu) { // Меню после геймплея
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.drawImage(BackGroundMenu, 0, 0, this);
            g.drawString("Record: " + MaxScore, 500, 350);
            g.drawString(ScoreStr, 500, 400);
            g.drawString("Play again!", 490, 500);
            g.drawString("Menu", 525, 600);
            g.drawRect(445, 450, 200, 100);
            g.drawRect(500, 550, 100, 75);
        }
        if (menu) { // Начальное меню
            g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
            g.drawImage(BackGroundMenu, 0, 0, this);
            g.drawString("WHAC-A-MOLE!", 420, 350);
            g.drawString("Record: " + MaxScore, 460, 400);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
            g.drawRect(445, 450, 200, 100);
            g.drawString("Play!", 515, 500);
        }
    }

    // Генерация рандомного крота
    public void randomMole() {
        moleX = new Random().nextInt(3);
        moleY = new Random().nextInt(3);
        probability = new Random().nextInt(10);
    }

    public void loadImages() {
        ImageIcon mole = new ImageIcon("mole.png");
        ImageIcon mole_del = new ImageIcon("mole_del.png");
        ImageIcon mole_hidden = new ImageIcon("mole-hidden.png");
        ImageIcon delite_score = new ImageIcon("del_score.png");
        ImageIcon BackMenu = new ImageIcon("MenuBackground.png");
        ImageIcon MolH = new ImageIcon("mole_hit.png");
        mole_visible = mole.getImage();
        mole_hide = mole_hidden.getImage();
        mole_delite = mole_del.getImage();
        del_score = delite_score.getImage();
        BackGroundMenu = BackMenu.getImage();
        mole_hit = MolH.getImage();
        mole_visible = mole_visible.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        mole_hide = mole_hide.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        mole_delite = mole_delite.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        mole_hit = mole_hit.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
    }

    // loop
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            if (Time == -2) {
                Time = 30;
                Score = 0;
            }
            if (helpTime) {
                Time--;
                helpTime = false;
            } else {
                helpTime = true;
            }

            TimeStr = "Времени осталось:  " + Integer.toString(Time);
            ScoreStr = new String("Счёт:  " + Integer.toString(Score));
        }
        if (Time == -1) {
            inGame = false;
            AfterMenu = true;
        }
        if (AfterMenu) {
            if (MaxScore < Score) {
                MaxScore = Score;
                try {
                    WriteRecord();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (menu) {
            if (MaxScore == 0) {
                try {
                    ReadRecord();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (inGame) {
            if ((positionY + moleY * 300 <= y && positionY + moleY * 300 + 200 >= y) &&
                    (positionX + moleX * 240 <= x && positionX + moleX * 240 + 200 >= x)) {
                Score++;
                moleHith = true;
                repaint();
            }
            ScoreStr = "Счёт:  " + Integer.toString(Score);
            System.out.println("You clic");
            System.out.println(positionX + moleX * 240);
            System.out.println(positionX + moleX * 240 + 200);
            System.out.println(positionY + moleY * 240);
            System.out.println(positionY + moleY * 240 + 200);
            System.out.println(x);
            System.out.println(y);
            System.out.println(Score);
        }
        if (AfterMenu) {
            if ((450 <= y && 550 >= y) &&
                    (445 <= x && 645 >= x)) {
                AfterMenu = false;
                inGame = true;
                Time = -2;
            }
            if ((550 <= y && 625 >= y) &&
                    (500 <= x && 600 >= x)) {
                AfterMenu = false;
                menu = true;
            }

            // g.drawRect(445, 450, 200, 100);
            // g.drawRect(500, 550, 100, 75);
        }
        if (menu) {
            if ((450 <= y && 550 >= y) &&
                    (445 <= x && 645 >= x)) {
                menu = false;
                inGame = true;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static class ColorIcon implements Icon {
        private int width;
        private int height;
        private Color color;

        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            // g.setColor(color);
            // g.fillRect(x, y, width, height);
        }
    }
}