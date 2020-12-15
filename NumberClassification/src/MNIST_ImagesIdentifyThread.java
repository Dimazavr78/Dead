import MNIST.metrics.EuclideanMetric;
import MNIST.MNIST_SET_server;
import MNIST.MNIST_image;

public class MNIST_ImagesIdentifyThread extends Thread
{
    MNIST_image[] images;
    MNIST_SET_server identifier;
    int from;
    int to;
    final ErrorsCounter errorsCounter;

    public MNIST_ImagesIdentifyThread (
            MNIST_image[] images, MNIST_SET_server identifier, int from, int to, ErrorsCounter errorsCounter
    ) {
        if(from<0 || to>images.length) {
            throw new RuntimeException("Некорректный индекс: выход за границы массива");
        }
        this.images = images;
        this.identifier = identifier;
        this.from = from;
        this.to = to;
        this.errorsCounter = errorsCounter;
    }

    @Override
    public void run()
    {
        int identified;
        int localErrorsCounter = 0;
        for(int i=from; i<to; i++) {
            identified = identifier.KNN_identify(images[i], new EuclideanMetric(), 3);
            if(identified != images[i].getLabel()) {
                synchronized (errorsCounter) {
                    errorsCounter.inc();
                    localErrorsCounter++;
                }
            }
            System.out.println("|Поток " + getName() + "| осталось: " + (to-i-1) + ", ошибок: " + localErrorsCounter);
        }
    }
}
