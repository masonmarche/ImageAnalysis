import org.opencv.core.Mat;

public class Main {

    public static void main(String [] args) {
        InstagramCollector c = new InstagramCollector("pantone");
        System.out.println(c.getHTML());
    }
}
