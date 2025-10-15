
package xbean;

public interface LDVideoRoleInfoDesList extends mkdb.Bean {
	public LDVideoRoleInfoDesList copy(); // deep clone
	public LDVideoRoleInfoDesList toData(); // a Data instance
	public LDVideoRoleInfoDesList toBean(); // a Bean instance
	public LDVideoRoleInfoDesList toDataIf(); // a Data instance If need. else return this
	public LDVideoRoleInfoDesList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistall(); // 生死战录像信息
	public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistallAsData(); // 生死战录像信息

}
