import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerReport extends Report {

    private static final String SQL = "" +
            "SELECT c.customername                         AS customerName, \n" +
            "       Concat(c.addressline1, c.addressline2) AS streetAdderess, \n" +
            "       c.city                                 AS city, \n" +
            "       c.postalcode                           AS postalCode, \n" +
            "       c.country                              AS country, \n" +
            "       Count(DISTINCT od.ordernumber)         AS numOrders, \n" +
            "       Sum(od.priceeach * od.quantityordered) AS orderValue \n" +
            "FROM   orders AS o \n" +
            "       NATURAL JOIN orderdetails AS od \n" +
            "       NATURAL JOIN customers AS c \n" +
            "WHERE  orderdate BETWEEN ? AND ? \n" +
            "GROUP  BY 1; ";

    private XmlList<Customer> customerXmlList;
    private String startDate, endDate;
    private Connection connection;


    public CustomerReport(ReportParams params) {
        this.setTag("customer_list");
        this.startDate = params.getStartDateStr();
        this.endDate = params.getEndDateStr();
        this.connection = params.getConnection();
        customerXmlList = new XmlList<>(this.getTag());
    }

    @Override
    public String toXML() {
        return customerXmlList.toXML();
    }

    @Override
    public void build() throws SQLException {

        if(!customerXmlList.isEmpty()) {
            System.out.println("Warning: Customer Report is already populated, recalculating.");
            customerXmlList = new XmlList<>(this.getTag());
        }

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, startDate);
        statement.setString(2, endDate);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Address address = new Address(rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5));
            Customer customer = new Customer(
                    rs.getString(1),
                    address, rs.getInt(6),
                    rs.getDouble(7)
            );
            customerXmlList.add(customer);
        }

    }
}
