/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Product Model Class.
 */
public class Product implements Xml {
    private String productName;
    private String productVendor;
    private Integer unitsSold;
    private Double totalSales;

    public Product(String productName, String productVendor, Integer unitsSold, Double totalSales) {
        this.productName = productName;
        this.productVendor = productVendor;
        this.unitsSold = unitsSold;
        this.totalSales = totalSales;
    }

    @Override
    public String toXML() {
        XmlObject obj = new XmlObject("product");
        obj.addField("product_name", productName);
        obj.addField("product_vendor", productVendor);
        obj.addField("units_sold", unitsSold);
        obj.addField("total_sales", totalSales);
        return obj.toXML();
    }
}
