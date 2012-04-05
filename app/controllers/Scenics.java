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
      // 检索所有结果集的索引 (第一次搜索,总体结果集)
      TopDocs results1 = searcher.search(query, Constants.SCENIC_SEARCH_PAGE_SIZE);
      total = results1.totalHits;
      // 上一页的最后一个document索引
      int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
      // 分页开始点的doc
      ScoreDoc afterDoc = null;
      if (index > 0) {
        afterDoc = results1.scoreDocs[index - 1];
      }
      // 检索所有结果集的索引 (第二次搜索,当页结果集)
      TopDocs results2 = searcher.searchAfter(afterDoc, query, Constants.SCENIC_SEARCH_PAGE_SIZE);
      ScoreDoc[] scoreDocs = results2.scoreDocs;
      for (int i = 0; i < scoreDocs.length; i++) {
        Document document = searcher.doc(scoreDocs[i].doc);
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
      // 检索所有结果集的索引 (第一次搜索,总体结果集)
      TopDocs results1 = searcher.search(query, Constants.SCENIC_SEARCH_PAGE_SIZE);
      // 上一页的最后一个document索引
      int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
      // 如果请求的数据超出索引范围，则停止查询
      if (index > results1.scoreDocs.length) {
        Logger.info("return :" + index + " ," + results1.scoreDocs.length);
        return;
      }
      // 分页开始点的doc
      ScoreDoc afterDoc = null;
      if (index > 0) {
        afterDoc = results1.scoreDocs[index - 1];
      }
      // 检索所有结果集的索引 (第二次搜索,当页的结果集)
      TopDocs results2 = searcher.searchAfter(afterDoc, query, Constants.SCENIC_SEARCH_PAGE_SIZE);
      ScoreDoc[] scoreDocs = results2.scoreDocs;
      Logger.info("result2.totalHits :" + results2.totalHits);
      Logger.info("scoreDocs.length :" + scoreDocs.length);
      Logger.info("index :" + index);
      for (int i = 0; i < scoreDocs.length; i++) {
        Document document = searcher.doc(scoreDocs[i].doc);
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
