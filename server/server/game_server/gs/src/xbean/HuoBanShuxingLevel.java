
package xbean;

public interface HuoBanShuxingLevel extends mkdb.Bean {
	public HuoBanShuxingLevel copy(); // deep clone
	public HuoBanShuxingLevel toData(); // a Data instance
	public HuoBanShuxingLevel toBean(); // a Bean instance
	public HuoBanShuxingLevel toDataIf(); // a Data instance If need. else return this
	public HuoBanShuxingLevel toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobans(); // key 为等级id,value为等级对应的伙伴信息
	public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobansAsData(); // key 为等级id,value为等级对应的伙伴信息

}
