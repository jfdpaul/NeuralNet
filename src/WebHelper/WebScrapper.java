package WebHelper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/*
 * Created by Jonathan Fidelis Paul on 4/27/2016.
 */
public class WebScrapper {

    URL url;

    public WebScrapper(String url) {
        try{
            this.url=new URL(url);
        }
        catch(MalformedURLException e){
            System.out.println("URL MALFORMED");
        }
    }

    public void getData(){
        try{
            URLConnection urlConnection=url.openConnection();
            //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            //urlConnection.setRequestProperty("User-Agent", "");
            //System.out.println(urlConnection.getContentType());
            //BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            BufferedWriter bw=new BufferedWriter(new PrintWriter("new.html"));
            String inputLine;
            while((inputLine=br.readLine())!=null) {
                System.out.println(inputLine);
                bw.write(inputLine);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[]args){
        //String url="http://api.duckduckgo.com/?q=DuckDuckGo&format=json";
        String url="http://www.faroo.com/api?q=iphone&start=1&length=10&l=en&src=web&i=false&f=json";
        //String url="http://www.gigablast.com/search?q=apple&format=json";
        //String url="https://docs.python.org/2/howto/urllib2.html";
        WebScrapper w=new WebScrapper(url);
        w.getData();
    }
}
