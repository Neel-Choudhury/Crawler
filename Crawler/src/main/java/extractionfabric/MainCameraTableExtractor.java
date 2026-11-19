package extractionfabric;

import dto.MainCameraDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class MainCameraTableExtractor  extends ExtractorContract {
    private static final String TRIPLE     ="Triple";
    private static final String FEATURES   ="Features";
    private static final String VIDEO      ="Video";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public MainCameraTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }
    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case TRIPLE            -> extractAndReturn.apply(HTML_TD,"cam1modules");
            case FEATURES          -> extractAndReturn.apply(HTML_TD,"cam1features");
            case VIDEO             -> extractAndReturn.apply(HTML_TD,"cam1video");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        MainCameraDTO mainCameraDTO = new MainCameraDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case TRIPLE:
                    String tripple = extractionDelegate(TRIPLE);
                    mainCameraDTO.setTripple(tripple.replace("\"",""));
                    break;
                case FEATURES:
                    String features = extractionDelegate(FEATURES);
                    mainCameraDTO.setFeatures(features);
                    break;
                case VIDEO:
                    String video = extractionDelegate(VIDEO);
                    mainCameraDTO.setVideo(video);
                    break;
            }
            parentDto.setMainCamera(mainCameraDTO);
        });
    }
}
