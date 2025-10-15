

package fire.pb.util;

import fire.pb.circletask.ActiveQuestData;
import fire.pb.circletask.SRefreshActiveQuest;
import fire.pb.circletask.SRefreshSpecialQuestState;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.mission.Module;
import mkdb.Procedure;


public class TaskDlgUtil {

	public static class NpcModel {

		private final long npckey;

		private final int npcid;

		private final int mapid;

		private final int posx;

		private final int posy;

		private NpcModel(long npckey, int npcid, int mapid, int posx, int posy) {

			super();
			this.npckey = npckey;
			this.npcid = npcid;
			this.mapid = mapid;
			this.posx = posx;
			this.posy = posy;
		}

		public long getNpckey() {

			return npckey;
		}

		public int getNpcid() {

			return npcid;
		}

		public int getMapid() {

			return mapid;
		}

		public int getPosx() {

			return posx;
		}

		public int getPosy() {

			return posy;
		}

	}
public static NpcModel createNpcModel(long npckey, int npcid, int mapid, int posx, int posy){
	return new NpcModel(npckey, npcid, mapid, posx, posy);
}
	
	public static void createAndRefreshTaskDlg(long roleid, int taskid, NpcModel npc, int sumnum, int round, int taskState, long remainTime) {

		xbean.TaskInfos infos = xtable.Roletasks.get(roleid);
		if (infos == null) {
			infos = xbean.Pod.newTaskInfos();
			xtable.Roletasks.insert(roleid, infos);
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			sqi = xbean.Pod.newTaskDlgInfo();
			infos.getTasksmap().put(taskid, sqi);
		}

		sqi.setTaskid(taskid);
		if (npc != null) {
			sqi.setDstmapid(npc.getMapid());
			sqi.setDstnpcid(npc.getNpcid());
			sqi.setDstnpckey(npc.getNpckey());
			sqi.setDstx(npc.getPosx());
			sqi.setDsty(npc.getPosy());

		}
		sqi.setTaskstate(taskState);
		sqi.setSumnum(sumnum);
		sqi.setRound(round);
		if (remainTime > 0)
			sqi.setEndtime(System.currentTimeMillis() + remainTime);
		// 加上任务追踪
		Module.getInstance().trackAccpetMission(roleid, taskid);
		ActiveQuestData aqd = new ActiveQuestData();
		aqd.questid = taskid;

		aqd.dstmapid = sqi.getDstmapid();
		aqd.dstnpcid = sqi.getDstnpcid();
		aqd.dstnpckey = sqi.getDstnpckey();
		aqd.dstx = sqi.getDstx();
		aqd.dsty = sqi.getDsty();

		if (remainTime > 0)
			aqd.rewardmoney = sqi.getEndtime();
		aqd.queststate = sqi.getTaskstate();
			aqd.sumnum = sumnum;
		SRefreshActiveQuest sraq = new SRefreshActiveQuest();
		sraq.questdata = aqd;
		Procedure.psendWhileCommit(roleid, sraq);
	}
	
	
	public static void createAndRefreshTaskDlg(long roleid, int taskid, NpcModel npc, int sumnum, int round, long remainTime) {

		xbean.TaskInfos infos = xtable.Roletasks.get(roleid);
		if (infos == null) {
			infos = xbean.Pod.newTaskInfos();
			xtable.Roletasks.insert(roleid, infos);
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			sqi = xbean.Pod.newTaskDlgInfo();
			infos.getTasksmap().put(taskid, sqi);
		}

		sqi.setTaskid(taskid);
		if (npc != null) {
			sqi.setDstmapid(npc.getMapid());
			sqi.setDstnpcid(npc.getNpcid());
			sqi.setDstnpckey(npc.getNpckey());
			sqi.setDstx(npc.getPosx());
			sqi.setDsty(npc.getPosy());

		}
		sqi.setSumnum(sumnum);
		sqi.setRound(round);
		if (remainTime > 0)
			sqi.setEndtime(System.currentTimeMillis() + remainTime);
		// 加上任务追踪
		Module.getInstance().trackAccpetMission(roleid, taskid);
		ActiveQuestData aqd = new ActiveQuestData();
		aqd.questid = taskid;

		aqd.dstmapid = sqi.getDstmapid();
		aqd.dstnpcid = sqi.getDstnpcid();
		aqd.dstnpckey = sqi.getDstnpckey();
		aqd.dstx = sqi.getDstx();
		aqd.dsty = sqi.getDsty();

		if (remainTime > 0)
			aqd.rewardmoney = sqi.getEndtime();
		aqd.queststate = sqi.getTaskstate();
			aqd.sumnum = sumnum;
		SRefreshActiveQuest sraq = new SRefreshActiveQuest();
		sraq.questdata = aqd;
		Procedure.psendWhileCommit(roleid, sraq);
	}

	
	public static void createAndRefreshItemTaskDlg(long roleid, int taskid, int npcid, int itemid, int itemnum, int mapid, int posx, int posy, int round,
			int taskState) {

		xbean.TaskInfos infos = xtable.Roletasks.get(roleid);
		if (infos == null) {
			infos = xbean.Pod.newTaskInfos();
			xtable.Roletasks.insert(roleid, infos);
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			sqi = xbean.Pod.newTaskDlgInfo();
			infos.getTasksmap().put(taskid, sqi);
		}

		sqi.setDstnpcid(npcid);

		sqi.setTaskid(taskid);
		sqi.setDstitemid(itemid);
		sqi.setDstitemnum(itemnum);
		sqi.setDstmapid(mapid);
		sqi.setDstx(posx);
		sqi.setDsty(posy);

		sqi.setTaskstate(taskState);
		sqi.setSumnum(itemnum);
		sqi.setRound(round);
		// 加上任务追踪
		Module.getInstance().trackAccpetMission(roleid, taskid);
		ActiveQuestData aqd = new ActiveQuestData();
		aqd.questid = taskid;

		aqd.dstitemid = itemid;
		aqd.sumnum = sqi.getSumnum();
		aqd.dstmapid = mapid;
		aqd.dstx = posx;
		aqd.dsty = posy;

		aqd.queststate = sqi.getTaskstate();
		aqd.rewardsmoney = round;
		// aqd.sumnum = sumnum;
		SRefreshActiveQuest sraq = new SRefreshActiveQuest();
		sraq.questdata = aqd;
		Procedure.psendWhileCommit(roleid, sraq);
	}

