import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {





    Screen screen = new Screen();
    Player player;
    Backpack backpack;
    Tokens tokens;
    Storage storage;
    Trap trap;

    private String direction = "Up";

    boolean showTitleScreen = true;

    Thread gameTread;
    KeyHandler keyHandler = new KeyHandler();



    boolean isGameStarted = false;

    public GamePanel(){
        backpack = new Backpack();
        player = new Player();
        player.isPlayerDeath = false;
        tokens = new Tokens();
        storage = new Storage();
        trap = new Trap();
        this.setPreferredSize(new Dimension(screen.screenWith, screen.screenHeight));
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

        double drawInterval = (double) 1000000000 / screen.fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        long imageChangingTime;
        while (gameTread != null){


            currentTime = System.nanoTime();

            imageChangingTime = (System.currentTimeMillis() % 10000) /1000;

            trap.getAnimationImage(imageChangingTime);



            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime -lastTime);
            lastTime = currentTime;


            if (delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }



    public void update(){


        int futurePlayerX = player.playerX;
        int futurePlayerY = player.playerY;

        //Checking what key was clicked and based on that switching player direction
        //If player is death and was clicked R button game reset
        switch (keyHandler.getDirection()){
            case 'U':
                futurePlayerY -= player.playerSpeed;
                direction = "Up";
                break;
            case 'D':
                futurePlayerY += player.playerSpeed;
                direction = "Down";
                break;
            case 'L':
                futurePlayerX -= player.playerSpeed;
                direction = "Left";
                break;
            case 'R':
                futurePlayerX += player.playerSpeed;
                direction = "Right";
                break;
            case 'S':
                showTitleScreen = false;
                isGameStarted = true;
                break;
            case 'r':
                if (player.isPlayerDeath) {
                    resetGame();
                }
                showTitleScreen = true;
                isGameStarted = false;
                break;
        }

        //On every update these methods will be run if
        //the game is started and if not it does not allow the methods below to be executed
        if (!isGameStarted){
            return;
        }
        backpack.backpackGuardMove();
        player.checkIfPlayerStepOnBackpackGuard(backpack);
        player.checkIfPlayerIsOnBackpack(backpack);
        player.playerMove(futurePlayerX,futurePlayerY);
        tokens.tokenMoving(backpack,player,storage);
        trap.trapMoving(player);





    }

    public void resetGame(){
            player = new Player();
            trap = new Trap();
            tokens = new Tokens();
            storage = new Storage();
            backpack = new Backpack();
    }






    public Image getAnimationImage(Long second)  {

        //Switch trap image every even second
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








    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;




        //Print end game screen when player is death
        if (player.isPlayerDeath){
            g2.setColor(Color.WHITE);
            String message = "Game Over! Press R to Restart";
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2.drawString(message, x, y);
            return;
        }


        //Print start game screen
        if (showTitleScreen){
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            String title = "Chiprivsko choveche";
            String instruction = "Press SPACE to start";
            FontMetrics metrics = g2.getFontMetrics();
            int titleX = (getWidth() - metrics.stringWidth(title)) / 2;
            int titleY = getHeight() / 3;
            int instructionX = (getWidth() - metrics.stringWidth(instruction)) / 2;
            int instructionY = getHeight() / 2;
            g2.drawString(title, titleX, titleY);
            g2.drawString(instruction, instructionX, instructionY);
            g2.dispose();
        }






        tokens.getTokenImage();
        storage.getStorageImage();

        player.getPlayerImage(direction,backpack);
        backpack.getBackpackImage();




        //print player image
        g2.drawImage(player.image,player.playerX,player.playerY,screen.titleSize,screen.titleSize,null);


        //print trap image
        g2.drawImage(trap.animationImage, trap.trapX, trap.trapY, screen.titleSize * 2, screen.titleSize * 2 , null);


        //print main token storage image
        g2.drawImage(tokens.tokensImage,tokens.tokenX ,tokens.tokenY ,screen.titleSize * 2,screen.titleSize * 2,null);
        //print storage image
        g2.drawImage(storage.storageImage,storage.storageX, storage.storageY,screen.titleSize * 2,screen.titleSize * 2,null);


        //Check if backpack is picked -> if it isnt  print separate backpack image
        if (!player.isBackPackPicked){
            g2.drawImage(backpack.backpackImage,backpack.backpackX, backpack.backpackY,screen.titleSize,screen.titleSize,null);
        }




        //print backpack guard rectangle
        g2.setColor(Color.RED);
        g2.fillRect(backpack.backpackGuardX,backpack.backpackGuardY,24,24);


        //print screen side frames
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, screen.titleSize/2, screen.screenHeight);
        g2.fillRect(0, 0, screen.screenWith, screen.titleSize/2);
        g2.fillRect(screen.screenWith - screen.titleSize/2, 0, screen.titleSize/2, screen.screenHeight);
        g2.fillRect(0, screen.screenHeight - screen.titleSize/2, screen.screenWith, screen.titleSize/2);


        // print how many tokens player have in his backpack
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String playerCapacity = "Player capacity: " + this.player.playerCapacity;
        FontMetrics playerCounterMetrics = g2.getFontMetrics();
        int scoreX = (getWidth() - playerCounterMetrics.stringWidth(playerCapacity)) / 2;
        int scoreY = screen.originalTitleSize;
        g.drawString(playerCapacity, scoreX, scoreY);



        // print how many tokens are left in main storage
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String tokenCapacity = "Token remaining capacity: " + this.tokens.tokenCapacity;
        FontMetrics tokenCounterMetrics = g2.getFontMetrics();
        int tokenCounterX = (tokenCounterMetrics.stringWidth(tokenCapacity)) / 2;
        int tokenCounterY = screen.originalTitleSize;
        g.drawString(tokenCapacity, tokenCounterX, tokenCounterY);


        // print how many token are transferred successfully
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String storageCapacity = "Storage capacity: " + storage.storageCapacity;
        FontMetrics storageCounterMetrics = g2.getFontMetrics();
        int storageCounterX = (getWidth() - storageCounterMetrics.stringWidth(storageCapacity) * 2);
        int storageCounterY = screen.originalTitleSize;
        g.drawString(storageCapacity, storageCounterX, storageCounterY);


        // print if player is alive
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String isPlayerDeath = "Is player death: " + player.isPlayerDeath;
        FontMetrics isPlayerDeathMetrics = g2.getFontMetrics();
        int gameStatusX = (getWidth() - isPlayerDeathMetrics.stringWidth(isPlayerDeath)) / 2;
        int gameStatusY = getHeight() - screen.originalTitleSize;
        g.drawString(isPlayerDeath, gameStatusX, gameStatusY);

        g2.dispose();


    }
}
