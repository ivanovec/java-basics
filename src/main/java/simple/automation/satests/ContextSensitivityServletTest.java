package simple.automation.satests;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

interface UsernameProvider {
    String getUsername(HttpServletRequest request);
}

class TaintedUsernameProvider implements UsernameProvider {
    @Override
    public String getUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }
}

class ConstantUsernameProvider implements UsernameProvider {
    @Override
    public String getUsername(HttpServletRequest request) {
        return "admin";
    }
}

public class ContextSensitivityServletTest extends HttpServlet {
    private UsernameProvider provider;

    public ContextSensitivityServletTest(UsernameProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void  doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = provider.getUsername(request);
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "")){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            while (rs.next()){
                response.getWriter().println(rs.getString("username"));
            }
        } catch (SQLException e) {
            throw  new ServletException(e);
        }
    }
}
