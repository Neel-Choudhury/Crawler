package extractionContract;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Root;
import extractionfabric.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.TableLabelLst;

import java.net.ConnectException;
import java.net.http.HttpConnectTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ExtractionDelegate implements TableLabelLst, Runnable {
    private final Predicate<Exception> resolveInstance    =
            (Exception e)->e instanceof HttpConnectTimeoutException
                        || e instanceof ConnectException;
    private TableExtractorDispatch tableExtractorDispatch = null;
    private List<String>              directPhoneUrlLinks = null;
    private ObjectMapper                           mapper = null;
    private String                         targetTableUrl = null;
  /*  private KafkaTemplate<String, String> kvKafkaTemplate=null;
    private  <K, V> ProducerFactory<K, V> createOrderProducerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.56.1:9092");
        config.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   JsonSerializer.class);
        config.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    private <K, V> KafkaTemplate<K, V> createOrderKafkaTemplate(){
        return new KafkaTemplate<>(createOrderProducerFactory());
    }*/
    public ExtractionDelegate(final String provisionedTableUrl) {
        this.targetTableUrl                  = provisionedTableUrl;
        this.tableExtractorDispatch          = new TableExtractorDispatch();
        this.directPhoneUrlLinks             = new LinkedList<>();
        this.mapper                          = new ObjectMapper();
      //  this.kvKafkaTemplate                 = createOrderKafkaTemplate();
    }
    public ExtractionDelegate() { }

    public void extractDoc(String url) {
        try {
            Thread.sleep(3500);
            Document doc = Jsoup
                    .connect(url)
                    .timeout(7000)
                    .get();
            Root parentDto = new Root();
            parentDto.setModel(doc.select("h1.specs-phone-name-title").text());
            int [] count={0};
            doc.select("ul.specs-spotlight-features")
                    .select(HTML_ATTR_LI_PRFIX)
                    .select(HTML_SPAN_SPAN_PRFIX)
                    .forEach(iterable->{
                        switch(count[0]){
                            case 0:
                                parentDto.setRelease_date(iterable.text());
                                break;
                            case 4:
                                parentDto.setAndroidVersion(iterable.text());
                                break;
                            case 6:
                                parentDto.setStorage(iterable.text());
                                break;
                            case 16:
                                parentDto.setBatteryCapacity(iterable.text());
                                break;
                        }
                        count[0]++;
            });
            Elements elements = doc.select("div#specs-list").select(TableLabelLst.HTML_TBODY);

            elements.forEach(fragment->{
                switch (fragment.select(TableLabelLst.HTML_TH).text()) {
                    case NETWORK:
                        tableExtractorDispatch.triggerExtractor(new NetworkTableExtractor(fragment, parentDto));
                        break;
                    case LAUNCH:
                        tableExtractorDispatch.triggerExtractor(new LaunchTableExtractor(fragment, parentDto));
                        break;
                    case BODY:
                        tableExtractorDispatch.triggerExtractor(new BodyTableExtractor(fragment, parentDto));
                        break;
                    case DISPLAY:
                        tableExtractorDispatch.triggerExtractor(new DisplayTableExtractor(fragment, parentDto));
                        break;
                    case BATTERY:
                        tableExtractorDispatch.triggerExtractor(new BatteryTableExtractor(fragment, parentDto));
                        break;
                    case PLATFORM:
                        tableExtractorDispatch.triggerExtractor(new PlatformTableExtractor(fragment, parentDto));
                        break;
                    case MEMORY:
                        tableExtractorDispatch.triggerExtractor(new MemoryTableExtractor(fragment, parentDto));
                        break;
                    case MAIN_CAMERA:
                        tableExtractorDispatch.triggerExtractor(new MainCameraTableExtractor(fragment, parentDto));
                        break;
                    case SELFIE_CAMERA:
                        tableExtractorDispatch.triggerExtractor(new SelfieCameraTableExtractor(fragment, parentDto));
                        break;
                    case SOUND:
                        tableExtractorDispatch.triggerExtractor(new SoundTableExtractor(fragment, parentDto));
                        break;
                    case COMMS:
                        tableExtractorDispatch.triggerExtractor(new CommsTableExtractor(fragment, parentDto));
                        break;
                    case FEATURES:
                        tableExtractorDispatch.triggerExtractor(new FeaturesTableExtractor(fragment, parentDto));
                        break;
                    case MISC:
                        tableExtractorDispatch.triggerExtractor(new MiscTableExtractor(fragment, parentDto));
                        break;
                }
            });
            //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parentDto));
            //kvKafkaTemplate.send("TestTopic", mapper.writeValueAsString(parentDto));
        } catch (Exception e) {
            e.printStackTrace();
            if (resolveInstance.test(e)) {
                try{
                    Thread.currentThread().wait(10000);
                }catch (InterruptedException e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    private void extractTables() {
        try {
            int counter = 0;
            while (counter < directPhoneUrlLinks.size()) {
                String url = directPhoneUrlLinks.get(counter);
                extractDoc(HOST_URL + url);
                counter++;
            }
        } catch (Exception e) {
            System.out.println("URL that failed to fetch doc payload\n" + targetTableUrl);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Document phoneList = Jsoup
                    .connect(HOST_URL + targetTableUrl)
                    .timeout(40000)
                    .get();

            phoneList.select("div.makers")
                    .select(HTML_ATTR_UL_PRFIX)
                    .select(HTML_ATTR_LI_PRFIX)
                    .select(HTML_ATTR_A_PRFIX)
                    .forEach(link -> {
                        directPhoneUrlLinks.add(link.attr("href"));
                    });
            extractTables();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
