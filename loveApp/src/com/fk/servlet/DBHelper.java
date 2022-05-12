package com.fk.servlet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
    /**
     * 增删改的方法
     * @param sql       要执行的sql语句
     * @param params    注入点的参数
     * @return  受影响的行
     */
    public int doUpdate(String sql, Object... params){
        int result = -1;
        try {
            Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            execParams(ps, params);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Map<String, String>> find(String sql, Object... params){
        List<Map<String, String>> list = new ArrayList();
        try {
            Connection con = getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            execParams(ps, params);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            String[] colNames = new String[ rsmd.getColumnCount() ];
            for(int i = 0; i < colNames.length; i++){
                colNames[i] = rsmd.getColumnName( i + 1 );
            }
            while( rs.next() ){
                Map<String, String> map = new HashMap<String,String>();
                for(int i = 0; i < colNames.length; i++){
                    map.put( colNames[i], rs.getString( colNames[i] ) );
                }
                list.add(map);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 注入参数
     * @param ps    语句对象
     * @param params    量词参数
     */
    public void execParams(PreparedStatement ps, Object... params) {
        if( ps != null  && params.length > 0 ){
            for(int i = 0; i < params.length; i++){
                try {
                    ps.setObject(i + 1, params[i]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取链接
     * @return   获取到的连接
     */
    public Connection getCon(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loveapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "151011");
        } catch (SQLException e) { }
        return connection;
    }

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
