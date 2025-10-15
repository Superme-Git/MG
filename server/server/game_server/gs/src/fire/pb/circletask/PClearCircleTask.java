package fire.pb.circletask;

import java.util.Calendar;

import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.SpecialQuestState;


public class PClearCircleTask extends mkdb.Procedure {
	private Long rid;
	private boolean unit; // 标识是 一天结束后的统一的操作，还是玩家上线时的单一的操作。
	private boolean isLogin;//是否登录使用
	
	public PClearCircleTask(Long roleid, boolean unit){
		this.unit = unit;
		this.rid = roleid;
		this.isLogin = false;
	}
	
	public PClearCircleTask(Long roleid, boolean unit, boolean islogin){
		this.unit = unit;
		this.rid = roleid;
		this.isLogin = islogin;
	}
	
	public boolean process(){
	//	this.lock(xtable.Locks.ROLELOCK, roleids); 一次锁很多锁会死锁,注掉这行,刷新任务放到多个procedure里执行
	//	for (final Long rid : roleids){
			pexecuteWhileCommit(new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					final xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(rid);
					if (quest_map == null)
						return true;
					
					// 对于循环师门任务的处理
					for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
						final xbean.CircleTaskInfo ctsqinfo = quest_map.getTaskmap().get(curType);
						if (ctsqinfo == null) continue;

						final CircTask ctask = CircleTaskManager.getInstance().getCircTask(ctsqinfo.getId());
						if (ctask == null) {
							CircleTaskManager.logger.error("角色[" + rid + "]x循环任务配置表:" + ctsqinfo.getId() + ",未找到数据!");
							continue;
						}
						
						final long now = Calendar.getInstance().getTimeInMillis();
						
						final CircTask ct = CircleTaskManager.getInstance().getCircTask(ctsqinfo.getId());
						if (ct == null) {
							continue;
						}
						
						if (unit){
							if (ctsqinfo.getQueststate() != 0 && ctsqinfo.getQueststate() != SpecialQuestState.FAIL){
								
								
								int circle = ct.getCycle();
								//判断是否清0的代码放到这里
								switch(circle) {
								case 1: {
									if (!fire.pb.util.DateValidate.inTheSameDay(ctsqinfo.getTakequesttime(), now)) {
										ctsqinfo.setSumnum(0);
										//ctsqinfo.setRound(1);
										ctsqinfo.setTakequesttime(now);
										ctsqinfo.setFirsttasktime(now);
										ctsqinfo.setCatchitsingletime(0);
										ctsqinfo.setCatchitdoubletime(0);
										if (ctask.getClearround() == 1) {
											if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
												ctsqinfo.setRound(1);
											}
											else
												ctsqinfo.setRound(0);
										}
									}
									break;
								}
								case 2: {
									if (!fire.pb.util.DateValidate.inTheSameWeek(ctsqinfo.getTakequesttime(), now)) {
										ctsqinfo.setSumnum(0);
										//ctsqinfo.setRound(1);
										ctsqinfo.setTakequesttime(now);
										ctsqinfo.setFirsttasktime(now);
										ctsqinfo.setCatchitsingletime(0);
										ctsqinfo.setCatchitdoubletime(0);
										if (ctask.getClearround() == 1) {
											if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
												ctsqinfo.setRound(1);
											}
											else
												ctsqinfo.setRound(0);
										}
									}
									break;
								}
								case 3: {
									if (!fire.pb.util.DateValidate.inTheSameMonth(ctsqinfo.getTakequesttime(), now)) {
										ctsqinfo.setSumnum(0);
										//ctsqinfo.setRound(1);
										ctsqinfo.setTakequesttime(now);
										ctsqinfo.setFirsttasktime(now);
										ctsqinfo.setCatchitsingletime(0);
										ctsqinfo.setCatchitdoubletime(0);
										if (ctask.getClearround() == 1) {
											if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
												ctsqinfo.setRound(1);
											}
											else
												ctsqinfo.setRound(0);
										}
									}
									break;
								}
								default:
									break;
								}
							}
							if(isLogin) {
								CircleTaskManager.getInstance().refresh2roleOnEnterWorld(ctsqinfo, rid);		
							} else {
								CircleTaskManager.getInstance().refresh2role(ctsqinfo, rid, false);
							}
						}
						else {
							boolean serverRestart = false; //玩家上次领循环师门,到他这次上线,中间服务器有没有重启过
							if (ctsqinfo.getTakequesttime() < fire.pb.main.Gs.getStartTime()) 
								serverRestart = true;
							if (ctsqinfo.getQueststate() == SpecialQuestState.DONE && ctsqinfo.getQuesttype()==0){//说明数据异常,清理一下
								ctsqinfo.setQueststate(0);
							}
							if (!fire.pb.util.DateValidate.inTheSameDay(ctsqinfo.getTakequesttime(),now)){
								if (serverRestart){//如果服务器重启过,且是限时任务,师门就彻底清空

								}else if (ctsqinfo.getQueststate() != 0
										&& ctsqinfo.getQueststate() != SpecialQuestState.FAIL){
									
									int circle = ct.getCycle();
									//判断是否清0的代码放到这里
									switch(circle) {
									case 1: {
										if (!fire.pb.util.DateValidate.inTheSameDay(ctsqinfo.getTakequesttime(), now)) {
											ctsqinfo.setSumnum(0);
											//ctsqinfo.setRound(1);
											ctsqinfo.setTakequesttime(now);
											ctsqinfo.setFirsttasktime(now);
											ctsqinfo.setCatchitsingletime(0);
											ctsqinfo.setCatchitdoubletime(0);
											if (ctask.getClearround() == 1) {
												if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
													ctsqinfo.setRound(1);
												}
												else
													ctsqinfo.setRound(0);
											}
										}
										break;
									}
									case 2: {
										if (!fire.pb.util.DateValidate.inTheSameWeek(ctsqinfo.getTakequesttime(), now)) {
											ctsqinfo.setSumnum(0);
											//ctsqinfo.setRound(1);
											ctsqinfo.setTakequesttime(now);
											ctsqinfo.setFirsttasktime(now);
											ctsqinfo.setCatchitsingletime(0);
											ctsqinfo.setCatchitdoubletime(0);
											if (ctask.getClearround() == 1) {
												if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
													ctsqinfo.setRound(1);
												}
												else
													ctsqinfo.setRound(0);
											}
										}
										break;
									}
									case 3: {
										if (!fire.pb.util.DateValidate.inTheSameMonth(ctsqinfo.getTakequesttime(), now)) {
											ctsqinfo.setSumnum(0);
											//ctsqinfo.setRound(1);
											ctsqinfo.setTakequesttime(now);
											ctsqinfo.setFirsttasktime(now);
											ctsqinfo.setCatchitsingletime(0);
											ctsqinfo.setCatchitdoubletime(0);
											if (ctask.getClearround() == 1) {
												if (ctsqinfo.getQueststate() == SpecialQuestState.UNDONE) {
													ctsqinfo.setRound(1);
												}
												else
													ctsqinfo.setRound(0);
											}
										}
										break;
									}
									default:
										break;
									}
								}
							}
							if(isLogin) {
								CircleTaskManager.getInstance().refresh2roleOnEnterWorld(ctsqinfo, rid);		
							} else {
								CircleTaskManager.getInstance().refresh2role(ctsqinfo, rid, false);
							}
						}
						//TODO: 不要删此日志，为了一个重现不了的bug
						fire.pb.PropRole prole = new fire.pb.PropRole(rid, false);
						CircleTaskManager.logger.info("玩家-"+prole.getName()+"("+rid+")"+"当前的循环师门总次数:"+ctsqinfo.getSumnum() + "当前等级:"+prole.getLevel());	
					}				
					return true;
				}
				
			});
		return true;
	}

}
