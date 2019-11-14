import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Product Report Class.
 * Extends @Report stub.
 */
public class ProductReport extends Report {

    // SQL Query for the report.
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

    public ProductReport() {
        this.setTag("product_list");
        productList = new XmlList<>(this.getTag());
    }

    @Override
    public String toXML() {
        return productList.toXML();
    }

    /**
     * Builds the report and persists in the object.
     * @throws SQLException
     */
    @Override
    public void build() throws SQLException {
        PreparedStatement stmt = getConnection().prepareStatement(SQL);
        stmt.setString(1, getStartDate());
        stmt.setString(2, getEndDate());
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
