package models.utils.area;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 省,包含直辖市、特别行政区、自治区
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_PROVINCE")
public class Province extends Model {
  @Required
  public String name;
  @OneToMany(mappedBy = "province", cascade = CascadeType.REMOVE)
  public List<City> cities;
}