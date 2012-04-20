package models.plan;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 计划天中的“时间段”
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_PLAN_TIME")
public class PlanTime extends Model {
  // 13:30
  public String startTime;
  // 14:30
  public String endTime;
  @ManyToOne
  public PlanDay planDay;
}
