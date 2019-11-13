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
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", true);
            writer.getDomConfig().setParameter("xml-declaration", true);

            return writer.writeToString(document);
        } catch (Exception e) {
            return xml;
        }
    }
}
