package extractionfabric;

import dto.DisplayDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class DisplayTableExtractor  extends ExtractorContract {
    private static final String         TYPE = "Type";
    private static final String         SIZE = "Size";
    private static final String   RESOLUTION = "Resolution";
    private static final String   PROTECTION = "Protection";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public DisplayTableExtractor(Element provisionedElementContainer, Root providedContainer) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto = providedContainer;
    }
    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case TYPE            -> extractAndReturn.apply(HTML_TD,"displaytype");
            case SIZE            -> extractAndReturn.apply(HTML_TD,"displaysize");
            case RESOLUTION      -> extractAndReturn.apply(HTML_TD,"displayresolution");
            case PROTECTION      -> extractAndReturn.apply(HTML_TD,"displayprotection");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        DisplayDTO displayDTO = new DisplayDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case TYPE:
                    String dimensions = extractionDelegate(TYPE);
                    displayDTO.setType(dimensions);
                    break;
                case SIZE:
                    String weight     = extractionDelegate(SIZE);
                    displayDTO.setSize(weight);
                    break;
                case RESOLUTION:
                    String build      = extractionDelegate(RESOLUTION);
                    displayDTO.setResolution(build);
                    break;
                case PROTECTION:
                    String sim        = extractionDelegate(PROTECTION);
                    displayDTO.setProtection(sim);
                    break;
            }
            parentDto.setDisplay(displayDTO);
        });
    }
}
