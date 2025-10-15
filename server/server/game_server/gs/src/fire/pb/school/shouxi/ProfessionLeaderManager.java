package fire.pb.school.shouxi;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.game.MoneyType;
import fire.pb.map.SceneManager;
import fire.pb.npc.NpcServices;
import fire.pb.school.SSendCandidates;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.school.Shouxi;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.title.Title;
import fire.pb.util.MessageUtil;

import org.apache.log4j.Logger;

/***
 * 职业领袖管理器 by changhao
 *
 */
public class ProfessionLeaderManager {
	private Logger logger = Logger.getLogger(ProfessionLeaderManager.class);
	private long challengeStartTime = 0;// 挑战的开始时间
	private long challengeEndTime = 0;// 挑战的结束时间
	private long voteStartTime = 0;// 投票的结束时间
	private long voteEndTime = 0;// 投票的结束时间
    private static Map<Integer, Long> shouxiKeys = new HashMap<Integer, Long>();
    private static Map<Integer, Long> shouxiOnlineMsgTime = new HashMap<Integer, Long>(); //上次上线发门派消息的时间
    private static Map<Integer, Long> shouxiOfflineMsgTime = new HashMap<Integer, Long>();
	private ProfessionLeaderManager() {
	}

	private static ProfessionLeaderManager instance = new ProfessionLeaderManager();

	public static ProfessionLeaderManager getInstance() {
		
		return instance;
	}

	public long getChallengeStartTime() {
		return challengeStartTime;
	}

	public void setChallengeStartTime(long challengeStartTime) {
		this.challengeStartTime = challengeStartTime;
	}

	public long getChallengeEndTime() {
		return challengeEndTime;
	}

	public void setChallengeEndTime(long challengeEndTime) {
		this.challengeEndTime = challengeEndTime;
	}

	public long getVoteStartTime() {
		return voteStartTime;
	}

	public void setVoteStartTime(long voteStartTime) {
		this.voteStartTime = voteStartTime;
	}

	public long getVoteEndTime() {
		return voteEndTime;
	}

	public void setVoteEndTime(long voteEndTime) {
		this.voteEndTime = voteEndTime;
	}
	
