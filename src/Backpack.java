import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Backpack{



    Screen screen = new Screen();
    Image backpackImage;

    int backpackX = screen.screenWith / 2;
    int backpackY = screen.screenHeight - screen.titleSize * 2;


    int backpackGuardX = backpackX - 50;
    int backpackGuardY = screen.screenHeight - screen.titleSize;

    boolean isBackPackPicked = false;
    boolean guardFlag = true;


    public void checkIfPlayerIsOnBackpack(Player player){
        if (((player.playerY <= backpackY + screen.titleSize) && (player.playerY + screen.titleSize >= backpackY)) && ((player.playerX <= backpackX + screen.titleSize) && (player.playerX + screen.titleSize   >= backpackX))){
            isBackPackPicked = true;
        }
    }


    public Player checkIfPlayerStepOnBackpackGuard(Player player){
        if (((backpackGuardY <= player.playerY + screen.titleSize) && (backpackGuardY + 24 >= player.playerY)) && ((backpackGuardX <= player.playerX + screen.titleSize && (backpackGuardX + 24 >= player.playerX)))){
            player.isPlayerDeath = true;
        }
        return player;
    }

    public Player backpackGuardMove(Player player){

       player  = checkIfPlayerStepOnBackpackGuard(player);

       //guard moving is splitted on half this flag is for first half of the way of the guard
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

        return player;



    }


    public void getBackpackImage(){

        try {
            backpackImage = ImageIO.read(getClass().getResourceAsStream("image/backpack.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
