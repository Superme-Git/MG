package fire.pb.skill;

import fire.pb.huoban.HuoBanColumn;

public class PRoleOnline extends mkdb.Procedure
{
	private long roleId;

	public PRoleOnline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		SkillRole srole = new SkillRole(roleId,false);
		//发送技能
		srole.sendAllSkillsWhileOnline();
		srole.sendSpecialSkills();		
		HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleId, false);
		if (huobancol != null)
			huobancol.sendHuoBanListOnChange();
		return true;
	}
}
