package fire.pb.fushi.redpack;

import org.apache.log4j.Logger;


public class PClearRedPackCountProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("REDPACK");
	private long roleid;
	
	public PClearRedPackCountProc(final long roleid){
		this.roleid = roleid;
	}
	public boolean process(){
		xbean.SRRedPackNumList list = xtable.Srredpacknumlisttab.get(roleid);
		if(list!=null){
			list.getSrredpacknumlist().clear();
		}
		return true;
	}

}
