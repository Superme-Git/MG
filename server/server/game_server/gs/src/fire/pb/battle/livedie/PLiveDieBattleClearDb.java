package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;
import fire.pb.PropRole;
import fire.pb.util.DateValidate;

public class PLiveDieBattleClearDb extends Procedure {

	public PLiveDieBattleClearDb() {
		
	}

	@Override
	protected boolean process() throws Exception {
		//清理数据
		long time=System.currentTimeMillis();
		List<String> delvideos=new ArrayList<String>();
		xbean.LDVideoHisList lDVideoHisList=xtable.Ldvideohislisttab.select(1);
		for(xbean.LDVideoRoleInfoDes des:LiveDieMange.ldvideoroleinfodeslistall.values()){
			if(des!=null){
				if(!DateValidate.inTheSameWeek(des.getFighttime(), time)){
					if(lDVideoHisList!=null){
						if(lDVideoHisList.getLdvidehisinfo().contains(des.getVideoid())){//历史里没有的,只能一周清理一次，包括玩家的
							continue;
						}
					}
					delvideos.add(des.getVideoid());
				}
			}
		}
		
		List<xbean.LDVideoRoleInfoDes> delmap=new ArrayList<xbean.LDVideoRoleInfoDes>();
		
		xbean.LDVideoRoleInfoDesList lDVideoRoleInfoDesList=xtable.Ldvideoroleinfodeslisttab.get(1);
		if(lDVideoRoleInfoDesList!=null){
			for(xbean.LDVideoRoleInfoDes lDVideoRoleInfoDes:lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall()){
				if(delvideos.contains(lDVideoRoleInfoDes.getVideoid())){
					delmap.add(lDVideoRoleInfoDes);
				}
			}
			lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall().removeAll(delmap);
		}
		//删除记录
		for(String video:delvideos){
			LiveDieMange.ldvideoroleinfodeslistall.remove(video);
		}
		
		return true;
	}

	

}
