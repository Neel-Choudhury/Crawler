package extractionContract;

public class TableExtractorDispatch {
    public void triggerExtractor(ExtractorContract candidate){
        candidate.extractElementsAndPopulateList();
    }
}
