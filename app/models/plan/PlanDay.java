package models.plan;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 计划中的”天“
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_PLAN_DAY")
public class PlanDay extends Model {
  // “天”只需要 YYYY-MM-DD 格式即可
  public String date;

  @ManyToOne
  public Plan plan;
  @OneToMany(mappedBy = "planDay")
  public List<PlanTime> planTimes;
}
