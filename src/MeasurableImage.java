import org.opencv.core.* ;
import org.opencv.highgui.HighGui ;
import org.opencv.imgproc.Imgproc ;
import org.opencv.imgcodecs.Imgcodecs ;
import java.util.* ;

public class MeasurableImage {

    static{ System.loadLibrary( Core.NATIVE_LIBRARY_NAME ) ; }

    private Mat mat ;
    private Size size ;

    private static final double[] HSVTHRESH = {25,   25,   225,   2.5,  17.5,   32.5,   47.5,   62.5,   77.5,   92.5,   107.5,   122.5,   137.5,   152.5,   167.5} ;
                       //HSV Threshold values: grey  black white  red   redyel  yellow  yelgre  green   cyagre  cyan    blucya   blue     magblu   magenta  redmag

    private static final int HUE = 0 ; //Location of hue value in HSV double[]
    private static final int SAT = 1 ; //Location of saturation value in HSV double[]
    private static final int VAL = 2 ; //Location of value (lightness) value in HSV double[]

    private static final int BLUE = 0 ; //Location of blue value in BGR double[]
    private static final int GREEN = 1 ; //Location of green value in BGR double[]
    private static final int RED = 2 ; //Location of red value in BGR double[]


    public MeasurableImage() {
        mat = Imgcodecs.imread("src/flower.jpg" ) ;
        if( mat.empty() ) {
            System.out.println( "Error opening image!" ) ;
            System.exit( -1 ) ;
        }
        size  = mat.size() ;
    }

    public static void main ( String[] args ) {
        new MeasurableImage() ;
    }

    public double[] getAvgBGRColor() { //Returns double[] of average blue, green, and red values of BGR image
        double[] color = {0,0,0} ;
        for ( int i = 0 ; i < size.height ; i++ ) {
            for ( int j = 0 ; j < size.width ; j++ ) {
                double[] data = mat.get( i, j ) ;
                color[BLUE] += data[BLUE] ;
                color[GREEN] += data[GREEN] ;
                color[RED] += data[RED] ;
            }
        }
        color[BLUE] /= ( size.width * size.height ) ;
        color[GREEN] /= ( size.width * size.height ) ;
        color[RED] /= ( size.width * size.height ) ;
        return color ;
    }

    public double getAvgHue() { return getAvgHSV( HUE ) ; } //Uses getAvgHSV to return average hue value

    public double getAvgSat() { return getAvgHSV( SAT ) ; } //Uses getAvgHSV to return average saturation value

    public double getAvgVal() { return getAvgHSV( VAL ) ; } //Uses getAvgHSV to return average lightness value

    private double getAvgHSV( int location  ) { //Returns average value of specified location in HSV double[]
        Mat temp = new Mat( ( int ) size.height, ( int ) size.width, CvType.CV_8UC3 ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_BGR2HSV ) ;
        double sum = 0 ;
        for ( int i = 0 ; i < size.height ; i++ ) {
            for ( int j = 0 ; j < size.width ; j++ ) {
                double[] data = temp.get( i, j ) ;
                sum += data[location] ;
            }
        }
        return sum / ( size.height * size.width ) ;
    }

    public String getColor( double[] hsv ) { //Returns color based on HSVTHRESH double[]
        if( hsv[SAT] < HSVTHRESH[HUE] ) { if( hsv[VAL] < HSVTHRESH[1] ) { return "Black" ; }  if( hsv[VAL] > HSVTHRESH[2] ) { return "White" ; } return "Grey" ; }
        if( hsv[HUE] <= HSVTHRESH[3] ) { return "Red" ; }
        if( hsv[HUE] <= HSVTHRESH[4] ) { return "Yellow-Red" ; }
        if( hsv[HUE] <= HSVTHRESH[5] ) { return "Yellow" ; }
        if( hsv[HUE] <= HSVTHRESH[6] ) { return "Green-Yellow" ; }
        if( hsv[HUE] <= HSVTHRESH[7] ) { return "Green" ; }
        if( hsv[HUE] <= HSVTHRESH[8] ) { return "Cyan-Green" ; }
        if( hsv[HUE] <= HSVTHRESH[9] ) { return "Cyan" ; }
        if( hsv[HUE] <= HSVTHRESH[10] ) { return "Blue-Cyan" ; }
        if( hsv[HUE] <= HSVTHRESH[11] ) { return "Blue" ; }
        if( hsv[HUE] <= HSVTHRESH[12] ) { return "Magenta-Blue" ; }
        if( hsv[HUE] <= HSVTHRESH[13] ) { return "Magenta" ; }
        if( hsv[HUE] <= HSVTHRESH[14] ) { return "Red-Magenta" ; }
        return "Red" ;
    }

