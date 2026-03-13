import java.io.IOException;//Used to handle input and output errors
import java.io.PrintWriter;//Used to  send the output to the browser
import java.sql.Connection;//Used to connect to the Database
import java.sql.DriverManager;//used to load JDBC driver into the memory
import java.sql.PreparedStatement;//Used to avoid the SQL injection(avoids hampering with the code)
import java.sql.ResultSet;//Used to store data returned from the select Query
import jakarta.servlet.http.HttpServlet;//It is a base class for creating http Servlet
import jakarta.servlet.http.HttpServletRequest;//Get the data from the client
import jakarta.servlet.http.HttpServletResponse;//Send the data to the client

public class BookServlet extends HttpServlet{
    
    //Handles GET request
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)//handles the GET method from the browser/get request
            throws IOException{

        //Set response type
        res.setContentType("text/html");//It tells the browser the response is HTML
        PrintWriter out=res.getWriter();//It creates an output string to write the html to the browser

        //Get search keyword from HTML form
        String key = req.getParameter("key");//it used to read the search keyword send from the html form
        
        try{
            Class.forName("org.postgresql.Driver");


            Connection con=DriverManager.getConnection(
                "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require&connectTimeout-10",
                "postgres.iikbcxxqbrjvuclltend",
                "Himangshu@2005"
            );
            String sql = "SELECT * FROM public.books WHERE title ILIKE ? OR author ILIKE ?";//selects all the records from the books table/ILIKE-used for case insensitive search
                PreparedStatement ps = con.prepareStatement(sql);//Used for partial Matching
                    ps.setString(1,"%"+key+"%");
                ps.setString(2,"%"+key+"%");
                
            ResultSet rs = ps.executeQuery();

            //Display result in HTML table
            out.println("<h2>Book List</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Title</th><th>Author</th><th>Price</th></tr>");

            while(rs.next()){
                out.println("<tr>");
                out.println("<td>" + rs.getString("title")+"</td>");
                out.println("<td>" + rs.getString("author")+"</td>");
                out.println("<td>" + rs.getInt("price")+"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            //Close connection
            con.close();
        }catch(Exception e){
            out.println("<p>Error:"+e+"</p>");
        }
    }
}
