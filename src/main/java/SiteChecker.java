import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.List;

/**
 * SiteChecker
 * version: 2.0
 */


public class SiteChecker {
    public static void printResults(String[] args){
        String delimiter = "|";

        System.out.println(String.join(delimiter, args));
    }

    public static void searchAds (String location, String autoMakeModel, int price) {
        String modelWithOutWhitespace = autoMakeModel.replaceAll(" ", "%20");
        String link = "https://"+ location +".craigslist.org/search/cta?auto_make_model="+ modelWithOutWhitespace +"&max_price=" + price + "&purveyor=owner&sort=priceasc#search=1~gallery~0~0";

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage page = client.getPage(link);
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

    public static void main(String[] args){
        String[] modelArray = {"Lexus IS300"};
        String[] cityArray = {"sfbay", "atlanta","hanford", "austin", "boston", "chicago", "dallas", "denver", "detroit", "houston", "lasvegas", "losangeles", "miami", "minneapolis", "newyork", "philadelphia", "phoenix", "portland", "raleigh", "sacramento", "sandiego", "seattle", "washingtondc", "cnj"
        };
        int price = 4000;

        for (String location : cityArray) {
            System.out.println("location : " + location);

            for (String model : modelArray) {
                searchAds(location, model, price);
            }
        }
    }
}
