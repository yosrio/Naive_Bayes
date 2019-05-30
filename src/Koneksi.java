import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection(){
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bayes","root","");
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return con;
    }
}