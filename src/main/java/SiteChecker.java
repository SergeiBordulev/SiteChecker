import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * SiteChecker
 * version: 2.0
 * An example of running an application from the command line
 * java "Brand and Model" price location
 * example:
 * java "Lexus IS300" 4000 sfbay
 */


public class SiteChecker {
    public static void printResults(String[] args){
        String delimiter = "|";

        System.out.println(String.join(delimiter, args));
    }

    public static void searchAds (String location, String autoMakeModel, int price) {
        String autoMakeModelWithOutWhitespace = autoMakeModel.replaceAll(" ", "%20");
        String url = "https://"+ location +".craigslist.org/search/cta?auto_make_model="+ autoMakeModelWithOutWhitespace +"&max_price=" + price + "&purveyor=owner&sort=priceasc#search=1~gallery~0~0";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Document doc = Jsoup.parse(response.body());

                Elements results = doc.select("li.cl-static-search-result");
                for (Element result : results) {
                    String itemTitle = result.select("div.title").text();
                    String itemPrice = result.select("div.price").text();
                    String itemLocation = result.select("div.location").text();

                    String[] resultsArray = {itemPrice, itemLocation.trim(), itemTitle};
                    printResults(resultsArray);
                }
            } else {
                System.out.println("No items found or failed to retrieve the page. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] modelArray = {"Lexus IS300"};
        String[] cityArray = {"sfbay", "atlanta","hanford", "austin", "boston", "chicago", "dallas", "denver", "detroit", "houston", "lasvegas", "losangeles", "miami", "minneapolis", "newyork", "philadelphia", "phoenix", "portland", "raleigh", "sacramento", "sandiego", "seattle", "washingtondc", "cnj"
        };
        int price = 4000;
        String[] tempCity = new String[1];

        if(args.length == 3){
            modelArray[0] = args[0];
            price = Integer.parseInt(args[1]);
            tempCity[0] = args[2];
            cityArray = tempCity;
        }

        for (String location : cityArray) {
            System.out.println("location : " + location);

            for (String model : modelArray) {
                searchAds(location, model, price);
            }
        }
    }
}
