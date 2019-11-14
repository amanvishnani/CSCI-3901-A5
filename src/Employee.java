/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Employee Model Class.
 */
public class Employee implements Xml{

    private String firstName;
    private String lastName;
    private String officeCity;
    private Integer activeCustomers;
    private Double totalSales;

    public Employee(String firstName, String lastName, String officeCity, Integer activeCustomers, Double totalSales) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.officeCity = officeCity;
        this.activeCustomers = activeCustomers;
        this.totalSales = totalSales;
    }

    @Override
    public String toXML() {
        XmlObject obj = new XmlObject("employee");
        obj.addField("first_name", this.firstName);
        obj.addField("last_name", this.lastName);
        obj.addField("office_city", this.officeCity);
        obj.addField("active_customers", this.activeCustomers);
        obj.addField("total_sales", this.totalSales);
        return obj.toXML();
    }
}
