
package xbean;

public interface SkillRole extends mkdb.Bean {
	public SkillRole copy(); // deep clone
	public SkillRole toData(); // a Data instance
	public SkillRole toBean(); // a Bean instance
	public SkillRole toDataIf(); // a Data instance If need. else return this
	public SkillRole toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getSkillbases(); // key = skillbase id; value = skillbase level
	public java.util.Map<Integer, Integer> getSkillbasesAsData(); // key = skillbase id; value = skillbase level
	public java.util.Map<Integer, Integer> getInborns(); // key = inborn id; value = inborn level
	public java.util.Map<Integer, Integer> getInbornsAsData(); // key = inborn id; value = inborn level
	public long getLastdegradetime(); // 上次穴位降级时间，一天只能降级一次
	public java.util.Map<Integer, xbean.AssistSkill> getAssistskills(); // 辅助技能
	public java.util.Map<Integer, xbean.AssistSkill> getAssistskillsAsData(); // 辅助技能
	public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskill(); // 修炼技能 by changhao
	public java.util.Map<Integer, xbean.PracticeSkill> getPracticeskillAsData(); // 修炼技能 by changhao
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskill(); // 生活技能 by changhao
	public java.util.Map<Integer, xbean.LiveSkill> getLiveskillAsData(); // 生活技能 by changhao
	public java.util.List<xbean.SpecialSkill> getSpecialskills(); // 装备提供的特技特效
	public java.util.List<xbean.SpecialSkill> getSpecialskillsAsData(); // 装备提供的特技特效
	public java.util.Map<Integer, Integer> getExtskills(); // key = extskill id; value = extskill level
	public java.util.Map<Integer, Integer> getExtskillsAsData(); // key = extskill id; value = extskill level
	public java.util.Map<Integer, Integer> getSkillsequence(); // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
	public java.util.Map<Integer, Integer> getSkillsequenceAsData(); // 技能顺序，key为技能ID。value=1~100时为职业技能，101~200时为特技
	public java.util.Map<Integer, Long> getLastusedtime(); // 上一次使用技能的时间，目前只用在结拜拉人技能里
	public java.util.Map<Integer, Long> getLastusedtimeAsData(); // 上一次使用技能的时间，目前只用在结拜拉人技能里

	public void setLastdegradetime(long _v_); // 上次穴位降级时间，一天只能降级一次
}
