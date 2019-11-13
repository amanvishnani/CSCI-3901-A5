import java.sql.Connection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String startDateStr = "1970-01-01";
        String endDateStr = "2050-12-31";
        String outputFileName = "output.xml";
        String x = "";

        Scanner s = new Scanner(System.in);

        Connection connection = ConnectionManager.getConnection();
        if(connection == null) {
            System.err.println("Could Not Establish Connection to the Database. Please Configure db details in" +
                    " ConnectionManager.java file and ensure driver is on class path.");
            return;
        }

        do {
            System.out.print(String.format("Enter Start Date in YYYY-MM-DD format (default: %s):", startDateStr));
            x = s.nextLine().trim();
        } while(
            !x.isEmpty() && !isDate(x)
        );
        if(!x.isEmpty()) {
            startDateStr = x;
            x = "";
        }

        do {
            System.out.print(String.format("Enter End Date in YYYY-MM-DD format (default: %s):", endDateStr));
            x = s.nextLine().trim();
        } while(
                !x.isEmpty() && !isDate(x)
        );
        if(!x.isEmpty()) {
            endDateStr = x;
            x = "";
        }

        System.out.print(String.format("Enter Output File Name (default: %s):", outputFileName));
        x = s.nextLine().trim();

        if(!x.isEmpty()) {
            outputFileName = x;
        }

        ReportParams params = new ReportParams(startDateStr, endDateStr, outputFileName, connection);
        ReportGenerator reportGenerator = new ReportGenerator(params);
    }

    static boolean isDate(String dateStr) {
        Pattern p = Pattern.compile("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
        Matcher m = p.matcher(dateStr);
        return m.matches();
    }
}
