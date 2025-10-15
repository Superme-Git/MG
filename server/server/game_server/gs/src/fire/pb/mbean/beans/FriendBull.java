

package fire.pb.mbean.beans;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.mbean.AbstractRequestHandler;
import fire.pb.talk.BroadcastSysMsg;
import fire.pb.talk.MessageMgr;


public class FriendBull extends AbstractRequestHandler {

	
	public FriendBull(String name) {

		
		super(name);
		
	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {

		try {
			
			final String bullString = (String) paras.get("text");
			if (bullString==null || bullString.isEmpty()) {
				return failedMsg("公告内容不正确");
			}
			final String roleids = (String) paras.get("roleid");
			if (roleids == null || roleids.isEmpty())
				return failedMsg("roleids 字段为不存在或为空");
			if (roleids.equals("0")){
				new mkdb.Procedure(){
					protected boolean process() throws Exception{
						new BroadcastSysMsg().broadcastSysMsg(bullString);
						return true;
					}
				}.submit();			
			}else {
				List<String> param = new LinkedList<String>();
				param.add(bullString);
				String[] roleidArray = roleids.split(",");
				Set<Long> roleidSet = new HashSet<Long>();
				Set<Long> failedRoleids = new HashSet<Long>();
				for (String roleidstr : roleidArray){
					Long rid = Long.parseLong(roleidstr);
					if (xtable.Properties.select(rid)==null){
						failedRoleids.add(rid);
					}else
					    roleidSet.add(rid);
				}
				for (Long rid : roleidSet) {	
					MessageMgr.sendSystemMessageToRole(rid, 141661, param);
				}
				if (roleidSet.isEmpty()){
					return failedMsg("所有角色id都不存在!");
				}
				if (!failedRoleids.isEmpty()&&!roleidSet.isEmpty()) {
					return successMsg("部分角色发送成功. 以下角色id不存在,所以发送失败:"+failedRoleids.toString());
				}
			}
			return successMsg("全部角色发送好友公告成功!");
		} catch (Exception e) {
			return failedMsg("发送好友公告失败:"+e.getMessage());
		}
	}

}

