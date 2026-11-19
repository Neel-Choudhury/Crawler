package com.example.crawler;

import extractionContract.ExtractionDelegate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication

public class CrawlerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CrawlerApplication.class, args);

        boolean isDebug = true;
        if(isDebug){
            Document doc = Jsoup
                    .connect("https://www.gsmarena.com/samsung_galaxy_m17_5g-14221.php")
                    .timeout(7000)
                    .get();

        }

        int mode=0;
       if(mode==1){
           List<String> resolvedRankedPageUrlLst = List.of("samsung-phones-9.php");
           /*ResolvePaginationLinks.nextRankPageResolver("samsung-phones-9.php");*/
           resolvedRankedPageUrlLst
                   .forEach(resolvedTargetRankedPageUrl -> {
                       try {
                           Thread t = new Thread(new ExtractionDelegate(resolvedTargetRankedPageUrl));
                           t.start();
                           Thread.sleep(2000);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   });
       }
	}

}
