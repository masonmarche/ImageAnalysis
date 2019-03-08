import org.jsoup.Jsoup;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstagramCollector extends ImageCollector{

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private String username;

    private int imageSize;

    private static final String INSTAGRAM_COMPONENT = "http://www.instagram.com/";

    public InstagramCollector(String username) {
        super();
        this.username = username;

        imageSize = 240;
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

    private String getHTML() {
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
        List<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile("https://[^{}]*?"+ imageSize + "x" + imageSize + "[^{}]*?\\.net").matcher(html);
        while (m.find()) {
            String str = m.group();
            if (!matches.contains(str)) {
                matches.add(str);
            }
        }
        return matches;
    }

}
