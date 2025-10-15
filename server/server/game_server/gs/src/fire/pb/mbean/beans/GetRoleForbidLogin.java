

package fire.pb.mbean.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xbean.PunishRecord;
import fire.pb.mbean.AbstractRequestHandler;


public class GetRoleForbidLogin extends AbstractRequestHandler {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public GetRoleForbidLogin(String name) {

		super(name);
	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {
		
		String roleidStr = (String) paras.get("roleid");
		Long roleid = Long.valueOf(roleidStr);
		Integer userid = xtable.Properties.selectUserid(roleid);
		if(userid == null) return failedMsg("该玩家不存在.roleid:"+roleid);
		xbean.UserPunish punish = xtable.Userpunish.select(userid);	
		if (punish == null || punish.getRecords().isEmpty()) {
			return failedMsg("该玩家没有封禁记录");
		}
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("return", "true");
		PunishRecord punishRecord = punish.getRecords().get(punish.getRecords().size()-1);
		long curTime = System.currentTimeMillis();
		if (curTime<punishRecord.getOptime()+punishRecord.getForbidtime()) {
			result.put("forbidtype", String.valueOf(punishRecord.getType()));
			result.put("begintime", sdf.format(new Date(punishRecord.getOptime())));
			result.put("lasttime",String.valueOf(punishRecord.getForbidtime()/60000));
			result.put("desc", punishRecord.getReason());
		
		}else {
			result.put("forbidtype", String.valueOf(punishRecord.getType()));
			result.put("begintime", "");
			result.put("lasttime","");
			result.put("desc", "玩家未处于禁止登录或禁言状态");
		}
		
		
		return result;
	}

}

