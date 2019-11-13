import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Period;

public class ProductReport extends Report {

    private static final String SQL = "" +
            "SELECT p.productline, \n" +
            "       p.productname, \n" +
            "       p.productvendor, \n" +
            "       Sum(od.quantityordered)                AS units_sold, \n" +
            "       Sum(od.quantityordered * od.priceeach) AS total_sales \n" +
            "FROM   orders AS o \n" +
            "       NATURAL JOIN orderdetails AS od \n" +
            "       NATURAL JOIN products AS p \n" +
            "WHERE  o.orderdate BETWEEN ? AND ? \n" +
            "GROUP  BY 1, \n" +
            "          2, \n" +
            "          3; ";

    private XmlList<XmlObject> productList;
    private String startDate, endDate;
    private Connection connection;

    public ProductReport(ReportParams params) {
        this.setTag("product_list");
        this.startDate = params.getStartDateStr();
        this.endDate = params.getEndDateStr();
        this.connection = params.getConnection();
        productList = new XmlList<>(this.getTag());
    }

    @Override
    public String toXML() {
        return productList.toXML();
    }

    @Override
    public void build() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SQL);
        stmt.setString(1, startDate);
        stmt.setString(2, endDate);
        ResultSet set = stmt.executeQuery();
        while (set.next()) {
            XmlObject object = new XmlObject("product_set");
            object.addField("product_line_name", set.getString(1));
            Product p = new Product(set.getString(2), set.getString(3),
                    set.getInt(4), set.getDouble(5));
            object.addField("product", p);
            productList.add(object);
        }
    }
}
