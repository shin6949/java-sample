package com.cocoblue.movingcar.object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Car {
    private BufferedImage image;
    private int x;
    private int y;

    public Car(String imagePath, int x, int y) throws IOException {
        this.image = ImageIO.read(new File(imagePath));
        this.x = x;
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
