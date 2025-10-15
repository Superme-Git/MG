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
import fire.pb.shop.srv.market.pojo.MarketGoodsHandler;
import fire.pb.shop.srv.market.pojo.NormalDAOHandler;
import fire.pb.shop.srv.market.pojo.NormalDaoBean;
import fire.pb.shop.utils.MarketUtils;

public class NormalDAO extends MarketDAO {
	
	private NormalDAO() {}
	
	private static class NormalDAOHolder {
		static NormalDAO instance = new NormalDAO();
	}

	public static NormalDAO getInstance() {
		return NormalDAOHolder.instance;
	}
	
	/**
	 * 上架普通道具
	 */
	public boolean addNormal(NormalDaoBean normalDaoBean) {
		StringBuilder sql = new StringBuilder("INSERT INTO ITEM_NORMAL(ID, FIRSTNO, TWONO, THREENO, UNIQUEID, KEY, ROLEID, ITEMID, NAME, EXTID, LEVEL, NUMBER, PRICE, ATTENTION, SHOWTIME, EXPIRETIME) VALUES(");
		sql.append(normalDaoBean.getId()).append(",");
		sql.append(normalDaoBean.getFirstno()).append(",");
		sql.append(normalDaoBean.getTwono()).append(",");
		sql.append(normalDaoBean.getThreeno()).append(",");
		sql.append(normalDaoBean.getUniquid()).append(",");
		sql.append(normalDaoBean.getKey()).append(",");
		sql.append(normalDaoBean.getRoleid()).append(",");
		sql.append(normalDaoBean.getItemid()).append(",");
		sql.append("'");
		sql.append(normalDaoBean.getName());
		sql.append("'");
		sql.append(",");
		sql.append(normalDaoBean.getExtid()).append(",");
		sql.append(normalDaoBean.getLevel()).append(",");
		sql.append(normalDaoBean.getNumber()).append(",");
		sql.append(normalDaoBean.getPrice()).append(",");
		sql.append(normalDaoBean.getAttentionnumber()).append(",");
		sql.append(normalDaoBean.getShowtime()).append(",");
		sql.append(normalDaoBean.getExpiretime());
		sql.append(");");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("拍卖上架普通物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 价格从低到高，同价格品质或等级高的优先
	 */
	public List<MarketGoods> queryNormalOrderByPriceAndLeve(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL WHERE ");
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
		
		sql.append(" order by price asc, level desc limit 0,1");
		
		if (!exc) {
			return null;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		
		List<MarketGoods> results = null;
		
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("价格从低到高，同价格品质或等级高的优先", e);
		}
		
		return results;
	}
	
	/**
	 * 查询普通道具
	 */
	public NormalDaoBean queryNormal(long id) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL WHERE ");
		sql.append("id=").append(id).append(" for update");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		
		List<NormalDaoBean> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new NormalDAOHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库表记录错误:", e);
		}
		
		if (results == null || results.size() == 0) 
			return null;
		
		if (results.size() > 1) {
			for (NormalDaoBean bean : results) {
				MarketManager.LOG.error("出现多条数据错误" + bean.toString());
			}
			return null;
		}
		
		return results.size() > 0 ? results.get(0) : null;
	}
	
	
	/**
	 * 查询普通道具
	 */
	public MarketGoods queryNormal(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		List<MarketGoods> results = null;
		
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("查询普通道具", e);
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
	 * 范围条件查询普通道具
	 */
	public List<MarketGoods> queryNormal(int browseType, int firstno, int twono, ArrayList<Integer> threeno, int limitmin, int limitmax, long currtime, Page page, int priceSort) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL WHERE ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		sql.append("level>=").append(limitmin).append(" and ");
		sql.append("level<=").append(limitmax).append(" and ");
		
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
			sql.append(" order by price desc");
		} else {
			sql.append(" order by price asc");
		}
		
		if (!exc) {
			return null;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		return this.queryPage(page, sql.toString());
	}
	
