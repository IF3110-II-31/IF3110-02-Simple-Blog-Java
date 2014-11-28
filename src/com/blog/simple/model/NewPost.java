package com.mycompany.wbd;
import java.util.Date;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ramandika
 */
@ManagedBean(name="newPost")
@RequestScoped
public class NewPost {
    String title;
    Statement stmt;  
    ResultSet result;
    Date tanggal;
    String content;
    Connection con;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }  

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
    
    public void setTanggal(String tanggal) {
        this.tanggal = new Date(tanggal);
    }

    public Date getTanggal() {
        return tanggal;
    }
    
    public String add() throws SQLException {
        java.sql.Date valid=new java.sql.Date(tanggal.getTime());
        try {  
            Class.forName("com.mysql.jdbc.Driver");  
            con = DriverManager.getConnection("jdbc:mysql://localhost/simpleblog","root","");  
        } catch (ClassNotFoundException e) {  
            System.out.println("Class Not Found");  
        } catch (SQLException e) {  
            System.out.println("Unable to connect");  
        }    
            try {
                System.out.println("Ready to do Query");
                String query = "INSERT INTO posting (tanggal, judul, konten) VALUES (?,?,?)";
                PreparedStatement ps = con.prepareStatement(query);
                
                Object values[] = {
                    valid,
                    title,
                    content
                };
                for(int i = 0; i < values.length; i++) 
                    ps.setObject(i+1, values[i]);
                
                int affectedRow = ps.executeUpdate();  
                if(affectedRow == 0)
                    throw new SQLException("Data insertion failed");
                  
            } catch (SQLException e) {
                throw e;
            }
       return "Home";
    }
    
    
}
