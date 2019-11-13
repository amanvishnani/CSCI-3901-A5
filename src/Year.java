public class Year implements Xml {

    private String startDate;
    private String endDate;

    public Year(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toXML() {
        XmlObject obj = new XmlObject("year");
        obj.addField("start_date", startDate);
        obj.addField("end_date", endDate);
        return obj.toXML();
    }
}
