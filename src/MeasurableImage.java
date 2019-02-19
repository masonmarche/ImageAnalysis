import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class MeasurableImage {

    private Mat mat;
    private Size size;

    public MeasurableImage( Mat mat ) {
        this.mat = mat ;
        size = mat.size() ;
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
        Mat temp = new Mat( ( int )size.height, ( int )size.width, CvType.CV_8U ) ;
        Imgproc.cvtColor( mat, temp, Imgproc.COLOR_RGB2HSV ) ;
    }

    public int getColor( Mat mat, int i, int j ) {
        String color = "" ;
        String shade = "" ;
        String saturation = "" ;

        double[] data = mat.get( i, j ) ;
        double hue = data[0] ;
        double sat = data[1] ;
        double val = data[2] ;

    }
}
