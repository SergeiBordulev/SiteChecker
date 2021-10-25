import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SiteChecker {
    public static void main(String[] args) throws IOException {
        String[] modelArray = {"bmw m3"};
        String[] cityArray = {"atlanta", "austin", "boston", "chicago", "dallas", "denver", "detroit", "houston", "lasvegas", "losangeles", "miami", "minneapolis", "newyork", "philadelphia", "phoenix", "portland", "raleigh", "sacramento", "sandiego", "seattle", "sfbay", "washingtondc", "cnj"};
        int maxPrice = 10000;

        for (String city : cityArray) {
            for (String model : modelArray) {
                String modelWithOutWhitespace = model.replaceAll(" ", "%20");
                String nameDomain = "https://" + city + ".craigslist.org/d/cars-trucks-by-owner/search/cto?auto_make_model=";
                URL aUrl = new URL(nameDomain + modelWithOutWhitespace + "&max_price=" + maxPrice);
                int adsCurrent = 0;

                try (BufferedReader in = new BufferedReader(new InputStreamReader(aUrl.openStream()))) {
                    String line;

                    while ((line = in.readLine()) != null) {
                        if (line.indexOf("<span class=\"result-meta\">") > 0) {
                            if ((line = in.readLine()) != null) {
                                int startIndexPrice = line.indexOf("result-price");

                                if (startIndexPrice != -1) {
                                    adsCurrent++;
                                    int endIndexPrice = line.indexOf("</", startIndexPrice);
                                    System.out.println(model + " #" + adsCurrent + " price : " + line.substring(startIndexPrice + 14, endIndexPrice));
                                }
                            }
                        }
                    }

                    System.out.println("Number of ads " + model + " in " + city + " and area: " + adsCurrent);
                }
            }
        }
    }
}
