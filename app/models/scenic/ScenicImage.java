package models.scenic;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
  @Required
  @ManyToOne
  public Scenic scenic;

  public ScenicImage() {
  }

  public ScenicImage(String imageName, Scenic scenic) {
    this.imageName = imageName;
    this.scenic = scenic;
  }
}
