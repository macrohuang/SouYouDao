package models.comments;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.scenic.Scenic;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 景点评论
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_SCENIC_COMMENT")
public class ScenicComment extends Model {
  @Required
  public String content;
  @ManyToOne
  public Scenic scenic;
}
