package models.plan;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 计划天中的图片
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_PLAN_DAY_IMAGE")
public class PlanDayImage extends Model {
  public String name;
  @ManyToOne
  public PlanDay planDay;
}
