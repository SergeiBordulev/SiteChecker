import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.Arrays;
import java.util.List;

/**
 * SiteChecker
 * version: 2.0
 * An example of running an application from the command line
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

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage page = client.getPage(url);
            List<HtmlElement> items = page.getByXPath("//li[@class='cl-static-search-result']") ;

            if(items.isEmpty()){
                System.out.println("No items found !");
            }
            else {
                for(HtmlElement htmlItem : items){
                    HtmlElement divTitle = htmlItem.getFirstByXPath(".//div[@class='title']");
                    HtmlElement divLocation = htmlItem.getFirstByXPath(".//div[@class='location']");
                    HtmlElement divPrice = htmlItem.getFirstByXPath(".//div[@class='price']");

                    String itemPrice = divPrice == null ? "0.0" : divPrice.getTextContent();
                    String itemTitle = divTitle.getTextContent() ;
                    String itemLocation = divLocation.getTextContent() ;

                    String[] resultsArray = {itemPrice, itemLocation.trim(), itemTitle};
                    printResults(resultsArray);
                }
            }
        } catch(Exception e){
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
