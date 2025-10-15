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
		
//		//��Ҫ�ϲ�goodsmap�����ֵ��Ҫ������2��ֵ��ȡ���ǵ�ƽ��ֵ������ֵ����
////		<variable name="priorperiodprice" type="int" default="0"/>���ڼ۸�
////		<variable name="price" type="int"/>��Ʒ�۸�
//		Map<Integer, xbean.GoodsBusiness> resultmap=dstNpcSaleBusiness.getGoodsmap();
//		Map<Integer, xbean.GoodsBusiness> srcmap=srcNpcSaleBusiness.getGoodsmap();
//		Map<Integer, xbean.GoodsBusiness> dstmap=dstNpcSaleBusiness.getGoodsmap();
//		for(Entry<Integer, xbean.GoodsBusiness> entry:dstmap.entrySet()){
//			int key=entry.getKey();
//			xbean.GoodsBusiness dstgoodsbusiness=entry.getValue();
//			//���ԭ���ݿ��м�¼ȡƽ��ֵ��û��ֱ�Ӻϲ�
//			xbean.GoodsBusiness srcgoodsbusiness=srcmap.get(key);
//			if(srcgoodsbusiness!=null){
//				dstgoodsbusiness.setPrice((srcgoodsbusiness.getPrice()+dstgoodsbusiness.getPrice())/2);
//				dstgoodsbusiness.setPriorperiodprice((srcgoodsbusiness.getPriorperiodprice()+dstgoodsbusiness.getPriorperiodprice())/2);
//				dstgoodsbusiness.setPrice(dstgoodsbusiness.getPrice());
//				dstgoodsbusiness.setPriorperiodprice(dstgoodsbusiness.getPriorperiodprice());
//			}
//		}
//		dstNpcSaleBusiness.getGoodsmap().putAll(srcNpcSaleBusiness.getGoodsmap());
//		//�����������
//		for(xbean.GoodsBusiness entry:dstNpcSaleBusiness.getGoodsmap().values()){
//			entry.setBuynum(0);//���ڹ��������
//			entry.setSalenum(0);//��������������
//			entry.setLastnum(0);//���ڳɽ���
//			entry.setCount(0);//���ڳɽ���С�ڵ�����Ĵ���,���ۺ�����������¼���
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