	// procedure中调用
	public static void refreshTaskState(long roleid, int taskid, int state) {

		xbean.TaskInfos infos = xtable.Roletasks.get(roleid);
		if (infos == null) {
			return;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			return;
		}
		if (sqi.getTaskstate() == SpecialQuestState.SUCCESS || sqi.getTaskstate() == SpecialQuestState.ABANDONED || sqi.getTaskstate() == SpecialQuestState.INSTANCE_ABANDONED)
			return;
		sqi.setTaskstate(state);
		// 取消任务追踪
		if (state == SpecialQuestState.SUCCESS || state == SpecialQuestState.ABANDONED || state == SpecialQuestState.FAIL || sqi.getTaskstate() == SpecialQuestState.INSTANCE_ABANDONED) {
			Module.getInstance().untrackMission(roleid, taskid);
		}
		final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
		sendstate.questid = taskid;
		sendstate.state = state;
		mkdb.Procedure.psendWhileCommit(roleid, sendstate);
	}

	
	public static boolean existTask(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos == null) {
			return false;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			return false;
		}
		if (sqi.getTaskstate() == SpecialQuestState.DONE || sqi.getTaskstate() == SpecialQuestState.UNDONE)
			return true;

		return false;
	}

	public static boolean existDoneUndoneFailTask(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos == null) {
			return false;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			return false;
		}
		if (sqi.getTaskstate() == SpecialQuestState.DONE || sqi.getTaskstate() == SpecialQuestState.UNDONE || sqi.getTaskstate() == SpecialQuestState.FAIL)
			return true;

		return false;
	}

	public static xbean.TaskDlgInfo selecTaskDlgInfo(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos == null) {
			return null;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		return sqi;
	}

	public static xbean.TaskDlgInfo getTaskDlgInfo(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.get(roleid);
		if (infos == null) {
			return null;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		return sqi;
	}

	public static boolean existUndoneTask(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos == null) {
			return false;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi != null && sqi.getTaskstate() == SpecialQuestState.UNDONE)
			return true;
		return false;
	}

	// 如果找不到任务,返回0
	public static int getTaskState(long roleid, int taskid) {

		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos == null) {
			return 0;
		}
		xbean.TaskDlgInfo sqi = infos.getTasksmap().get(taskid);
		if (sqi == null) {
			return 0;
		}
		return sqi.getTaskstate();
	}

}
