package models.scenic;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 景点别名
 * 
 * @author macrohuang.whu@gmail.com
 */
@Entity
@Table(name = "T_SCENIC_ALIAS")
public class ScenicAlias extends Model {

	@Required
	public String alias;
	@ManyToOne
	public Scenic scenic;
}
