package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Equals;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 用户
 * @author royguo1988@gmail.com
 */
@Entity
@Table(name = "T_USER")
public class User extends Model {
  @Required(message = "请填写账号")
  public String username;
  @Required(message = "请填写密码")
  @Equals(value = "passwordConfirm", message = "密码输入不一致")
  public String password;
  @Transient
  public String passwordConfirm;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
