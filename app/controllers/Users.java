package controllers;

import models.User;
import play.data.validation.Valid;
import play.libs.Codec;
import utils.Constants;


public class Users extends Application {
  /**
   * 登录或注册页面
   */
  public static void login() {
    render();
  }

  public static void doLogin(String username, String password) {
    if (username != null && password != null) {
			User user = User.find("username = ? and password = ?", username, Codec.hexMD5(password)).first();
      if (user != null) {
        // 登陆成功
        loginSession(user);
        Plans.index();
      }
    }
    login();
  }

  public static void logout() {
    clearSession();
    Application.index();
  }

  public static void save(@Valid User user) {
    if (validation.hasErrors()) {
      validation.keep();
      login();
    }
    user.password = Codec.hexMD5(user.password);
    user.save();
    loginSession(user);
    Plans.index();
  }

  private static void loginSession(User user) {
    session.put(Constants.USER_ID_IN_SESSION, user.id);
    session.put(Constants.USERNAME_IN_SESSION, user.username);
  }

  private static void clearSession() {
    session.clear();
  }
}
