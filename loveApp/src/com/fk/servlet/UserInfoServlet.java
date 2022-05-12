package com.fk.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/userinfoservlet")
public class UserInfoServlet extends BasicServlet{

    private DBHelper db = new DBHelper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        findUserInfo(req,resp);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);

    }

    private void findUserInfo(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "select * from userinto as u1 left join user as u2 on u1.id=u2.id ";
        List<Map<String, String>> list= db.find(sql);
        this.send(resp,list);

    }


}
