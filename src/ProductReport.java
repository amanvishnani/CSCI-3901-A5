import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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

    private XmlList<XmlList> productList;

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
        LinkedHashMap<String, ArrayList<Product>> map = new LinkedHashMap<>();
        while (set.next()) {
            Product p = new Product(set.getString(2), set.getString(3),
                    set.getInt(4), set.getDouble(5));
            map.computeIfAbsent(set.getString(1), key -> new ArrayList<>());
            map.get(set.getString(1)).add(p);
        }

        for (Map.Entry<String, ArrayList<Product>> entry :
                map.entrySet()) {
            XmlList<Xml> list = new XmlList<>("product_set");
            list.add(new XmlUnit("product_line_name", entry.getKey()));
            for (Product p :
                    entry.getValue()) {
                list.add(p);
            }
            productList.add(list);
        }
    }
}
