package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.mission.SLandFightConfig;

public class ServiceConfig {	
	public final static int TYPE_ANSWER_QUESTION = 4;//回答问题
	public final static int TYPE_BATTLE = 5;//开启战斗
	public final static int TYPE_SUBMIT_ITEM = 6;//交物品	
	
	public final int servId; //服务id
	public final int stepId; //做子进度用
	public final int times;
	public final int instzoneId;
	public final int instzoneType;//类型
	public final int ptAwardid;
	public final int ptAwardidTimes;
	public final int sAwardid;
	public final int sAwardTimes;
	public final int msgid;
	
	public final int matchtype; //1 表示匹配个人等级; 2 表示队伍平均等级

	public final Map<String, Object> params;//参数，各类型需求的参数不同，所以用params
	List<LevelGroupAward> groupAward = new ArrayList<LevelGroupAward>();
	
	public ServiceConfig(SLandFightConfig config) {
		this.servId = config.getId();
		this.stepId = config.getStep();
		this.times = config.getTimes();
		this.instzoneId = config.getInstancezoneid();
		this.instzoneType = config.getInstancetype();
		this.ptAwardid = config.getAwardid();
		this.ptAwardidTimes = config.getAwardtimes();
		this.sAwardid = config.getSawardid();
		this.sAwardTimes = config.getSawardtimes();
		this.msgid = config.getNpcmsgid();
		this.params = ConfigParamName.parseConfigParams(config.getParams());
		this.matchtype = config.getMatchtype();
		
		InitGroupAward(config.getAwardids());
	}
	
	private void InitGroupAward(final String awardstring) {
		if (awardstring != null) {
			String [] awardLst = awardstring.split(";");
			for (String awardStr : awardLst) {
				String [] awardSub = awardStr.split(",");
				LevelGroupAward group = new LevelGroupAward(servId, Integer.parseInt(awardSub[0]), 
						Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
				
				groupAward.add(group);
			}
		}
	}

}
