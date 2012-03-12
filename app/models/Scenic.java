package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.comments.ScenicComment;
import models.images.ScenicImage;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 景区
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_SCENIC")
public class Scenic extends Model {
  // 全称
  @Required
  @Column(unique = true)
  public String name;
  // 详细地址
  @Required
  public String address;
  // 联系电话
  public String tel;
  // 景点介绍
  public String description;
  // 评级
  public String level;
  // 景点坐标，如 123,123(以英文逗号分割)
  public String location;
  // 景点门票（RMB/人）
  public int ticketPrice;
  // 景点规模（亩）
  public int scale;
  // 景点内部游览图
  public String roadMapImage;
  // 游览路线推荐
  public String innerRoadMaps;
  // 周边交通
  public String outterTraffic;
  // 景区内交通
  public String innerTraffic;
  // 景点评论
  @OneToMany(mappedBy = "scenic")
  public List<ScenicComment> comments;
  @OneToMany(mappedBy = "scenic")
  public List<ScenicImage> images;
}
