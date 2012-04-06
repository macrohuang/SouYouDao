package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Admin;
import models.Scenic;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Constants;
import utils.Secure;

public class Application extends Controller {

  public static void index() {
    render();
  }

  public static void search(String keywords) {
    List<Scenic> scenics = new ArrayList<Scenic>();
    Logger.info("首页搜索：" + keywords);
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
      TopDocs results = searcher.search(query, 5);
      ScoreDoc[] scoreDocs = results.scoreDocs;
      for (ScoreDoc scoreDoc : scoreDocs) {
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
    render(keywords, scenics);
  }

  @Before
  public static void login() {

  }

  @Before
  public static void admin() {
    Secure secure = getActionAnnotation(Secure.class);
    // 如果没有注解或者注解需要登录均需验证登录状态
    if (secure != null && secure.admin() == true) {
      if (session.get(Constants.ADMIN_ID_IN_SESSION) == null) {
        Admins.login();
      }
      Long admin_id = Long.parseLong(session.get(Constants.ADMIN_ID_IN_SESSION));
      Admin admin = Admin.findById(admin_id);
      if (admin == null) {
        Admins.login();
      }
    }
  }
}
