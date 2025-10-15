
package xbean;

public interface SingleCompensation extends mkdb.Bean {
	public SingleCompensation copy(); // deep clone
	public SingleCompensation toData(); // a Data instance
	public SingleCompensation toBean(); // a Bean instance
	public SingleCompensation toDataIf(); // a Data instance If need. else return this
	public SingleCompensation toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 操作时间
	public long getValidtime(); // 有效时间
	public String getTitle(); // 标题
	public com.locojoy.base.Octets getTitleOctets(); // 标题
	public String getContent(); // 内容
	public com.locojoy.base.Octets getContentOctets(); // 内容
	public java.util.List<xbean.SingleCompensationAward> getAwards(); // 奖励列表
	public java.util.List<xbean.SingleCompensationAward> getAwardsAsData(); // 奖励列表
	public int getGivetype(); // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
	public java.util.List<Long> getRoleids(); // 
	public java.util.List<Long> getRoleidsAsData(); // 
	public java.util.List<Long> getRoleidsofget(); // 把key给角色后,把角色id记录在这里
	public java.util.List<Long> getRoleidsofgetAsData(); // 把key给角色后,把角色id记录在这里
	public java.util.List<Long> getRoleidsofexcept(); // 排除的角色id列表
	public java.util.List<Long> getRoleidsofexceptAsData(); // 排除的角色id列表
	public java.util.List<xbean.SingleCompensationCond> getConditions(); // 条件
	public java.util.List<xbean.SingleCompensationCond> getConditionsAsData(); // 条件
	public String getOpid(); // 操作流水号,10字节定长字符串,运营需求
	public com.locojoy.base.Octets getOpidOctets(); // 操作流水号,10字节定长字符串,运营需求
	public String getSign(); // 签名,32字节定长字符串,运营需求
	public com.locojoy.base.Octets getSignOctets(); // 签名,32字节定长字符串,运营需求

	public void setTime(long _v_); // 操作时间
	public void setValidtime(long _v_); // 有效时间
	public void setTitle(String _v_); // 标题
	public void setTitleOctets(com.locojoy.base.Octets _v_); // 标题
	public void setContent(String _v_); // 内容
	public void setContentOctets(com.locojoy.base.Octets _v_); // 内容
	public void setGivetype(int _v_); // 发送类型 0=发送指定id的角色(使用roleids字段) 1=发送所有玩家(使用roleidsofget字段)
	public void setOpid(String _v_); // 操作流水号,10字节定长字符串,运营需求
	public void setOpidOctets(com.locojoy.base.Octets _v_); // 操作流水号,10字节定长字符串,运营需求
	public void setSign(String _v_); // 签名,32字节定长字符串,运营需求
	public void setSignOctets(com.locojoy.base.Octets _v_); // 签名,32字节定长字符串,运营需求
}
