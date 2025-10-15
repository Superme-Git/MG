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
		

		dstMarketFloatingGoods.setFloatingmin(srcMarketFloatingGoods.getFloatingmin()); // �۸�����
		dstMarketFloatingGoods.setFloatingmax(srcMarketFloatingGoods.getFloatingmax()); // �۸�����
		dstMarketFloatingGoods.setFloatingprice(srcMarketFloatingGoods.getFloatingprice()); // �����۸�ϵ��
		dstMarketFloatingGoods.setBasehangordernum(srcMarketFloatingGoods.getBasehangordernum()); // ��׼�ҵ���
		dstMarketFloatingGoods.setBasesalenum(srcMarketFloatingGoods.getBasesalenum()); // ��׼�ɽ���
		dstMarketFloatingGoods.setHangordernum(srcMarketFloatingGoods.getHangordernum()); // �ҵ���
		dstMarketFloatingGoods.setSalenum(srcMarketFloatingGoods.getSalenum()); // ���ڳɽ���
		dstMarketFloatingGoods.setPriorperiodprice(srcMarketFloatingGoods.getPriorperiodprice()); // ���ڼ۸�
		dstMarketFloatingGoods.setPrice(srcMarketFloatingGoods.getPrice()); // ��ǰ�۸�
		dstMarketFloatingGoods.setTotalmoney(srcMarketFloatingGoods.getTotalmoney()); // ���������ܽ��

		
		dest_value.clear();
		dstMarketFloatingGoods.marshal(dest_value);
		
		return true;
	}
	
}