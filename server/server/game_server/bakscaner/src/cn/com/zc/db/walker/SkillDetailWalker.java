package cn.com.zc.db.walker;

import java.util.Map;

import xbean.AssistSkill;
import xbean.SkillRole;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.Dbx;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;
import cn.com.zc.db.util.FileManager;

import com.goldhuman.Common.Marshal.OctetsStream;

public class SkillDetailWalker extends Walker {

	private static final int SKILL_LIANYAO_ID = 310101;
	private static final int SKILL_COOKING_ID = 310201;
	private static final int SKILL_DAZAO1_ID = 300101;
	private static final int SKILL_QIAOJIANG_ID = 300201;
	private static final int SKILL_FENREN_ID = 300301;
	
	//修炼的skill id
	private static final int SKILL_YONGWU_ID = 360001;
	private static final int SKILL_MOULUE_ID = 360002;
	private static final int SKILL_YINIAN_ID = 360003;
	private static final int SKILL_ZHAOSHI_ID = 360004;
	private static final int SKILL_KANGMING_ID = 360005;
	private static final int SKILL_MINGSI_ID = 360006;
	
	public SkillDetailWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		Long roleid = (Long)key;
		xbean.SkillRole skillRole = (xbean.SkillRole)value;
		
		try {
			doQuickPhoto(roleid, skillRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private void doQuickPhoto(Long roleid, SkillRole skillRole) throws Exception {
		Dbx.Table properties = DbxManager.getInstance().getTableByName("properties");
		long userId = 0;
		if(properties != null){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = properties.find(os.marshal(roleid));
			 if(null != result){
				 xbean.Properties xProperties = xbean.Pod.newProperties();
				 xProperties.unmarshal(result);
				 userId = xProperties.getUserid();
			 }
		}

		int lianyaoLevel = getAssistSkillLevelByID(SKILL_LIANYAO_ID, skillRole);
		int pengrenLevel = getAssistSkillLevelByID(SKILL_COOKING_ID, skillRole);
		int dazaoLevel = getAssistSkillLevelByID(SKILL_DAZAO1_ID, skillRole);
		int qiaojiangLevel = getAssistSkillLevelByID(SKILL_QIAOJIANG_ID, skillRole);
		int fengrenLevel = getAssistSkillLevelByID(SKILL_FENREN_ID, skillRole);
		
		StringBuffer sb = new StringBuffer();
		String separator = ",";
		sb.append(userId + separator);
		sb.append(roleid + separator);
		sb.append(lianyaoLevel + separator);
		sb.append(pengrenLevel + separator);
		sb.append(dazaoLevel + separator);
		sb.append(qiaojiangLevel + separator);
		sb.append(fengrenLevel);
		
		FileManager.getInstance().setQuickPhotoRecord(Constant.SKILL_DETAIL, sb.toString());
		
		int yongwuLev = getAssistSkillLevelByID(SKILL_YONGWU_ID, skillRole);
		long yongwuExp = getAssistSkillExpByID(SKILL_YONGWU_ID, skillRole);
		int moulueLev = getAssistSkillLevelByID(SKILL_MOULUE_ID, skillRole);
		long moulueExp = getAssistSkillExpByID(SKILL_MOULUE_ID, skillRole);
		int yinianLev = getAssistSkillLevelByID(SKILL_YINIAN_ID, skillRole);
		long yinianExp = getAssistSkillExpByID(SKILL_YINIAN_ID, skillRole);
		int zhaoshiLev = getAssistSkillLevelByID(SKILL_ZHAOSHI_ID, skillRole);
		long zhaoshiExp = getAssistSkillExpByID(SKILL_ZHAOSHI_ID, skillRole);
		int kangmingLev = getAssistSkillLevelByID(SKILL_KANGMING_ID, skillRole);
		long kangmingExp = getAssistSkillExpByID(SKILL_KANGMING_ID, skillRole);
		int mingsiLev = getAssistSkillLevelByID(SKILL_MINGSI_ID, skillRole);
		long mingsiExp = getAssistSkillExpByID(SKILL_MINGSI_ID, skillRole);
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append(userId + separator);
		sb2.append(roleid + separator);
		sb2.append(yongwuLev + separator);
		sb2.append(yongwuExp + separator);
		sb2.append(moulueLev + separator);
		sb2.append(moulueExp + separator);
		sb2.append(yinianLev + separator);
		sb2.append(yinianExp + separator);
		sb2.append(zhaoshiLev + separator);
		sb2.append(zhaoshiExp + separator);
		sb2.append(kangmingLev + separator);
		sb2.append(kangmingExp + separator);
		sb2.append(mingsiLev + separator);
		sb2.append(mingsiExp);
		
		FileManager.getInstance().setQuickPhotoRecord(Constant.ROLE_XIULIAN_PROPERTY, sb2.toString());
		
	}

	private int getAssistSkillLevelByID(int skillId, SkillRole skillRole) {
		Map<Integer, AssistSkill> assistSkills = skillRole.getAssistskills();
		AssistSkill assistSkill = assistSkills.get(skillId);
		if(null == assistSkill)
			return 0;
		
		return assistSkill.getLevel();
	}
	
	private long getAssistSkillExpByID(int skillId, SkillRole skillRole){
		Map<Integer, AssistSkill> assistSkills = skillRole.getAssistskills();
		AssistSkill assistSkill = assistSkills.get(skillId);
		if(null == assistSkill)
			return 0;
		
		return assistSkill.getExp();
	}

}
