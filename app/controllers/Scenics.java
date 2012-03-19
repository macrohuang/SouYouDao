package controllers;

import models.images.ScenicImage;
import play.mvc.Controller;

public class Scenics extends Controller {

  public static void index() {
    render();
  }

  public static void detail() {
    render();
  }

  /**
   * 删除景区内的图片一张
   * @param imageId
   */
  public static void deleteImage(Long imageId) {
    ScenicImage image = ScenicImage.findById(imageId);
    image.delete();
  }
}
