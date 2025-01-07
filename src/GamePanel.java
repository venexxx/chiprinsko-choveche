import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable {

//    final int originalTitleSize = 16;
//    final int scale = 4;
//    final int titleSize = originalTitleSize * scale;
//
//    final int maxScreenCol = 16;
//    final int maxScreenRow = 12;
//    final int screenWith = titleSize * maxScreenCol;
//    final int screenHeight = titleSize * maxScreenRow;
//
//    double fps = 60;



    Screen screen = new Screen();

    private String direction = "Up";

    boolean showTitleScreen = true;

    Thread gameTread;
    KeyHandler keyHandler = new KeyHandler();
    Image image;
    Image animationImage;

    Image backpackImage;

    Image tokensImage;
    Image storageImage;


    int playerX = 350;
    int playerY = 450;

    int tokenX = screen.titleSize;
    int tokenY = screen.screenHeight / 2 - screen.titleSize;

    int storageX = screen.screenWith - screen.titleSize * 4;
    int storageY = screen.screenHeight / 2 - screen.titleSize;


    int trapX = 100;
    int trapY = 100;

    int trapXSpeed = 3;
    int trapYSpeed = 3;

    int backpackX = screen.screenWith / 2;
    int backpackY = screen.screenHeight - screen.titleSize * 2;


    int backpackGuardX = backpackX - 50;
    int backpackGuardY = screen.screenHeight - screen.titleSize;


    boolean guardFlag = true;


    int playerSpeed = 6;


    int playerCapacity = 0;
    int tokenCapacity = 8;
    int storageCapacity = 0;

    boolean isPlayerDeath = false;

    boolean isGameStarted = false;

    boolean isBackPackPicked = false;





    public GamePanel(){
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
            case 'S':
                showTitleScreen = false;
                isGameStarted = true;
                break;
            case 'r':
                showTitleScreen = true;
                isGameStarted = false;
                isPlayerDeath = false;
                break;
        }

        if (currentPlayerX< 1 + screen.titleSize / 2 || currentPlayerX >= screen.screenWith - (screen.titleSize + screen.titleSize / 2)||
                currentPlayerY < 1 + screen.titleSize / 2 || currentPlayerY >= screen.screenHeight - (screen.titleSize + screen.titleSize / 2)) {
            return;
        }

        if(((currentPlayerX >= 34 && currentPlayerX <= 180) && (currentPlayerY >= 262 && currentPlayerY <= 430)) || ((currentPlayerX >= 715 && currentPlayerX <= 900) && (currentPlayerY >= 256 && currentPlayerY <= 430))){


            return;
        }

        playerX = currentPlayerX;
        playerY = currentPlayerY;

        tokenMoving();
        trapMoving();
        backpackPicking();
        backpackGuardMoving();

    }

    public void trapMoving(){

        if (!isGameStarted || isPlayerDeath){
            trapX = 100;
            trapY = 100;
            playerX = 350;
            playerY = 450;

            playerCapacity = 0;
            tokenCapacity = 8;
            storageCapacity = 0;

            isBackPackPicked = false;
            return;
        }

        if (((trapY <= playerY + screen.titleSize) && (trapY + screen.titleSize * 2 >= playerY)) && ((trapX <= playerX + screen.titleSize && (trapX + screen.titleSize * 2 >= playerX)))){
            System.out.println("ok");
            isPlayerDeath = true;
        }

        trapX += trapXSpeed;
        trapY += trapYSpeed;

        if ((trapX <= 0 || trapX + screen.titleSize * 2 >= getWidth()) || ((trapX >= 33 && trapX <= 184) && (trapY >= 256 && trapY <= 430))) {
            trapXSpeed = -trapXSpeed;
        }
        if ((trapY <= 0 || trapY + screen.titleSize * 2 >= getHeight()) || ((trapX >= 712 && trapX <= 900) && (trapY >= 250 && trapY <= 436))) {
            trapYSpeed = -trapYSpeed;
        }




    }

    public void backpackPicking(){
        if (((playerY <= backpackY + screen.titleSize) && (playerY + screen.titleSize >= backpackY)) && ((playerX <= backpackX + screen.titleSize) && (playerX + screen.titleSize   >= backpackX))){
            isBackPackPicked = true;
        }
    }

    public void backpackGuardMoving(){

        if (((backpackGuardY <= playerY + screen.titleSize) && (backpackGuardY + 24 >= playerY)) && ((backpackGuardX <= playerX + screen.titleSize && (backpackGuardX + 24 >= playerX)))){
            System.out.println("ok");
            isPlayerDeath = true;
        }


        if (guardFlag) {
            if (((screen.screenHeight - screen.titleSize) - 100 != backpackGuardY) && (backpackX - 50) == backpackGuardX) {
                backpackGuardY-=2;
            } else if (((screen.screenHeight - screen.titleSize) - 100 == backpackGuardY) && (backpackX - 50) + 150 != backpackGuardX) {
                backpackGuardX+=2;
            } else if (((screen.screenHeight - screen.titleSize) != backpackGuardY) && (backpackX - 50) + 150 == backpackGuardX) {
                backpackGuardY+=2;
            }else {
                guardFlag = false;
            }
        }else {
           if (((screen.screenHeight - screen.titleSize) - 100 != backpackGuardY) && (backpackX - 50) + 150 == backpackGuardX){
               backpackGuardY-=2;
           } else if (((screen.screenHeight - screen.titleSize) - 100 == backpackGuardY) && (backpackX - 50)  != backpackGuardX) {
               backpackGuardX-=2;
           } else if (((screen.screenHeight - screen.titleSize) != backpackGuardY) && (backpackX - 50) == backpackGuardX) {
               backpackGuardY+=2;
           }else {
               guardFlag = true;
           }
        }



    }

    public void tokenMoving(){

        if (!isBackPackPicked){
            return;
        }

        if ((playerX >= 33 && playerX <= 184) && (playerY >= 256 && playerY <= 430)){
             if (playerCapacity < 4 && tokenCapacity > 0){
                playerCapacity++;
                tokenCapacity--;

            }
        }

        if ((playerX >= 710 && playerX <= 902) && (playerY >= 250 && playerY <= 436)){
            if (playerCapacity > 0 && storageCapacity < storageCapacity + playerCapacity){
                playerCapacity--;
                storageCapacity++;
            }
        }
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

    public Image getTokenImage(){

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

        Image storageImage = null;
        try {
            storageImage = ImageIO.read(getClass().getResourceAsStream("image/storages/storage" + storageCapacity + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return storageImage;
    }


    public Image getBackpackImage(){


        try {
            backpackImage = ImageIO.read(getClass().getResourceAsStream("image/backpack.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return backpackImage;
    }
    public Image getPlayerImage(String direction) {
        Image playerImage = null;

        try {


            if (!isBackPackPicked){
                switch (direction) {
                    case "Up" ->
                            playerImage = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche.png"));
                    case "Down" ->
                            playerImage = ImageIO.read(getClass().getResourceAsStream("image/down/chiprovskoChoveche.png"));
                    case "Left" ->
                            playerImage = ImageIO.read(getClass().getResourceAsStream("image/left/chiprovskoChoveche.png"));
                    case "Right" ->
                            playerImage = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche.png"));
                }
                return playerImage;
            }
            switch (direction) {
                case "Up" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche" + playerCapacity + ".png"));
                case "Down" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/down/chiprovskoChoveche" + playerCapacity + ".png"));
                case "Left" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/left/chiprovskoChoveche" + playerCapacity + ".png"));
                case "Right" ->
                        playerImage = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche" + playerCapacity + ".png"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return playerImage;

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;




        if (isPlayerDeath){
            g2.setColor(Color.WHITE);
            String message = "Game Over! Press R to Restart";
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2.drawString(message, x, y);
            return;
        }

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





        image = getPlayerImage(direction);

        tokensImage = getTokenImage();
        storageImage = getStorageImage();
        getBackpackImage();




        g2.drawImage(image,playerX,playerY,screen.titleSize,screen.titleSize,null);


        g2.drawImage(animationImage, trapX, trapY, screen.titleSize * 2, screen.titleSize * 2 , null);


        g2.drawImage(tokensImage,tokenX ,tokenY ,screen.titleSize * 2,screen.titleSize * 2,null);
        g2.drawImage(storageImage,storageX, storageY,screen.titleSize * 2,screen.titleSize * 2,null);

        if (!isBackPackPicked){
            g2.drawImage(backpackImage,backpackX, backpackY,screen.titleSize,screen.titleSize,null);
        }




        g2.setColor(Color.RED);
        g2.fillRect(backpackGuardX,backpackGuardY,24,24);

        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, screen.titleSize/2, screen.screenHeight);
        g2.fillRect(0, 0, screen.screenWith, screen.titleSize/2);
        g2.fillRect(screen.screenWith - screen.titleSize/2, 0, screen.titleSize/2, screen.screenHeight);
        g2.fillRect(0, screen.screenHeight - screen.titleSize/2, screen.screenWith, screen.titleSize/2);


        // Изобразяваме колко квадратчета е взел човеяето
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String playerCapacity = "Player capacity: " + this.playerCapacity;
        FontMetrics playerCounterMetrics = g2.getFontMetrics();
        int scoreX = (getWidth() - playerCounterMetrics.stringWidth(playerCapacity)) / 2;
        int scoreY = screen.originalTitleSize;
        g.drawString(playerCapacity, scoreX, scoreY);



        // Изобразяваме колко квадратчета остават в хранилишето
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String tokenCapacity = "Token remaining capacity: " + this.tokenCapacity;
        FontMetrics tokenCounterMetrics = g2.getFontMetrics();
        int tokenCounterX = (tokenCounterMetrics.stringWidth(tokenCapacity)) / 2;
        int tokenCounterY = screen.originalTitleSize;
        g.drawString(tokenCapacity, tokenCounterX, tokenCounterY);


        // Изобразяваме колко квадратчета са пренесени успесшно
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String storageCapacity = "Storage capacity: " + this.storageCapacity;
        FontMetrics storageCounterMetrics = g2.getFontMetrics();
        int storageCounterX = (getWidth() - storageCounterMetrics.stringWidth(storageCapacity) * 2);
        int storageCounterY = screen.originalTitleSize;
        g.drawString(storageCapacity, storageCounterX, storageCounterY);


        // Изобразяваме дали човечето е живо
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String isPlayerDeath = "Is player death: " + this.isPlayerDeath;
        FontMetrics isPlayerDeathMetrics = g2.getFontMetrics();
        int gameStatusX = (getWidth() - isPlayerDeathMetrics.stringWidth(isPlayerDeath)) / 2;
        int gameStatusY = getHeight() - screen.originalTitleSize;
        g.drawString(isPlayerDeath, gameStatusX, gameStatusY);

        g2.dispose();


    }
}
