import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class ImageCollector {

    public abstract List<Mat> getImages();


    BufferedImage downloadImage(String urlString) {
        BufferedImage img = null;
        try {
            URL url = new URL(urlString);
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    Mat bufferedImageToMat(BufferedImage img) {
        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);

        mat.put(0, 0, pixels);

        return mat;
    }

}