	/***
	 * 计算职业领袖挑战时间 by changhao
	 * @return
	 */
	public synchronized boolean setChallengeTime()
	{
		Calendar cal = Calendar.getInstance();

		//周四的23:59:50 分 by changhao
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal.add(Calendar.DAY_OF_MONTH, -3);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 50);
		}
		else
		{
			int c = cal.get(Calendar.DAY_OF_WEEK);
			int tmp = 7 - c - 2;
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 50);
		}

		long endTime = cal.getTimeInMillis();
		
		setChallengeEndTime(endTime);
		
		long startTime = endTime - 4 * 24 * 3600 * 1000 + 10 * 1000;
		setChallengeStartTime(startTime);
		
		return true;
	}

	/***
	 * 计算职业领袖投票时间 by changhao
	 * @return
	 */
	public synchronized boolean setVoteTime()
	{
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		//周日的23:57分 by changhao
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			cal2.set(Calendar.HOUR_OF_DAY, 23);
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 50);
			if (cal.after(cal2))
			{
				cal.add(Calendar.DAY_OF_MONTH, 7);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 50);
			}
			else
			{
				cal = cal2;
			}
		}
		else
		{
			int tmp = 7 - (cal.get(Calendar.DAY_OF_WEEK) - 1);
			cal.add(Calendar.DAY_OF_MONTH, tmp);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 50);
		}

		long endTime = cal.getTimeInMillis();
		
		setVoteEndTime(endTime);
		
		long startTime = endTime - 3 * 24 * 3600 * 1000 + 10 * 1000;
		setVoteStartTime(startTime);
		
		return true;
	}

	/**
	 * 计算下周的职业领袖投票和挑战时间 by changhao
	 */
	public synchronized boolean adjustVoteChallengeTime()
	{
		long cur = System.currentTimeMillis();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cur);
		
		long endTime = getVoteEndTime() + 7 * 24 * 3600 * 1000;
		setVoteEndTime(endTime);
		long startTime = getVoteStartTime() + 7 * 24 * 3600 * 1000;
		setVoteStartTime(startTime);
		
		endTime = getChallengeEndTime() + 7 * 24 * 3600 * 1000;
		setChallengeEndTime(endTime);
		
		startTime = getChallengeStartTime() + 7 * 24 * 3600 * 1000;
		setChallengeStartTime(startTime);
		
		return true;
	}

	/**
	 * 判断当前时间是否能投票
	 * 
	 * @param time 这次投票的时间
	 * @return
	 */
	public boolean checkCanVote(long time) {
		
		if (voteStartTime < time && time < voteEndTime)
			return true;
		return false;
	}
	/**
	 * 玩家在本周内是否已经投票了 by changhao
	 * @param roleid
	 * @return
	 */
	public boolean checkAlreadyVote(long roleid){
		//玩家在本周内是否已经投票了
		xbean.ProfessionLeaderVoteInfo voteInfo=xtable.Professionleadervotechallenge.select(roleid);
		if(voteInfo != null)
		{
			long lastVoteTime = voteInfo.getVotetime();
			//如果上次投票时间也在voteStartTime和voteEndTime之间,说明已经投票了,不能再投 by changhao
			if(voteStartTime < lastVoteTime && lastVoteTime < voteEndTime)
				return true;
		}
		
		return false;
	}
	/**
	 * 根据roleid从xdb表中获取首席弟子的得票信息
	 * @param roleid
	 * @param school
	 * @return
	 */
	
	public xbean.ProfessionLeaderTicketInfo getCandInfoFromRoleid(long roleid,int school){
		  xbean.ProfessionLeaderCand shouxiCand=xtable.Professionleadercandidates.select(school);
		  if(shouxiCand==null)
			  return null;
	        List<Long> candIDs=shouxiCand.getCandidatelist();
	        xbean.ProfessionLeaderTicketInfo candInfo=null;
	        for (long element : candIDs) {
				if(element==roleid){
					candInfo=xtable.Roleid2professionleadertickets.select(element);
					break;
				}
			}
		return candInfo;
	}
	
	/***
	 * 发送职业领袖候选人 by changhao
	 */
	public void refreshCandidateswithSP(long roleid,int school,long npckey) 
	{
		SSendCandidates ssc = new SSendCandidates();
		xbean.ProfessionLeaderCand candidates = xtable.Professionleadercandidates.select(school);
		
		if(candidates != null)
		{
			java.util.List<Long> cans = candidates.getCandidatelist();
			for (long candID : cans)
			{
				xbean.ProfessionLeaderTicketInfo candInfo = xtable.Roleid2professionleadertickets.select(candID);
				fire.pb.school.CandidateInfo info = new fire.pb.school.CandidateInfo();
				info.roleid = candInfo.getRoleid();
				info.tickets = candInfo.getTickets();
				info.words = candInfo.getWords();
				String name = xtable.Properties.selectRolename(info.roleid);
				info.rolename = name;
				info.shape = xtable.Properties.selectShape(info.roleid);
				ssc.candidatelist.add(info);
			}
			
			if(checkAlreadyVote(roleid))
				ssc.alreadyvote=1;
		}
		
		ssc.shouxikey = npckey;
		gnet.link.Onlines.getInstance().send(roleid, ssc);
	}
	/**
	 * 判断当前时间是否能挑战首席
	 * @param time
	 * @return
	 */
    public boolean isInChallengePeriod(long time)
    {
    	
    	
    	if (challengeStartTime < time && time < challengeEndTime)
			return true;
		return false;
    }

	public static void addDynamicServices(long roleid, long npckey,int npcid, List<Integer> result) {
		School npcSchool = npcIsShouxi(npckey,npcid);
		if(npcSchool == null)
			return;
		
		PropRole propRole=new PropRole(roleid,true);
		int school=propRole.getSchool();
		//如果玩家的门派和npc的门派不一致,返回
		if(school!=npcSchool.getValue())
			return;		
		
		School s = School.getSchoolBySchoolid(propRole.getProperties().getSchool());		
		int oneleveltitle = School.getTitleIdBySchool(1, propRole.getProperties().getSex(), s);
		int twoleveltitle = School.getTitleIdBySchool(2, propRole.getProperties().getSex(), s);
		int threeleveltitle = School.getTitleIdBySchool(3, propRole.getProperties().getSex(), s);
		
		Title title = new Title(roleid, true);
		
		if (propRole.getLevel() >= 70 && !title.roleHaveTitle(threeleveltitle) && title.roleHaveTitle(twoleveltitle))
		{
			result.add(NpcServices.THREE_LIEVEL_TITLE);
		}
		
		if (propRole.getLevel() >= 50 && !title.roleHaveTitle(twoleveltitle) && title.roleHaveTitle(oneleveltitle))
		{
			result.add(NpcServices.TWO_LIEVEL_TITLE);
		}
		
		if (propRole.getLevel() >= 30 && !title.roleHaveTitle(oneleveltitle))
		{
			result.add(NpcServices.ONE_LIEVEL_TITLE);
		}
		
		xbean.ProfessionLeaderVoteInfo voteInfo = xtable.Professionleadervotechallenge.select(roleid);
		
		boolean b = false;
		if (voteInfo != null)
		{
			b = ProfessionLeaderManager.getInstance().isInChallengePeriod(voteInfo.getChallengetime());
		}
		
		xbean.ProfessionLeaderInfo shouxiInfo=xtable.Professionleader.select(school);
		if(shouxiInfo==null||shouxiInfo.getRoleid()!=roleid)
		{			
			if (ProfessionLeaderManager.getInstance().checkCanVote(System.currentTimeMillis())) //如果在投票时间 by changhao
			{
				//已经参加竞选 可以修改宣言 by changhao
				xbean.ProfessionLeaderTicketInfo c = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
				if (c != null)
				{
					result.add(NpcServices.LEADER_MY_CAMPAIGN);
				}
			
				if (propRole.getLevel() >= 50)
					result.add(NpcServices.LEADER_SEE_CAMPAIGN_LIST);
			}
			
			if(ProfessionLeaderManager.getInstance().isInChallengePeriod(System.currentTimeMillis())) //如果在挑战时间  by changhao
			{							
				if (b == true) //已经挑战过了 by changhao
				{
					//已经参加竞选 可以修改宣言 by changhao
					xbean.ProfessionLeaderTicketInfo c = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
					if (c != null)
					{
						result.add(NpcServices.LEADER_MY_CAMPAIGN);	
					}
					else
					{
						result.add(NpcServices.LEADER_CAMPAIGN);			
					}				
				}
				else
				{
					if (propRole.getLevel() >= 50)
						result.add(NpcServices.LEADER_CHALLENGE);					
				}
			}
		}
		else
		{	
			b = true;
			if(ProfessionLeaderManager.getInstance().checkCanVote(System.currentTimeMillis())) //如果在投票时间 by changhao
			{
				result.add(NpcServices.LEADER_SEE_CAMPAIGN_LIST);	
				
				//已经参加竞选 可以修改宣言 by changhao
				xbean.ProfessionLeaderTicketInfo c = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
				if (c != null)
				{
					result.add(NpcServices.LEADER_MY_CAMPAIGN);
				}
			}
			
			if(ProfessionLeaderManager.getInstance().isInChallengePeriod(System.currentTimeMillis())) //如果在挑战时间  by changhao
			{							
				if (b == true) //已经挑战过了 by changhao
				{
					//已经参加竞选 可以修改宣言 by changhao
					xbean.ProfessionLeaderTicketInfo c = fire.pb.school.shouxi.ProfessionLeaderManager.getInstance().getCandInfoFromRoleid(roleid, school);
					if (c != null)
					{
						result.add(NpcServices.LEADER_MY_CAMPAIGN);	
					}
					else
					{
						result.add(NpcServices.LEADER_CAMPAIGN);			
					}				
				}
			}
		}
	}

	private static School npcIsShouxi(long npckey,int npcid) {
		//int npcid=SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		for (Shouxi shouxi : Shouxi.values()) {
			if(shouxi.getValue()==npcid){
				School school = School.getSchoolByNpcid(npcid);
				if (shouxiKeys.get(school.getValue())==null) 
				shouxiKeys.put(school.getValue(), npckey);
				return school;
			}
		}
		return null;
	}
	public static boolean roleIsShouxi(long roleid){
		PropRole role = new PropRole(roleid, true);
		xbean.ProfessionLeaderInfo sxi=xtable.Professionleader.select(role.getSchool());
		if (sxi!=null){
			if(sxi.getRoleid()==roleid)
				return true;
		}
		return false;
	}
	//online为true表示上线,否则为下线
	public static void sendShouxiOnLineOfflineMsg(long roleid,boolean Online){
		if (roleIsShouxi(roleid)) {
			PropRole role = new PropRole(roleid, true);
			int school = role.getSchool();
			int sex=role.getSex();
			int msgid=0;
			long currentTime = System.currentTimeMillis();
			if (sex==1){//如果是男的
				if (Online){
					Long lastTime=shouxiOnlineMsgTime.get(school);
					if (lastTime!=null&&currentTime-lastTime<600000) 
						return;
					msgid = SchoolConst.MALE_SHOUXI_ONLINE;
					shouxiOnlineMsgTime.put(school, currentTime);
				}else {
					Long lastTime=shouxiOfflineMsgTime.get(role.getSchool());
					if (lastTime!=null&&currentTime-lastTime<600000) 
						return;
					msgid = SchoolConst.MALE_SHOUXI_OFFLINE;
					shouxiOfflineMsgTime.put(school, currentTime);
				}
			}else {//如果是女的
				if (Online){
					Long lastTime=shouxiOnlineMsgTime.get(role.getSchool());
					if (lastTime!=null&&currentTime-lastTime<600000) 
						return;
					msgid = SchoolConst.FEMALE_SHOUXI_ONLINE;
					shouxiOnlineMsgTime.put(school, currentTime);
				}else {
					Long lastTime=shouxiOfflineMsgTime.get(role.getSchool());
					if (lastTime!=null&&currentTime-lastTime<600000) 
						return;
					msgid = SchoolConst.FEMALE_SHOUXI_OFFLINE;
					shouxiOfflineMsgTime.put(school, currentTime);
				}
			}
			STransChatMessageNotify2Client ssm = MessageMgr.getMsgNotify(msgid, 0, MessageUtil.getMsgParaList(role.getName()));
			SceneManager.psendAllBySchoolWhileCommit(ssm, role.getSchool());
		}
	}
	public static long getShouxiKey(int school){
		return shouxiKeys.get(school);
	}
	
	public int GetBattleId(int school)
	{
		if (school == 1)
		{
			
		}
		
		return 1;
	}
	
}
