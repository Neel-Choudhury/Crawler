package extractionfabric;

import dto.CommsDTO;
import dto.Root;
import extractionContract.ExtractorContract;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.BiFunction;

public class CommsTableExtractor extends ExtractorContract {
    private static final String WLAN="WLAN";
    private static final String BLUETOOTH="Bluetooth";
    private static final String POSITIONING="Positioning";
    private static final String NFC="NFC";
    private static final String RADIO="Radio";
    private static final String USB="USB";
    private final Element targetElementContainer;
    private Root parentDto =null;
    public CommsTableExtractor(Element provisionedElementContainer, Root parentDto) {
        this.targetElementContainer=provisionedElementContainer;
        this.parentDto=parentDto;
    }

    @Override
    public String extractionDelegate(String elementName) {
        BiFunction<String,String,String> extractAndReturn=(htmlAttribute, rowSpecificAttributeName)
                ->targetElementContainer
                .select(htmlAttribute+HTML_DATA_SPEC+rowSpecificAttributeName+HTML_CLOSE_BRKT).text();
        return  switch(elementName){
            case WLAN            -> extractAndReturn.apply(HTML_TD,"wlan");
            case BLUETOOTH       -> extractAndReturn.apply(HTML_TD,"bluetooth");
            case POSITIONING     -> extractAndReturn.apply(HTML_TD,"gps");
            case NFC             -> extractAndReturn.apply(HTML_TD,"nfc");
            case RADIO           -> extractAndReturn.apply(HTML_TD,"radio");
            case USB             -> extractAndReturn.apply(HTML_TD,"usb");
            default -> throw new IllegalStateException("Unexpected value: " + elementName);
        };
    }

    @Override
    public void extractElementsAndPopulateList() {
        CommsDTO  commsDTO=new CommsDTO();
        targetElementContainer
                .select(HTML_TD_TTL)
                .select(HTML_A)
                .forEach(e->{
            switch(e.text()){
                case WLAN:
                    String wlan = extractionDelegate(WLAN);
                    commsDTO.setWlan(wlan);
                    break;
                case BLUETOOTH:
                    String blueTooth = extractionDelegate(BLUETOOTH);
                    commsDTO.setBlueTooth(blueTooth);
                    break;
                case POSITIONING:
                    String positioning = extractionDelegate(POSITIONING);
                    commsDTO.setPositioning(positioning);
                    break;
                case NFC:
                    String nfc = extractionDelegate(NFC);
                    commsDTO.setNfc(nfc);
                    break;
                case RADIO:
                    String radio = extractionDelegate(RADIO);
                    commsDTO.setRadio(radio);
                    break;
                case USB:
                    String usb = extractionDelegate(USB);
                    commsDTO.setUsb(usb);
                    break;
            }
            parentDto.setComms(commsDTO);
        });
    }
}
