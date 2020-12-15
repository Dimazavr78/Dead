import MNIST.MNIST_SET_server;

import java.io.*;

public class Main {

    public static void main(String[] args)
    {
      /*  try(InputStream in = new FileInputStream("src/Numbers data/t10k-images.idx3-ubyte"))
        {
            in.skipNBytes(16+784+784+784);
            MNIST.MNIST_image image = new MNIST.MNIST_image(28, 28);
            for (int i=0; i<28; i++) {
                for (int j=0; j<28; j++) {
                    image.setPixel(i, j, (short)(in.read()&0xff));
                }
            }
            System.out.println(image);
            MNIST.MNIST_SET_server identifier = new MNIST.MNIST_SET_server(
                    "src/Numbers data/train-images.idx3-ubyte",
                    "src/Numbers data/train-labels.idx1-ubyte");
            System.out.println("Идентифицировано: " + identifier.KNN_identify(image, new MNIST.metrics.AngleMetric(), 3));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try
        {
            MNIST_SET_server identifier = new MNIST_SET_server (
                    "src/Numbers data/train-images.idx3-ubyte",
                    "src/Numbers data/train-labels.idx1-ubyte");

            ErrorsCounter errors = new ErrorsCounter();
            Thread[] threads = new Thread[10];

            //Запускаем 10 потоков, каждый определяет свою 1000 картинок
            for(int i=0; i<threads.length; i++) {
                threads[i] = new MNIST_ImagesIdentifyThread (
                        MNIST_SET_server.getImagesFromIDX(
                                "src/Numbers data/t10k-images.idx3-ubyte",
                                "src/Numbers data/t10k-labels.idx1-ubyte"
                        ), identifier,i*1000, i*1000+1000, errors);
                threads[i].start();
            }

            //Ожидаем завершения потоков
            for(Thread thread: threads) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) {}
            }

            System.out.println("\nТочность алгоритма: " + (100 - errors.getErrorsCounter()/100) + "%");

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
