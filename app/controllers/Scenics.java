package controllers;

import java.io.File;
import java.util.List;

import models.Scenic;
import models.images.ScenicImage;
import play.Logger;
import play.mvc.Controller;
import utils.FileUtil;

public class Scenics extends Controller {

  public static void index() {
    render();
  }

  public static void detail(Long id) {
    Scenic scenic = Scenic.findById(id);
    render(scenic);
  }

  public static void search(String keywords) {
    Logger.info("搜索景区：" + keywords);
    // String[] words = QuerySpliter.splite(keywords);
    List<Scenic> scenics = Scenic.findAll();
    render("Scenics/list.html", scenics, keywords);
  }

  /**
   * 删除景区内的图片一张
   * @param imageId
   */
  public static void deleteImage(Long imageId) {
    ScenicImage image = ScenicImage.findById(imageId);
    new File(FileUtil.getApplicationPath("data", "scenic", "images") + image.imageName).delete();
    image.delete();
  }

  public static void deleteRoadmap(Long scenicId) {
    Scenic scenic = Scenic.findById(scenicId);
    new File(FileUtil.getApplicationPath("data", "scenic", "roadmaps") + scenic.roadmapImage)
        .delete();
    scenic.roadmapImage = null;
    scenic.save();
  }
}
