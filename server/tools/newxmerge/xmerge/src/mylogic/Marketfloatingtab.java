package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Marketfloatingtab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {

		xbean.MarketFloatingGoods srcMarketFloatingGoods = xbean.Pod.newMarketFloatingGoodsData(); 
		xbean.MarketFloatingGoods dstMarketFloatingGoods = xbean.Pod.newMarketFloatingGoodsData(); 
		srcMarketFloatingGoods.unmarshal(src_value);
		dstMarketFloatingGoods.unmarshal(dest_value);
		

		dstMarketFloatingGoods.setFloatingmin(srcMarketFloatingGoods.getFloatingmin()); // 价格下限
		dstMarketFloatingGoods.setFloatingmax(srcMarketFloatingGoods.getFloatingmax()); // 价格上限
		dstMarketFloatingGoods.setFloatingprice(srcMarketFloatingGoods.getFloatingprice()); // 浮动价格系数
		dstMarketFloatingGoods.setBasehangordernum(srcMarketFloatingGoods.getBasehangordernum()); // 基准挂单量
		dstMarketFloatingGoods.setBasesalenum(srcMarketFloatingGoods.getBasesalenum()); // 基准成交量
		dstMarketFloatingGoods.setHangordernum(srcMarketFloatingGoods.getHangordernum()); // 挂单量
		dstMarketFloatingGoods.setSalenum(srcMarketFloatingGoods.getSalenum()); // 当期成交量
		dstMarketFloatingGoods.setPriorperiodprice(srcMarketFloatingGoods.getPriorperiodprice()); // 上期价格
		dstMarketFloatingGoods.setPrice(srcMarketFloatingGoods.getPrice()); // 当前价格
		dstMarketFloatingGoods.setTotalmoney(srcMarketFloatingGoods.getTotalmoney()); // 本期售卖总金额

		
		dest_value.clear();
		dstMarketFloatingGoods.marshal(dest_value);
		
		return true;
	}
	
}