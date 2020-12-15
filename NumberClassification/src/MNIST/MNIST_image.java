package MNIST;

public class MNIST_image
{
    private short [][] pixels;
    private int width;
    private int height;
    private short label;

    public MNIST_image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new short[height][width];
    }

    public void setImage(short[][] pixels) {
        if(height*width != pixels.length) {
            throw new RuntimeException("Изображение не соответствует объявленным размерам");
        }
        this.pixels = pixels;
    }

    public void setLabel(short label) {
        this.label = label;
    }

    public void setPixel(int row, int col, short value) {
        pixels[row][col] = value;
    }

    public int getPixel(int row, int col) {
        return pixels[row][col];
    }

    public int getLabel() {
        return label;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        String image = "";
        for(int row=0; row<height; row++) {
            for(int col=0; col<width; col++) {
                image = image.concat(String.format("%4d", pixels[row][col]));
            }
            image = image.concat("\n");
        }
        return image;
    }
}