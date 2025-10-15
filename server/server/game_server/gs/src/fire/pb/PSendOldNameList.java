/**
 * 
 */
package fire.pb;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;

/**
 * 
 * 发送角色曾用名列表
 * 
 * @author nobody
 *
 */
public class PSendOldNameList extends Procedure {

	private long roleId;
	
	private long toRoleId;
	
	public PSendOldNameList(long roleId,long toRoleId){
		this.roleId = roleId;
		this.toRoleId = toRoleId;
	}
	
	@Override
	protected boolean process() throws Exception{
		//获取目标角色的曾用名列表
		xbean.Properties pro = xtable.Properties.get(toRoleId);
		//对方没有曾用名
		if(pro == null){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,145622,null);
			return false;
		}
		//对方没有曾用名
		if(pro.getUsedname().size() <=0){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,145622,null);
			return false;
		}
		//发送曾用名列表
//		SOldNameList msg = new SOldNameList();
//		for(String oldName : pro.getUsedname()){
//			msg.oldnames.add(oldName);
//		}
//		xdb.Procedure.psendWhileCommit(roleId, msg);
		//改用透明提示框的方式弹出
		StringBuffer names = new StringBuffer("");
		int size = pro.getUsedname().size();
		//只显示最近的12个曾用名
		int num = 12;
		if(size > 0){
			int index = 0;
			for(int i=size-1;i>=0;i--){
				if(index >= num){
					break;
				}
				String name = pro.getUsedname().get(i);
				names.append(name + ",");
				index ++ ;
			}
		}
		String oldNames = names.toString();
		oldNames = oldNames.substring(0,oldNames.length()-1);
		List<String> params = new ArrayList<String>();
		params.add(oldNames);
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145625, params);	
		return true;
	}
}
