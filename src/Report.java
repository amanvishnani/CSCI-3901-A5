import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Report Stub for other reports to follow.
 */
public abstract class Report implements Xml {
    private String tag;
    private String startDate, endDate;
    private Connection connection;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setParams(ReportParams params) {
        this.startDate = params.getStartDateStr();
        this.endDate = params.getEndDateStr();
        this.connection = params.getConnection();
    }

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
