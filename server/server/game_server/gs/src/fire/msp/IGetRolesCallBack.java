package fire.msp;

import java.util.List;

/**
 * 已经在procedure中,实现该接口的process方法时,不要在里面submit procedure
 */
public interface IGetRolesCallBack
{
	public void process(List<Long> roleIds);
}
