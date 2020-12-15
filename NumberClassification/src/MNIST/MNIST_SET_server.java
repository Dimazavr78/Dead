package MNIST;

import MNIST.metrics.Metric;

import java.io.*;

public class MNIST_SET_server
{
    private MNIST_image[] images;

    public MNIST_SET_server(String imagesFilePath, String labelsFilePath) throws IOException
    {
        images = getImagesFromIDX(imagesFilePath, labelsFilePath);
    }

    public static MNIST_image[] getImagesFromIDX(String imagesFilePath, String labelsFilePath) throws IOException
    {
        if(imagesFilePath.equals(labelsFilePath)) {
            throw new RuntimeException("Файлы изображений и ярлыков не могут совпадать");
        }

        int imageBufferSize = 10000;
        int labelBufferSize = 1000;
        try (DataInputStream images_in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(imagesFilePath), imageBufferSize));
             DataInputStream labels_in = new DataInputStream(
                     new BufferedInputStream(
                             new FileInputStream(labelsFilePath), labelBufferSize)))
        {
            //Сигнатуры файла картинок
            images_in.skipNBytes(4); //Магическое число нам незачем
            int imagesCount = images_in.readInt();
            int height = images_in.readInt();
            int width = images_in.readInt();

            //Сигнатуры файла ярлыков
            labels_in.skipNBytes(4); //Магическое число нам незачем
            int labelsCount = labels_in.readInt();

            if(imagesCount != labelsCount) {
                throw new RuntimeException("Число изображений и ярлыков не совпадают");
            }

            MNIST_image[] images = new MNIST_image[imagesCount];

            for(int i=0; i<imagesCount; i++)
            {
                images[i] = new MNIST_image(width, height);
                images[i].setLabel((short) (labels_in.read()&0xff));
                for (int row=0; row<height; row++) {
                    for (int col=0; col<width; col++) {
                        images[i].setPixel(row, col, (short) (images_in.read()&0xff));
                    }
                }
            }
            return images;
        }
    }

    public int KNN_identify(MNIST_image image, Metric metric, int K)
    {
        if(K<1 || K+1 > images.length) {
            throw new RuntimeException("Некорректное число соседей");
        }

        int[] neighbours = new int[K];

        for(int i=0; i<K; i++)
        {
            double distance, minDistance = 0.0;
            int minDistanceIndex = 0;
            boolean alreadyIncluded;

            //Задаём минимальный элемент по умолчанию (первый попавшийся, не находящийся в neighbours[])
            for(int j=0; j<images.length; j++)
            {
                alreadyIncluded = false;
                for(int k=0; k<i; k++) {
                    if(j == neighbours[k]) {
                        alreadyIncluded = true;
                        break;
                    }
                }
                if(!alreadyIncluded) {
                    minDistance = metric.measure(image, images[j]);;
                    minDistanceIndex = j;
                    break;
                }
            }

            //Находим истинный минимальный элемент (наименьший среди всех, не находящихся в neighbours[])
            for(int j=0; j<images.length; j++)
            {
                distance = metric.measure(image, images[j]);
                alreadyIncluded = false;
                if(distance < minDistance) {
                    for(int k=0; k<i; k++) {
                        if(j == neighbours[k]) {
                            alreadyIncluded = true;
                            break;
                        }
                    }
                    if(!alreadyIncluded) {
                        minDistance = distance;
                        minDistanceIndex = j;
                    }
                }
            }
            neighbours[i] = minDistanceIndex;
        }

        //Находим самый распространённый элемент
        int maxCount=0, maxIndex=0;
        for(int i=0; i<neighbours.length; i++)
        {
            int count = 0;
            for (int neighbour : neighbours) {
                if (neighbours[i] == neighbour) {
                    count++;
                }
            }
            if(maxCount < count) {
                maxCount = count;
                maxIndex = i;
            }
        }

        return images[neighbours[maxIndex]].getLabel();
    }
}