
package xbean;

public interface LDVideoRoleRoseInfoList extends mkdb.Bean {
	public LDVideoRoleRoseInfoList copy(); // deep clone
	public LDVideoRoleRoseInfoList toData(); // a Data instance
	public LDVideoRoleRoseInfoList toBean(); // a Bean instance
	public LDVideoRoleRoseInfoList toDataIf(); // a Data instance If need. else return this
	public LDVideoRoleRoseInfoList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolist(); // 点赞记录 key=videoid
	public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolistAsData(); // 点赞记录 key=videoid

}
