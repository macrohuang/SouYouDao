package jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Scenic;
import models.utils.SystemState;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import play.Logger;
import play.jobs.Job;
import utils.Constants;

/**
 * 异步索引景区信息
 * @author royguo1988@gmail.com
 */
public class ScenicIndexer extends Job {

  @Override
  public void doJob() throws Exception {
    Directory directory = null;
    IndexWriter writer = null;
    Long total = Scenic.count();
    int pageSize = 500;
    int pages = total.intValue() / pageSize + (total % pageSize == 0 ? 0 : 1);
    //创建索引所需工具类
    try{
      directory = Constants.OPEN_SCENIC_INDEX_DIR();
      writer = Constants.CREATE_INDEX_WRITER(directory);
      //每次索引500条数据，防止内存中的对象太多，造成堆溢出
      for(int i = 1; i <= pages; i++){
        List<Scenic> scenics = Scenic.all().fetch(i, pageSize);
        for (Scenic s : scenics) {
          Document doc = new Document();
          doc.add(new Field("id", s.id+"",Field.Store.YES,Field.Index.NO));
          doc.add(new Field("name", s.name,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("description", s.description==null?"":s.description,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("address", s.address==null?"":s.address,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("tel", s.tel==null?"":s.tel,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("province.name", s.province==null?"":s.province.name,Field.Store.YES,Field.Index.ANALYZED));
          doc.add(new Field("city.name", s.city==null?"":s.city.name,Field.Store.YES,Field.Index.ANALYZED));
          writer.addDocument(doc);
          Logger.info("Scenic " + s.name + " (" + s.id + ") has been indexed !");
        }
      }
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SystemState indexDate = SystemState.find("keyName = ?", SystemState.KEYS.上次景点索引日期.name()).first();
      if(indexDate == null){
        new SystemState(SystemState.KEYS.上次景点索引日期.name(),format.format(new Date())).save();;
      }else{
        indexDate.value = format.format(new Date());
        indexDate.save();
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{
        if(writer != null) writer.close();
      }catch(Exception e){
        e.printStackTrace();
      }

    }
  }
}
