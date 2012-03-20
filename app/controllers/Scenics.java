package controllers;

import java.io.File;

import models.Scenic;
import models.images.ScenicImage;
import play.mvc.Controller;
import utils.FileUtil;

public class Scenics extends Controller {

  public static void index() {
    render();
  }

  public static void detail() {
    render();
  }

  public static void list() {
    render();
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
