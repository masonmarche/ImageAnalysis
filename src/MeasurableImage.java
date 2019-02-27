import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.*;

public class MeasurableImage {

    static{ System.loadLibrary( Core.NATIVE_LIBRARY_NAME ) ; }

    private Mat mat ;
    private Size size ;

    public MeasurableImage() {
        mat = Imgcodecs.imread("src/test.png" ) ;
        size  = mat.size() ;
        System.out.println( getAvgHue() ) ;
    }

    public static void main ( String[] args ) {

        new MeasurableImage() ;
    }

    public double[] getAvgRGBColor() {
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

    public double getAvgHue() {
        return getAvgHSV( 0 ) ;
    }

    public double getAvgSat() {
        return getAvgHSV( 1 ) ;
    }

    public double getAvgVal() {
        return getAvgHSV( 2 ) ;
    }

    private double getAvgHSV( int location  ) {
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_RGB2HSV ) ;
        double sum = 0 ;
        for ( int i = 0 ; i < size.height ; i++ ) {
            for ( int j = 0 ; j < size.width ; j++ ) {
                double[] data = temp.get( i, j ) ;
                sum += data[location] ;
            }
        }
        return sum / ( size.height * size.width ) ;
    }

    public double getModeHue() {
        return getModeHSV( 0 ) ;
    }

    public double getModeSat() {
        return getModeHSV( 1 ) ;
    }

    public double getModeVal() {
        return getModeHSV( 2 ) ;
    }

    private double getModeHSV( int location ) {
        HashMap<Double, Integer> colorMap = new HashMap<Double, Integer>() ;
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_RGB2HSV ) ;
        for ( int i = 0; i < size.height; i++ ) {
            for ( int j = 0; j < size.width; j++ ) {
                double[] hsv = temp.get( i, j ) ;
                if ( colorMap.keySet().contains( hsv[location] ) ) {
                    colorMap.replace( hsv[location], colorMap.get( hsv[location] ) + 1 ) ;
                } else {
                    colorMap.put( hsv[location], 1 ) ;
                }
            }
        }
        double mode = -1 ;
        int max = -1 ;
        for( double k : colorMap.keySet() ) {
            if( colorMap.get( k ) > max ) {
                mode = k ;
                max = colorMap.get( k ) ;
            }
        }
        return  mode ;
    }

    public double stdDevHue() {
        return stdDevHSV( 0 ) ;
    }

    public double stdDevSat() {
        return stdDevHSV( 1 ) ;
    }

    public double stdDevVal() {
        return stdDevHSV( 2 ) ;
    }

    private double stdDevHSV( int location ) {
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_RGB2HSV ) ;
        double sum = 0 ;
        double avg = getModeHSV( location ) ;
        for ( int i = 0; i < size.height; i++ ) {
            for ( int j = 0; j < size.width; j++ ) {
               sum += Math.pow( avg - temp.get( i, j )[location], 2 ) ;
            }
        }
        return Math.sqrt( sum / ( size.height * size.width ) ) ;
    }
}
