package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import models.Admin;
import models.Scenic;
import models.images.ScenicImage;
import models.roadmaps.ScenicInnerRoadmap;
import models.utils.area.Region;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.libs.Codec;
import utils.Constants;
import utils.FileUtil;
import utils.Secure;

public class Admins extends Application {

  @Secure(admin=true)
  public static void index() {
    render();
  }
  /**
   * 创建新景点
   * @param status (1:success,-1:failure)
   */
  @Secure(admin=true)
  public static void newScenic(int status){
    render("Admins/scenic/new.html",status);
  }
  @Secure(admin=true)
  public static void saveNewScenic(@Required String name,Region region){
    Scenic scenic = new Scenic();
    scenic.name = name;
    scenic.region = region;
    scenic.save();
    newScenic(1);
  }
  /**
   * 进入景区审核页面
   * @param scenicId
   * @param next
   */
  @Secure(admin=true)
  public static void editScenic(Long scenicId, boolean next) {
    Scenic scenic = null;
    if (scenicId != null && !next) {
      scenic = Scenic.findById(scenicId);
    } else if (!next) {
      scenic = scenic.getNextUnAuthorized(0);
    } else {
      scenic = scenic.getNextUnAuthorized(scenicId);
    }
    long count = Scenic.count("authorized = false");
    if(scenic == null){
      newScenic(-1);
    }
    List<Scenic> scenics = Scenic.find("authorized = ? and authorizer.id = ? order by authorizeDate desc",
        true,Long.parseLong(session.get(Constants.ADMIN_ID_IN_SESSION))).fetch();
    render("Admins/scenic/index.html", scenic, count, scenics);
  }

  /**
   * 完善并保存景区数据
   */
  @Secure(admin=true)
  public static void saveScenic(@Valid(message = "") Scenic scenic,
      File roadmapImage,File[] images,@Required(message = "内部游览路线是必须的！") String[] innerRoadmaps) throws Exception {
    if (validation.hasErrors()) {
      for (play.data.validation.Error e : validation.errors()) {
        System.out.println(e.getKey() + " ---- " + e.message());
      }
      validation.keep();
      editScenic(scenic.id, false);
    }
    // 保存RoadmapImage
    if(roadmapImage!=null){
     String roadmapName = scenic.id + "_" + Codec.UUID() + "." + FileUtil.getExtension(roadmapImage);
     FileUtil.moveFile(roadmapImage,
         new File(FileUtil.getApplicationPath("data", "scenic", "roadmaps") + roadmapName));
     scenic.roadmapImage = roadmapName;
    }
    // 保存Scenic Images
    if(images!=null){
     for (int i = 0; i < images.length; i++) {
       String imageName = scenic.id + "_" + Codec.UUID() + "." + FileUtil.getExtension(images[i]);
       FileUtil.moveFile(images[i], new File(FileUtil.getApplicationPath("data", "scenic", "images")
           + imageName));
       new ScenicImage(imageName, scenic).save();
     }
    }
    // 保存景点内部路线推荐
    for (String i : innerRoadmaps) {
      new ScenicInnerRoadmap(i, scenic).save();
    }
    scenic.authorized = true;
    scenic.authorizeDate = new Date();
    scenic.authorizer = Admin.findById(Long.parseLong(session.get(Constants.ADMIN_ID_IN_SESSION)));
    scenic.save();
    editScenic(scenic.id, true);
  }
  public static void login(){
    render("Admins/login.html");
  }
  public static void loginOK(@Valid Admin admin){
    if(validation.hasErrors()){
      validation.keep();
      login();
    }
    Admin a = Admin.find("username = ? and password = ?", admin.username,admin.password).first();
    if(a == null){
      validation.addError("Login Failed","没有该账号!");
    }
    if(validation.hasErrors()){
      validation.keep();
      login();
    }
    session.put(Constants.ADMIN_ID_IN_SESSION, a.id);
    render("Admins/index.html");
  }
}
