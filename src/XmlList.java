import java.util.ArrayList;

public class XmlList<T extends Xml> implements Xml {

    private String tag = "list";

    private ArrayList<T> list;

    public XmlList(String tag) {
        this();
        this.tag = tag;
    }

    public XmlList() {
        this.tag = "list";
        list = new ArrayList<>();
    }

    @Override
    public String toXML() {
        String xmlString = "<%s> %s </%s>";
        StringBuilder xmlContent = new StringBuilder();
        for (Xml obj :
                this.list) {
            xmlContent.append(obj.toXML());
        }
        return String.format(xmlString, tag, xmlContent, tag);
    }

    public void add(T obj) {
        list.add(obj);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }
}
