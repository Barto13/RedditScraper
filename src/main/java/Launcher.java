import java.util.Scanner;

public class Launcher {

    private String input;
    private int postsNumber = 0;

    public void run(){
        Scraper scraper = new Scraper();
        System.out.println("Paste subreddit name you want to scrap posts from: ");
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        while(postsNumber==0) {
            System.out.println("How many posts? ");
            postsNumber = sc.nextInt();
            if(postsNumber==0){
                System.out.println("Number of posts must be higher than 0! ");
            }
        }
        scraper.authenticateApp();
        scraper.scrap(input, postsNumber);

    }
}
