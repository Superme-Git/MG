package fire.pb.shop.srv.market.jdbc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.attr.AttrType;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.MarketSearchAttr;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.jdbc.JdbcTemplate;
import fire.pb.shop.srv.market.jdbc.MarketDAO;
import fire.pb.shop.srv.market.jdbc.Page;
import fire.pb.shop.srv.market.jdbc.SQL;
import fire.pb.shop.srv.market.jdbc.utils.handler.ArrayHandler;
import fire.pb.shop.srv.market.pojo.MarketGoodsHandler;
import fire.pb.shop.srv.market.pojo.PetDAOHandler;
import fire.pb.shop.srv.market.pojo.PetDaoBean;
import fire.pb.shop.utils.MarketUtils;

public class PetDAO extends MarketDAO {
	
	private PetDAO() {}
	
	private static class PetDAOHolder {
		static PetDAO instance = new PetDAO();
	}

	public static PetDAO getInstance() {
		return PetDAOHolder.instance;
	}
	
	/**
	 * 上架宠物
	 */
	public boolean addPet(PetDaoBean petDaoBean) {
		StringBuilder sql = new StringBuilder("INSERT INTO ITEM_PET(ID, FIRSTNO, TWONO, THREENO, UNIQUEID, KEY, ROLEID, ITEMID, NAME, LEVEL, ATTACK, DEFEND, SPEED, MAGICATTACK, MAGCIDEF, MAXHP, ATTACKAPT, DEFENDAPT, "
				+ "PHYFORCEAPT, MAGICAPT, SPEEDAPT, DODGEAPT, GROWRATE, SKILLNUMBER, PETSCORE, NUMBER, PRICE, ATTENTION, SHOWTIME, EXPIRETIME) VALUES(");
		sql.append(petDaoBean.getId()).append(",");
		sql.append(petDaoBean.getFirstno()).append(",");
		sql.append(petDaoBean.getTwono()).append(",");
		sql.append(petDaoBean.getThreeno()).append(",");
		sql.append(petDaoBean.getUniquid()).append(",");
		sql.append(petDaoBean.getKey()).append(",");
		sql.append(petDaoBean.getRoleid()).append(",");
		sql.append(petDaoBean.getItemid()).append(",");
		sql.append("'");
		sql.append(petDaoBean.getName());
		sql.append("'");
		sql.append(",");
		sql.append(petDaoBean.getLevel()).append(",");
		sql.append(petDaoBean.getAttack()).append(",");
		sql.append(petDaoBean.getDefend()).append(",");
		sql.append(petDaoBean.getSpeed()).append(",");
		sql.append(petDaoBean.getMagicattack()).append(",");
		sql.append(petDaoBean.getMagicdef()).append(",");
		sql.append(petDaoBean.getMaxhp()).append(",");
		sql.append(petDaoBean.getAttackapt()).append(",");
		sql.append(petDaoBean.getDefendapt()).append(",");
		sql.append(petDaoBean.getPhyforceapt()).append(",");
		sql.append(petDaoBean.getMagicapt()).append(",");
		sql.append(petDaoBean.getSpeedapt()).append(",");
		sql.append(petDaoBean.getDodgeapt()).append(",");
		sql.append(petDaoBean.getGrowrate()).append(",");
		sql.append(petDaoBean.getSkillNumber()).append(",");
		sql.append(petDaoBean.getPetscore()).append(",");
		sql.append(petDaoBean.getNumber()).append(",");
		sql.append(petDaoBean.getPrice()).append(",");
		sql.append(petDaoBean.getAttentionnumber()).append(",");
		sql.append(petDaoBean.getShowtime()).append(",");
		sql.append(petDaoBean.getExpiretime());
		sql.append(");");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("拍卖上架装备物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 查询宠物
	 */
	public PetDaoBean queryPet(long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_PET WHERE ");
		sql.append("id=").append(id).append(" for update");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖宠物物品SQL语句[" + sql.toString() + "]");
		}
		
		List<PetDaoBean> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new PetDAOHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库表记录错误:", e);
		}

		if (results == null || results.size() == 0) 
			return null;
		
		if (results.size() > 1) {
			for (PetDaoBean bean : results) {
				MarketManager.LOG.error("出现多条数据错误" + bean.toString());
			}
			return null;
		}
		
		return results.size() > 0 ? results.get(0) : null;
	}

	/**
	 * 浏览我要购买或公示物品
	 * 查询宠物价格升序
	 */
	public List<MarketGoods> queryPet(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime, Page page, int priceSort) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_PET WHERE ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		
		boolean exc = false;
		if (browseType == MarketUtils.BROWSE_MY_BUY) {
			sql.append("showtime<=").append(currtime).append(" and ");
			sql.append("expiretime>").append(currtime);
			exc = true;
		}
		else if (browseType == MarketUtils.BROWSE_SHOW) {
			sql.append("showtime>").append(currtime);
			exc = true;
		}
		
		if (priceSort == 2) {
			sql.append(" order by price desc");//价格升序
		} else {
			sql.append(" order by price asc");
		}
		
		if (!exc) {
			return null;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖宠物物品SQL语句[" + sql.toString() + "]");
		}
		
