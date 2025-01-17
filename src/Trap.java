import javax.imageio.ImageIO;
import java.awt.*;

public class Trap extends Screen {

    Image animationImage;
    int trapX = 100;
    int trapY = 100;

    int trapXSpeed = 3;
    int trapYSpeed = 3;


    public void trapMoving(Player player){


        //Checks if player was hit by moving trap
        if (((this.trapY <= player.playerY + this.titleSize) && (this.trapY + this.titleSize * 2 >= player.playerY)) && ((this.trapX <= player.playerX + this.titleSize && (this.trapX + this.titleSize * 2 >= player.playerX)))){
            System.out.println("ok");
            player.isPlayerDeath = true;
        }

        //and if was not move player
        this.trapX += this.trapXSpeed;
        this.trapY += this.trapYSpeed;


        //Move trap on screen like dvd logo on opposite direction every time trap hits wall
        if ((this.trapX <= 0 || this.trapX + this.titleSize * 2 >= this.screenWith) || ((this.trapX >= 33 && this.trapX <= 184) && (this.trapY >= 256 && this.trapY <= 430))) {
            this.trapXSpeed =- this.trapXSpeed;
        }
        if ((this.trapY <= 0 || this.trapY + this.titleSize * 2 >= this.screenHeight) || ((this.trapX >= 712 && this.trapX <= 900) && (this.trapY >= 250 && this.trapY <= 436))) {
            this.trapYSpeed =- this.trapYSpeed;
        }
    }

    public void getAnimationImage(Long second)  {

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

       animationImage = currentAnimatiuonImage;
    }
}
