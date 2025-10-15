package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;
/**
 * 大转盘 统计次数
 * @author guohuiming
 *
 */
public class DazhuanpanstaticTable implements IMerge{

    @Override
    public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
        return true;
    }

}
