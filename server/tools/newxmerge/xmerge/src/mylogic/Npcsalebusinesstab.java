package mylogic;

import java.util.Map;
import java.util.Map.Entry;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Npcsalebusinesstab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {

		xbean.NpcSaleBusiness srcNpcSaleBusiness = xbean.Pod.newNpcSaleBusinessData(); 
		xbean.NpcSaleBusiness dstNpcSaleBusiness = xbean.Pod.newNpcSaleBusinessData(); 
		srcNpcSaleBusiness.unmarshal(src_value);
		dstNpcSaleBusiness.unmarshal(dest_value);
		
//		//需要合并goodsmap里面的值需要把下面2个值分取他们的平均值，其他值清零
////		<variable name="priorperiodprice" type="int" default="0"/>上期价格
////		<variable name="price" type="int"/>商品价格
//		Map<Integer, xbean.GoodsBusiness> resultmap=dstNpcSaleBusiness.getGoodsmap();
//		Map<Integer, xbean.GoodsBusiness> srcmap=srcNpcSaleBusiness.getGoodsmap();
//		Map<Integer, xbean.GoodsBusiness> dstmap=dstNpcSaleBusiness.getGoodsmap();
//		for(Entry<Integer, xbean.GoodsBusiness> entry:dstmap.entrySet()){
//			int key=entry.getKey();
//			xbean.GoodsBusiness dstgoodsbusiness=entry.getValue();
//			//如果原数据库有记录取平均值，没有直接合并
//			xbean.GoodsBusiness srcgoodsbusiness=srcmap.get(key);
//			if(srcgoodsbusiness!=null){
//				dstgoodsbusiness.setPrice((srcgoodsbusiness.getPrice()+dstgoodsbusiness.getPrice())/2);
//				dstgoodsbusiness.setPriorperiodprice((srcgoodsbusiness.getPriorperiodprice()+dstgoodsbusiness.getPriorperiodprice())/2);
//				dstgoodsbusiness.setPrice(dstgoodsbusiness.getPrice());
//				dstgoodsbusiness.setPriorperiodprice(dstgoodsbusiness.getPriorperiodprice());
//			}
//		}
//		dstNpcSaleBusiness.getGoodsmap().putAll(srcNpcSaleBusiness.getGoodsmap());
//		//清除其他数据
//		for(xbean.GoodsBusiness entry:dstNpcSaleBusiness.getGoodsmap().values()){
//			entry.setBuynum(0);//本期购买的数量
//			entry.setSalenum(0);//本期售卖的数量
//			entry.setLastnum(0);//上期成交量
//			entry.setCount(0);//周期成交量小于等于零的次数,降价后次数清零重新计数
//		}
//		dstNpcSaleBusiness.setTime(System.currentTimeMillis());
//		dstNpcSaleBusiness.setIsactive((short) 1);
		
		dstNpcSaleBusiness.getGoodsmap().clear();
		dstNpcSaleBusiness.getGoodsmap().putAll(srcNpcSaleBusiness.getGoodsmap());
		dstNpcSaleBusiness.setIsactive(srcNpcSaleBusiness.getIsactive());
		dstNpcSaleBusiness.setTime(srcNpcSaleBusiness.getTime());
		
		dest_value.clear();
		dstNpcSaleBusiness.marshal(dest_value);
		
		return true;
	}
	
}