package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.Scenic;
import models.images.ScenicImage;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import play.Logger;
import play.mvc.Controller;
import utils.Constants;
import utils.FileUtil;

public class Scenics extends Controller {

  public static void index() {
    List<Scenic> scenics = Scenic.all().fetch(4);
    render(scenics);
  }

  public static void detail(Long id) {
    Scenic scenic = Scenic.findById(id);
    List<ScenicImage> images = ScenicImage.find("scenic.id = ? order by id desc", id).fetch(21);
    ScenicImage firstImage = images.size() > 0 ? images.get(0) : null;
    render(scenic, images, firstImage);
  }

  /**
   * Search后进入列表页面
   * @param keywords
   * @param page start at 1
   */
  public static void search(String keywords, int page) {
    int total = 0;
    if (page < 1) page = 1;
    List<Scenic> scenics = new ArrayList<Scenic>();
    Logger.info("搜索景区：" + keywords);
    Directory directory = null;
    IndexSearcher searcher = null;
    try {
      directory = Constants.OPEN_SCENIC_INDEX_DIR();
      searcher = Constants.CREATE_INDEX_SEARCHER(directory);
      Query query =
          IKQueryParser.parseMultiField(new String[] {"name", "description", "address", "tel",
              "province.name", "city.name"}, keywords);
      searcher.setSimilarity(new IKSimilarity());
      // 执行检索操作，根据每次查询的页数取topN
      TopDocs results = searcher.search(query, page * Constants.SCENIC_SEARCH_PAGE_SIZE);
      total = results.totalHits;
      // 当前页面起点
      int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
      for (int i = 0; i < Constants.SCENIC_SEARCH_PAGE_SIZE; i++) {
        //超过总结果集数目跳出循环
        if(index + i > results.scoreDocs.length){
          break;
        }
        ScoreDoc scoreDoc = results.scoreDocs[index + i];
        Document document = searcher.doc(scoreDoc.doc);
        Scenic scenic = Scenic.findById(Long.parseLong(document.get("id")));
        if (scenic != null) {
          scenics.add(scenic);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (searcher != null) searcher.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    render("Scenics/list.html", scenics, keywords, total, page);
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

  /**
   * AJAX分页使用的query.
   * @param keywords
   * @param page start at 1
   * @param template (default = "Scenics/page.html")
   */
  public static void query(String keywords, int page, String template) {
    if (template == null || template.equals("")) {
      template = "Scenics/page.html";
    }
    if (page < 1) page = 1;
    List<Scenic> scenics = new ArrayList<Scenic>();
    Directory directory = null;
    IndexSearcher searcher = null;
    try {
      directory = Constants.OPEN_SCENIC_INDEX_DIR();
      searcher = Constants.CREATE_INDEX_SEARCHER(directory);
      Query query =
          IKQueryParser.parseMultiField(new String[] {"name", "description", "address", "tel",
              "province.name", "city.name"}, keywords);
      searcher.setSimilarity(new IKSimilarity());
      // 执行检索操作，根据每次查询的页数取topN
      TopDocs results = searcher.search(query, page * Constants.SCENIC_SEARCH_PAGE_SIZE);
      // 当前页面起点
      int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
      for (int i = 0; i < Constants.SCENIC_SEARCH_PAGE_SIZE; i++) {
        //超过总结果集数目跳出循环
        if(index + i > results.scoreDocs.length){
          break;
        }
        ScoreDoc scoreDoc = results.scoreDocs[index + i];
        Document document = searcher.doc(scoreDoc.doc);
        Scenic scenic = Scenic.findById(Long.parseLong(document.get("id")));
        if (scenic != null) {
          scenics.add(scenic);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (searcher != null) searcher.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    renderTemplate(template, scenics);
  }
}
