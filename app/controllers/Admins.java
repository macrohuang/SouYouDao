package controllers;

import java.util.Date;
import java.util.List;

import jobs.ScenicImageDownloader;
import jobs.ScenicIndexer;
import models.Admin;
import models.Scenic;
import models.roadmaps.ScenicInnerRoadmap;
import models.utils.SystemState;
import models.utils.area.Region;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import play.data.validation.Required;
import play.data.validation.Valid;
import utils.Constants;
import utils.FileUtil;
import utils.Secure;

public class Admins extends Application {

  @Secure(admin = true)
  public static void index() {
    render();
  }

  /**
   * 创建新景点
   * @param status (1:success,-1:failure)
   */
  @Secure(admin = true)
  public static void newScenic(int status) {
    render("Admins/scenic/new.html", status);
  }

  @Secure(admin = true)
  public static void saveNewScenic(@Required String name, Region region) {
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
  @Secure(admin = true)
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
    if (scenic == null) {
      newScenic(-1);
    }
    List<Scenic> scenics =
        Scenic.find("authorized = ? and authorizer.id = ? order by authorizeDate desc", true,
            Long.parseLong(session.get(Constants.ADMIN_ID_IN_SESSION))).fetch();
    render("Admins/scenic/index.html", scenic, count, scenics);
  }

