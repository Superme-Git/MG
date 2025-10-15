package fire.pb.shop.srv.market.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.shop.GoodsType;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.jdbc.dao.EquipDAO;
import fire.pb.shop.srv.market.jdbc.dao.NormalDAO;
import fire.pb.shop.srv.market.jdbc.dao.PetDAO;
import fire.pb.shop.srv.market.pojo.EquipDaoBean;
import fire.pb.shop.srv.market.pojo.MarketGoodsHandler;
import fire.pb.shop.srv.market.pojo.NormalDaoBean;
import fire.pb.shop.srv.market.pojo.PetDaoBean;
import fire.pb.shop.utils.BeanCopy;
import fire.pb.shop.utils.MarketUtils;
import fire.pb.util.FireProp;

/**
 * 拍卖数据库操作
 * 
 * @author liangyanpeng
 */
public class MarketDAO implements DAO {
	
	public final static Properties MARKET_TABLE = ConfigManager.getInstance().getPropConf("market");
	
	private static class MarketDAOHolder {
		static MarketDAO instance = new MarketDAO();
	}
	
	public static MarketDAO getInstance() {
		return MarketDAOHolder.instance;
	}
	
	@Override
	public void createTable() {
		try {
			// 最先执行
			this.update("SET DB_CLOSE_DELAY -1");
			/////////////////////////////////////////////////////
			// 读取删除表SQL
			String dropNormalTab = FireProp.getStringValue(MARKET_TABLE, "DropNormalTab");
			String dropEquiplTab = FireProp.getStringValue(MARKET_TABLE, "DropEquipTab");
			String dropPetTab = FireProp.getStringValue(MARKET_TABLE, "DropPetTab");
			
			// 读取删除索引SQL
			String dropNormalTabIdx = FireProp.getStringValue(MARKET_TABLE, "DropNormalTabIdx");
			String dropEquipTabIdx = FireProp.getStringValue(MARKET_TABLE, "DropEquipTabIdx");
			String dropPetTabIdx = FireProp.getStringValue(MARKET_TABLE, "DropPetTabIdx");
			
			// 读取删除宠物表itemid索引
			String dropPetItemIdIdx = FireProp.getStringValue(MARKET_TABLE, "DropPetItemIdIdx");
			// 读取删除普通道具level索引
			String dropNormalTabLevelIdx = FireProp.getStringValue(MARKET_TABLE, "DropNormalTabLevelIdx");
			// 读取删除普通道具price索引
			String dropNormalTabPriceIdx = FireProp.getStringValue(MARKET_TABLE, "DropNormalTabPriceIdx");
			
			
			// 删除表
			this.update(dropNormalTab);
			this.update(dropEquiplTab);
			this.update(dropPetTab);
			
			// 删除索引
			this.update(dropNormalTabIdx);
			this.update(dropEquipTabIdx);
			this.update(dropPetTabIdx);
			this.update(dropPetItemIdIdx);
			this.update(dropNormalTabLevelIdx);
			this.update(dropNormalTabPriceIdx);
			
			///////////////////////////////////////////////////////////
			// 读取创建表SQL
			String normalTab = FireProp.getStringValue(MARKET_TABLE, "NormalTab");
			String equipTab = FireProp.getStringValue(MARKET_TABLE, "EquipTab");
			String petTab = FireProp.getStringValue(MARKET_TABLE, "PetTab");
			
			// 读取创建主键SQL
			String normalTabPri = FireProp.getStringValue(MARKET_TABLE, "NormalTabPri");
			String equipTabPri = FireProp.getStringValue(MARKET_TABLE, "EquipTabPri");
			String petTabPri = FireProp.getStringValue(MARKET_TABLE, "PetTabPri");
			
			// 读取创建索引SQL
			String normalTabIdx = FireProp.getStringValue(MARKET_TABLE, "NormalTabIdx");
			String equipTabIdx = FireProp.getStringValue(MARKET_TABLE, "EquipTabIdx");
			String petTabIdx = FireProp.getStringValue(MARKET_TABLE, "PetTabIdx");
			
			// 读取宠物表itemid索引
			String petTabItemIdIdx = FireProp.getStringValue(MARKET_TABLE, "PetTabItemIdIdx");
			// 读取普通道具level索引
			String normalTabLevelIdx = FireProp.getStringValue(MARKET_TABLE, "NormalTabLevelIdx");
			// 读取普通道具price索引
			String normalTabPriceIdx = FireProp.getStringValue(MARKET_TABLE, "NormalTabPriceIdx");
			
			// 创建表
			this.update(normalTab);
			this.update(equipTab);
			this.update(petTab);
			
			// 创建主键
			this.update(normalTabPri);
			this.update(equipTabPri);
			this.update(petTabPri);
			
			// 创建索引
			this.update(normalTabIdx);
			this.update(equipTabIdx);
			this.update(petTabIdx);
			
			// 创建宠物表itemid索引
			this.update(petTabItemIdIdx);
			// 创建普通道具level索引
			this.update(normalTabLevelIdx);
			// 创建普通道具price索引
			this.update(normalTabPriceIdx);
			
			// 查看数据库表
			this.getTables();
			
		} catch (Exception e) {
			MarketManager.LOG.error("初始化market嵌入式数据库表出错:", e);
		}
	}
	
	
	/**
	 * 打印数据库中的所有表名
	 */
	public void getTables() {
		
		if (!MarketManager.LOG.isDebugEnabled()) {
			return;
		}
		
		Connection conn = null;
		ResultSet tableRet = null;
		try {
			conn = ConnectionUtils.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			tableRet = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
			while (tableRet.next()) {
				MarketManager.LOG.debug(" H2 数据库中的表 " + tableRet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			MarketManager.LOG.error("market修改数据库表记录错误:", e);
		} finally {
			ConnectionUtils.closeConnetion();
		}
	}
	
	@Override
	public boolean initializationDataFromXdb() {
		// 事务开始
		Transaction.begin();

		xtable.Normaltab.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.NormalItem>() {
					@Override
					public boolean onRecord(Long k, xbean.NormalItem v) {
						NormalDaoBean dest = BeanCopy.normalMkdbCopyDaoBean(v);
						NormalDAO.getInstance().addNormal(dest);
						return true;
					}
				}
		);
		
		xtable.Equiptab.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.EquipItem>() {
					@Override
					public boolean onRecord(Long k, xbean.EquipItem v) {
						EquipDaoBean dest = BeanCopy.equipMkdbCopyDaoBean(v);
						EquipDAO.getInstance().addEquip(dest);
						return true;
					}
				}
		);
		
		xtable.Pettab.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.PetItem>() {
					@Override
					public boolean onRecord(Long k, xbean.PetItem v) {
						PetDaoBean dest = BeanCopy.petMkdbCopyDaoBean(v);
						PetDAO.getInstance().addPet(dest);
						return true;
					}
				}
		);
		
