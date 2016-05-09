package com.meetu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SQLiteJDBC {

    public static JSONArray schools() {
        Connection c = null;
        Statement stmt = null;
        JSONArray json = new JSONArray();
        try {
            Class.forName("org.sqlite.JDBC");
            // c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/schools.db");
            c = DriverManager.getConnection("jdbc:sqlite:schools.db");
            c.setAutoCommit(false);
            // System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from schools");
            while (rs.next()) {
                JSONObject jb = new JSONObject();
                jb.put("school_id", rs.getString("school_id"));
                jb.put("province_id", rs.getString("province_id"));
                jb.put("school_name", rs.getString("school_name"));

                json.add(jb);
                // System.out.println(jb.toString());
            }

            stmt.close();
            c.close();

        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
            // e.printStackTrace();
        }
        // System.out.println("Operation done successfully");
        return json;
    }

    public static JSONArray citys() {
        Connection c = null;
        Statement stmt = null;
        JSONArray ja = new JSONArray();
        try {
            Class.forName("org.sqlite.JDBC");
            // c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/city_new.db");
            c = DriverManager.getConnection("jdbc:sqlite:city_new.db");
            c.setAutoCommit(false);
            // System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from city");

            while (rs.next()) {
                JSONObject jb = new JSONObject();
                jb.put("id", rs.getString("id"));
                jb.put("province", rs.getString("province"));
                jb.put("province_num", rs.getString("province_num"));
                jb.put("city", rs.getString("city"));
                jb.put("city_num", rs.getString("city_num"));
                jb.put("town", rs.getString("town"));
                jb.put("town_num", rs.getString("town_num"));
                ja.add(jb);

            }
            // System.out.println(ja.size());

            // for(int i=0; i<ja.size(); i++){
            // if(i%5==0){
            // System.out.println(i+"_"+ja.get(i).toString());
            // }
            //
            // }
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        // System.out.println("Operation done successfully");
        return ja;
    }

    public static void main(String args[]) {

        citys();

    }
}
