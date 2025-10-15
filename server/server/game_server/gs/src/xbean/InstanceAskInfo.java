
package xbean;

public interface InstanceAskInfo extends mkdb.Bean {
	public InstanceAskInfo copy(); // deep clone
	public InstanceAskInfo toData(); // a Data instance
	public InstanceAskInfo toBean(); // a Bean instance
	public InstanceAskInfo toDataIf(); // a Data instance If need. else return this
	public InstanceAskInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getAnswerinfo(); // key为角色id,value为回答状态,0为不同意,1为同意
	public java.util.Map<Long, Integer> getAnswerinfoAsData(); // key为角色id,value为回答状态,0为不同意,1为同意
	public int getInstid(); // 副本ID
	public long getAsktime(); // 询问的时间

	public void setInstid(int _v_); // 副本ID
	public void setAsktime(long _v_); // 询问的时间
}
