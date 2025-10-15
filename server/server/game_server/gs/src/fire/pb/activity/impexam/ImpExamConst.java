

package fire.pb.activity.impexam;

import fire.pb.util.FireProp;


public class ImpExamConst {

	public static final int IMPEXAM_VILL = 1;

	public static final int IMPEXAM_PROV = 2;

	public static final int IMPEXAM_STATE = 3;

	public static final int IMPEXAM_ACCEPTABLE = 0;// 可参加智力试练

	public static final int IMPEXAM_SUCC = 1; // 智力试练成功

	public static final int IMPEXAM_FAILED = 2; // 智力试练失败

	public static final int IMPEXAM_RUNNING = 3; // 正在参加智力试练

	public static final int IMPEXAM_TOP1_TITLE_ID = FireProp.getIntValue("activity", "mtgserv.impexam.stateTop1TitleID");

	public static final int IMPEXAM_TOP2_TITLE_ID = FireProp.getIntValue("activity", "mtgserv.impexam.stateTop2TitleID");

	public static final int IMPEXAM_TOP3_TITLE_ID = FireProp.getIntValue("activity", "mtgserv.impexam.stateTop3TitleID");

	public static final int IMPEXAM_VILL_QUESIZE = FireProp.getIntValue("activity", "mtgserv.impexam.villQuesListSize");

	public static final int IMPEXAM_PROV_QUESIZE = FireProp.getIntValue("activity", "mtgserv.impexam.provQuesListSize");

	public static final int IMPEXAM_STATE_QUESIZE = FireProp.getIntValue("activity", "mtgserv.impexam.stateQuesListSize");


	public static final int IMPEXAM_VILL_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.villAwardID");
	
	public static final int IMPEXAM_PROV_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.provAwardID");
	
	public static final int IMPEXAM_STATE_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.stateAwardID");
	
	public static final int IMPEXAM_TOP1_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.top1AwardID");
	
	public static final int IMPEXAM_TOP2_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.top2AwardID");
	
	public static final int IMPEXAM_TOP3_AWARDID = FireProp.getIntValue("activity", "mtgserv.impexam.top3AwardID");


}
