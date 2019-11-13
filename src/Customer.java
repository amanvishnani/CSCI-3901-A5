public class Customer implements Xml {

    private String customerName;
    private Address address;
    private Integer numOrders;
    private Double orderValue;

    public Customer(String customerName, Address address, Integer numOrders, Double orderValue) {
        this.customerName = customerName;
        this.address = address;
        this.numOrders = numOrders;
        this.orderValue = orderValue;
    }

    @Override
    public String toXML() {
        XmlObject obj = new XmlObject("customer");
        obj.addField("customer_name", customerName);
        obj.addField("address", address);
        obj.addField("num_orders", numOrders);
        obj.addField("order_values", orderValue);
        return obj.toXML();
    }
}
