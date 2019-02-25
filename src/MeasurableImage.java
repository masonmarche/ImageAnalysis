import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.* ;
import java.io.*;

public class MeasurableImage {

    private Mat mat;
    private Size size;

    public MeasurableImage( Mat mat ) {
        this.mat = mat ;
        size = mat.size() ;
        System.out.print( getAvgColor() ) ;
    }

    public static void main ( String[] args ) throws IOException {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        File file = new File( "src/test.png" ) ;
        BufferedImage img = ImageIO.read( file ) ;
        int rows = img.getWidth();
        int cols = img.getHeight();
        int type = CvType.CV_8U;
        Mat newMat = new Mat(rows,cols,type);
        for(int r=0; r<rows; r++){
            for(int c=0; c<cols; c++){
                newMat.put(r, c, img.getRGB(r, c));
            }
        }
        new MeasurableImage( newMat ) ;
    }

    public double[] getAvgColor() {
        double[] color = {0,0,0} ;
        for ( int i = 0 ; i < size.height ; i++ ) {
            for ( int j = 0 ; j < size.width ; j++ ) {
                double[] data = mat.get( i, j ) ;
                color[0] += data[0] ;
                color[1] += data[1] ;
                color[2] += data[2] ;
            }
        }
        color[0] /= ( size.width * size.height ) ;
        color[1] /= ( size.width * size.height ) ;
        color[2] /= ( size.width * size.height ) ;
        return color ;
    }

    public int getMode() {
        HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        Mat temp = new Mat((int) size.height, (int) size.width, CvType.CV_8U);
        Imgproc.cvtColor(mat, temp, Imgproc.COLOR_RGB2HSV);
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                int color = getColor(temp, i, j);
                if (colorMap.keySet().contains(color)) {
                    colorMap.replace(color, colorMap.get(color) + 1);
                } else {
                    colorMap.put( color, 1) ;
                }
            }
        }
        int mode = -1 ;
        int max = -1 ;
        for( int k : colorMap.keySet() ) {
            if( colorMap.get( k ) > max ) {
                mode = k ;
                max = colorMap.get( k ) ;
            }
        }
        return  mode ;
    }

    public int getColor( Mat mat, int i, int j ) {
        int color = 0 ;
        int shade = 0 ;
        int saturation = 0 ;

        double[] data = mat.get( i, j ) ;
        double hue = data[0] ;
        double sat = data[1] ;
        double val = data[2] ;

        color =( int )( hue + 15 / 30 )  ;
        if ( color > 6 ) {
            color = 0 ;
        }
        shade = ( int ) ( val / 5 ) ;
        saturation = ( int ) ( sat / 5 ) ;

        return color << 16 + shade << 8 + saturation ;
    }
}
