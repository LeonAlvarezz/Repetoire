package com.example.repertoire;
import java.sql.*;
public class dbConnection {
    Connection con = null;
    public static Connection dbConnector(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:accountData.db");
            return con;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
