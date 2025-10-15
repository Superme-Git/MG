
package xbean;

public interface InstanceNpcServ extends mkdb.Bean {
	public InstanceNpcServ copy(); // deep clone
	public InstanceNpcServ toData(); // a Data instance
	public InstanceNpcServ toBean(); // a Bean instance
	public InstanceNpcServ toDataIf(); // a Data instance If need. else return this
	public InstanceNpcServ toBeanIf(); // a Bean instance If need. else return this

	public long getServiceid(); // serviceid
	public int getActid(); // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作

	public void setServiceid(long _v_); // serviceid
	public void setActid(int _v_); // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
}
