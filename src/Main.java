import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private Object[][] data;
    private Object[] category;
    private Object[][] klasifikasi = {{"<=30","medium","yesn","fair"}};
//    private Object[][] klasifikasi = {{"3","IPS","B","A"}};
    private HashSet<String> hash = new HashSet<>();    
    int sum[];
    private Bayes b;
    
    public void read_db()throws Exception{
        String Query = "select * from data_bayes";
        Statement st = Koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery(Query);
        data = new Object[getRow()][5];
        
        System.out.println("Data :");
        int i = 0;
        while(rs.next()){
            for(int j = 0; j < data[0].length; j++){
                data[i][j] = rs.getString(j+1);
                System.out.print(data[i][j]+", ");
            }
            System.out.println();
            i++;
        }
        System.out.println("-----------------------------------");
        category = new Object[data.length];
        for(i = 0; i < data.length; i++){
            category[i] = data[i][data[i].length-1];
            hash.add(String.valueOf(category[i]));
        }        
        sum = new int[hash.size()];        
    }
    
    public void proses(){
        b= new Bayes(data, category, klasifikasi, hash.size(), 4);        
        if(!b.dataBaru.isEmpty()){
            b.dataBaru.clear();
            b.key.clear();
        }
        int idx = 0;
        for(String s:hash){
            for(int ii = 0; ii < category.length; ii++){
                if(s.equals(String.valueOf(category[ii]))){
                    sum[idx]+=1;
                }
            }
            b.key.add(new Key(idx, s));
            idx++;
        }
        b.classify(sum);
        System.out.println("\nHasil Klasifikasi :");
        int row = 0;
        for(Key key:b.getNewLabel()){
            System.out.println("Data ke "+(row+1)+" diklasifikasikan ke kelas :\t"+key.getInfo());
            row++;
        }
    }
    
    public Main(){
        try {
            read_db();
            proses();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getRow()throws Exception{
        int row = 0;
        String Query = "SELECT COUNT(*) as baris FROM data_bayes";
        Statement st = Koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery(Query);
        while(rs.next())
            row = rs.getInt("baris");
        st.close();
        rs.close();
        return row;
    }
    
    public static void main(String[]args){
        new Main();
    }
}