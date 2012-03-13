package controllers;

import java.io.File;

import models.Scenic;
import models.images.ScenicImage;
import models.roadmaps.ScenicInnerRoadmap;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.libs.Codec;
import play.mvc.Controller;
import utils.FileUtil;

public class Admins extends Controller {

  public static void index() {
    render();
  }

  /**
   * 进入景区审核页面
   * @param scenicId
   * @param next
   */
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
    render("Admins/scenic/index.html", scenic, count);
  }

  /**
   * 审核并保存景区数据
   */
  public static void saveScenic(@Valid Scenic scenic,
      @Required(message = "内部游览图是必须的！") File roadmapImage,
      @Required(message = "景点图片是必须的！") File[] images,
      @Required(message = "内部游览路线是必须的！") String[] innerRoadmaps) throws Exception {
    if (validation.hasErrors()) {
      for (play.data.validation.Error e : validation.errors()) {
        System.out.println(e.getKey() + " " + e.message());
      }
      validation.keep();
      editScenic(scenic.id, false);
    }
    // 保存RoadmapImage
    String roadmapName = scenic.id + "_" + Codec.UUID() + "." + FileUtil.getExtension(roadmapImage);
    FileUtil.moveFile(roadmapImage,
        new File(FileUtil.getApplicationPath("data", "scenic", "roadmaps") + roadmapName));
    scenic.roadmapImage = roadmapName;
    // 保存Scenic Images
    for (int i = 0; i < images.length; i++) {
      String imageName = scenic.id + "_" + Codec.UUID() + "." + FileUtil.getExtension(images[i]);
      FileUtil.moveFile(images[i], new File(FileUtil.getApplicationPath("data", "scenic", "images")
          + imageName));
      new ScenicImage(imageName, scenic).save();
    }
    // 保存景点内部路线推荐
    for (String i : innerRoadmaps) {
      new ScenicInnerRoadmap(i, scenic).save();
    }
  }
}
