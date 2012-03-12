package models.utils.area;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 县、区域
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_REGION")
public class Region extends Model {
  @Required
  public String name;
  @Required
  @ManyToOne
  public City city;
}
