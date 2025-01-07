import javax.imageio.ImageIO;
import java.awt.*;

public class Player extends Screen{



    int playerX = 350;
    int playerY = 450;

    Image image;

    int playerSpeed = 6;


    int playerCapacity;

    boolean isPlayerDeath;

    public Player() {
        this.playerCapacity = 0;
        this.isPlayerDeath = false;
    }



    //TODO after making trap class fix this method
    public void checkIfPlayerIsDeath(){

    }

    public void playerMove(int futurePlayerX, int futurePlayerY) {

        if (futurePlayerX< 1 + titleSize / 2 || futurePlayerX >= screenWith - (titleSize + titleSize / 2)||
                futurePlayerY < 1 + titleSize / 2 || futurePlayerY >= screenHeight - (titleSize + titleSize / 2)) {
            return;
        }

        if(((futurePlayerX >= 34 && futurePlayerX <= 180) && (futurePlayerY >= 262 && futurePlayerY <= 430)) || ((futurePlayerX >= 715 && futurePlayerX <= 900) && (futurePlayerY >= 256 && futurePlayerY <= 430))){
            return;
        }

        playerX = futurePlayerX;
        playerY = futurePlayerY;
    }

    public void getPlayerImage(String direction,Backpack backpack) {


        try {


            if (!backpack.isBackPackPicked){
                switch (direction) {
                    case "Up" ->
                            image = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche.png"));
                    case "Down" ->
                            image = ImageIO.read(getClass().getResourceAsStream("image/down/chiprovskoChoveche.png"));
                    case "Left" ->
                            image = ImageIO.read(getClass().getResourceAsStream("image/left/chiprovskoChoveche.png"));
                    case "Right" ->
                            image = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche.png"));
                }
                return;

            }
            switch (direction) {
                case "Up" ->
                        image = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche" + playerCapacity + ".png"));
                case "Down" ->
                        image = ImageIO.read(getClass().getResourceAsStream("image/down/chiprovskoChoveche" + playerCapacity + ".png"));
                case "Left" ->
                        image = ImageIO.read(getClass().getResourceAsStream("image/left/chiprovskoChoveche" + playerCapacity + ".png"));
                case "Right" ->
                        image = ImageIO.read(getClass().getResourceAsStream("image/rigthAndUp /chiprovskoChoveche" + playerCapacity + ".png"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }

}
