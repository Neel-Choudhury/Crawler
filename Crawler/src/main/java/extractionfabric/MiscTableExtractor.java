package extractionfabric;

import dto.MiscDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class MiscTableExtractor extends ExtractorContract {
    private static final String COLORS="Colors";
    private static final String PRICE="Price";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public MiscTableExtractor(Element provisionedElementContainer, Root parentDto){
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }
    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case COLORS            -> extractAndReturn.apply(HTML_TD,"colors");
            case PRICE             -> extractAndReturn.apply(HTML_TD,"price");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        MiscDTO miscDTO=new MiscDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case COLORS:
                    String colors = extractionDelegate(COLORS);
                    miscDTO.setColors(colors);
                    break;
                case PRICE:
                    String price = extractionDelegate(PRICE);
                    miscDTO.setPrice(price);
                    break;
            }
            parentDto.setMisc(miscDTO);
        });
    }
}
