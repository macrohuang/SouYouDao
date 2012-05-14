package jobs;

import models.Admin;
import models.User;
import models.scenic.Scenic;
import models.utils.area.City;
import models.utils.area.Province;
import models.utils.area.Region;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Codec;

/**
 * 测试环境初始化系统所需数据
 * 
 * @author royguo1988@gmail.com
 */
@OnApplicationStart
public class DataInitial extends Job {

	@Override
	public void doJob() throws Exception {
        if (Play.configuration.get("db").equals("mem")) {
			Logger.info("data initialize ");
			// Admins
			Admin.deleteAll();
			Scenic.deleteAll();
			Admin admin = new Admin("admin", "admin");
			admin.save();

			// Scenics
			Province p = new Province();
			p.name = "福建";
			p.save();

			Province p2 = new Province();
			p2.name = "北京";
			p2.save();

			City c1 = new City();
			c1.name = "福州";
			c1.province = p;
			c1.save();

			City c2 = new City();
			c2.name = "泉州";
			c2.province = p;
			c2.save();

			City c3 = new City();
			c3.name = "厦门";
			c3.province = p;
			c3.save();

			City c4 = new City();
			c4.name = "北京";
			c4.province = p2;
			c4.save();

			Region r = new Region();
			r.name = "南安";
			r.city = c2;
			r.save();

			r = new Region();
			r.name = "安溪";
			r.city = c2;
			r.save();

			r = new Region();
			r.name = "晋江";
			r.city = c2;
			r.save();

			r = new Region();
			r.name = "海淀";
			r.city = c4;
			r.save();

			r = new Region();
			r.name = "朝阳";
			r.city = c4;
			r.save();

			Scenic scenic = new Scenic();
			scenic.name = "你好乌鲁木齐";
			scenic.description = "<div><p>&nbsp;&nbsp;&nbsp; 明客家祖地──石壁是客家祖地，现为宁化县西部的一个镇，位于武夷山东麓。古之石壁，土地肥沃、地势平坦、森林茂密，有“玉屏”美称。</p>"
			        + "<p>　　石壁是客家民系形成的重要地域。许多专家论证，自晋永嘉开始，中原许多汉人为避战乱（经历代五次大迁徙），离开河洛祖地，一路向南流亡，他们翻越武夷山脉，来到宁化石壁繁衍生息，从石壁播衍的客家人地广人多。</p>"
			        + "<p>　　石壁人的品性富有客家人坚韧不拔、刻苦耐劳、守礼节、重道义、好学问、论伦理的特点。同时，石壁人在漫长历史的演变中，逐渐形成了较独特的客家文化及民俗风情。</p>"
			        + "<p>　　“北有大槐树，南有石壁村”。石壁这一客家人的发源地，吸引着许多专家、学者慕名前来采风、调查、考证。随着客家学研究的深入开展，石壁在客家史上的重要地位被进一步确立，众多海内外客家后裔纷来沓至、寻根祭祖、投资开发、观光旅游。</p>"
			        + "<p>　　石壁近年来在海外众多客家后裔的捐款赞助下，已修复了维藩桥、德润亭等古迹，新建了客家祖地牌坊、长廊、客家民俗陈列馆等景观，世界首座聚客家一百多姓祖先神位于一堂的客家公祠也已建成，并先后在此成功的举办了世界性的首届客家民俗文化节，首届世界客属祭祖大典及客家公祠落成仪式等旅游节庆活动。 　 </p>"
			        + "<p>&nbsp;&nbsp;&nbsp; 三明历史悠久，人杰地灵。“清流人”古人类化石的发掘，把福建人类活动史前溯至一万年以前。三明石壁村是中国历史上客家大迁徙的中转站，被誉为“客家的摇篮”。宋代“理学四贤”中的杨时、罗从彦、朱熹和清代画坛“扬州八怪”之一的黄慎及清代“书法四大家”之一的伊秉绶等历史名人都诞生在这里。以全国重点保护单位泰宁尚书为代表的众多的，明、清时代建筑群、石雕群等文物古迹。极大地丰富了三名旅游业的文化内涵。 </p>"
			        + "<p>&nbsp;&nbsp;&nbsp; 三明客家祖地──石壁最佳旅游季节：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 去三明客家祖地──石壁最佳旅游季节是在每年的秋季. <br>&nbsp;<br>　&nbsp; 三明客家祖地──石壁交通：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 石壁位于闽赣两省交界处.交通便利.<br>&nbsp;&nbsp;&nbsp; 福州、厦门---火车---到三明，转乘中巴（半小时有一部）到宁化后坐车到石壁只需20分钟。 江西的赣州、瑞金等地均有车经过石壁。<br>&nbsp; <br>　&nbsp; 三明客家祖地──石壁住宿：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 去石壁游玩可以在当地居民家住宿,以便体验客家人的热情与淳朴.<br>&nbsp;&nbsp;&nbsp; 如果需要宾馆,那可以到宁化城关的客家宾馆(宁化境内最好的).&nbsp; </p>"
			        + "<p>　&nbsp; 三明客家祖地──石壁美食：<br>&nbsp; <br>&nbsp;&nbsp;&nbsp; 甘甜可口的“客家擂茶”和远近闻名的“烧卖”是这里的主打特色地方小吃。特别是当地居民家里做的擂茶或烧卖，那个味道呀，简直要流口水了。快去尝尝吧！ </p>"
			        + "</div>";
			scenic.save();
			// Users
			User.deleteAll();
			User user = new User();
			user.username = "admin";
			user.password = Codec.hexMD5("admin");
			user.save();
		}
	}
}
