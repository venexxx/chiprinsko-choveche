import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {





    Screen screen = new Screen();
    Player player;
    Backpack backpack;

    private String direction = "Up";

    boolean showTitleScreen = true;

    Thread gameTread;
    KeyHandler keyHandler = new KeyHandler();




    Image animationImage;


    Image tokensImage;
    Image storageImage;





    int tokenX = screen.titleSize;
    int tokenY = screen.screenHeight / 2 - screen.titleSize;

    int storageX = screen.screenWith - screen.titleSize * 4;
    int storageY = screen.screenHeight / 2 - screen.titleSize;


    int trapX = 100;
    int trapY = 100;

    int trapXSpeed = 3;
    int trapYSpeed = 3;







    int tokenCapacity = 8;
    int storageCapacity = 0;


    boolean isGameStarted = false;






    public GamePanel(){
        backpack = new Backpack();
        player = new Player();
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

            animationImage = getAnimationImage(imageChangingTime);



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
                showTitleScreen = true;
                isGameStarted = false;
                player.isPlayerDeath = false;
                break;
        }

        //On every update these methods will be run
        backpack.checkIfPlayerIsOnBackpack(player);
        player =  backpack.backpackGuardMove(player);

        player.playerMove(futurePlayerX,futurePlayerY);
        tokenMoving();
        trapMoving();


    }

    public void trapMoving(){

        //TODO this if statement to be moved in Player class
        if (!isGameStarted || player.isPlayerDeath){
            trapX = 100;
            trapY = 100;
            player.playerX = 350;
            player.playerY = 450;

            player.playerCapacity = 0;
            tokenCapacity = 8;
            storageCapacity = 0;

            backpack.isBackPackPicked = false;
            return;
        }

        //Checks if player was hit by moving trap
        if (((trapY <= player.playerY + screen.titleSize) && (trapY + screen.titleSize * 2 >= player.playerY)) && ((trapX <= player.playerX + screen.titleSize && (trapX + screen.titleSize * 2 >= player.playerX)))){
            System.out.println("ok");
            player.isPlayerDeath = true;
        }

        //and if was not move player
        trapX += trapXSpeed;
        trapY += trapYSpeed;


        //Move trap on screen like dvd logo on opposite direction every time trap hits wall
        if ((trapX <= 0 || trapX + screen.titleSize * 2 >= getWidth()) || ((trapX >= 33 && trapX <= 184) && (trapY >= 256 && trapY <= 430))) {
            trapXSpeed = -trapXSpeed;
        }
        if ((trapY <= 0 || trapY + screen.titleSize * 2 >= getHeight()) || ((trapX >= 712 && trapX <= 900) && (trapY >= 250 && trapY <= 436))) {
            trapYSpeed = -trapYSpeed;
        }




    }




    public void tokenMoving(){

        //Checks if backpack was picked and if was not you cant move the tokens from storage
        if (!backpack.isBackPackPicked){
            return;
        }

        //Check if player is on token and decreases token capacity
        if ((player.playerX >= 33 && player.playerX <= 184) && (player.playerY >= 256 && player.playerY <= 430)){
             if (player.playerCapacity < 4 && tokenCapacity > 0){
                 player.playerCapacity++;
                tokenCapacity--;

            }
        }

        //Check if player is on storage and put all token that are collected in backpack to storage sector
        if ((player.playerX >= 710 && player.playerX <= 902) && (player.playerY >= 250 && player.playerY <= 436)){
            if (player.playerCapacity > 0 && storageCapacity < storageCapacity + player.playerCapacity){
                player.playerCapacity--;
                storageCapacity++;
            }
        }
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

    public Image getTokenImage(){

        //Get main token storage image based on how mane tokens are moved
        Image tokenImage = null;
        try {
            if (tokenCapacity == 8){
                tokenImage = ImageIO.read(getClass().getResourceAsStream("image/tokens/tokens.png"));
            }else {
               int tokenImageIndex = tokenCapacity + 1;
                tokenImage = ImageIO.read(getClass().getResourceAsStream("image/tokens/tokens" + tokenImageIndex + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokenImage;
    }

    public Image getStorageImage(){

        //Get storage image based on how mane tokens are moved
        Image storageImage = null;
        try {
            storageImage = ImageIO.read(getClass().getResourceAsStream("image/storages/storage" + storageCapacity + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return storageImage;
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






        tokensImage = getTokenImage();
        storageImage = getStorageImage();

        player.getPlayerImage(direction,backpack);
        backpack.getBackpackImage();




        //print player image
        g2.drawImage(player.image,player.playerX,player.playerY,screen.titleSize,screen.titleSize,null);


        //print trap image
        g2.drawImage(animationImage, trapX, trapY, screen.titleSize * 2, screen.titleSize * 2 , null);


        //print main token storage image
        g2.drawImage(tokensImage,tokenX ,tokenY ,screen.titleSize * 2,screen.titleSize * 2,null);
        //print storage image
        g2.drawImage(storageImage,storageX, storageY,screen.titleSize * 2,screen.titleSize * 2,null);


        //Check if backpack is picked -> if it isnt  print separate backpack image
        if (!backpack.isBackPackPicked){
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
        String tokenCapacity = "Token remaining capacity: " + this.tokenCapacity;
        FontMetrics tokenCounterMetrics = g2.getFontMetrics();
        int tokenCounterX = (tokenCounterMetrics.stringWidth(tokenCapacity)) / 2;
        int tokenCounterY = screen.originalTitleSize;
        g.drawString(tokenCapacity, tokenCounterX, tokenCounterY);


        // print how many token are transferred successfully
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String storageCapacity = "Storage capacity: " + this.storageCapacity;
        FontMetrics storageCounterMetrics = g2.getFontMetrics();
        int storageCounterX = (getWidth() - storageCounterMetrics.stringWidth(storageCapacity) * 2);
        int storageCounterY = screen.originalTitleSize;
        g.drawString(storageCapacity, storageCounterX, storageCounterY);


        // print if player is alive
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String isPlayerDeath = "Is player death: " + this.player.isPlayerDeath;
        FontMetrics isPlayerDeathMetrics = g2.getFontMetrics();
        int gameStatusX = (getWidth() - isPlayerDeathMetrics.stringWidth(isPlayerDeath)) / 2;
        int gameStatusY = getHeight() - screen.originalTitleSize;
        g.drawString(isPlayerDeath, gameStatusX, gameStatusY);

        g2.dispose();


    }
}
