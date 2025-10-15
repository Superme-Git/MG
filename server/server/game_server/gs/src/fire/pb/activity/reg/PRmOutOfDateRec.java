
package fire.pb.activity.reg;

import java.util.Calendar;

import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;


//删除过期失效的签到记录
public class PRmOutOfDateRec extends Procedure {
	private long roleId;

	private RegAssistant regAssistant;

	private long lastRegTime;

	public PRmOutOfDateRec(long roleId, RegAssistant helper,long lastModifyTime) {
		this.roleId = roleId;
		this.regAssistant = helper;
	}

	public PRmOutOfDateRec(long roleId,long lastModifyTime) {
		this(roleId, (RegAssistant) SingleContainer.getInstance(RegAssistant.class),lastModifyTime);
	}

	@Override
	protected boolean process() throws Exception {
		Calendar calen = Calendar.getInstance();
		calen.setTimeInMillis(lastRegTime);
		int monthOfYear = regAssistant.getMonthOfYear(calen);
		// 删除过期月的数据
		xbean.RegRec regRec = regAssistant.getRecord(roleId);
		regRec.getMonthmap().remove(monthOfYear);
		return true;
	}

}
