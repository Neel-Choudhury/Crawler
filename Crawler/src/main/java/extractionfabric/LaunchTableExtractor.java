package extractionfabric;

import dto.BatteryDTO;
import dto.LaunchDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class LaunchTableExtractor extends ExtractorContract {
    private static final String   ANNOUNCED = "Announced";
    private static final String      STATUS = "Status";

    private Element targetElementContainer=null;
    private Root parentDto =null;
    public LaunchTableExtractor(Element provisionedElementContainer, Root providedContainer) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto = providedContainer;
    }
    @Override
    public String extractionDelegate(String elementName){
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case ANNOUNCED -> extractAndReturn.apply(HTML_TD,"year");
            case STATUS    -> extractAndReturn.apply(HTML_TD,"status");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }
    @Override
    public void extractElementsAndPopulateList() {
        LaunchDTO launchDTO=new LaunchDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case ANNOUNCED:
                    String announced = extractionDelegate(ANNOUNCED);
                    launchDTO.setAnnounced(announced);
                    break;
                case STATUS:
                    String status =  extractionDelegate(STATUS);
                    launchDTO.setStatus(status);
                    break;
            }
            parentDto.setLaunch(launchDTO);
        });

    }
}
