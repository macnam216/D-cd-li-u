import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Main extends Thread{


    public static int insertCandidate(int id,String ten,int tuoi,
                                      String gioitinh) {
        // for insert a new candidate
        ResultSet rs = null;
        int candidateId = 0;

        String sql = "INSERT INTO SINHVIEN(id,ten,tuoi,gioitinh) "
                + "VALUES(?,?,?,?)";

        try (Connection conn = MySQLJDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {

            // set parameters for statement
            pstmt.setInt(1, id);
            pstmt.setString(2, ten);
            pstmt.setInt(3, tuoi);
            pstmt.setString(4, gioitinh);
            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1)
            {
                // get candidate id
                rs = pstmt.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return candidateId;
    }
    @Override
    public void run(){
        super.run();
        FileInputStream fin = null;
        String nam;
        boolean errDetail=true;

        String[] w = new String[0];
        try {
            fin = new FileInputStream("/home/default/nam.txt");
            BufferedInputStream bis = new BufferedInputStream(fin);
            int data = bis.read();
            StringBuilder line = new StringBuilder();
            int a=0;
            while (data != -1) {

                if (((char) data == '\n') || ((char) data == '\r')) {
                    a++;
                    w = line.toString().split(";");
                   /* for( int i=0; i<w.length;i++){
                        if(w[i].compareTo("")==0)
                            errDetail=false;
                            break;
                    }*/

                    if(w[0].compareTo("")==0||w[1].compareTo("")==0||w[2].compareTo("")==0||w[3].compareTo("")==0){
                        System.out.println("Thieu thong tin ơ dong thu "+a);
                        line.delete(0, line.length());
                        data = bis.read();
                        continue;
                    }
                    else {
                        int id = insertCandidate(Integer.parseInt(w[0]), w[1], Integer.parseInt(w[2]), w[3]);
                        System.out.println("A new candidate with id="+w[0]+" has been inserted.");
                        line.delete(0, line.length());
                        data = bis.read();
                        continue;
                    }

                }
                line.append((char) data);
                data = bis.read();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null)
                    fin.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static void main(String[] args) {
        Main thread1= new Main();
        thread1.start();

    }
}