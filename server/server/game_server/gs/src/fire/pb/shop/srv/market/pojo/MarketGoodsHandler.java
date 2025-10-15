package fire.pb.shop.srv.market.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.GoodsType;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.MarketThreeTable;
import fire.pb.shop.srv.market.jdbc.utils.handler.ResultSetHandler;

public class MarketGoodsHandler implements ResultSetHandler<List<MarketGoods>> {

	@Override
	public List<MarketGoods> handle(ResultSet rs) throws SQLException {
		List<MarketGoods> results = new ArrayList<>();
		while(rs.next()) {
			MarketGoods bean = new MarketGoods();
			bean.id = rs.getLong("id");
			bean.saleroleid = rs.getLong("roleid");
			bean.itemid = rs.getInt("itemid");
			if (bean.itemid > 0) {
				MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(bean.itemid);
				int itemType = itemConf.getItemtype();
				if (itemType == GoodsType.PET) {
					bean.itemtype = GoodsType.PET;
				} else {
					bean.itemtype = GoodsType.NORMAL_ITEM;
				}
			}
			bean.key = rs.getInt("key");
			bean.price = rs.getInt("price");
			bean.showtime = rs.getLong("showtime");
			bean.expiretime = rs.getLong("expiretime");
			bean.num = rs.getInt("number");
			bean.level = rs.getInt("level");
			bean.attentionnumber = rs.getInt("attention");
			results.add(bean);
		}
		return results;
	}

}