		// 事务结束
		return Transaction.commit();
		
	}

	@Override
	public boolean persistenceDataToXdb() {
		return true;
	}

	public int update(String sql) {
		int ret = 0;
		
		try {
			ret = JdbcTemplate.getInstance().update(sql);
		} catch (SQLException e) {
			MarketManager.LOG.error("market修改数据库表记录错误:", e);
		}
		
		return ret;
	}

	public List<MarketGoods> queryPage(Page page, String sql) {
		List<MarketGoods> results = new ArrayList<>();
		
		try {
			results = JdbcTemplate.getInstance().query(page, sql, new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库表记录错误:", e);
		}
		
		return results;
	}
	
	
	/**
	 * 获得符合条件的记录数
	 * @param sql
	 * @return 记录数
	 */
	public int queryTotalRow(String sql) {
		ResultSet rs = null;
		int totalRow = 0;
		
		try {
			rs = JdbcTemplate.getInstance().query(sql);
			while(rs.next()) {
				totalRow = rs.getInt(1);
			}
		} catch (SQLException e) {
			MarketManager.LOG.error("market查询数据库表记录错误:", e);
		}
		
		return totalRow;
	}
	
	/**
	 * 更新普通道具数量
	 * @param id 记录id
	 * @param newSaleNumber 新的销售数量
	 */
	public boolean updateNumber(Table table, long id, int newSaleNumber) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET NUMBER=");
		sql.append(newSaleNumber);
		sql.append(" WHERE ID=");
		sql.append(id);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 更新道具过期时间
	 * @param id 记录id
	 * @param newExpireTime 新的过期时间
	 */
	public boolean updateExpireTime(Table table, long id, long newExpireTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET EXPIRETIME=");
		sql.append(newExpireTime);
		sql.append(" WHERE ID=");
		sql.append(id);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 更新道具公示时间
	 * @param id 记录id
	 * @param newShowTime 新的公示时间
	 */
	public boolean updateShowTime(Table table, long id,long newShowTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET SHOWTIME=");
		sql.append(newShowTime);
		sql.append(" WHERE ID=");
		sql.append(id);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 更新道具公示时间
	 * @param id 记录id
	 * @param newPrice 新的价格
	 */
	public boolean updatePrice(Table table, long id, int newPrice) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET PRICE=");
		sql.append(newPrice);
		sql.append(" WHERE ID=");
		sql.append(id);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 更新道具关注次数
	 * @param id 记录id
	 * @param newSaleNumber 新的销售数量
	 */
	public boolean updateAttentionNumber(Table table, long id, int newNumber) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET ATTENTION=");
		sql.append(newNumber);
		sql.append(" WHERE ID=");
		sql.append(id);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 结束公示时间
	 * @param table
	 * @param now 公示结束时间
	 */
	public boolean overShowTime(Table table, long newShowTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET SHOWTIME=");
		sql.append(newShowTime);
		sql.append(" WHERE SHOWTIME > ");
		sql.append(newShowTime);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * 延长过期时间
	 * @param table
	 * @param now 过期时间
	 */
	public boolean extendExprieTime(Table table, long newShowTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET EXPIRETIME=");
		sql.append(newShowTime);
		int ret = this.update(sql.toString());
		return ret > 0 ? true : false;
	}
	
	/**
	 * @param table  表名
	 * @return 返回表中所有记录
	 */
	public List<MarketGoods> getAll(Table table) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ");
		sql.append(table);
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("获取所有" + table + "物品SQL语句[" + sql.toString() + "]");
		}
		List<MarketGoods> results = null;
		try {
			results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
		} catch (SQLException e) {
			MarketManager.LOG.error("market获取数据库" + table + "表所有记录错误:", e);
		}
		return results;
	}
	
	/**
	 * @param table
	 * @param id
	 * @param page 分页对象
	 * @return Object数组包括{浏览类型、一级、二级、三级、总行数、目标行数, 等级}
	 */
	public Object[] queryTotalRowAndRowNum(Table table, long id, int itemType) {
		try {
			long now = System.currentTimeMillis();
			StringBuilder sql = new StringBuilder();
			sql.append("select firstno, twono, threeno, level, showtime, expiretime from ").append(table).append(" where id = ").append(id);
			sql.append(" and ");
			sql.append("expiretime > ").append(now + 2000);// 增加2秒, 防止查询结果返回已经超时
			ResultSet rs1 = JdbcTemplate.getInstance().query(sql.toString());
			// 无数据返回
			if (!rs1.next()) {
				return null;
			}
			// 重置游标
			rs1.beforeFirst();
			
			int firstNo = 0, twoNo = 0, threeNo = 0, level = 0;
			long showTime = 0L, expireTime = 0L;
			while (rs1.next()) {
				firstNo = rs1.getInt("firstno");
				twoNo = rs1.getInt("twono");
				threeNo = rs1.getInt("threeno");
				showTime = rs1.getLong("showtime");
				expireTime = rs1.getLong("expiretime");
				level = rs1.getInt("level");
			}
			
			// 浏览类型默认购买
			int browseType = MarketUtils.BROWSE_MY_BUY;
			// 判断是否是公示期
			if (showTime > now) {
				browseType = MarketUtils.BROWSE_SHOW;
			}
						
			// 清理
			sql.delete(0, sql.length());
			
			if (GoodsType.OTHER_ITEM == itemType) {
				int[] range = MarketUtils.getLevelRange(level);
				int limitmin = range[0];
				int limitmax = range[1];
				// 查询行号及目标道具所在行号
				sql.append("select count(*) as totalrow , t2.indexrow, t2.id, t2.firstno, t2.twono, t2.threeno, t2.level from ");
				sql.append("(select count(1) as totalrow from ");
				sql.append(table);
				sql.append(" where firstno = ");
				sql.append(firstNo);
				sql.append(" and twono = ");
				sql.append(twoNo);
				sql.append(" and threeno = ");
				sql.append(threeNo);
				sql.append(" and level >= ");
				sql.append(limitmin);
				sql.append(" and level <= ");
				sql.append(limitmax);
				if (browseType == MarketUtils.BROWSE_MY_BUY) {
					sql.append(" and ");
					sql.append("showtime <= ").append(now);
					sql.append(" and ");
					sql.append("expiretime > ").append(now);
				} else if (browseType == MarketUtils.BROWSE_SHOW) {
					sql.append(" and ");
					sql.append("showtime>").append(now);
				}
				sql.append(") as t1 ");
				sql.append("left join ");
				sql.append("(select rownum() as indexrow, * from ");
				sql.append("(select id, firstno, twono, threeno, level from ");
				sql.append(table);
				sql.append(" where firstno = ");
				sql.append(firstNo);
				sql.append(" and twono = ");
				sql.append(twoNo);
				sql.append(" and threeno = ");
				sql.append(threeNo);
				sql.append(" and level >= ");
				sql.append(limitmin);
				sql.append(" and level <= ");
				sql.append(limitmax);
				if (browseType == MarketUtils.BROWSE_MY_BUY) {
					sql.append(" and ");
					sql.append("showtime <= ").append(now);
					sql.append(" and ");
					sql.append("expiretime > ").append(now);
				} else if (browseType == MarketUtils.BROWSE_SHOW) {
					sql.append(" and ");
					sql.append("showtime>").append(now);
				}
				sql.append(" order by price asc");
				sql.append(")) as t2 where t2.id = ");
				sql.append(id);
			}
			else {
				// 查询行号及目标道具所在行号
				sql.append("select count(*) as totalrow , t2.indexrow, t2.id, t2.firstno, t2.twono, t2.threeno, t2.level from ");
				sql.append("(select count(1) as totalrow from ");
				sql.append(table);
				sql.append(" where firstno = ");
				sql.append(firstNo);
				sql.append(" and twono = ");
				sql.append(twoNo);
				sql.append(" and threeno = ");
				sql.append(threeNo);
				if (browseType == MarketUtils.BROWSE_MY_BUY) {
					sql.append(" and ");
					sql.append("showtime <= ").append(now);
					sql.append(" and ");
					sql.append("expiretime > ").append(now);
				} else if (browseType == MarketUtils.BROWSE_SHOW) {
					sql.append(" and ");
					sql.append("showtime>").append(now);
				}
				sql.append(") as t1 ");
				sql.append("left join ");
				sql.append("(select rownum() as indexrow, * from ");
				sql.append("(select id, firstno, twono, threeno, level from ");
				sql.append(table);
				sql.append(" where firstno = ");
				sql.append(firstNo);
				sql.append(" and twono = ");
				sql.append(twoNo);
				sql.append(" and threeno = ");
				sql.append(threeNo);
				if (browseType == MarketUtils.BROWSE_MY_BUY) {
					sql.append(" and ");
					sql.append("showtime <= ").append(now);
					sql.append(" and ");
					sql.append("expiretime > ").append(now);
				} else if (browseType == MarketUtils.BROWSE_SHOW) {
					sql.append(" and ");
					sql.append("showtime>").append(now);
				}
				sql.append(" order by price asc");
				sql.append(")) as t2 where t2.id = ");
				sql.append(id);
			}
			
			if (MarketManager.LOG.isDebugEnabled()) {
				MarketManager.LOG.debug("聊天界面打开道具浏览" + sql.toString());
			}
			
			ResultSet rs2 = JdbcTemplate.getInstance().query(sql.toString());
			// 没有找到总行数和指定道具所在行数
			if (!rs2.next()) {
				return null;
			}
			// 重置游标
			rs2.beforeFirst();
			int totalRow = 0, indexRow = 0;
			while (rs2.next()) {
				totalRow = rs2.getInt("totalrow");
				indexRow = rs2.getInt("indexrow");
			}
			
			return new Object[] {browseType, firstNo, twoNo, threeNo, totalRow, indexRow, level};
		} catch (SQLException e) {
			MarketManager.LOG.error("聊天浏览拍卖道具", e);
		}
		return null;
	}
	
}
