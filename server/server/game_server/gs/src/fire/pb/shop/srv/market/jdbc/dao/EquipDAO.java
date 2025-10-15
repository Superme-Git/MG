package fire.pb.shop.srv.market.jdbc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.MarketGoods;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.jdbc.JdbcTemplate;
import fire.pb.shop.srv.market.jdbc.MarketDAO;
import fire.pb.shop.srv.market.jdbc.Page;
import fire.pb.shop.srv.market.jdbc.SQL;
import fire.pb.shop.srv.market.jdbc.utils.handler.ArrayHandler;
import fire.pb.shop.srv.market.pojo.EquipDAOHandler;
import fire.pb.shop.srv.market.pojo.EquipDaoBean;
import fire.pb.shop.srv.market.pojo.MarketGoodsHandler;
import fire.pb.shop.utils.MarketUtils;

public class EquipDAO extends MarketDAO {
	
	private EquipDAO() {}
	
	private static class EquipDAOHolder {
		static EquipDAO instance = new EquipDAO();
	}

	public static EquipDAO getInstance() {
		return EquipDAOHolder.instance;
	}
	
	/**
	 * 上架装备
	 */
	public boolean addEquip(EquipDaoBean equipDaoBean) {
		StringBuilder sql = new StringBuilder("INSERT INTO ITEM_EQUIP(ID, FIRSTNO, TWONO, THREENO, UNIQUEID, KEY, ROLEID, ITEMID, NAME, EXTID, LEVEL, EFFECT, SKILL, COLOR, TOTALATTR, EQUIPSCORE, NUMBER, PRICE, ATTENTION, SHOWTIME, EXPIRETIME) VALUES(");
		sql.append(equipDaoBean.getId()).append(",");
		sql.append(equipDaoBean.getFirstno()).append(",");
		sql.append(equipDaoBean.getTwono()).append(",");
		sql.append(equipDaoBean.getThreeno()).append(",");
		sql.append(equipDaoBean.getUniquid()).append(",");
		sql.append(equipDaoBean.getKey()).append(",");
		sql.append(equipDaoBean.getRoleid()).append(",");
		sql.append(equipDaoBean.getItemid()).append(",");
		sql.append("'");
		sql.append(equipDaoBean.getName());
		sql.append("'");
		sql.append(",");
		sql.append(equipDaoBean.getExtid()).append(",");
		sql.append(equipDaoBean.getLevel()).append(",");
		// 特效
		sql.append(equipDaoBean.getEffect()).append(",");
		// 特技
		sql.append(equipDaoBean.getSkill()).append(",");
		// 品质
		sql.append(equipDaoBean.getColor()).append(",");
		// 属性总和
		sql.append(equipDaoBean.getTotalattr()).append(",");
		sql.append(equipDaoBean.getEquipscore()).append(",");
		sql.append(equipDaoBean.getNumber()).append(",");
		sql.append(equipDaoBean.getPrice()).append(",");
		sql.append(equipDaoBean.getAttentionnumber()).append(",");
		sql.append(equipDaoBean.getShowtime()).append(",");
		sql.append(equipDaoBean.getExpiretime());
		sql.append(");");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("拍卖上架装备物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 查询装备
	 */
	public EquipDaoBean queryEquip(long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_EQUIP WHERE ");
		sql.append("id=").append(id).append(" for update");
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖装备物品SQL语句[" + sql.toString() + "]");
		}
		
		List<EquipDaoBean> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new EquipDAOHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库item_equip表记录错误:", e);
		}

		if (results == null || results.size() == 0) 
			return null;
		
		if (results.size() > 1) {
			for (EquipDaoBean bean : results) {
				MarketManager.LOG.error("出现多条数据错误" + bean.toString());
			}
			return null;
		}
		
		return results.size() > 0 ? results.get(0) : null;
	}
	
	/**
	 * 查询装备
	 */
	public MarketGoods queryEquip(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_EQUIP WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖装备物品SQL语句[" + sql.toString() + "]");
		}
		
		List<MarketGoods> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库item_equip表记录错误:", e);
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
	 * 浏览我要购买或公示物品
	 * 查询装备价格升序
	 */
	public List<MarketGoods> queryEquip(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime, Page page, int priceSort) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_EQUIP WHERE ");
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
			MarketManager.LOG.debug("浏览拍卖装备物品SQL语句[" + sql.toString() + "]");
		}
		
		return this.queryPage(page, sql.toString());
	}
	
	/**
	 * 获得记录数
	 * 浏览我要购买或公示物品
	 * 查询普通道具
	 * @return -1 未执行, 其它为记录数
	 */
	public int queryEquipTotalRow(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime) {
		StringBuilder sql = new StringBuilder("SELECT count(id) FROM ITEM_EQUIP WHERE ");
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
			MarketManager.LOG.debug("查询浏览拍卖记录数,装备物品SQL语句[" + sql.toString() + "]");
		}
		
		return this.queryTotalRow(sql.toString());
	}

	/**
	 * 装备下架或购买走
	 */
	public boolean removeEquip(long id) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_EQUIP WHERE ");
		sql.append("id=").append(id);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖装备物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	
	/**
	 * @return 装备搜索结果
	 */
	public List<Object[]> searchEquip(int euqipType, int level, int priceMin, int priceMax, int effect, int skill,
			List<Integer> colors, int totalAttr, int score, int sellState) {
		
		StringBuilder sql = new StringBuilder("SELECT ID FROM ITEM_EQUIP WHERE ");
		sql.append("TWONO=").append(euqipType).append(" AND ");
		
		if (level > 0) {
			sql.append("LEVEL=").append(level).append(" AND ");
		}
		
		if (priceMin > 0) {
			sql.append("PRICE>=").append(priceMin).append(" AND ");
		}
		
		if (priceMax > 0) {
			sql.append("PRICE<=").append(priceMax).append(" AND ");
		}
		
		if (effect > 0) {
			sql.append("EFFECT=").append(effect).append(" AND ");
		}
		
		if (skill > 0) {
			sql.append("SKILL=").append(skill).append(" AND ");
		}
		
		if (colors.size() > 0) {
			sql.append("COLOR ").append(SQL.inWrapper(colors)).append(" AND ");
		}
		
		if (totalAttr > 0) {
			sql.append("TOTALATTR>=").append(totalAttr).append(" AND ");
		}
		
		if (score > 0) {
			sql.append("EQUIPSCORE>=").append(score).append(" AND ");
		}
		
		long nowTime = System.currentTimeMillis();
		if (sellState == 1) { 
			sql.append("SHOWTIME<=").append(nowTime).append(" AND ");
			sql.append("EXPIRETIME>").append(nowTime);
		} else if (sellState == 2) {
			sql.append("SHOWTIME>").append(nowTime);
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("装备搜索=" + sql);
		}
		
		List<Object[]> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new ArrayHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库item_equip表记录错误:", e);
		}
		
		return results;
	}
	
	/**
	 * 装备下架或购买走
	 * @deprecated  性能低
	 */
	public boolean removeEquip(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_EQUIP WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖装备物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
}
