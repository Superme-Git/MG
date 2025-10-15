package fire.pb.mission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import fire.pb.friends.PFriendsInfoInit;
import fire.pb.friends.SpaceDropGift;
import fire.pb.friends.StepSpace;
import fire.pb.main.ConfigManager;
import fire.pb.mission.SAllTaskLine;
import fire.pb.mission.SMissionData;
import fire.pb.mission.SNewFunctionOpen;
import fire.pb.mission.SQuestCanAcceptList;
import fire.pb.mission.SSpecialScenarioQuestConfig;



public class MissionManager {
	private static MissionManager instance;
	public static MissionManager getInstance() {
		if ( instance == null ) {
			instance = new MissionManager( fire.pb.main.ConfigManager.getInstance() );
		}
		return instance;
	}
	Map<Integer, QuestCanAcceptList> canAcceptMissionMap = new HashMap<Integer, QuestCanAcceptList>();
	public Map<Integer, QuestCanAcceptList> getCanAcceptMissionMap() {
		return canAcceptMissionMap;
	}
	List<Integer> tuPoTask = new LinkedList<Integer>();
	public List<Integer> getTuPoTaskList() {
		return tuPoTask;
	}
	Map<Integer, Set<Integer> > nextlineMap = new HashMap<Integer, Set<Integer> >();
	public final Map<Integer, MissionConfig> missionConfMap = new HashMap<Integer, MissionConfig>();
	final Map<Integer, SchoolMissionSet > schoolMissionlineMap = new HashMap<Integer, SchoolMissionSet >();
	
	private static final class SchoolMissionSet {
		private final Set<Integer> majorMissionset = new HashSet<Integer>();
		private final Set<Integer> branchMissionset = new HashSet<Integer>();
		public void addSchoolTask( final int schooltask ) {
			final int taskid = UtilHelper.getMissionId( schooltask, 1 );
			if ( UtilHelper.isMajorScenarioMission( taskid ) ) {
				majorMissionset.add( schooltask );
			} else if ( UtilHelper.isBranchScenarioMission( taskid ) ) {
				branchMissionset.add( schooltask );
			}
		}
		public Set<Integer> getMajorTaskset() { return majorMissionset; }
		public Set<Integer> getBranchTaskset() { return branchMissionset; }
	}
	
	Map<Integer, Set<Integer> > newFuncOpenMap = new HashMap<Integer, Set<Integer> >();
	
