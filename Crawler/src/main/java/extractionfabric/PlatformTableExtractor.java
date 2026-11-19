package extractionfabric;

import dto.PlatformDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class PlatformTableExtractor extends ExtractorContract {
    private static final String OS     ="OS";
    private static final String CHIPSET="Chipset";
    private static final String CPU    ="CPU";
    private static final String GPU    ="GPU";
    private final Element targetElementContainer;
    private       Root    parentDto =null;
    public PlatformTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }
    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT)
                .text();
        return  switch(elementName){
             case OS      -> extractAndReturn.apply(HTML_TD,"os");
             case CHIPSET -> extractAndReturn.apply(HTML_TD,"chipset");
             case CPU     -> extractAndReturn.apply(HTML_TD,"cpu");
             case GPU     -> extractAndReturn.apply(HTML_TD,"gpu");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        PlatformDTO platformDTO = new PlatformDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case OS:
                    String os      = extractionDelegate(OS);
                    platformDTO.setOS(os);
                    break;
                case CHIPSET:
                    String chipset = extractionDelegate(CHIPSET);
                    platformDTO.setChipset(chipset);
                    break;
                case CPU:
                    String cpu     = extractionDelegate(CPU);
                    platformDTO.setCPU(cpu);
                    break;
                case GPU:
                    String gpu     = extractionDelegate(GPU);
                    platformDTO.setGPU(gpu);
                    break;
            }
            parentDto.setPlatform(platformDTO);
        });
    }
}
