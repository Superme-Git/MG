
package xbean;

public interface GetRolesCallBackInst extends mkdb.Bean {
	public GetRolesCallBackInst copy(); // deep clone
	public GetRolesCallBackInst toData(); // a Data instance
	public GetRolesCallBackInst toBean(); // a Bean instance
	public GetRolesCallBackInst toDataIf(); // a Data instance If need. else return this
	public GetRolesCallBackInst toBeanIf(); // a Bean instance If need. else return this

	public fire.msp.IGetRolesCallBack getCallback(); // 

	public void setCallback(fire.msp.IGetRolesCallBack _v_); // 
}
