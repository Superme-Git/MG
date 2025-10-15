package fire.pb.effect;

public interface IEffect extends com.locojoy.base.Marshal.Marshal{
	void attach(long targetid,int targettype);	
	void dettach(long targetid,int targettype);	
}
