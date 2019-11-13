public class Product implements Xml {
    private String productName;
    private String productVendor;
    private Integer unitsSold;
    private Integer totalSales;

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
