import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    List<Report> reports = new ArrayList<>();

    public ReportGenerator(ReportParams params) {
        this.reports.add(new CustomerReport(params));
        System.out.println(generate(params));
    }

    public String generate(ReportParams params) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<year_end_summary> %s </year_end_summary>";
        Year year = new Year(params.getStartDateStr(), params.getEndDateStr());

        StringBuilder reportContent = new StringBuilder(year.toXML());
        for (Report report:
             reports) {
            try {
                report.build();
                reportContent.append(report.toXML());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return String.format(xml, reportContent.toString());
    }
}
