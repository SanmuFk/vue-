package com.fk.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet("/goodsServlet")
public class GoodsServlet extends BasicServlet{
    DBHelper db=new DBHelper();

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
        //设置编码
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        String op=req.getParameter("op");

        if("findAllGoods".equals(op)){
            findAllGoods(req,resp);
        }else if("findByGid".equals(op)){
            findByGid(req,resp);
        }else if("addCart".equals(op)){
            addCart(req,resp);
        }else if("findCart".equals(op)){
            findCart(req,resp);
        }

    }

    //查询当前用户购物车的数据
    private void findCart(HttpServletRequest req, HttpServletResponse resp) {
        String username=req.getSession().getAttribute("username")+"";

        String sql="select * from cartinfo,goods where cartinfo.gid=goods.gid and id=(select id from users where username=?)";
        List<Map<String,String>> list=db.find(sql,username);

        this.send(resp,list);
    }

    //加入购物车
    private void addCart(HttpServletRequest req,HttpServletResponse resp){
        String gid=req.getParameter("gid");
        String username=req.getSession().getAttribute("username")+"";

        //先判断一下，这个商品，这个用户，有没有加入过该商品到购物车
        String sql1="select * from cartinfo where gid=? and id=(select id from users where username=?)";
        List<Map<String,String>> list=db.find(sql1,gid,username);

        if(list.size()>0){
            //有，则修改商品数据
            String sql2="update cartinfo set num=num+1 where cid=?";
            db.doUpdate(sql2,list.get(0).get("cid"));
            this.send(resp,1);
        }else{
            //没有，则添加商品数据
            String sql3="insert into cartinfo values(null,(select id from users where username=?),?,1)";
            db.doUpdate(sql3,username,gid);
            this.send(resp,2);
        }
    }
    //根据gid查询商品
    private void findByGid(HttpServletRequest req,HttpServletResponse resp){
        String gid =req.getParameter("gid");

        String sql="select * from goods,attribute where goods.gid=attribute.gid and goods.gid=?";
        List<Map<String,String>> list=db.find(sql,gid);

        if(list.size()>0){
            this.send(resp,list);
        }else{
            this.send(resp,-1);
        }
    }

    //查询所有商品
    private void findAllGoods(HttpServletRequest req,HttpServletResponse resp){
        String sql ="select gid,gname,price,pic from goods limit 0,10";
        List<Map<String,String>> list=db.find(sql);

        this.send(resp,list);
    }

}
