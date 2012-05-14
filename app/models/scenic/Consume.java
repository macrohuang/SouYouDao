package models.scenic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "T_CONSUME")
public class Consume extends Model {
    @Column
    public int total;
    @Column
    public int count;
    @OneToOne
    public Scenic scenic;
}
