package fire.pb.skill;


public class SkillManager
{
	//根据roleId，场景，获取能使用的SkillRole
	public static SceneSkillRole getSceneSkillRole(long roleId)
	{
		return new SceneSkillRole(roleId);
	}
	
}
