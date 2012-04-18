package models.plan;

import java.util.Date;

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
  public Date date;
  @ManyToOne
  public PlanDay planDay;
}