    public double getModeHue() { return getModeHSV( HUE ) ; } //Uses getModeHSV to return most common hue value

    public double getModeSat() { return getModeHSV( SAT ) ; } //Uses getModeHSV to return most common saturation value

    public double getModeVal() { return getModeHSV( VAL ) ; } //Uses getModeHSV to return most common lightness value

    private double getModeHSV( int location ) { //Returns most common value of specified location in HSV double[]
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

    public double stdDevBGR() { //returns double[] indicating standard deviation of BGR values
        double [] avg = getAvgBGRColor() ;
        double sum = 0 ;

        for ( int i = 0; i < size.height; i++ ) {
            for ( int j = 0; j < size.width; j++ ) {
                double [] bgr = mat.get( i, j ) ;
                sum += Math.pow( bgr[BLUE] - avg[BLUE], 2 ) + Math.pow( bgr[GREEN] - avg[GREEN], 2 ) + Math.pow( bgr[RED] - avg[RED], 2 ) ;
            }
        }
        return Math.sqrt( sum / ( size.height * size.width ) ) ;
    }

    public double stdDevHue() { return stdDevHSV( HUE ) ; } //Uses stdDevHSV to return standard deviation of hue value

    public double stdDevSat() { return stdDevHSV( SAT ) ; } //Uses stdDevHSV to return standard deviation of saturation value

    public double stdDevVal() { return stdDevHSV( VAL ) ; } //Uses stdDevHSV to return standard deviation of lightness value

    private double stdDevHSV( int location ) { //Returns returns standard deviation of value of specified location in HSV double[]
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

    public Mat detectLines() { //I found this online, I don't fully understand it. Please be careful
        Mat dst = new Mat(), /*cdst = new Mat(),*/ cdstP = new Mat() ;
        Imgproc.Canny( mat, dst, 50, 200, 3, false ) ;
        Imgproc.cvtColor( dst, cdstP, Imgproc.COLOR_GRAY2BGR ) ;
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
        //HighGui.imshow("Source Image", mat) ;
        //HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst) ;
        //HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP) ;
        //HighGui.waitKey() ;
        return cdstP ;
    }

    public Mat skeletonize() { //Maybe finish one day, does not work
        Mat dst = new Mat() ;
        Imgproc.Canny( mat, dst, 220, 255, 3, false ) ;
        Imgproc.threshold( dst, dst, 100, 255, Imgproc.THRESH_BINARY_INV ) ;
        Imgproc.distanceTransform( dst, dst, Imgproc.CV_DIST_C, 3 ) ;
        Size dstSize = dst.size() ;
        int amtCleared = -1 ;
        while ( amtCleared != 0 ) {
            for ( int i = 0; i < dstSize.height; i++ ) {
                for ( int j = 0; j < dstSize.width; j++ ) {
                    //find neighborsx
                    //run iterations to find pixels to delete
                    //find a way to append delete/save info to pixels
                    //sweep through, delete pixels, run again
                }
            }
        }
        Imgcodecs.imwrite( "src/dst.png", dst ) ;
        return dst;
    }
}
// https://docs.opencv.org/3.4/javadoc/org/opencv/core/Mat.html <= MAT Java Documentation

// http://www-prima.inrialpes.fr/perso/Tran/Draft/gateway.cfm.pdf <= Zhang-Suen Skeleton Algorithm
// http://paper.ijcsns.org/07_book/200707/20070729.pdf <= Chang Skeleton Algorithm