import java.sql.Connection;

/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Report Parameters class.
 */
public class ReportParams {
    String startDateStr;
    String endDateStr;
    String outputFileName;
    Connection connection;

    public ReportParams(String startDateStr, String endDateStr, String outputFileName, Connection connection) {
        this.startDateStr = startDateStr;
        this.endDateStr = endDateStr;
        this.outputFileName = outputFileName;
        this.connection = connection;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
