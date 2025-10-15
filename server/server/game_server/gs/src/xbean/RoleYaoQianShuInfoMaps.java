
package xbean;

public interface RoleYaoQianShuInfoMaps extends mkdb.Bean {
	public RoleYaoQianShuInfoMaps copy(); // deep clone
	public RoleYaoQianShuInfoMaps toData(); // a Data instance
	public RoleYaoQianShuInfoMaps toBean(); // a Bean instance
	public RoleYaoQianShuInfoMaps toDataIf(); // a Data instance If need. else return this
	public RoleYaoQianShuInfoMaps toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumaps(); // key 为npckey
	public java.util.Map<Long, xbean.RoleYaoQianShuInfo> getYaoqianshumapsAsData(); // key 为npckey

}
