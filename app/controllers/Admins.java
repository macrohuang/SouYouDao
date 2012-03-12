package controllers;

import play.mvc.Controller;

public class Admins extends Controller {

  public static void index() {
    render();
  }

  public static void scenic() {
    render("Admins/scenic/index.html");
  }
}
