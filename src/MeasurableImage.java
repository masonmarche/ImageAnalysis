import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import java.util.*;

public class MeasurableImage {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private Mat mat;
    private Size size;

    public MeasurableImage() {
        mat = Imgcodecs.imread("src/test.png" ) ;
        size  = mat.size() ;
        System.out.println( getHue( getModeHue() ) ) ;
    }

    public static void main ( String[] args ) {

        new MeasurableImage() ;
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

    public int getModeHue() {
        HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>() ;
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_RGB2HSV ) ;
        for ( int i = 0; i < size.height; i++ ) {
            for ( int j = 0; j < size.width; j++ ) {
                double[] hsv = temp.get( i, j ) ;
                if ( colorMap.keySet().contains( ( int ) hsv[0] ) ) {
                    colorMap.replace( ( int ) hsv[0], colorMap.get( ( int ) hsv[0] ) + 1 ) ;
                } else {
                    colorMap.put( ( int ) hsv[0], 1 ) ;
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

    public String getHue( int hue ) {
        String color = "" ;
        System.out.println( hue ) ;
        switch ( ( ( int ) ( hue + 15 * 2 ) / 30 ) * 30 ) {
            case 0: color = "red" ; break ;
            case 30: color = "orange" ; break ;
            case 60: color = "yellow" ; break ;
            case 90: color = "green-yellow" ; break ;
            case 120: color = "green" ; break ;
            case 150: color = "cyan-green" ; break ;
            case 180: color = "cyan" ; break ;
            case 210: color = "blue-cyan" ; break ;
            case 240: color = "blue" ; break ;
            case 270: color = "magenta-blue" ; break ;
            case 300: color = "magenta" ; break ;
            case 330: color = "red-magenta" ; break ;
            default: color = "red" ; break ;

        }
        System.out.println( ( ( int ) ( hue + 15 * 2 ) / 30 ) * 30  ) ;
        return color ;
    }
}
