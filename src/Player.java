import javax.imageio.ImageIO;
import java.awt.*;

public class Player extends Screen{



    int playerX = 350;
    int playerY = 450;

    Image image;

    int playerSpeed = 6;


    int playerCapacity;

    boolean isPlayerDeath;

    boolean isBackPackPicked;

    public Player() {
        this.playerCapacity = 0;
    }





    public void playerMove(int futurePlayerX, int futurePlayerY) {

        //checks if the player has reached the screen border
        if (futurePlayerX< 1 + titleSize / 2 || futurePlayerX >= screenWith - (titleSize + titleSize / 2)||
                futurePlayerY < 1 + titleSize / 2 || futurePlayerY >= screenHeight - (titleSize + titleSize / 2)) {
            return;
        }

        if(((futurePlayerX >= 34 && futurePlayerX <= 180) && (futurePlayerY >= 262 && futurePlayerY <= 430)) || ((futurePlayerX >= 715 && futurePlayerX <= 900) && (futurePlayerY >= 256 && futurePlayerY <= 430))){
            return;
        }

        //if it has not reached the screen borders, it changes its position
        playerX = futurePlayerX;
        playerY = futurePlayerY;
    }

    public void getPlayerImage(String direction,Backpack backpack) {


        try {


            //take the picture based on which side the player is looking when he hasnt picked the backpack.
            if (!this.isBackPackPicked){
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

            //take the picture based on which side the player is looking when he has picked the backpack.
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

    public void checkIfPlayerIsOnBackpack(Backpack backpack){
        if (((this.playerY <= backpack.backpackY + this.titleSize) && (this.playerY + this.titleSize >= backpack.backpackY)) && ((this.playerX <= backpack.backpackX + this.titleSize) && (this.playerX + this.titleSize   >= backpack.backpackX))){
            this.isBackPackPicked = true;
        }
    }

    public void checkIfPlayerStepOnBackpackGuard(Backpack backpack){
        if (((backpack.backpackGuardY <= this.playerY + this.titleSize) && (backpack.backpackGuardY + 24 >= this.playerY)) && ((backpack.backpackGuardX <= this.playerX + this.titleSize && (backpack.backpackGuardX + 24 >= this.playerX)))){
            this.isPlayerDeath = true;
        }

    }

}
