package extractionfabric;

import dto.NetworkDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class NetworkTableExtractor extends ExtractorContract {
    private static final String TECHNOLOGY = "Technology";
    private static final String        B2G = "2G bands";
    private static final String        B3G = "3G bands";
    private static final String        B4G = "4G bands";
    private static final String        B5G = "5G bands";
    private static final String      SPEED = "Speed";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public NetworkTableExtractor(Element provisionedElementContainer, Root providedContainer){
        Element table = new Element("table");
        this.targetElementContainer=table.appendChild(provisionedElementContainer);
        this.parentDto = providedContainer;
    }

    @Override
    public String extractionDelegate(String elementName){
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute,rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case TECHNOLOGY -> extractAndReturn.apply(HTML_A,"nettech");
            case B2G        -> extractAndReturn.apply(HTML_TD,"net2g");
            case B3G        -> extractAndReturn.apply(HTML_TD,"net3g");
            case B4G        -> extractAndReturn.apply(HTML_TD,"net4g");
            case B5G        -> extractAndReturn.apply(HTML_TD,"net5g");
            case SPEED      -> extractAndReturn.apply(HTML_TD,"speed");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }
    @Override
    public void extractElementsAndPopulateList(){
        NetworkDTO ndto = new NetworkDTO();
        targetElementContainer.select(HTML_TD_TTL).select(HTML_A).forEach(e->{
            switch(e.text()){
                case TECHNOLOGY:
                    String netTech = extractionDelegate(TECHNOLOGY);
                    ndto.setTechnology(netTech);
                    break;
                case B2G:
                    String net2g =  extractionDelegate(B2G);
                    ndto.setB2g(net2g);
                    break;
                case B3G:
                    String net3g =  extractionDelegate(B3G);
                    ndto.setB3g(net3g);
                    break;
                case B4G:
                    String net4g =  extractionDelegate(B4G);
                    ndto.setB4g(net4g);
                    break;
                case B5G:
                    String net5g =  extractionDelegate(B5G);
                    ndto.setB5g(net5g);
                    break;
                case SPEED:
                    String speed =  extractionDelegate(SPEED);
                    ndto.setSpeed(speed);
                    break;
            }
            parentDto.setNetwork(ndto);
        });
    }
}
