package fire.pb;

import java.util.Date;

import org.apache.log4j.Logger;

import fire.pb.battle.livedie.PClearLiveDieRoseCountProc;
import fire.pb.fushi.redpack.PClearRedPackCountProc;

/**
 * 登陆的时候处理玩家每日更新事件
 *
 */
public class PLoginDayUpdatesProc extends mkdb.Procedure{
	public static final Logger LOG = Logger.getLogger("SYSTEM");
	private long roleId;
	
	public PLoginDayUpdatesProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.RoleUpdateTime roleUpdateTime=xtable.Roleupdatetimetab.get(roleId);
		if(roleUpdateTime==null){
			roleUpdateTime=xbean.Pod.newRoleUpdateTime();
			xtable.Roleupdatetimetab.insert(roleId, roleUpdateTime);
		}
		Date everyday=null;
		if(roleUpdateTime.getDateupdatetime()>0){
			everyday=new Date(roleUpdateTime.getDateupdatetime());//玩家更新时间
		}
		
		long preTime,curentTime,timeArea1=0,timeArea2=0;
		long timeArea=24*60*60*1000;
//		long timeKye= 8*60*60*1000-4*60*60*1000;//凌晨4点
		long timeKye= 8*60*60*1000;//0点
        
        curentTime = System.currentTimeMillis()+timeKye;
        if (everyday == null){
        	roleUpdateTime.setDateupdatetime(curentTime);
        	return true;//这里返回true也是为了让他保存一下数据
        }
        //上次更新时间
        preTime = roleUpdateTime.getDateupdatetime()+timeKye;

        timeArea1 = curentTime-(curentTime%timeArea);
        timeArea2 = timeArea1 + timeArea;
        
        if ((timeArea1<=preTime)&&(preTime<timeArea2)){
        	return true;
        }else{
        	//更新
        	try {
        		//更新玩家每天购买药房物品数量
        		pexecute(new PLoginDayUpdateMedicItemProc(roleId));
        		//更新公会玩家请求次数信息
        		pexecute(new PLoginDayUpdateRuneNumProc(roleId));
//        		pexecute(new PClearHelpCountProc(roleId,PClearHelpCountProc.UPDATE_DAY));//处理援助统计
        		//处理生死战点赞次数
        		pexecute(new PClearLiveDieRoseCountProc(roleId));
        		//清理玩家每天发送和接受红包数
        		pexecute(new PClearRedPackCountProc(roleId));
        		//清理玩家一些每日计数信息
        		pexecute(new PLoginDayUpdateModelNumProc(roleId));
        		
        		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error("", e);
			}
        	roleUpdateTime.setDateupdatetime(System.currentTimeMillis());
			LOG.info("角色id\t"+roleId+"--------每日更新");
        }
	
		
		
		
		
		
		return true;
	}
	
}
