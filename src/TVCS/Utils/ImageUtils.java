package TVCS.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by ina on 2017-11-17.
 */
public class ImageUtils {
    public static int SIMILAR_THRESHHOLD = 50;

    public static Mat BufferedImageToMat(BufferedImage image) {
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_32SC4);
        int[] data = new int[image.getWidth() * image.getHeight() * (int)mat.elemSize()];
        int[] dataRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0 ; i < dataRGB.length; i++) {
            data[i * 4] = ((dataRGB[i] >> 24) & 0xFF);
            data[i * 4 + 1] = ((dataRGB[i] >> 16) & 0xFF);
            data[i * 4 + 2] = ((dataRGB[i] >> 8) & 0xFF);
            data[i * 4 + 3] = ((dataRGB[i] >> 0) & 0xFF);
        }
        mat.put(0, 0, data);
        return mat;
    }

    public static BufferedImage MatToBufferedImage(Mat mat) {
        int[] data = new int[mat.rows()*mat.cols()*mat.channels()];
        int[] dataRGB = new int[mat.rows()*mat.cols()];
        mat.get(0, 0, data);
        for (int i = 0 ; i < dataRGB.length; i++) {
            dataRGB[i] = ((data[i * 4] << 24) & 0xFF000000) +
                    ((data[i * 4 + 1] << 16) & 0x00FF0000) +
                    ((data[i * 4 + 2] << 8) & 0x0000FF00) +
                    ((data[i * 4 + 3] << 0) & 0x000000FF);
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), dataRGB, 0, image.getWidth());
        return image;
    }

    public static BufferedImage CopyBufferedImage(BufferedImage image) {
        int[] dataRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        int[] newDataRGB = dataRGB.clone();
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.setRGB(0, 0, newImage.getWidth(), newImage.getHeight(), newDataRGB, 0, newImage.getWidth());
        return newImage;
    }

    public static BufferedImage AlphaRepresentingImage(BufferedImage image) {
        int[] dataRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        int[] newDataRGB = new int[image.getWidth() * image.getHeight()];
        for (int i = 0; i < dataRGB.length; i++) {
            /*if (((dataRGB[i] >> 24) & 0xFF) == 0) {
                newDataRGB[i] = 0xFF000000;
                newDataRGB[i] += 255 << 8;
            }
            else {*/
                newDataRGB[i] = dataRGB[i];
            //}
        }
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.setRGB(0, 0, image.getWidth(), image.getHeight(), newDataRGB, 0, image.getWidth());
        return newImage;
    }

    public static BufferedImage TransParentFloodFilledImage(BufferedImage image, int x, int y) {
        int[] dataRGB = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        floodFill(dataRGB, image.getWidth(), image.getHeight(), image.getRGB(x,y));
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.setRGB(0, 0, newImage.getWidth(), newImage.getHeight(), dataRGB, 0, newImage.getWidth());
        return newImage;
    }

    private static void floodFill(int[] dataRGB, int cols, int rows, int color) {
       for (int j = 0 ; j < rows; j++) {
           for (int i = 0 ; i < cols ; i++) {
               if (similarColor(dataRGB[j * cols + i], color)) {
                   dataRGB[j * cols + i] = 0;
               }
           }
       }
    }

    static private boolean similarColor(int color1, int color2) {
        int valueDiff = 0;
        valueDiff += Math.abs(((color1 >> 16) & 0xFF) - ((color2 >> 16) & 0xFF));
        valueDiff += Math.abs(((color1 >> 8) & 0xFF) - ((color2>> 8) & 0xFF));
        valueDiff += Math.abs(((color1 >> 0) & 0xFF) - ((color2 >> 0) & 0xFF));
        return valueDiff < SIMILAR_THRESHHOLD;
    }

    public static BufferedImage ResizeBufferedImage(BufferedImage image, int width, int height, boolean preserveRatio) {
        if (preserveRatio) {
            double widthRatio = (double) width / (double)image.getWidth();
            double heightRatio = (double) height / (double) image.getHeight();
            if (widthRatio < heightRatio) {
                height = (int) ((double) image.getHeight() * widthRatio);
            } else {
                width = (int) ((double) image.getWidth() * heightRatio);
            }
        }
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return newImage;
    }

    public static Image BufferedImageToFxImage(BufferedImage image) {
        return SwingFXUtils.toFXImage(image, null);
    }

    public static  BufferedImage MergeBufferedImages(int width, int height, ArrayList<BufferedImage> images) {
        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0 ; j < height ; j++) {
            for (int i = 0; i < width; i++) {
                /*int rgb = 0;
                for (int index = images.size() - 1; index >= 0; index--) {
                    BufferedImage selectedImage = images.get(index);
                    if (i >= selectedImage.getWidth() || j >= selectedImage.getHeight())
                        continue;
                    int thisRGB = selectedImage.getRGB(i, j);
                    if ((thisRGB >> 24) != 0) {
                        rgb = thisRGB;
                        break;
                    }
                }*/
                int rgb = 0;
                if (images.size() == 0) {
                    mergedImage.setRGB(i, j, 0);
                    continue;
                }
                if (i < images.get(0).getWidth() && j < images.get(0).getHeight())
                    rgb = images.get(0).getRGB(i, j);

                for (int index = 1; index < images.size(); index++) {
                    int paint = 0;
                    if (i < images.get(index).getWidth() && j < images.get(index).getHeight())
                        paint = images.get(index).getRGB(i, j);
                    rgb = MixARGB(rgb, paint);
                }
                mergedImage.setRGB(i, j, rgb);
            }
        }

        return mergedImage;
    }

    public static int MixARGB (int background, int paint) {
        int ba = (background >> 24) & 0xFF;
        int br = (background >> 16) & 0xFF;
        int bg = (background >> 8) & 0xFF;
        int bb = (background >> 0) & 0xFF;

        int pa = (paint >> 24) & 0xFF;
        int pr = (paint >> 16) & 0xFF;
        int pg = (paint >> 8) & 0xFF;
        int pb = (paint >> 0) & 0xFF;

        int ra = pa + (ba * (255 - pa) / 255);
        int rr = (pr * pa / 255) + (br * ba * (255 - pa) / (255 * 255));
        int rg = (pg * pa / 255) + (bg * ba * (255 - pa) / (255 * 255));
        int rb = (pb * pa / 255) + (bb * ba * (255 - pa) / (255 * 255));

        int result = (ra << 24) + (rr << 16) + (rg << 8) + (rb << 0);
        return result;
    }

    public static boolean isImageDifferent(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
            return true;
        for (int j = 0 ; j < image1.getHeight(); j++) {
            for (int i = 0 ; i < image1.getWidth(); i++) {
                if (image1.getRGB(i, j) != image2.getRGB(i, j))
                    return true;
            }
        }
        return false;
    }
}
