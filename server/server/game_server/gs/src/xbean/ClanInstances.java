
package xbean;

public interface ClanInstances extends mkdb.Bean {
	public ClanInstances copy(); // deep clone
	public ClanInstances toData(); // a Data instance
	public ClanInstances toBean(); // a Bean instance
	public ClanInstances toDataIf(); // a Data instance If need. else return this
	public ClanInstances toBeanIf(); // a Bean instance If need. else return this

	public long getClanid(); // 公会id
	public long getCurrentzonekey(); // 当前激活的公会副本key，0为该公会没有激活公会副本
	public java.util.Map<Integer, Long> getInstkey(); // 公会副本,key副本id; value为对应的副本key.
	public java.util.Map<Integer, Long> getInstkeyAsData(); // 公会副本,key副本id; value为对应的副本key.
	public java.util.Map<Integer, Integer> getInststate(); // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
	public java.util.Map<Integer, Integer> getInststateAsData(); // 公会副本,key副本id; value为对应的副本状态. 0 表示未完成, 1 表示已经完成
	public java.util.Map<Integer, Integer> getInststep(); // 公会副本进行的副本id对应的层数
	public java.util.Map<Integer, Integer> getInststepAsData(); // 公会副本进行的副本id对应的层数
	public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfo(); // key副本id
	public java.util.Map<Integer, xbean.ClanSaveInfo> getSaveinfoAsData(); // key副本id
	public java.util.Map<Integer, Long> getInstreset(); // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
	public java.util.Map<Integer, Long> getInstresetAsData(); // 公会副本是否需要重启重置, key副本id, value 表示是否重置: 0 表示没有重置,1 表示重置
	public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfo(); // key:battleconfigId value:bossinfo
	public java.util.Map<Integer, xbean.ClanBossInfo> getBossinfoAsData(); // key:battleconfigId value:bossinfo

	public void setClanid(long _v_); // 公会id
	public void setCurrentzonekey(long _v_); // 当前激活的公会副本key，0为该公会没有激活公会副本
}
