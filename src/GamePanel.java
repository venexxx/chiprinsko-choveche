import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    final int originalTitleSize = 16;
    final int scale = 4;
    final int titleSize = originalTitleSize * scale;

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWith = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;

    private String direction = "Up";

    boolean showTitleScreen = true;

    Thread gameTread;
    KeyHandler keyHandler = new KeyHandler();
    Image image;
    Image animationImage;

    int playerX = 100;
    int playerY = 100;

    int playerSpeed = 6;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWith,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

    }


    public void startGameTread(){
        gameTread = new Thread(this);
        gameTread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long imageChangingTime;
        while (gameTread != null){


            currentTime = System.nanoTime();
            imageChangingTime = (System.currentTimeMillis() % 10000) /1000;

            animationImage = getAnimationImage(imageChangingTime);

            System.out.println("image time:"+imageChangingTime);

            delta += (currentTime - lastTime) / drawInterval;

            lastTime  = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
            }

        }
    }

    public void update(){


        int currentPlayerX = playerX;
        int currentPlayerY = playerY;


        switch (keyHandler.getDirection()){
            case 'U':
                currentPlayerY -= playerSpeed;
                direction = "Up";
                break;
            case 'D':
                currentPlayerY += playerSpeed;
                direction = "Down";
                break;
            case 'L':
                currentPlayerX -= playerSpeed;
                direction = "Left";
                break;
            case 'R':
                currentPlayerX += playerSpeed;
                direction = "Right";
                break;
        }

        if (currentPlayerX< 1 + titleSize / 2 || currentPlayerX >= screenWith - (titleSize + titleSize / 2)||
                currentPlayerY < 1 + titleSize / 2 || currentPlayerY >= screenHeight - (titleSize + titleSize / 2)) {
            return;
        }

        playerX = currentPlayerX;
        playerY = currentPlayerY;







//        if (keyHandler.getDirection() == 'U'){
//
//        } else if (keyHandler.getDirection() == 'D') {
//
//        } else if (keyHandler.getDirection() == 'L') {
//
//        }else if (keyHandler.getDirection() == 'R'){
//
//        }




    }

    public Image getAnimationImage(Long second)  {

        Image currentAnimatiuonImage = null;
        try {

            if (second % 2 == 0) {
                currentAnimatiuonImage = ImageIO.read(getClass().getResourceAsStream("image/animationImage.png"));
            } else {
                currentAnimatiuonImage = ImageIO.read(getClass().getResourceAsStream("image/animationImage 2.png"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return currentAnimatiuonImage;
    }
    public Image getPlayerImage(String direction) {
        Image playerImage = null;

        try {


            switch (direction) {
                case "Up" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/chiprovskoChoveche.png"));
                case "Down" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/chiprovskoChoveche 2.png"));
                case "Left" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/chiprovskoChoveche 3.png"));
                case "Right" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/chiprovskoChoveche.png"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return playerImage;

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setBackground(Color.BLACK);



        image = getPlayerImage(direction);


        g2.drawImage(image,playerX,playerY,titleSize,titleSize,null);


        g2.drawImage(animationImage,screenWith/2 - titleSize,50,titleSize*2,titleSize*2,null);


        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, titleSize/2, screenHeight);
        g2.fillRect(0, 0, screenWith, titleSize/2);
        g2.fillRect(screenWith - titleSize/2, 0, titleSize/2, screenHeight);
        g2.fillRect(0, screenHeight - titleSize/2, screenWith, titleSize/2);




        if (!showTitleScreen){
            this.setBackground(Color.PINK);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            String title = "Chiprivsko choveche";
            String instruction = "Press SPACE to start";
            FontMetrics metrics = g.getFontMetrics();
            int titleX = (getWidth() - metrics.stringWidth(title)) / 2;
            int titleY = getHeight() / 3;
            int instructionX = (getWidth() - metrics.stringWidth(instruction)) / 2;
            int instructionY = getHeight() / 2;
            g.drawString(title, titleX, titleY);
            g.drawString(instruction, instructionX, instructionY);

        }

        g2.dispose();

    }
}
