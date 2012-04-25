package utils;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Constants {
  // SESSION 中的常量
  public static final String USER_ID_IN_SESSION = "USER_ID_IN_SESSION";
  public static final String USERNAME_IN_SESSION = "USERNAME_IN_SESSION";
  public static final String ADMIN_ID_IN_SESSION = "ADMIN_ID_IN_SESSION";

  // 常用文件夹位置,如 /ROOT/TEST_DIR/
  public static final String PLAN_DAY_IMAGE_PATH = FileUtil.getApplicationPath("data", "plan",
      "images");
  // 各种查询的分页数量
  public static final int SCENIC_SEARCH_PAGE_SIZE = 24;
  public static final int WEB_SEARCH_PAGE_SIZE = 24;

  public static final String SCENIC_INDEX_STATUS = "SCENIC_INDEX_STATUS";
  // 全局分词器
  public static final Analyzer analyzer = new IKAnalyzer();
  public static final String SCENIC_INDEX_PATH = FileUtil.getApplicationPath("data", "scenic",
      "indexes");

  // 打开景点索引目录
  public static final Directory OPEN_SCENIC_INDEX_DIR() throws Exception {
    File file = new File(SCENIC_INDEX_PATH);
    if (!file.exists()) {
      file.mkdirs();
    }
    return FSDirectory.open(file);
  }

  // 创建索引写入工具
  public static final IndexWriter CREATE_INDEX_WRITER(Directory directory) throws Exception {
    @SuppressWarnings("deprecation")
    IndexWriter writer =
        new IndexWriter(directory, Constants.analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
    return writer;
  }

  // 创建索引检索工具
  public static final IndexSearcher CREATE_INDEX_SEARCHER(Directory directory) throws Exception {
    @SuppressWarnings("deprecation")
    IndexSearcher searcher = new IndexSearcher(directory);
    return searcher;
  }
}
