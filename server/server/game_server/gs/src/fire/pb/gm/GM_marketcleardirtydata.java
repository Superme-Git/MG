package fire.pb.gm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.shop.GoodsType;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.MarketStore;
import fire.pb.shop.srv.market.jdbc.JdbcTemplate;
import fire.pb.shop.srv.market.jdbc.Table;
import fire.pb.shop.srv.market.jdbc.Transaction;
import fire.pb.shop.srv.market.pojo.MarketGoodsHandler;

/**
 * 清除拍卖脏数据
 * 
 * @author liangyanpeng
 */
public class GM_marketcleardirtydata extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 4) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		int tableName = Integer.valueOf(args[0]);
		int firstno = Integer.valueOf(args[1]);
		int towno = Integer.valueOf(args[2]);
		int threeno = Integer.valueOf(args[3]);
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				
				Table table = null;
				switch (tableName) {
				case GoodsType.NORMAL_ITEM:
					table = Table.ITEM_NORMAL;
					break;
				case GoodsType.EQUIP_ITEM:
					table = Table.ITEM_EQUIP;
					break;
				case GoodsType.PET:
					table = Table.ITEM_PET;
					break;
				}
				if (null == table) 
					return false;
				
				// 事务开始
				Transaction.begin();
				
				List<MarketGoods> results = this.getTable(table);
				if (null == results)
					return false;

				List<MarketGoods> removes = new ArrayList<>();
				
				switch (tableName) {
				case GoodsType.NORMAL_ITEM:
					for (MarketGoods marketGoods : results) {
						long id = marketGoods.id;
						long roleId = marketGoods.saleroleid;
						int key = marketGoods.key;
						int itemId = marketGoods.itemid;
						ItemMaps marketContainer = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, false);
						
						ItemBase itemBase = marketContainer.getItem(key);
						if (null == itemBase || itemId != itemBase.getItemId()) {
							MarketStore.removeNormal(id);
							removes.add(marketGoods);
						}
					}
					break;
				case GoodsType.EQUIP_ITEM:
					for (MarketGoods marketGoods : results) {
						long id = marketGoods.id;
						long roleId = marketGoods.saleroleid;
						int key = marketGoods.key;
						int itemId = marketGoods.itemid;
						ItemMaps marketContainer = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, false);
						
						ItemBase itemBase = marketContainer.getItem(key);
						if (null == itemBase || itemId != itemBase.getItemId()) {
							MarketStore.removeEquip(id);
							removes.add(marketGoods);
						}
					}
					break;
				case GoodsType.PET:
					for (MarketGoods marketGoods : results) {
						long id = marketGoods.id;
						long roleId = marketGoods.saleroleid;
						int key = marketGoods.key;
						int itemId = marketGoods.itemid;
						PetColumn dstcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, false);
						
						fire.pb.pet.Pet pet = dstcol.getPet(key);
						if (null == pet || itemId != pet.getBaseId()) {
							MarketStore.removePet(id);
							removes.add(marketGoods);
						}
					}
					break;
				}
				
				for (MarketGoods marketGoods : removes) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("删除数据库id=").append(marketGoods.id).append(", 道具id=").append(marketGoods.itemid).append(", 道具表=").append(table);
					MarketManager.LOG.fatal(sbd);
				}
				
				// 事务结束
				return Transaction.commit();
			}
			
			/**
			 * @param table  表名
			 * @return 返回表中指定标签下物品记录
			 */
			public List<MarketGoods> getTable(Table table) {
				StringBuilder sql = new StringBuilder("SELECT * FROM ");
				sql.append(table).append(" WHERE firstno= ").append(firstno).append(" and twono= ").append(towno)
				.append(" and threeno in ( ").append(threeno).append(")");
				if (MarketManager.LOG.isInfoEnabled()) {
					MarketManager.LOG.info("查询" + table + "指定标签下物品SQL语句[" + sql.toString() + "]");
				}
				List<MarketGoods> results = null;
				try {
					results = JdbcTemplate.getInstance().query(sql.toString(), new MarketGoodsHandler());
				} catch (SQLException e) {
					MarketManager.LOG.error("market获取数据库" + table + "表指定标签下物品记录错误:", e);
				}
				return results;
			}
			
			
		}.submit();
		
		
		return true;
	}

	@Override
	String usage() {
		return "//marketcleardirtydata 必须4个参数, 1、table(1-3)  2、firstno 3、towno 4、threeno";
	}
}
