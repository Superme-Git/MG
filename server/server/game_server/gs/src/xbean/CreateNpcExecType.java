
package xbean;

public interface CreateNpcExecType extends mkdb.Bean {
	public CreateNpcExecType copy(); // deep clone
	public CreateNpcExecType toData(); // a Data instance
	public CreateNpcExecType toBean(); // a Bean instance
	public CreateNpcExecType toDataIf(); // a Data instance If need. else return this
	public CreateNpcExecType toBeanIf(); // a Bean instance If need. else return this

	public fire.msp.GMCreateNpcExec getExecinstance(); // 

	public void setExecinstance(fire.msp.GMCreateNpcExec _v_); // 
}
