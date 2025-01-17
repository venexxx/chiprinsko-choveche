import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Backpack extends Screen{




    Image backpackImage;

    int backpackX;
    int backpackY;


    int backpackGuardX;
    int backpackGuardY ;


    boolean guardFlag;


    public Backpack() {
        backpackX = this.screenWith / 2;
        backpackY = this.screenHeight - this.titleSize * 2;
        backpackGuardX = backpackX - 50;
        backpackGuardY = this.screenHeight - this.titleSize;

        guardFlag = true;
    }






    public void backpackGuardMove(){



       //guard moving is splitted on half this flag is for first half of the way of the guard
        if (guardFlag) {
            if (((this.screenHeight - this.titleSize) - 100 != backpackGuardY) && (backpackX - 50) == backpackGuardX) {
                backpackGuardY-=2;
            } else if (((this.screenHeight - this.titleSize) - 100 == backpackGuardY) && (backpackX - 50) + 150 != backpackGuardX) {
                backpackGuardX+=2;
            } else if (((this.screenHeight - this.titleSize) != backpackGuardY) && (backpackX - 50) + 150 == backpackGuardX) {
                backpackGuardY+=2;
            }else {
                guardFlag = false;
            }
        }else {
            if (((this.screenHeight - this.titleSize) - 100 != backpackGuardY) && (backpackX - 50) + 150 == backpackGuardX){
                backpackGuardY-=2;
            } else if (((this.screenHeight - this.titleSize) - 100 == backpackGuardY) && (backpackX - 50)  != backpackGuardX) {
                backpackGuardX-=2;
            } else if (((this.screenHeight - this.titleSize) != backpackGuardY) && (backpackX - 50) == backpackGuardX) {
                backpackGuardY+=2;
            }else {
                guardFlag = true;
            }
        }





    }


    public void getBackpackImage(){

        try {
            backpackImage = ImageIO.read(getClass().getResourceAsStream("image/backpack.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
