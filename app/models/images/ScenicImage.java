package models.images;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.Scenic;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 景点介绍使用的图片(五张以上)
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_SCENIC_IMAGE")
public class ScenicImage extends Model {
  @Required
  public String imageName;
  // 相对于data目录的文件夹
  @Required
  public String dir = "scenic";
  @ManyToOne
  @Required
  public Scenic scenic;
}
