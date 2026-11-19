package extractionfabric;

import dto.FeaturesDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class FeaturesTableExtractor extends ExtractorContract {
    private static final String SENSORS ="Sensors";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public FeaturesTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }

    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case SENSORS            -> extractAndReturn.apply(HTML_TD,"sensors");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        FeaturesDTO featuresDTO = new FeaturesDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case SENSORS:
                    String sensors = extractionDelegate(SENSORS);
                    featuresDTO.setSensors(sensors);
                    break;
            }
            parentDto.setFeatures(featuresDTO);
        });
    }
}
