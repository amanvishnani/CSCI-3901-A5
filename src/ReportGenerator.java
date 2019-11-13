import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    List<Report> reports = new ArrayList<>();

    public ReportGenerator(ReportParams params) {
        this.reports.add(new CustomerReport(params));
        this.reports.add(new ProductReport(params));
        this.reports.add(new StaffReport(params));
        System.out.println(generate(params));
    }

    public boolean generate(ReportParams params) {
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
                return false;
            }
        }
        try {
            FileWriter fw = new FileWriter(params.outputFileName);
            String output = String.format(xml, reportContent.toString());
            output = formatXml(output);
            fw.write(output);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String formatXml(String xml) {
        try {
            final InputSource inputSource = new InputSource(new StringReader(xml));
            final Node documentElement = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                                        .parse(inputSource ).getDocumentElement();
            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer lsSerializer = impl.createLSSerializer();

            lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
            lsSerializer.getDomConfig().setParameter("xml-declaration", true);

            return lsSerializer.writeToString(documentElement);
        } catch (Exception e) {
            return xml;
        }
    }
}