		return this.queryPage(page, sql.toString());
	}
	
	/**
	 * 获得记录数
	 * 浏览我要购买或公示物品
	 * 查询普通道具
	 * @return -1 未执行, 其它为记录数
	 */
	public int queryPetTotalRow(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime) {
		StringBuilder sql = new StringBuilder("SELECT count(id) FROM ITEM_PET WHERE ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		
		boolean exc = false;
		if (browseType == MarketUtils.BROWSE_MY_BUY) {
			sql.append("showtime<=").append(currtime).append(" and ");
			sql.append("expiretime>").append(currtime);
			exc = true;
		}
		else if (browseType == MarketUtils.BROWSE_SHOW) {
			sql.append("showtime>").append(currtime);
			exc = true;
		}
		
		if (!exc) {
			return -1;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("查询浏览拍卖记录数,宠物物品SQL语句[" + sql.toString() + "]");
		}
		
		return this.queryTotalRow(sql.toString());
	}
	
	/**
	 * 查询宠物
	 */
	public MarketGoods queryPet(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_PET WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖宠物物品SQL语句[" + sql.toString() + "]");
		}
		
		List<MarketGoods> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (results == null || results.size() == 0) 
			return null;
		
		if (results.size() > 1) {
			for (MarketGoods goods : results) {
				MarketManager.LOG.error("出现多条数据错误" + goods.toString());
			}
			return null;
		}
		
		return results.size() > 0 ? results.get(0) : null;	
	}
	
	/**
	 * 宠物下架或购买走
	 */
	public boolean removePet(long id) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_PET WHERE ");
		sql.append("id=").append(id);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖宠物物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * @return 宠物搜索结果
	 */
	public List<Object[]> searchPet(int petType, int levelMin, int levelMax, int priceMin, int priceMax,
			List<MarketSearchAttr> talents, List<MarketSearchAttr> attrs, int skillNumber, int score, int sellState) {

		StringBuilder sql = new StringBuilder("SELECT ID FROM ITEM_PET WHERE ");
		sql.append("ITEMID=").append(petType).append(" AND ");

		if (levelMin >= 0) {
			sql.append("LEVEL>=").append(levelMin).append(" AND ");
		}

		if (levelMin != levelMax && levelMax > 0) {
			sql.append("LEVEL<=").append(levelMax).append(" AND ");
		}

		if (priceMin > 0) {
			sql.append("PRICE>=").append(priceMin).append(" AND ");
		}

		if (priceMax > 0) {
			sql.append("PRICE<=").append(priceMax).append(" AND ");
		}
		
		if (talents != null && talents.size() > 0) {
			for (MarketSearchAttr marketSearchAttr : talents) {
				if (marketSearchAttr.attrid == AttrType.PET_ATTACK_APT) {
					sql.append("ATTACKAPT>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.PET_DEFEND_APT) {
					sql.append("DEFENDAPT>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.PET_PHYFORCE_APT) {
					sql.append("PHYFORCEAPT>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.PET_MAGIC_APT) {
					sql.append("MAGICAPT>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.PET_SPEED_APT) {
					sql.append("SPEEDAPT>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.PET_GROW_RATE) {
					sql.append("GROWRATE>=").append(marketSearchAttr.attrval).append(" AND ");
				}
			}
		}
		
		if (attrs != null && attrs.size() > 0) {
			for (MarketSearchAttr marketSearchAttr : attrs) {
				if (marketSearchAttr.attrid == AttrType.ATTACK) {
					sql.append("ATTACK>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.DEFEND) {
					sql.append("DEFEND>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.SPEED) {
					sql.append("SPEED>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.MAGIC_ATTACK) {
					sql.append("MAGICATTACK>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.MAGIC_DEF) {
					sql.append("MAGCIDEF>=").append(marketSearchAttr.attrval).append(" AND ");
				} else
				if (marketSearchAttr.attrid == AttrType.MAX_HP) {
					sql.append("MAXHP>=").append(marketSearchAttr.attrval).append(" AND ");
				}
			}
		}

		if (skillNumber > 0) {
			sql.append("SKILLNUMBER>=").append(skillNumber).append(" AND ");
		}

		if (score > 0) {
			sql.append("PETSCORE>=").append(score).append(" AND ");
		}

		long nowTime = System.currentTimeMillis();
		if (sellState == 1) {
			sql.append("SHOWTIME<=").append(nowTime).append(" AND ");
			sql.append("EXPIRETIME>").append(nowTime);
		} else if (sellState == 2) {
			sql.append("SHOWTIME>").append(nowTime);
		}

		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("宠物搜索=" + sql);
		}

		List<Object[]> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new ArrayHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库item_pet表记录错误:", e);
		}

		return results;
	}
	
	public List<MarketGoods> getAll() {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_PET ");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("获取所有宠物物品SQL语句[" + sql.toString() + "]");
		}
		List<MarketGoods> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market获取数据库ITEM_PET表所有记录错误:", e);
		}
		return results;
	}

	/**
	 * 宠物下架或购买走
	 *  @deprecated  性能低
	 */
	public boolean removePet(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_PET WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖宠物物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	

	
}
