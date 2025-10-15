
package xbean;

public interface RedPack extends mkdb.Bean {
	public RedPack copy(); // deep clone
	public RedPack toData(); // a Data instance
	public RedPack toBean(); // a Bean instance
	public RedPack toDataIf(); // a Data instance If need. else return this
	public RedPack toBeanIf(); // a Bean instance If need. else return this

	public long getWorldredpack(); // 

	public void setWorldredpack(long _v_); // 
}
