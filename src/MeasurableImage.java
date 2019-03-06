import org.opencv.core.* ;
import org.opencv.highgui.HighGui ;
import org.opencv.imgproc.Imgproc ;
import org.opencv.imgcodecs.Imgcodecs ;
import java.util.* ;

public class MeasurableImage {

    static{ System.loadLibrary( Core.NATIVE_LIBRARY_NAME ) ; }

    private Mat mat ;
    private Size size ;

    public MeasurableImage() {
        mat = Imgcodecs.imread("src/test.png" ) ;
        if( mat.empty() ) {
            System.out.println( "Error opening image!" ) ;
            System.exit( -1 ) ;
        }
        size  = mat.size() ;
        //add methods to test here -R
    }

    public static void main ( String[] args ) {
        new MeasurableImage() ;
    }

    public double[] getAvgBGRColor() {
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

    public String getColor( double hue ) {
        if( hue <= 10 ) { return "Red" ; }
        if( hue <= 40 ) { return "Yellow" ; }
        if( hue <= 70 ) { return "Green" ; }
        if( hue <= 100 ) { return "Cyan" ; }
        if( hue <= 130 ) { return "Blue" ; }
        if( hue <= 160 ) { return "Magenta" ; }
        return "Red" ;
    }

    public double getModeHue() { return getModeHSV( 0 ) ; }

    public double getModeSat() { return getModeHSV( 1 ) ; }

    public double getModeVal() { return getModeHSV( 2 ) ; }

    private double getModeHSV( int location ) {
        HashMap<Double, Integer> colorMap = new HashMap<Double, Integer>() ;
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_BGR2HSV ) ;

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

    public double stdDevHue() { return stdDevHSV( 0 ) ; }

    public double stdDevSat() { return stdDevHSV( 1 ) ; }

    public double stdDevVal() { return stdDevHSV( 2 ) ; }

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

    public double stdDevBGR() {
        return 0 ; //Temp method, progress will be made later -R
    }

    public void detectLines() {
        Mat dst = new Mat(), /*cdst = new Mat(),*/ cdstP = new Mat() ;

        Imgproc.Canny( mat, dst, 50, 200, 3, false ) ;

        Imgproc.cvtColor( dst, cdstP, Imgproc.COLOR_GRAY2BGR ) ; //Set to only display probability transform. Uncomment all other lines of code and comment this line to display both types of transform. -R

        /*Imgproc.cvtColor( dst, cdst, Imgproc.COLOR_GRAY2BGR ) ;
        cdstP = cdst.clone() ;

        Mat lines = new Mat() ;
        Imgproc.HoughLines( dst, lines, 1, Math.PI / 180, 150 ) ;
        for ( int x = 0; x < lines.rows() ; x++ ) {
            double rho = lines.get( x, 0 )[0],
                    theta = lines.get( x, 0 )[1] ;
            double a = Math.cos( theta ), b = Math.sin( theta ) ;
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point( Math.round( x0 + 1000*( -b ) ), Math.round( y0 + 1000 * ( a ) ) ) ;
            Point pt2 = new Point( Math.round( x0 - 1000*( - b ) ), Math.round( y0 - 1000 * ( a ) ) ) ;
            Imgproc.line( cdst, pt1, pt2, new Scalar( 0, 0, 255 ), 3, Imgproc.LINE_AA, 0 ) ;
        }*/

        Mat linesP = new Mat() ;
        Imgproc.HoughLinesP( dst, linesP, 1, Math.PI / 180, 50, 10, 10 ) ;
        for ( int z = 0; z < linesP.rows() ; z++ ) {
            double[] l = linesP.get( z, 0 ) ;
            Imgproc.line( cdstP, new Point( l[0], l[1] ), new Point( l[2], l[3] ), new Scalar( 0, 0, 255 ), 3, Imgproc.LINE_AA, 0 ) ;
        }

        HighGui.imshow("Source Image", mat) ;
        //HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst) ;
        HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP) ;
        HighGui.waitKey() ;
    }
}

//https://docs.opencv.org/3.4/db/df6/tutorial_erosion_dilatation.html <= skeletonization
//https://docs.opencv.org/3.4/d9/db0/tutorial_hough_lines.html <= hough line transform