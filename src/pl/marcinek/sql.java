package pl.marcinek;

import java.sql.*;
import static pl.marcinek.Load.*;

public class sql {
    private static Connection conn = null;
    private static Statement st = null;
    private static PreparedStatement preparedStmt = null;
    public static String id;
    public static String punkty;

    public static void sql() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/points", "root", "");
        st = conn.createStatement();
    }

    public static void addpkt() throws SQLException, ClassNotFoundException {
        sql.sql();
        String query = "UPDATE points SET punkty = punkty + '" + amount + "' WHERE admin ='" + who + "'";
        st.executeUpdate(query);
        conn.close();
    }

    public static void removepkt() throws SQLException, ClassNotFoundException {
        sql.sql();
        String query = "UPDATE points SET punkty = punkty - '" + amount + "' WHERE admin = '" + who + "'";
        st.executeUpdate(query);
        conn.close();
    }

    public static void adduser() throws SQLException, ClassNotFoundException {
        sql.sql();
        String query = "insert into points(admin, punkty)" + " values (?, ?)";
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, odp);
        preparedStmt.setString(2, "0");
        preparedStmt.executeUpdate();
        conn.close();
    }

    public static void removeuser() throws SQLException, ClassNotFoundException {
        sql.sql();
        PreparedStatement st = conn.prepareStatement("DELETE FROM points WHERE admin = '" + odp + "';");
        st.executeUpdate();
        conn.close();
    }

    public static void showuser() throws SQLException, ClassNotFoundException {
        sql.sql();
        String query = "SELECT * FROM points WHERE admin = '" + who + "';";
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            id = rs.getString("id");
            punkty = rs.getString("punkty");

        }
        st.executeQuery(query);
        conn.close();
    }
}