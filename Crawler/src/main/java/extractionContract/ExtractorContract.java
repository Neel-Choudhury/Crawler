package extractionContract;

public abstract class ExtractorContract{
    public static final String     HTML_TD     = "TD";
    public static final String     HTML_A      = "a";
    public static final String HTML_TD_TTL     = "td.ttl";
    public static final String HTML_DATA_SPEC  = "[data-spec=";
    public static final String HTML_CLOSE_BRKT = "]";

    public abstract String extractionDelegate(String elementName);
    public abstract void extractElementsAndPopulateList();
}
