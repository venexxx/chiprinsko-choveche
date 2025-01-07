public class Screen {


    final int originalTitleSize = 16;
    final int scale = 4;
    final int titleSize = originalTitleSize * scale;

    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWith = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;

    double fps = 60;
}
