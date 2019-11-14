import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Staff Report Class.
 * Extends @Report stub.
 */
public class StaffReport extends Report {

    // SQL Query for the report.
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

    public StaffReport() {
        this.setTag("staff_list");
        staffList = new XmlList<>(this.getTag());
    }

    @Override
    public String toXML() {
        return staffList.toXML();
    }

    /**
     * Builds the report and persists in the object.
     * @throws SQLException
     */
    @Override
    public void build() throws SQLException {
        if(!staffList.isEmpty()) {
            System.out.println("Warning: Staff List Report is already populated, recalculating.");
            staffList = new XmlList<>(this.getTag());
        }
        PreparedStatement statement = getConnection().prepareStatement(SQL);
        statement.setString(1, getStartDate());
        statement.setString(2, getEndDate());
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Employee employee = new Employee(rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getInt(5), rs.getDouble(6));
            staffList.add(employee);
        }
    }
}
