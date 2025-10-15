package fire.pb.gm;

import java.util.Map.Entry;

import mkdb.Procedure;
import fire.pb.FunOpenClose;
import fire.pb.FunOpenCloseType;
import fire.pb.SSetFunOpenClose;

public class GM_openfun extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int fun=Integer.parseInt(args[0]);
		final int value=Integer.parseInt(args[1]);  //0开启   1关闭功能
		final long roleid=getGmroleid();
		
		if(fun!=FunOpenCloseType.FUN_REDPACK
				&&fun!=FunOpenCloseType.FUN_JHM
				&&fun!=FunOpenCloseType.FUN_CHECKPOINT
				&&fun!=FunOpenCloseType.FUN_MONTHCARD
				&&fun!=FunOpenCloseType.FUN_BLACKMARKET){
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		
		if(value!=0&&value!=1){
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.FunOpenClose funopen=xtable.Funopenclosetab.get(1);
				if(funopen==null){
					funopen=xbean.Pod.newFunOpenClose();
					xtable.Funopenclosetab.insert(1, funopen);
				}
				java.util.Map<Integer, Integer> funmap=funopen.getFunmap();
				funmap.put(fun, value);
				
				//不发送更新了，让客户端重新登录就可以
//				SSetFunOpenClose sSetFunOpenClose=new SSetFunOpenClose();
//				for(Entry<Integer, Integer> map:funmap.entrySet()){
//					FunOpenClose funOpenClose=new FunOpenClose();
//					funOpenClose.key=map.getKey();
//					funOpenClose.state=map.getValue();
//					sSetFunOpenClose.info.add(funOpenClose);
//				}
//				Procedure.psendWhileCommit(roleid, sSetFunOpenClose);
				
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "openfun funtype value";
	}

}
