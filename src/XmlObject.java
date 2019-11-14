import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Emulates XML Object Map.
 */
public class XmlObject implements Xml {

    private String tag;
    private Map<String, Object> map;

    public XmlObject(String tag) {
        map = new LinkedHashMap<>();
        this.tag = tag;
    }

    public void addField(String key, Object value) {
        map.put(key,value);
    }

    /**
     * Converts XmlObject to XML string.
     * @return XML String
     */
    @Override
    public String toXML() {
        String xmlString = "<%s> %s </%s>";
        StringBuilder xmlContent = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof Xml) {
                Xml obj = (Xml) entry.getValue();
                xmlContent.append(obj.toXML());
            } else {
                String fieldStr = "<%s> %s </%s>";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                value = sanitize(value);
                xmlContent.append(String.format(fieldStr, entry.getKey(), value, entry.getKey()));
            }
        }
        return String.format(xmlString, tag, xmlContent, tag);
    }

    /**
     * Takes xml data value and sanitizes it by escaping the ampersands from them.
     * @param string the xml string value.
     * @return the string with escaped ampersand values
     */
    private static String sanitize(String string) {
        return string.replaceAll("&", "&amp;");
    }
}
