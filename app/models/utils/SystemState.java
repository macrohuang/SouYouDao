package models.utils;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;


/**
 * 存储当前系统状态,如上次景点索引日期、版本等
 * @author royguo1988@gmail.com
 *
 */
@Entity
@Table(name="T_SYSTEM_STATE")
public class SystemState extends Model{
  public enum KEYS {上次景点索引日期};
  public String keyName;
  public String value;

  public SystemState(){}
  public SystemState(String keyName,String value){
    this.keyName = keyName;
    this.value = value;
  }
}