	/**
	 * 免费服务器的显示查询
	 */
	public List<MarketGoods> queryNormal(int browseType, int firstno, int twono, ArrayList<Integer> threeno, int limitmin, int limitmax, long currtime) {
		boolean exc = false;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select c.* from item_normal c inner join (");
		sql.append("select min(b.id) as id, a.level, a.price from ");
		sql.append("(select level, min(price) as price from item_normal where ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		sql.append("level>=").append(limitmin).append(" and ");
		sql.append("level<=").append(limitmax).append(" and ");
		if (browseType == MarketUtils.BROWSE_MY_BUY) {
			sql.append("showtime<=").append(currtime).append(" and ");
			sql.append("expiretime>").append(currtime).append(" ");
		}
		else if (browseType == MarketUtils.BROWSE_SHOW) {
			sql.append("showtime>").append(currtime).append(" ");
		}
		sql.append("group by level) a ");
		sql.append("inner join item_normal b on ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		sql.append("a.level = b.level and a.price = b.price").append(" and ");
		if (browseType == MarketUtils.BROWSE_MY_BUY) {
			sql.append("b.showtime<=").append(currtime).append(" and ");
			sql.append("b.expiretime>").append(currtime).append(" ");
			exc = true;
		}
		else if (browseType == MarketUtils.BROWSE_SHOW) {
			sql.append("b.showtime>").append(currtime).append(" ");
			exc = true;
		}
		sql.append("group by a.level, a.price) d ");
		sql.append("on c.id = d.id").append(" ");
		sql.append("order by c.price, c.level desc");
		
		if (!exc) {
			return null;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		
		List<MarketGoods> results = null;
		
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("价格从低到高，同价格品质或等级高的优先", e);
		}
		
		return results;
	}
	
	/**
	 * 获得记录数
	 * 浏览我要购买或公示物品
	 * 范围条件查询普通道具
	 * @return -1 未执行, 其它为记录数
	 */
	public int queryNormalTotalRow(int browseType, int firstno, int twono, ArrayList<Integer> threeno, int limitmin, int limitmax, long currtime) {
		StringBuilder sql = new StringBuilder("SELECT count(id) FROM ITEM_NORMAL WHERE ");
		sql.append("firstno=").append(firstno).append(" and ");
		sql.append("twono=").append(twono).append(" and ");
		sql.append("threeno ").append(SQL.inWrapper(threeno)).append(" and ");
		sql.append("level>=").append(limitmin).append(" and ");
		sql.append("level<=").append(limitmax).append(" and ");
		
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
			MarketManager.LOG.debug("查询浏览拍卖范围条件记录数,普通物品SQL语句[" + sql.toString() + "]");
		}
		return this.queryTotalRow(sql.toString());
	}
	
	/**
	 * 获得记录数
	 * 浏览我要购买或公示物品
	 * 查询普通道具
	 * @param browseType 公示或购买
	 * @return -1 未执行, 其它为记录数
	 */
	public int queryNormalTotalRow(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime) {
		StringBuilder sql = new StringBuilder("SELECT count(id) FROM ITEM_NORMAL WHERE ");
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
			MarketManager.LOG.debug("查询浏览拍卖记录数,普通物品SQL语句[" + sql.toString() + "]");
		}
		return this.queryTotalRow(sql.toString());
	}
	
	/**
	 * 浏览我要购买或公示物品
	 * 查询普通道具
	 * @param browseType 公示或购买
	 */
	public List<MarketGoods> queryNormal(int browseType, int firstno, int twono, ArrayList<Integer> threeno, long currtime, Page page, int priceSort) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL WHERE ");
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
			sql.append(" order by price desc");
		} else {
			sql.append(" order by price asc");
		}
		
		if (!exc) {
			return null;
		}
		
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("浏览拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		return this.queryPage(page, sql.toString());
	}
	
	/**
	 * 普通道具下架或购买走
	 */
	public boolean removeNormal(long id) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_NORMAL WHERE ");
		sql.append("id=").append(id);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	public List<MarketGoods> getAll() {
		StringBuilder sql = new StringBuilder("SELECT * FROM ITEM_NORMAL ");
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("获取所有普通物品SQL语句[" + sql.toString() + "]");
		}
		List<MarketGoods> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market获取数据库ITEM_NORMAL表所有记录错误:", e);
		}
		return results;
	}
	
	/**
	 * 普通道具下架或购买走
	 * @deprecated 性能低
	 */
	public boolean removeNormal(long roleId, long uniqueId, int itemId, int key) {
		StringBuilder sql = new StringBuilder("DELETE FROM ITEM_NORMAL WHERE ");
		sql.append("roleId=").append(roleId).append(" and ");
		sql.append("uniqueId=").append(uniqueId).append(" and ");
		sql.append("itemId=").append(itemId).append(" and ");
		sql.append("key=").append(key);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("删除拍卖普通物品SQL语句[" + sql.toString() + "]");
		}
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}

}
