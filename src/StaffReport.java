import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffReport extends Report {

    private static final String SQL = "" +
            "SELECT employeenumber, \n" +
            "       firstname, \n" +
            "       lastname, \n" +
            "       ofc.city, \n" +
            "       Count(DISTINCT customernumber), \n" +
            "       Sum(quantityordered * priceeach) \n" +
            "FROM   orders AS o \n" +
            "       NATURAL JOIN orderdetails AS od \n" +
            "       NATURAL JOIN customers AS c \n" +
            "       INNER JOIN employees AS e \n" +
            "               ON e.employeenumber = c.salesrepemployeenumber \n" +
            "       INNER JOIN offices AS ofc \n" +
            "               ON e.officecode = ofc.officecode \n" +
            "WHERE  orderdate BETWEEN ? AND ? \n" +
            "GROUP  BY 1; ";


    private XmlList<Employee> staffList;
    private String startDate, endDate;
    private Connection connection;

    public StaffReport(ReportParams params) {
        this.setTag("staff_list");
        this.startDate = params.getStartDateStr();
        this.endDate = params.getEndDateStr();
        this.connection = params.getConnection();
        staffList = new XmlList<>(this.getTag());
    }

    @Override
    public String toXML() {
        return staffList.toXML();
    }

    @Override
    public void build() throws SQLException {
        if(!staffList.isEmpty()) {
            System.out.println("Warning: Staff List Report is already populated, recalculating.");
            staffList = new XmlList<>(this.getTag());
        }
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, startDate);
        statement.setString(2, endDate);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Employee employee = new Employee(rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getInt(5), rs.getDouble(6));
            staffList.add(employee);
        }
    }
}
