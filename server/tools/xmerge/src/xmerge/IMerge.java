package xmerge;

import com.locojoy.base.Marshal.OctetsStream;

/**
 *所有Known_key_conflict的表来实现这个接口
 *<li>接口模式暂定如此
 *<li>将src_value合并到dest_value中，dest_value将作为合并后数据表中的value
 *<li>返回true则表示merge成功
 *<li>返回false则表示merge失败，暂将dest_value作为合并后的value并记录日志
 *<li>抛出异常则认为merge逻辑异常，中断整个合并操作
 * */
public interface IMerge {
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception;
}
