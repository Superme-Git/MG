package fire.pb.ranklist.getrankinfo;

import java.util.Arrays;

import fire.pb.Item;
import fire.pb.PropRole;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import mkdb.Procedure;

/***
 * 获得装备信息
 * @author changhao
 *
 */
public class PGrabEquipmentInfo extends Procedure{
	private long roleId;
	private int rank;
	
	public PGrabEquipmentInfo(int rank,long roleId){
		this.roleId = roleId;
		this.rank = rank;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.RoleLevelRankList list = xtable.Rolelevellist.select(1);
		if(list == null){
			return false;
		}
		
		xbean.RoleLevelListRecord record = list.getRecords().get(rank);
		if(record == null){
			return false;
		}
		
		long rankRoleId = record.getMarshaldata().getRoleid();
		xbean.Properties targetRoleProp=xtable.Properties.select(rankRoleId);
		final ItemMaps bag=Module.getInstance().getItemMaps(rankRoleId,BagTypes.EQUIP,true);
		
		final SRankRoleInfo2 res=new SRankRoleInfo2();
		res.roleid = rankRoleId;
		res.rolename = targetRoleProp.getRolename();
		res.shape = targetRoleProp.getShape();
		res.level = targetRoleProp.getLevel();
		res.school = targetRoleProp.getSchool();
		
		//TODO bag.getBagInfo()原来是protected，现在改成了public
		res.baginfo=bag.getPackInfo();
		res.totalscore = Module.getInstance().getEquipTotalScore(rankRoleId);
		res.rank = rank;
		for (Item item : res.baginfo.items){
			ItemBase bitem = bag.getItem(item.key);
			if (bitem == null)
				continue;
			res.tips.put( item.key, bitem.getTips() );
		}
		gnet.link.Onlines.getInstance().send(roleId, res);
		
		fire.pb.talk.MessageMgr.psendMsgNotify(rankRoleId, 146331, 
				Arrays.asList(new PropRole(roleId,true).getName()));//某人在等级排行榜上偷看你
		
		return true;
	}
}
