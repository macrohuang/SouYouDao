package controllers;

import models.Admin;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Constants;
import utils.Secure;

public class Application extends Controller {

    public static void index() {
      render();
    }

    public static void search(String keywords){
      render(keywords);
    }

    @Before
    public static void login(){

    }
    @Before
    public static void admin(){
      Secure secure = getActionAnnotation(Secure.class);
      //如果没有注解或者注解需要登录均需验证登录状态
      if(secure != null && secure.admin() == true){
        Long admin_id = Long.parseLong(session.get(Constants.ADMIN_ID_IN_SESSION));
        Admin admin = Admin.findById(admin_id);
       if(admin==null){
         Admins.login();
       }
      }
    }
}