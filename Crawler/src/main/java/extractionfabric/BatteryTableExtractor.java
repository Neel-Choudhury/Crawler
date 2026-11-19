package extractionfabric;

import dto.BatteryDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class BatteryTableExtractor extends ExtractorContract {
    private static final String TYPE="Type";
    private static final String CHARGING="Charging";
    private static final String SPACE =" ";

    public String splitAndReturn(Elements candidate){
        return candidate
                .text().split(SPACE)[2]
                +SPACE+candidate
                .text().split(SPACE)[3];
    }
    private final Element targetElementContainer;
    private       Root    parentDto =null;
    public BatteryTableExtractor(Element provisionedElementContainer,Root providedContainer){
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=providedContainer;
    }

    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case TYPE                -> extractAndReturn.apply(HTML_TD,"batdescription1");
            case CHARGING            -> Optional.ofNullable(targetElementContainer.select("td.nfo"))
                                        .map(this::splitAndReturn)
                                        .orElse(SPACE);
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        BatteryDTO batteryDTO = new BatteryDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case TYPE:
                    String     type = extractionDelegate(TYPE);
                    batteryDTO.setType(type);
                    break;
                case CHARGING:
                    String charging = extractionDelegate(CHARGING);
                    batteryDTO.setCharging(charging);
                    break;
            }
         parentDto.setBattery(batteryDTO);
        });

    }
}
