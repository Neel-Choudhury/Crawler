package extractionfabric;

import dto.BodyDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class BodyTableExtractor extends ExtractorContract {
    private static final String DIMENSIONS = "Dimensions";
    private static final String WEIGHT = "Weight";
    private static final String BUILD = "Build";
    private static final String SIM = "SIM";

    private final Element targetElementContainer;
    private Root parentDto = null;

    public BodyTableExtractor(Element provisionedElementContainer, Root providedContainer) {
        this.targetElementContainer = provisionedElementContainer;
        this.parentDto = providedContainer;
    }

    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String, String, String> extractAndReturn = (htmlAttribute, rowSpecificAttributeName)
                -> targetElementContainer
                .select(htmlAttribute + HTML_DATA_SPEC + rowSpecificAttributeName + HTML_CLOSE_BRKT).text();
        return switch (elementName) {
            case DIMENSIONS -> extractAndReturn.apply(HTML_TD, "dimensions");
            case WEIGHT -> extractAndReturn.apply(HTML_TD, "weight");
            case BUILD -> extractAndReturn.apply(HTML_TD, "build");
            case SIM -> extractAndReturn.apply(HTML_TD, "sim");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        BiFunction<String, String, String> extractAndReturn = (htmlAttribute, rowSpecificAttributeName)
                -> targetElementContainer
                .select(htmlAttribute + HTML_DATA_SPEC + rowSpecificAttributeName + HTML_CLOSE_BRKT).text();
        String nbsp = extractAndReturn.apply(HTML_TD, "bodyother");
        BodyDTO bodyDTO = new BodyDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e -> {
                    switch (e.text()) {
                        case DIMENSIONS:
                            String dimensions = extractionDelegate(DIMENSIONS);
                            bodyDTO.setDimensions(dimensions);
                            break;
                        case WEIGHT:
                            String weight = extractionDelegate(WEIGHT);
                            bodyDTO.setWeight(weight);
                            break;
                        case BUILD:
                            String build = extractionDelegate(BUILD);
                            bodyDTO.setBuild(build);
                            break;
                        case SIM:
                            String sim = extractionDelegate(SIM);
                            bodyDTO.setSim(sim);
                            bodyDTO.setNotes(nbsp);
                            break;
                    }
                    parentDto.setBody(bodyDTO);
                });

    }
}
