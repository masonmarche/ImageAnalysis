import org.jsoup.Jsoup;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstagramCollector extends ImageCollector{

    private String username;

    private static final String INSTAGRAM_COMPONENT = "http://www.instagram.com/";

    public InstagramCollector(String username) {
        super();
        this.username = username;
    }

    public List<Mat> getImages() {
        String html = getHTML();
        List<String> imageUrls = parseImageUrls(html);


        List<Mat> mats = new ArrayList<>();

        for (String u : imageUrls) {
            mats.add(bufferedImageToMat(downloadImage(u)));
        }

        return mats;
    }

    public String getHTML() {
        String url = INSTAGRAM_COMPONENT + username;
        String html = "";
        try {
            html = Jsoup.connect(url).ignoreHttpErrors(true).get().html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    private List<String> parseImageUrls(String html) {
        return new ArrayList<>();
    }

}
