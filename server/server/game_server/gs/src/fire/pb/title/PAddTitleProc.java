package fire.pb.title;

import java.util.ArrayList;
import java.util.List;


//添加称谓的存储过程(只能在本模块中调用，不对外部模块开放)
public class PAddTitleProc extends mkdb.Procedure {
	private long roleid;
	private int titleid;
	private String name;
	private long availtime;
	

	public PAddTitleProc(final long roleid, final int titleid, final String name, final long availtime){
		this.roleid = roleid;
		this.titleid = titleid;
		this.name = name;
		this.availtime = availtime;
	}
	
	@Override
	protected boolean process(){
		Title title = new Title(roleid, false);
		if (title.addTitle(titleid, name, availtime)) {
			List<String> parameters = new ArrayList<String>();
			if (name == null || name.equals("")) {
				parameters.add(fire.pb.title.TitleManager.getTitleConfigById(titleid).titlename);
			} else {
				parameters.add(name);
			}
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 143001, parameters);
			return true;
		}
		return false;
	}
}
