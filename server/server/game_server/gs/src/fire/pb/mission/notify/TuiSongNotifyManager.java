package fire.pb.mission.notify;

import gnet.link.Onlines;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.activity.impexam.ImpExamManager;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.SNotifyTuiSongList;
import fire.pb.npc.ImpExamType;
import fire.pb.util.DateValidate;


public class TuiSongNotifyManager {
	
	static class SingletonHolder {
		static TuiSongNotifyManager singleton = new TuiSongNotifyManager();
	}

	public static TuiSongNotifyManager getInstance() {
		return SingletonHolder.singleton;
	}
	
	public static final int MONEY_TREE=4;
	public static final int IMPEXAM_PROV = 16;
	public static final int OFFLINE_PK = 17;
	
	public void roleLogin(final long roleId){
		new mkdb.Procedure(){
			
			@Override
			protected boolean process() throws Exception {
				return true;
			}
		}.submit();
	}
	
	public void dealKeju(long roleId, long cur, Set<Integer> set){
		
		xbean.ImpExamRecord record = xtable.Role2impexam.select(roleId);
		if(record == null){
			return;
		}
		int kejuType = ImpExamManager.getInstance().isInImpExamTime();
		if(kejuType != ImpExamType.IMPEXAM_PROV ){
			return;
		}
		
		if(!DateValidate.inTheSameWeek(cur, record.getBegintime())){
			return;
		}
		
		if(DateValidate.inTheSameDay(cur, record.getBegintime())){
			int flag = record.getFlag();
			if(flag == 1){
				set.add(IMPEXAM_PROV);
			}
		}else{
			set.add(IMPEXAM_PROV);
		}
	}
	
	public void dealXiaKe(long roleId, int roleLevel, long cur, xbean.RoleFutureNotifyMap futureMap, Set<Integer> set){
		
		int taskstate = new MissionColumn(roleId, true).getMissionState(102018);
		if(taskstate == MissionStatus.COMMITED || roleLevel>=35){
		}else{
			return;
		}
	}
	
	public void startTimerTask(long roleId, int notifyType, int key, long time){
		xbean.RolesNotifyMap rolesMap = xtable.Notify.get(1);
		if(rolesMap == null){
			rolesMap = xbean.Pod.newRolesNotifyMap();
			xtable.Notify.insert(1, rolesMap);
		}
		
		xbean.RoleFutureNotifyMap futureMap = rolesMap.getRolesfuturemap().get(roleId);
		if(futureMap == null){
			futureMap = xbean.Pod.newRoleFutureNotifyMap();
			rolesMap.getRolesfuturemap().put(roleId, futureMap);
		}

		ScheduledFuture<?> f = futureMap.getNotifymap().remove(key);
		if(f != null && !f.isCancelled()){
			f.cancel(true);
		}
		//启动定时器啊
		ScheduledFuture<?> future = mkdb.Mkdb.getInstance().getExecutor().schedule(new NotifyTask(roleId, notifyType), time, TimeUnit.MILLISECONDS);
		futureMap.getNotifymap().put(key, future);
	}
	
	
	
	public void roleLogout(final long roleId){
		
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.RolesNotifyMap rolesMap = xtable.Notify.get(1);
				if(rolesMap == null){
					return false;
				}
				xbean.RoleFutureNotifyMap futureMap = rolesMap.getRolesfuturemap().remove(roleId);
				if(futureMap == null){
					return false;
				}
				
				for(java.util.concurrent.ScheduledFuture<?> future: futureMap.getNotifymap().values()){
					if(future != null && !future.isCancelled()){
						future.cancel(true);
					}
				}
				return true;
			}
		}.call();
	}
	
	public void notify(int notifyId, long roleId){
		SNotifyTuiSongList send = new SNotifyTuiSongList();
		send.notifylist.add(notifyId);
		Onlines.getInstance().send(roleId, send);
	}
	
}
