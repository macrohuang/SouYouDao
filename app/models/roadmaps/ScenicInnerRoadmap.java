package models.roadmaps;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.Scenic;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "T_SCENIC_INNER_ROADMAP")
public class ScenicInnerRoadmap extends Model {
  @Required
  public String content;
  @Required
  @ManyToOne
  public Scenic scenic;

  public ScenicInnerRoadmap() {
  }

  public ScenicInnerRoadmap(String content, Scenic scenic) {
    this.content = content;
    this.scenic = scenic;
  }
}
