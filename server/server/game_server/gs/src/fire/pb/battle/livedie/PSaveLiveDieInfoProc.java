package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import fire.pb.util.DateValidate;


public class PSaveLiveDieInfoProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	
	public final xbean.LDVideoRoleInfoDes  lDVideoRoleInfoDes;
	public PSaveLiveDieInfoProc(xbean.LDVideoRoleInfoDes  lDVideoRoleInfoDes){
		this.lDVideoRoleInfoDes=lDVideoRoleInfoDes;
	}
	public boolean process(){
		//同步数据库
		xbean.LDVideoRoleInfoDesList lDVideoRoleInfoDesList=xtable.Ldvideoroleinfodeslisttab.get(1);
		if(lDVideoRoleInfoDesList==null){
			lDVideoRoleInfoDesList =xbean.Pod.newLDVideoRoleInfoDesList();
			xtable.Ldvideoroleinfodeslisttab.insert(1, lDVideoRoleInfoDesList);
		}
		lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall().add(lDVideoRoleInfoDes);
		
		List<xbean.LDVideoRoleInfoDes> list=new ArrayList<xbean.LDVideoRoleInfoDes>();
		//排序
		list.addAll(LiveDieMange.ldvideoroleinfodeslistall.values());
		Collections.sort(list, new LiveDieComparator());
		//存储数据库
		xbean.LDVideoHisList lDVideoHisList=xtable.Ldvideohislisttab.get(1);
		if(lDVideoHisList==null){
			lDVideoHisList=xbean.Pod.newLDVideoHisList();
			xtable.Ldvideohislisttab.insert(1, lDVideoHisList);
		}
		lDVideoHisList.getLdvidehisinfo().clear();//清除数据
		int min=Math.min(PLiveDieBattleRankView.RANK_MAX_HIS, list.size());
		for(int i=0;i<min;i++){
			lDVideoHisList.getLdvidehisinfo().add(list.get(i).getVideoid());  //记录生死战历史排行榜
		}
//		
//		//清理数据
//		List<String> delvideos=new ArrayList<String>();
//		for(xbean.LDVideoRoleInfoDes des:LiveDieMange.ldvideoroleinfodeslistall.values()){
//			if(des!=null){
//				if(!DateValidate.inTheSameWeek(des.getFighttime(), time)){
//					if(lDVideoHisList!=null){
//						if(lDVideoHisList.getLdvidehisinfo().contains(des.getVideoid())){//历史里没有的,只能一周清理一次，包括玩家的
//							continue;
//						}
//					}
//					delvideos.add(des.getVideoid());
//				}
//			}
//		}
//		//删除记录
//		for(String video:delvideos){
//			LiveDieMange.ldvideoroleinfodeslistall.remove(video);
//		}
//		//同步数据库
//		lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall().clear();
//		lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall().addAll(LiveDieMange.ldvideoroleinfodeslistall.values());
		
		return true;
	}

}
