import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Tokens extends Screen {


    Image tokensImage;


    int tokenX = titleSize;
    int tokenY = screenHeight / 2 - titleSize;

    int tokenCapacity;



    public Tokens() {
        this.tokenCapacity = 8;

    }


    public void tokenMoving(Backpack backpack,Player player,Storage storage){

        //Checks if backpack was picked and if was not you cant move the tokens from storage
        if (!backpack.isBackPackPicked){
            return;
        }

        //Check if player is on token and decreases token capacity
        if ((player.playerX >= 33 && player.playerX <= 184) && (player.playerY >= 256 && player.playerY <= 430)){
            if (player.playerCapacity < 4 && this.tokenCapacity > 0){
                player.playerCapacity++;
                this.tokenCapacity--;

            }
        }

        //Check if player is on storage and put all token that are collected in backpack to storage sector
        if ((player.playerX >= 710 && player.playerX <= 902) && (player.playerY >= 250 && player.playerY <= 436)){
            if (player.playerCapacity > 0 && storage.storageCapacity < storage.storageCapacity + player.playerCapacity){
                player.playerCapacity--;
                storage.storageCapacity++;
            }
        }
    }

    public void getTokenImage(){

        //Get main token storage image based on how mane tokens are moved
        Image tokenImage = null;
        try {
            if (this.tokenCapacity == 8){
                tokenImage = ImageIO.read(getClass().getResourceAsStream("image/tokens/tokens.png"));
            }else {
                int tokenImageIndex = this.tokenCapacity + 1;
                tokenImage = ImageIO.read(getClass().getResourceAsStream("image/tokens/tokens" + tokenImageIndex + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.tokensImage =tokenImage;
    }
}
