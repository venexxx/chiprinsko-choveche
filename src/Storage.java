import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Storage extends Screen{

    Image storageImage;

    int storageX = super.screenWith - super.titleSize * 4;
    int storageY = super.screenHeight / 2 - super.titleSize;
    int storageCapacity;



    public Storage() {
        storageCapacity = 0;
    }


    public void getStorageImage(){

        //Get storage image based on how many tokens are moved
        Image storageImage = null;
        try {
            storageImage = ImageIO.read(getClass().getResourceAsStream("image/storages/storage" + this.storageCapacity + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       this.storageImage =storageImage;
    }
}
