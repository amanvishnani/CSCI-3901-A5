import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aman Vishnani (aman.vishnani@dal.ca)
 * ReportBuilder Class.
 * Generates XML report and returns formatted XML
 */
public class ReportBuilder {

    private List<Report> reports = new ArrayList<>();
    private ReportParams params;

    public ReportBuilder() {
    }

    public ReportBuilder withParams(ReportParams params) {
        this.params = params;
        return this;
    }

    public ReportBuilder addReport(Report report) {
        report.setParams(this.params);
        this.reports.add(report);
        return this;
    }

    /**
     * Builds the report based on configuration and Prints it to the Output File.
     * @return true if the report was built successfully.
     */
    public boolean build() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-16\" ?>" +
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
            OutputStreamWriter fw;
            fw = new OutputStreamWriter(new FileOutputStream(params.outputFileName), StandardCharsets.UTF_16);
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
