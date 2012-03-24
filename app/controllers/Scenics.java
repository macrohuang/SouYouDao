package controllers;

import java.io.File;
import java.util.List;

import models.Scenic;
import models.images.ScenicImage;
import play.Logger;
import play.mvc.Controller;
import utils.Constants;
import utils.FileUtil;
import utils.QuerySpliter;

public class Scenics extends Controller {

  public static void index() {
    render();
  }

  public static void detail(Long id) {
    Scenic scenic = Scenic.findById(id);
    render(scenic);
  }

  public static void search(String keywords, int page) {
    Logger.info("搜索景区：" + keywords);
    String[] words = QuerySpliter.splite(keywords);
    StringBuilder query = new StringBuilder();
    for (int i = 0; i < words.length; i++) {
      query.append("name like '%" + words[i] + "%'");
      if (i < words.length - 1) {
        query.append(" or ");
      }
    }
    int totalPage = Integer.parseInt(Scenic.count(query + " order by name asc") + "");
    if (page < 1) page = 1;
    if (page > totalPage) page = totalPage;
    List<Scenic> scenics =
        Scenic.find(query + "order by name asc").fetch(page, Constants.SCENIC_SEARCH_PAGE_SIZE);
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
