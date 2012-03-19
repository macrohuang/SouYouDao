package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.comments.ScenicComment;
import models.images.ScenicImage;
import models.roadmaps.ScenicInnerRoadmap;
import models.utils.area.Region;
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
  @Required(message = "请输入景点全称")
  @Column(unique = true)
  public String name;
  // 详细地址
  @Required(message = "请输入景点地址")
  public String address;
  @Required(message = "请选择景点所在县区")
  @ManyToOne
  public Region region;
  // 联系电话
  public String tel;
  // 景点介绍
  @Required(message = "请输入景点描述")
  @Column(length = 2000)
  public String description;
  // 评级
  @Required(message = "请选择景点评级")
  public String level;
  // 景区坐标，如 123,123(以英文逗号分割)
  @Required(message = "请输入景点坐标,如：47.123,-47.123")
  public String location;
  // 景点门票（RMB/人)
  public String ticketPrice;
  // 景点规模（亩）
  public String scale;
  // 景点内部游览图
  public String roadmapImage;
  // 内部游览路线推荐
  @OneToMany(mappedBy = "scenic")
  public List<ScenicInnerRoadmap> innerRoadmaps;
  // 周边交通
  public String outterTraffic;
  // 景区内交通
  public String innerTraffic;
  // 是否通过管理员审核
  public boolean authorized = false;
  // 审核日期
  public Date authorizeDate;
  // 最终审核人
  @ManyToOne
  public Admin authorizer;
  // 景点评论
  @OneToMany(mappedBy = "scenic")
  public List<ScenicComment> comments;
  // 景点图片
  @OneToMany(mappedBy = "scenic")
  public List<ScenicImage> images;



  /**
   * 获取count条未审核数据
   * @param count
   * @return List<Scenic>
   */
  public static Scenic getNextUnAuthorized(long preId) {
    Scenic s = Scenic.find("authorized = false and id > ?order by id asc", preId).first();
    if (s == null) {
      s = Scenic.find("authorized = false order by id asc").first();
    }
    return s;
  }

  public static List<Scenic> getAuthorized(Long adminId) {
    return Scenic.find("authorizer.id = ? order by authorizeDate desc", adminId).fetch();
  }
}
