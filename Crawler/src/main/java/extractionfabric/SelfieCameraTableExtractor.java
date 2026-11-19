package extractionfabric;

import dto.Root;
import dto.SelfieCameraDTO;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class SelfieCameraTableExtractor  extends ExtractorContract {
    private static final String SINGLE     ="Single";
    private static final String VIDEO      ="Video";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public SelfieCameraTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }


    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case SINGLE            -> extractAndReturn.apply(HTML_TD,"cam2modules");
            case VIDEO             -> extractAndReturn.apply(HTML_TD,"cam2video");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        SelfieCameraDTO selfieCameraDTO = new SelfieCameraDTO();
        targetElementContainer.select(HTML_TD_TTL).select(HTML_A).forEach(e->{
            switch(e.text()){
                case SINGLE:
                    String card_slot = extractionDelegate(SINGLE);
                    selfieCameraDTO.setCardSlot(card_slot.replace("\"",""));
                    break;
                case VIDEO:
                    String internal =  extractionDelegate(VIDEO);
                    selfieCameraDTO.setVideo(internal);
                    break;
            }
            parentDto.setSelfieCamera(selfieCameraDTO);
        });
    }
}
