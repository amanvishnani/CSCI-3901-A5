import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    /**
     * Adds {@link Report} to the list of reports.
     * @param report the report to be added.
     * @return Report builder object for chaining.
     */
    public ReportBuilder addReport(Report report) {
        report.setParams(this.params);
        this.reports.add(report);
        return this;
    }

    /**
     * Builds the {@link Report} based on configuration and Prints it to the Output File.
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
                // Build every report
                report.build();
                // Append result of report to report content
                reportContent.append(report.toXML());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            OutputStreamWriter fw;
            fw = new OutputStreamWriter(new FileOutputStream(params.outputFileName), StandardCharsets.UTF_16);
            // Create final XML
            String output = String.format(xml, reportContent.toString());
            // Format final XML
            output = formatXml(output);
            // Write XML to file.
            fw.write(output);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Formats XML into a Human Readable form.
     * @param xml unformatted xml text
     * @return formatted xml text
     */
    public static String formatXml(String xml) {
        try {
            Node xmlDocument = getXmlDocument(xml);
            LSSerializer lsSerializer = getSerializer();
            return lsSerializer.writeToString(xmlDocument);
        } catch (Exception e) {
            return xml;
        }
    }

    /**
     * Converts XML String to a {@link Node} object.
     * @param xml the unformatted xml string.
     * @return {@link Node} object
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Node getXmlDocument(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource(new StringReader(xml));
        Node documentElement;
        documentElement = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(inputSource ).getDocumentElement();
        return documentElement;
    }

    /**
     * Provides DOM Serializer
     * @return the Serializer for XML
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static LSSerializer getSerializer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
        LSSerializer lsSerializer = impl.createLSSerializer();

        lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
        lsSerializer.getDomConfig().setParameter("xml-declaration", true);
        return lsSerializer;
    }
}
