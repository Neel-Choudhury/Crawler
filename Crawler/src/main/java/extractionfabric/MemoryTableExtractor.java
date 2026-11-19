package extractionfabric;

import dto.MemoryDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class MemoryTableExtractor extends ExtractorContract {
    private static final String CARD_SLOT     ="Card slot";
    private static final String INTERNAL      ="Internal";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public MemoryTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }

    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case CARD_SLOT            -> extractAndReturn.apply(HTML_TD,"memoryslot");
            case INTERNAL             -> extractAndReturn.apply(HTML_TD,"internalmemory");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        MemoryDTO memoryDTO = new MemoryDTO();
        targetElementContainer.select(HTML_TD_TTL).select(HTML_A).forEach(e->{
            switch(e.text()){
                case CARD_SLOT:
                    String card_slot = extractionDelegate(CARD_SLOT);
                    memoryDTO.setCardSlot(card_slot);
                    break;
                case INTERNAL:
                    String internal =  extractionDelegate(INTERNAL);
                    memoryDTO.setInternal(internal);
                    break;
            }
            parentDto.setMemory(memoryDTO);
        });
    }
}
