package util;

import extractionContract.ExtractionDelegate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class ResolvePaginationLinks {
    public static List<String> nextRankPageResolver(String urlPath){
        List<String> tableRankedPageUrlLst = new ArrayList<>();
        final String url =TableLabelLst.HOST_URL+urlPath;
        try{
            Document nextPageResolver = Jsoup.connect(url).timeout(9000).get();
            var nextPtr =nextPageResolver.select(TableLabelLst.HTML_ATTR_A_PRV_NXT).last();
            if(null!=nextPtr&&nextPtr.attr("title").equalsIgnoreCase("Next page")){
                var tableRankedPageUrl=nextPtr.attr(TableLabelLst.HTML_ATTR_HREF);
                tableRankedPageUrlLst.add(tableRankedPageUrl);
                Thread.sleep(800);
                nextRankPageResolver(tableRankedPageUrl);
            }
        }catch(Exception e){
          try{
              tableRankedPageUrlLst.clear();
              Thread.sleep(10000);
              nextRankPageResolver(url);
          }catch(InterruptedException inE){
              inE.printStackTrace();
          }
            e.printStackTrace();
        }
        return tableRankedPageUrlLst;
    }
}
