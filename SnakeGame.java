import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int GRID_WIDTH = WIDTH / TILE_SIZE;
    private final int GRID_HEIGHT = HEIGHT / TILE_SIZE;

    private Point human;
    private Point food;
    private ArrayList<Point> traps = new ArrayList<>();
    private ArrayList<String> trapDirections = new ArrayList<>();
    private char direction = ' ';
    private boolean running = true;
    private int score = 1;
    private int level = 1;
    private boolean showTitleScreen = true;

    private Timer timer;
    private GamePanel gamePanel;

    public SnakeGame() {
        initUI();
        startGame();
    }

    private void initUI() {
        setTitle("Chiprivsko choveche");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        add(gamePanel);

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (showTitleScreen && e.getKeyCode() == KeyEvent.VK_SPACE) {

                    showTitleScreen = false;
                    startGame();
                    gamePanel.repaint();
                    return;
                }

                if (!running && e.getKeyCode() == KeyEvent.VK_R) {
                    startGame();
                    return;
                }


                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> direction = 'U';
                    case KeyEvent.VK_DOWN -> direction = 'D';
                    case KeyEvent.VK_LEFT -> direction = 'L';
                    case KeyEvent.VK_RIGHT -> direction = 'R';
                }

                if (running && direction != ' ') {
                    moveHuman();
                    gamePanel.repaint();
                }
            }
        });

        setVisible(true);
    }

    private void startGame() {
        human = new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2); // Човечето е само една клетка
        food = spawnFood();
        traps.clear();
        trapDirections.clear();
        traps.add(spawnTrap("UP_DOWN"));
        trapDirections.add("UP_DOWN");
        direction = ' ';
        score = 1;
        level = 1;
        running = true;

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(200, e -> {
            if (running) {
                moveTraps();
                gamePanel.repaint();
            }
        });
        timer.start();
    }

    private void moveHuman() {
        Point newPosition = new Point(human);

        switch (direction) {
            case 'U' -> newPosition.y--;
            case 'D' -> newPosition.y++;
            case 'L' -> newPosition.x--;
            case 'R' -> newPosition.x++;
        }


        if (newPosition.x < 1 || newPosition.x >= GRID_WIDTH - 1 ||
                newPosition.y < 1 || newPosition.y >= GRID_HEIGHT - 1) {

            return;
        }

        human = newPosition;


        if (human.equals(food)) {
            food = spawnFood();
            score++;


            if (score % 5 == 0) {
                level++;
                String trapDirection = level % 2 == 0 ? "LEFT_RIGHT" : "UP_DOWN";
                traps.add(spawnTrap(trapDirection));
                trapDirections.add(trapDirection);
            }
        }

        for (Point trap : traps) {
            if (trap.equals(human)) {
                running = false;
                timer.stop();
                break;
            }
        }
    }

    private void moveTraps() {
        for (int i = 0; i < traps.size(); i++) {
            Point trap = traps.get(i);
            String direction = trapDirections.get(i);


            if ("UP_DOWN".equals(direction) || "DOWN".equals(direction) || "UP".equals(direction)) {

                if (trap.y >= GRID_HEIGHT - 2) {
                    trapDirections.set(i, "UP");
                } else if (trap.y <= 1) {
                    trapDirections.set(i, "DOWN");
                }

                if ("DOWN".equals(trapDirections.get(i))) {
                    trap.y++;
                } else if ("UP".equals(trapDirections.get(i))) {
                    trap.y--;
                }
            } else if ("LEFT_RIGHT".equals(direction) || "LEFT".equals(direction) || "RIGHT".equals(direction)) {

                if (trap.x >= GRID_WIDTH - 2) {
                    trapDirections.set(i, "LEFT");
                } else if (trap.x <= 1) {
                    trapDirections.set(i, "RIGHT");
                }

                if ("RIGHT".equals(trapDirections.get(i))) {
                    trap.x++;
                } else if ("LEFT".equals(trapDirections.get(i))) {
                    trap.x--;
                }
            }


            if (trap.equals(human)) {
                running = false;
                timer.stop();
            }
        }
    }



    private Point spawnFood() {
        Random rand = new Random();
        Point point;
        do {
            point = new Point(rand.nextInt(GRID_WIDTH - 2) + 1, rand.nextInt(GRID_HEIGHT - 2) + 1);
        } while (point.equals(human) || traps.contains(point));
        return point;
    }

    private Point spawnTrap(String direction) {
        int x;
        int y;
        if (traps.size() == 0){
            x = direction.equals("UP_DOWN") ? GRID_WIDTH / 2 : 1; // Средата по X или най-отляво
            y = direction.equals("UP_DOWN") ? 1 : GRID_HEIGHT / 2;
        }else {
            x = direction.equals("LEFT_RIGHT") ?1 : GRID_WIDTH / 2 ; // Средата по X или най-отляво
            y = direction.equals("LEFT_RIGHT") ? GRID_HEIGHT / 2 : 1; // Най-отгоре или средата по Y
        }
        return new Point(x, y);
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);


            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            if (showTitleScreen) {

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String title = "Chiprivsko choveche";
                String instruction = "Press SPACE to start";
                FontMetrics metrics = g.getFontMetrics();
                int titleX = (getWidth() - metrics.stringWidth(title)) / 2;
                int titleY = getHeight() / 3;
                int instructionX = (getWidth() - metrics.stringWidth(instruction)) / 2;
                int instructionY = getHeight() / 2;
                g.drawString(title, titleX, titleY);
                g.drawString(instruction, instructionX, instructionY);
                return;
            }

            if (!running) {

                g.setColor(Color.WHITE);
                String message = "Game Over! Press R to Restart";
                String totalScore = "SCORE:" + score;
                String endLevel = "LEVEL:" + level;
                g.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(message)) / 2;
                int y = getHeight() / 2;
                g.drawString(message, x, y);
                g.drawString(totalScore, (getWidth() - metrics.stringWidth(totalScore)) / 2, y+40);
                g.drawString(endLevel, (getWidth() - metrics.stringWidth(endLevel)) / 2, y+80);
                return;
            }


            g.setColor(Color.GRAY);
            g.fillRect(0, 0, TILE_SIZE, this.getHeight());
            g.fillRect(0, 0, this.getWidth(), TILE_SIZE);
            g.fillRect(this.getWidth() - TILE_SIZE, 0, TILE_SIZE, this.getHeight()); // Дясна рамка
            g.fillRect(0, this.getHeight() - TILE_SIZE, this.getWidth(), TILE_SIZE); // Долна рамка

            int segmentWidth = TILE_SIZE / 2;
            int segmentHeight = TILE_SIZE / 2;
            g.setColor(new Color(0, 100, 0)); // Тъмно зелено
            g.fillRect(human.x * TILE_SIZE, human.y * TILE_SIZE, TILE_SIZE / 2, TILE_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(human.x * TILE_SIZE + segmentWidth, human.y * TILE_SIZE, segmentWidth, segmentHeight);
            g.setColor(Color.PINK);
            g.fillRect(human.x * TILE_SIZE + segmentWidth, human.y * TILE_SIZE + segmentHeight, segmentWidth, segmentHeight);

            g.setColor(Color.YELLOW);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            for (Point trap : traps) {
                g.setColor(Color.RED);
                g.fillRect(trap.x * TILE_SIZE, trap.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String scoreAndLevel = "Score: " + score + " | Level: " + level;
            FontMetrics metrics = g.getFontMetrics();
            int scoreX = (getWidth() - metrics.stringWidth(scoreAndLevel)) / 2;
            int scoreY = TILE_SIZE - 5; // Малко под горния край на рамката
            g.drawString(scoreAndLevel, scoreX, scoreY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}
