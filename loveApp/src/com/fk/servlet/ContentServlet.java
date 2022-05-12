package com.fk.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/contentServlet")
public class ContentServlet extends BasicServlet{
    DBHelper db = new DBHelper();

    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException  {
        doPost(req,resp);
    }

    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        String op = req.getParameter("op");
        System.out.println(op);
        if("findAllType".equals(op)){
            findAllType(req, resp);
        } else if("findAllContent".equals(op)) {
            findAllContent(req,resp);
        } else if("findAllVideo".equals(op)){
            findAllVideo(req,resp);
        } else if("findByCid".equals(op)){
            findByCid(req,resp);
        } else if("put".equals(op)){
            put(req,resp);
        } else if("userinfo".equals(op)){
            userinfo(req,resp);
        }
    }

    private void userinfo(HttpServletRequest req, HttpServletResponse resp) {
        int id = 1 ;
        String sql ="select * from contents where id=?";
        List<Map<String,String>> list = db.find(sql,id);
        this.send(resp,list);
    }

    private void put(HttpServletRequest req, HttpServletResponse resp) {
        String ctitle = req.getParameter("ctitle");
        String id = req.getParameter("id");
        String content = req.getParameter("content");
        String pic = req.getParameter("pic");
        String clike = req.getParameter("click");
        String tid = req.getParameter("tid");
        String sql = "insert into contents values(null,?,?,?,?,?,?)";
        int result = db.doUpdate(sql,ctitle,pic,content,id,clike,tid);
        this.send(resp,result);
    }

    private void findByCid(HttpServletRequest req, HttpServletResponse resp) {
        String cid =req.getParameter("cid");
        String sql ="select * from contents as c join user as u on c.id=u.id where c.cid = ?";
        List<Map<String,String>> list = db.find(sql,cid);
        if(list.size() >0) {
            this.send(resp,list);
        } else {
            this.send(resp,-1);
        }
    }

    private void findAllVideo(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "SELECT vid,vtitle,vcontent,vtitle,vcontent,vlike,vpic,vhttp,nickname,img FROM video AS v LEFT JOIN USER AS u ON v.id=u.id";
        List<Map<String,String>> list = db.find(sql);
        this.send(resp,list);
    }

    private void findAllContent(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "select cid,ctitle,pic,nickname,img,clike,tid from contents as c left join user as u on c.id=u.id";
        List<Map<String,String>> list = db.find(sql);
        this.send(resp,list);
    }

    private  void findAllType(HttpServletRequest req, HttpServletResponse resp){
        String sql = "select tid,tname from type ";
        List<Map<String,String>> list =db.find(sql);
        System.out.println(list);
        this.send(resp,list);
    }

}
