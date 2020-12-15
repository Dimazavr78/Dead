package MNIST.metrics;

import MNIST.MNIST_image;

public abstract class Metric
{
    public abstract double measure(MNIST_image image_A, MNIST_image image_B);
    
    protected void compare_dimension(MNIST_image image_A, MNIST_image image_B) {
        if(image_A.getWidth() != image_B.getWidth()
                || image_A.getHeight() != image_B.getHeight()) {
            throw new RuntimeException("Мерности объектов не совпалдают");
        }
    }
}