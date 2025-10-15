package fire.pb.gm;

import java.util.Map.Entry;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.FunOpenClose;
import fire.pb.SSetFunOpenClose;

public class PFunOpenClose extends Procedure {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	
	private final long roleid;

	public PFunOpenClose(long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.FunOpenClose funopen=xtable.Funopenclosetab.select(1);
		if(funopen!=null){
			java.util.Map<Integer, Integer> funmap=funopen.getFunmap();
			SSetFunOpenClose sSetFunOpenClose=new SSetFunOpenClose();
			for(Entry<Integer, Integer> map:funmap.entrySet()){
				FunOpenClose funOpenClose=new FunOpenClose();
				funOpenClose.key=map.getKey();
				funOpenClose.state=map.getValue();
				sSetFunOpenClose.info.add(funOpenClose);
			}
			Procedure.psendWhileCommit(roleid, sSetFunOpenClose);
		}
		return true;
	}
	



	
	
	

}
