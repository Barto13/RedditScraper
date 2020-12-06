//import org.jsoup.Jsoup;
//
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import org.json.simple.JSONObject;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Scraper {

    RedditClient redditClient;
    int threadNumber = 0;

    public void scrap(String input, int postsNumber) {


        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(input);
        System.out.println(postsNumber);
        SubredditReference subreddit = redditClient.subreddit(input);
        // frontPage() returns a Paginator.Builder
        DefaultPaginator<Submission> frontPage = redditClient.frontPage()
                .sorting(SubredditSort.TOP)
                .timePeriod(TimePeriod.DAY)
                .limit(30)
                .build();



        DefaultPaginator<Submission> posts = redditClient.subreddit(input)
                .posts()
                .limit(postsNumber)
                .build();
        System.out.println("Time of scraping: " + ts);
        for (Submission s : posts.next()){
            threadNumber++;

            if(s.isStickied()){
                System.out.println("THIS IS STICKIED POST");
                threadNumber--;
            }
            System.out.println("thread number: " + threadNumber);
            System.out.println("rating: " + s.getScore());
            System.out.println("title: " + s.getTitle());
            System.out.println("created by: " + s.getAuthor());
            System.out.println("created: " + s.getCreated());
            System.out.println("number of comments: " + s.getCommentCount() + "\n");

            saveToJSON(s);
        }




    }

    public void authenticateApp(){

        String username = "example.login";
        String password = "example.password";
        String clientId = "example.id";
        String clientSecret = "example.secret";

        UserAgent userAgent = new UserAgent("windows 10", "com.redditscraper", "v0.1", username);
        Credentials credentials = Credentials.script(
                username,
                password,
                clientId,
                clientSecret);
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        redditClient = OAuthHelper.automatic(adapter, credentials);
    }

    public void saveToJSON(Submission s){

        JSONObject jsonObject = new JSONObject();
        Item jsonItem = new Item();

        jsonObject.put("Is stickied", s.isStickied());
        jsonObject.put("Thread number", threadNumber);
        jsonObject.put("Rating", s.getScore());
        jsonObject.put("Title", s.getTitle());
        jsonObject.put("Created by", s.getAuthor());
        jsonObject.put("Created", s.getCreated());
        jsonObject.put("Number of comments", s.getCommentCount());

        try{
            FileWriter file = new FileWriter("output.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
