package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;
/**
 * ��ת�� ������ݺϲ�
 * @author guohuiming
 *
 */
public class XinyundazhuanpanTable implements IMerge{

    @Override
    public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//        xbean.XinyunDaZhuanPan srcDazp = xbean.Pod.newXinyunDaZhuanPanData();
//        xbean.XinyunDaZhuanPan dstDazp = xbean.Pod.newXinyunDaZhuanPanData();
//        srcDazp.unmarshal(src_value);
//        dstDazp.unmarshal(dest_value);
//        
//        if (srcDazp.getLastendtime() > dstDazp.getLastendtime()){
//            //ȡ���ʱ���
//            dest_value.clear();
//            srcDazp.marshal(dest_value);
//            return true;
//        }else if (srcDazp.getLastendtime() < dstDazp.getLastendtime()){
//           //ȡ���ʱ���
//            dest_value.clear();
//            dstDazp.marshal(dest_value);
//            return true;
//        }else{
//            //�����ͳ�ֵ��ϲ�
//            dstDazp.setRecharge(dstDazp.getRecharge() + srcDazp.getRecharge());
//            dstDazp.setStopbnyynums(dstDazp.getStopbnyynums()  + srcDazp.getStopbnyynums());
//            dstDazp.setStopqznfnums(dstDazp.getStopqznfnums() + srcDazp.getStopqznfnums());
//            dstDazp.setAwardqznfnums(dstDazp.getAwardqznfnums() + srcDazp.getAwardqznfnums());
//            dstDazp.setAwardbnyynums(dstDazp.getAwardbnyynums() + srcDazp.getAwardbnyynums());
//            //������߶���û��ȡ�Ľ������Ͳ������� 
//            if (srcDazp.getAwarditemid() != 0){
//                dstDazp.setAwarditemid(srcDazp.getAwarditemid());
//            }
//            if (srcDazp.getAwardbnyyindex() >= 0){
//                dstDazp.setAwardbnyyindex(srcDazp.getAwardbnyyindex());
//            }
//            if (srcDazp.getAwardqznfindex() >= 0){
//                dstDazp.setAwardqznfindex(srcDazp.getAwardqznfindex());
//            }
//            dest_value.clear();
//            dstDazp.marshal(dest_value);
            return true;
//        }
    }

}
