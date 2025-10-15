package xmerge;

import com.locojoy.base.Marshal.OctetsStream;

/**
 *所有Known_key_conflict的表来实现这个接口
 *<li>接口模式暂定如此
 *<li>将src_value合并到dest_value中，dest_value将作为合并后数据表中的value
 *<li>返回true则表示merge成功
 *<li>返回false则表示merge失败，暂将dest_value作为合并后的value并记录日志
 *<li>抛出异常则认为merge逻辑异常，中断整个合并操作
 *一般这个方法里面的代码分三步:第一步unmarshal,第二步做你需要的修改,
 *第三步把最后修改好的value marshal到dest_value中(在marshal之前记得做一下dest_value.clear()清空以前的数据),
 *因为xmerge会把dest_value作为合服后的数据库里key对应的value值.最后return true就可以了.
 *简单的例子可以看下mylogic.User类的实现
 * */
public interface IMerge {
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception;
}
