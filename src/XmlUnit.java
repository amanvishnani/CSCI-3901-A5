/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Class for any xml object that has one key and one value.
 */
public class XmlUnit implements Xml {
    private String key;
    private Object value;

    public XmlUnit(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toXML() {
        return String.format("<%s>%s</%s>", key, XmlObject.sanitize(value.toString()), key);
    }
}
