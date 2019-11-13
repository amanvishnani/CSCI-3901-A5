import java.sql.SQLException;

public abstract class Report implements Xml {
    private String tag;

    public Report(String tag) {
        this.tag = tag;
    }

    public Report() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    abstract public String toXML();

    abstract public void build() throws SQLException;
}
