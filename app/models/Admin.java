package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 网站管理员
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_ADMIN")
public class Admin extends Model {
  @Required
  public String username;
  @Required
  @Password
  public String password;

  public Admin() {
  }

  public Admin(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
