package com.fk.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@WebServlet("/userServlet")
public class UserServlet extends BasicServlet {
      private DBHelper db=new DBHelper();

      //alt+shift+s
      @Override
      protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
          doPost(req,resp);
      }

      @Override
      protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
          //接受用户传过来的参数
          String op=req.getParameter("op");
          //System.out.println(op);

          //System.out.println("接收到请求了");

          //设置编码
          req.setCharacterEncoding("utf-8");
          resp.setCharacterEncoding("utf-8");

          if("login".equals(op)){
              login(req,resp);
          }

          //this.send(resp,2);
      }

    //登录
    private void login(HttpServletRequest req,HttpServletResponse resp){
        //首先，先得到传过来的用户名和密码
        String username=req.getParameter("username");
        String password=req.getParameter("password");

        String sql ="select * from user where username=?";
        List<Map<String,String>> list=db.find(sql,username);

        //String username = req.getSession().getAttribute("username") + "";// 通过session会话获取用户名字

        //判断逻辑
        if(list.size()<=0){
            //用户名不存在，意味着，这个账号不存在，根据我们的逻辑，账号不存在，则直接创建新账号用户
            Random r=new Random();
            String nickname="默认用户"+(r.nextInt(100000)+100000);
            String sql2="insert into user values(null,?,?,?,1,'images/cover.jpg')";
            int result=db.doUpdate(sql2,nickname,username,password);

            req.getSession().setAttribute("username", username);// 通过session会话获取用户名字
            //将结果返回给前端页面
            this.send(resp,nickname);
        }else{
            //这个用户存在，判断密码是否相等
            if(password.equals(list.get(0).get("pwd"))){
                //登录成功
                req.getSession().setAttribute("username", username);// 通过session会话获取用户名字
                this.send(resp,list.get(0).get("nickname"));
            }else{
                this.send(resp,-1);
            }
        }
    }
}
