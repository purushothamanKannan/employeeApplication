import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class display extends HttpServlet {
    private String dburl = "jdbc:mysql://localhost:3306/employee";
    private String dbuname = "root";
    private String dbpassword = "root";
    private String dbdriver = "com.mysql.jdbc.Driver";

    public void loadDriver(String dbDriver) {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(dburl, dbuname, dbpassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        res.setContentType("text/html");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Employee Search</title>");
        out.println("<style>");
        out.println("body {font-family: Arial, sans-serif; background-color: #f4f7f6; margin: 0; padding: 0;}");
        out.println(".container {width: 80%; margin: 0 auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}");
        out.println("h2 {text-align: center; color: #4CAF50;}");
        out.println("table {width: 100%; border-collapse: collapse; margin: 20px 0;}");
        out.println("th, td {padding: 10px; text-align: left; border: 1px solid #ddd;}");
        out.println("th {background-color: #4CAF50; color: white;}");
        out.println("input[type='text'], input[type='submit'] {padding: 10px; font-size: 16px; width: 200px; margin-bottom: 20px; border: 1px solid #ddd; border-radius: 4px;}");
        out.println("input[type='submit'] {background-color: #4CAF50; color: white; cursor: pointer;}");
        out.println("input[type='submit']:hover {background-color: #45a049;}");
        out.println("button {padding: 10px 20px; font-size: 16px; cursor: pointer; background-color: #f44336; color: white; border: none; border-radius: 4px;}");
        out.println("button:hover {background-color: #e53935;}");
        out.println(".link-container {text-align: center; margin-top: 20px;}");
        out.println(".link-container a {font-size: 16px; text-decoration: none; color: #4CAF50;}");
        out.println(".link-container a:hover {text-decoration: underline;}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='container'>");
        out.println("<h2>Search Employee</h2>");

        try {
            loadDriver(dbdriver);
            Connection con = getConnection();
            Statement stmt = con.createStatement();

            String employeeSearchName = req.getParameter("employeeName");

            out.println("<form method='get' action='display'>");
            out.println("<label for='employeeName'>Search Employee by Name: </label>");
            out.println("<input type='text' name='employeeName' value='" + (employeeSearchName != null ? employeeSearchName : "") + "' placeholder='Search by name' />");
            out.println("<input type='submit' value='Search' />");
            out.println("<button type='button' onclick='clearSearch()'>Clear</button>");
            out.println("</form>");

            out.println("<div class='link-container'><a href='index.jsp'>Back to Employee Form</a></div>");

            ResultSet rs = stmt.executeQuery("SELECT * FROM employee.emp");
            if (employeeSearchName != null && !employeeSearchName.trim().isEmpty()) {
                String searchQuery = "SELECT * FROM employee.emp WHERE employee_name LIKE '%" + employeeSearchName + "%'";
                rs = stmt.executeQuery(searchQuery);
            }

            out.println("<table>");
            out.println("<tr><th>EmpId</th><th>EmpName</th><th>Salary</th><th>Experience</th><th>Date of Join</th><th>Age</th></tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("emp_id") + "</td>");
                out.println("<td>" + rs.getString("employee_name") + "</td>");
                out.println("<td>" + rs.getInt("employee_salary") + "</td>");
                out.println("<td>" + rs.getString("employee_experieance") + "</td>");
                out.println("<td>" + rs.getString("employee_date_of_join") + "</td>");
                out.println("<td>" + rs.getString("employee_age") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            con.close();
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
        }

        out.println("</div>");
        out.println("</body>");
        out.println("<script>");
        out.println("function clearSearch() {");
        out.println("document.getElementsByName('employeeName')[0].value = '';");
        out.println("window.location.href = 'display';");
        out.println("}");
        out.println("</script>");
        out.println("</html>");
    }
}