	private MissionManager( fire.pb.main.ConfigManager cm ) {
		init( cm );
	}
	void init( fire.pb.main.ConfigManager cm ) {
		missionConfMap.clear();
		schoolMissionlineMap.clear();
		nextlineMap.clear();
		
		final java.util.NavigableMap<Integer, SMissionData> missionMap = cm.getConf( fire.pb.mission.SMissionData.class );
		Set<MissionConfig> headTasks = new HashSet<MissionConfig>();
		Set<MissionConfig> hasBrothers = new HashSet<MissionConfig>();
		Map<Integer, SSpecialScenarioQuestConfig> specailquestconfig = cm.getConf( fire.pb.mission.SSpecialScenarioQuestConfig.class );
		for ( final SMissionData mmi : missionMap.values() ) {
			MissionConfig conf = new MissionConfig(mmi, specailquestconfig);
			missionConfMap.put( mmi.id, conf );
			if ( mmi.NextMissionList != null && mmi.NextMissionList.size() > 1 ) {
				hasBrothers.add( conf );
			}
			if ( UtilHelper.getMissionStep( mmi.id ) == UtilHelper.getFirstStep() ) {
				headTasks.add( conf );
			}
		}
		for ( MissionConfig mission : hasBrothers ) {
			for ( final int postMission : mission.rewardInfo.postMissionList ) {
				final MissionConfig postMissionConf = missionConfMap.get( postMission );
				if ( postMissionConf == null )
					continue;
				for ( final int pm : mission.rewardInfo.postMissionList ) {
					if ( pm != postMissionConf.missionId )
						postMissionConf.limitInfo.brotherMissions.add( pm );
				}
			}
		}
		for ( MissionConfig mission : headTasks ) {
			if ( mission.limitInfo.minLevel != 0 || 
					mission.limitInfo.maxLevel != 0 ) {
				Set<Integer> set = nextlineMap.get( 0 );
				if ( set == null ) {
					set = new HashSet<Integer>();
					nextlineMap.put( 0, set );
				}
				set.add( mission.missionId );
			}
			if ( mission.limitInfo.requireMissionList != null ) {
				for ( int postmission : mission.limitInfo.requireMissionList ) {
					Set<Integer> set = nextlineMap.get( postmission );
					if ( set == null ) {
						set = new HashSet<Integer>();
						nextlineMap.put( postmission, set );
					}
					set.add( mission.missionId );
				}
			}
		}
		final java.util.Map<Integer, SAllTaskLine> lmap = cm.getConf( fire.pb.mission.SAllTaskLine.class);
		for ( final SAllTaskLine conf : lmap.values() ) {
			SchoolMissionSet schoollines = schoolMissionlineMap.get(conf.职业);
			if ( schoollines == null ) {
				schoollines = new SchoolMissionSet();
				schoolMissionlineMap.put( conf.职业, schoollines );
			}
			schoollines.addSchoolTask( conf.id );
		}
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			canAcceptMissionMap.putAll(cm.getConf(fire.pb.mission.SQuestCanAcceptList.class));
		}
		else {
			canAcceptMissionMap.putAll(cm.getConf(fire.pb.mission.SQuestCanAcceptListDianKa.class));
		}
		
		
		for (QuestCanAcceptList sqca : canAcceptMissionMap.values()) {
			if(sqca.任务等级min == sqca.任务等级max){
				tuPoTask.add(sqca.id);
			}
		}
		
		newFuncOpenMap.clear();
		final Map<Integer, fire.pb.mission.SNewFunctionOpen> snfomap = cm.getConf(fire.pb.mission.SNewFunctionOpen.class);
		if (snfomap != null) {
			for (fire.pb.mission.SNewFunctionOpen snfo:snfomap.values()) {
				Set<Integer> set = newFuncOpenMap.get( snfo.id );
				if(set == null){
					set = new HashSet<Integer>();
				}
				String[] strs = snfo.getTaskfinish().split(",");
				for(String str : strs){
					set.add( Integer.parseInt(str));
				}
				newFuncOpenMap.put(snfo.getId(), set);
			}
		}
		
	}
	public MissionConfig getMissionConfig( int missionid ) {
		return missionConfMap.get( missionid );
	
	}
	
	public Set<Integer> getPostmissions( final int lineid ) {
		if ( lineid == 0 )
			return nextlineMap.get( 0 );
		return nextlineMap.get( UtilHelper.getMissionId( lineid, UtilHelper.getLastStep() ) );
	}
	
	public Set<Integer> getTasklineCanAccept( final int school ) {
	//	final int roleschool = xtable.Properties.selectSchool( roleid );
		Set<Integer> ret = new TreeSet<Integer>();
		SchoolMissionSet lines = schoolMissionlineMap.get( school );
		if ( lines != null ) {
			ret.addAll( lines.getMajorTaskset() );
			ret.addAll( lines.getBranchTaskset() );
		}
		lines = schoolMissionlineMap.get( 0 );
		if ( lines != null) {
			ret.addAll( lines.getMajorTaskset() );
			ret.addAll( lines.getBranchTaskset() );
		}
		return ret;
	}
	
	
	boolean isNewFuncOpen(int nfoid, int taskid){
		Set<Integer> set = newFuncOpenMap.get( nfoid );
		if(set == null){
			return false;
		}
		
		boolean iscon = set.contains(taskid);
		
		return iscon;
	}
	
	int getNewFuncOpenTrigger(int nfoid){
		int taskid = 0;
		SNewFunctionOpen curFunc = ConfigManager.getInstance().getConf(fire.pb.mission.SNewFunctionOpen.class).get(nfoid);
		if(curFunc != null){
			taskid = (curFunc.getTriggertask() > 0) ? curFunc.getTriggertask() : 0;
		}
		return taskid;
	}
	

	


}
