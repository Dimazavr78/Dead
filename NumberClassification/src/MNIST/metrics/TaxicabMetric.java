package MNIST.metrics;

import MNIST.MNIST_image;

public class TaxicabMetric extends Metric
{
    @Override
    public double measure(MNIST_image image_A, MNIST_image image_B) {
        super.compare_dimension(image_A, image_B);
        double distance = 0.0;
        for(int row=0; row<image_A.getHeight(); row++) {
            for(int col=0; col<image_A.getWidth(); col++) {
                distance += Math.abs(image_A.getPixel(row, col) - image_B.getPixel(row, col));
            }
        }
        return distance;
    }
}