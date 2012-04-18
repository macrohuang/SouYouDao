package models.plan;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 计划
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_PLAN")
public class Plan extends Model {
  public String name;
  @OneToMany(mappedBy = "plan")
  public List<Plan> plans;
}
