
package xbean;

public interface LDVideoHisList extends mkdb.Bean {
	public LDVideoHisList copy(); // deep clone
	public LDVideoHisList toData(); // a Data instance
	public LDVideoHisList toBean(); // a Bean instance
	public LDVideoHisList toDataIf(); // a Data instance If need. else return this
	public LDVideoHisList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<String> getLdvidehisinfo(); // 生死战历史排行记录
	public java.util.List<String> getLdvidehisinfoAsData(); // 生死战历史排行记录

}