  /**
   * 完善并保存景区数据
   */
  @Secure(admin = true)
  public static void saveScenic(@Valid Scenic scenic, String roadmapImage, String images,
      String[] innerRoadmaps) throws Exception {
    if (validation.hasErrors()) {
      for (play.data.validation.Error e : validation.errors()) {
        System.out.println(e.getKey() + " ---- " + e.message());
      }
      validation.keep();
      editScenic(scenic.id, false);
    }
    // 保存RoadmapImage
    if (roadmapImage != null && roadmapImage.length() > 0) {
      new ScenicImageDownloader(new String[] {roadmapImage}, FileUtil.getApplicationPath("data",
          "scenic", "roadmaps"), scenic.id, ScenicImageDownloader.SaveType.ROADMAPS).now();
    }
    // 保存Scenic Images
    if (images != null && images.length() > 0) {
      String[] links = images.replace(" ", "").split("\\r|\\n");
      // 异步下载图片，存储于/data/scenic/images
      new ScenicImageDownloader(links, FileUtil.getApplicationPath("data", "scenic", "images"),
          scenic.id, ScenicImageDownloader.SaveType.IMAGES).now();
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
  public static void reIndexScenic(){
    SystemState indexDate = SystemState.find("keyName = ? ", SystemState.KEYS.上次景点索引日期.name()).first();
    render("Admins/scenic/reIndex.html",indexDate);
  }
  /**
   * 重建景点索引，用于用户搜索
   */
  public static void reIndexScenicOK(){
    if("INDEXING".equals(session.get(Constants.SCENIC_INDEX_STATUS))){
      return;
    }
    session.put(Constants.SCENIC_INDEX_STATUS, "INDEXING");
    new ScenicIndexer().now();
    session.remove(Constants.SCENIC_INDEX_STATUS);
  }
  public static void login() {
    render("Admins/login.html");
  }

  public static void loginOK(@Valid Admin admin) {
    if (validation.hasErrors()) {
      validation.keep();
      login();
    }
    Admin a = Admin.find("username = ? and password = ?", admin.username, admin.password).first();
    if (a == null) {
      validation.addError("Login Failed", "没有该账号!");
    }
    if (validation.hasErrors()) {
      validation.keep();
      login();
    }
    session.put(Constants.ADMIN_ID_IN_SESSION, a.id);
    render("Admins/index.html");
  }

  public static void logout() {
    session.remove(Constants.ADMIN_ID_IN_SESSION);
    login();
  }
  public static void main(String[] args) throws Exception{
    Directory directory = Constants.OPEN_SCENIC_INDEX_DIR();
    IndexWriter writer = Constants.CREATE_INDEX_WRITER(directory);
    IndexSearcher searcher = Constants.CREATE_INDEX_SEARCHER(directory);

    Long id = 123l;
    String title = "石家庄石壁";
    String content = "<div><p>&nbsp;&nbsp;&nbsp; 明客家祖地──石壁是客家祖地，现为宁化县西部的一个镇，位于武夷山东麓。古之石壁，土地肥沃、地势平坦、森林茂密，有“玉屏”美称。</p>" +
    "<p>　　石壁是客家民系形成的重要地域。许多专家论证，自晋永嘉开始，中原许多汉人为避战乱（经历代五次大迁徙），离开河洛祖地，一路向南流亡，他们翻越武夷山脉，来到宁化石壁繁衍生息，从石壁播衍的客家人地广人多。</p>" +
    "<p>　　石壁人的品性富有客家人坚韧不拔、刻苦耐劳、守礼节、重道义、好学问、论伦理的特点。同时，石壁人在漫长历史的演变中，逐渐形成了较独特的客家文化及民俗风情。</p>" +
    "<p>　　“北有大槐树，南有石壁村”。石壁这一客家人的发源地，吸引着许多专家、学者慕名前来采风、调查、考证。随着客家学研究的深入开展，石壁在客家史上的重要地位被进一步确立，众多海内外客家后裔纷来沓至、寻根祭祖、投资开发、观光旅游。</p>" +
    "<p>　　石壁近年来在海外众多客家后裔的捐款赞助下，已修复了维藩桥、德润亭等古迹，新建了客家祖地牌坊、长廊、客家民俗陈列馆等景观，世界首座聚客家一百多姓祖先神位于一堂的客家公祠也已建成，并先后在此成功的举办了世界性的首届客家民俗文化节，首届世界客属祭祖大典及客家公祠落成仪式等旅游节庆活动。 　 </p>" +
    "<p>&nbsp;&nbsp;&nbsp; 三明历史悠久，人杰地灵。“清流人”古人类化石的发掘，把福建人类活动史前溯至一万年以前。三明石壁村是中国历史上客家大迁徙的中转站，被誉为“客家的摇篮”。宋代“理学四贤”中的杨时、罗从彦、朱熹和清代画坛“扬州八怪”之一的黄慎及清代“书法四大家”之一的伊秉绶等历史名人都诞生在这里。以全国重点保护单位泰宁尚书为代表的众多的，明、清时代建筑群、石雕群等文物古迹。极大地丰富了三名旅游业的文化内涵。 </p>" +
    "<p>&nbsp;&nbsp;&nbsp; 三明客家祖地──石壁最佳旅游季节：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 去三明客家祖地──石壁最佳旅游季节是在每年的秋季. <br>&nbsp;<br>　&nbsp; 三明客家祖地──石壁交通：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 石壁位于闽赣两省交界处.交通便利.<br>&nbsp;&nbsp;&nbsp; 福州、厦门---火车---到三明，转乘中巴（半小时有一部）到宁化后坐车到石壁只需20分钟。 江西的赣州、瑞金等地均有车经过石壁。<br>&nbsp; <br>　&nbsp; 三明客家祖地──石壁住宿：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 去石壁游玩可以在当地居民家住宿,以便体验客家人的热情与淳朴.<br>&nbsp;&nbsp;&nbsp; 如果需要宾馆,那可以到宁化城关的客家宾馆(宁化境内最好的).&nbsp; </p>" +
    "<p>　&nbsp; 三明客家祖地──石壁美食：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 甘甜可口的“客家擂茶”和远近闻名的“烧卖”是这里的主打特色地方小吃。特别是当地居民家里做的擂茶或烧卖，那个味道呀，简直要流口水了。快去尝尝吧！ </p>" +
    "</div>";
    try{
      Document doc = new Document();
      doc.add(new Field("id", id+"",Field.Store.YES,Field.Index.NO));
      doc.add(new Field("title", title,Field.Store.YES,Field.Index.ANALYZED));
      doc.add(new Field("content", content,Field.Store.YES,Field.Index.ANALYZED));
      writer.addDocument(doc);

      String queryWords = "石壁后裔";
      Query query = IKQueryParser.parseMultiField(new String[]{"content","title"},queryWords);
      searcher.setSimilarity(new IKSimilarity());
      TopDocs docs = searcher.search(query, 5);
      ScoreDoc[] scoreDocs = docs.scoreDocs;
      for (int i = 0; i < scoreDocs.length; i++) {
        Document document = searcher.doc(scoreDocs[i].doc);
        System.out.println(scoreDocs[i].score + " " + document.get("title"));
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
