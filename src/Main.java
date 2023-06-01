import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {

    Connection conn = null;
    String table_name = ""; // example

    public String input(String s) {
        System.out.print(s + " : ");
        return new Scanner(System.in).nextLine();
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");

        String ip = ""; // 127.0.0.1
        String port_num = ""; // 3306
        String schema_name = ""; // example

        String url = "jdbc:mariadb://"+ip+":"+port_num+"/"+schema_name;
        String userid = input("userid"); // root
        String userpw = input("userpw"); // 1234

        conn = DriverManager.getConnection(url, userid, userpw);
    }

    public void insert(int lastId) throws SQLException, IOException {
        FileReader file = new FileReader(""); // C:\data.txt
        BufferedReader reader = new BufferedReader(file);
        String r;

        String sql = "INSERT INTO " + table_name + " VALUES(?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        reader.readLine();
        while (( r = reader.readLine()) != null) {
            String[] s = r.split(""); // ,

            pstmt.setInt(1, ++lastId);
            for(int i=1; i<s.length;++i) {
                pstmt.setString(i+1, s[i]);
            }

            pstmt.executeQuery();

            System.out.println(r);
        }
    }

    // Get id(primary key) final value
    public int getFinalId() throws SQLException {
        String id_column = ""; // id
        String sql = "SELECT " + id_column + " FROM " + table_name + " ORDER BY " + id_column + " DESC";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        return rs.next()?rs.getInt(1):0;
    }

    public void execute() {
        try {
            connect();

            System.out.println("connect");

            insert(getFinalId());

            System.out.println("success");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().execute();
    }
}
