package extractionfabric;

import dto.Root;
import dto.SoundDTO;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class SoundTableExtractor extends ExtractorContract {
    private static final String LOUDSPEAKER     ="Loudspeaker";
    private static final String TH3_P_5MM       ="3.5mm jack";
    private static final String HTML_TD_NFO     ="td.nfo";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public SoundTableExtractor(Element provisionedElementContainer, Root parentDto){
            this.targetElementContainer=provisionedElementContainer;
            this.parentDto=parentDto;
    }

    @Override
    public String extractionDelegate(String elementName) {
        return  switch(elementName){
            case LOUDSPEAKER      -> targetElementContainer.select(HTML_TD_NFO).get(0).text();
            case TH3_P_5MM        -> targetElementContainer.select(HTML_TD_NFO).get(1).text();
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        SoundDTO soundDTO = new SoundDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case LOUDSPEAKER:
                    String hasLoudSpeaker = extractionDelegate(LOUDSPEAKER);
                    soundDTO.setLoudSpeaker(hasLoudSpeaker);
                    break;
                case TH3_P_5MM:
                    String hasTh3p5mm = extractionDelegate(TH3_P_5MM);
                    soundDTO.setTH3_P_5MM(hasTh3p5mm);
                    break;
            }
            parentDto.setSound(soundDTO);
        });
    }
}
