/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * Address Model Class.
 */
public class Address implements Xml {

    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;

    public Address(String streetAddress, String city, String postalCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public String toXML() {
        XmlObject obj = new XmlObject("address");
        obj.addField("street_address", streetAddress);
        obj.addField("city", city);
        obj.addField("postal_code", postalCode);
        obj.addField("country", country);
        return obj.toXML();
    }
}
