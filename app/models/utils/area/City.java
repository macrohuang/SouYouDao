package models.utils.area;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 城市
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_CITY")
public class City extends Model {
  @Required
  public String name;
  @Required
  @ManyToOne
  public Province province;
  @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE)
  public List<Region> regions;
}
