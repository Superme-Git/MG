package fire.pb.cross;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 跨服和原服务之间的协议处理类
 * @author dc
 */
public class DeliverProtocolProcesser {

	public static void process(int dtype, Octets octets) {
		OctetsStream octstram = OctetsStream.wrap(octets);
		try {
			switch (dtype) {
			case Constant.SendRoleInfo:
				SendRoleInfo p = new SendRoleInfo();
				p.unmarshal(octstram);
				new PSendRoleInfoProc(p).submit();
				break;
				
			case Constant.SendRoleInfo_Rep:
				SendRoleInfo_Rep p1 = new SendRoleInfo_Rep();
				p1.unmarshal(octstram);
				new PSendRoleInfo_RepProc(p1).submit();
				break;
				
			case Constant.SendRoleData:
				SendRoleData p2 = new SendRoleData();
				p2.unmarshal(octstram);
				new PSendRoleDataProc(p2).submit();
				break;
				
			case Constant.FinishCopyRole:
				FinishCopyRole p3 = new FinishCopyRole();
				p3.unmarshal(octstram);
				new PFinishCopyRoleProc(p3).submit();
				break;
				
			}
		} catch (Exception e) {
			CrossManager.logger.info("DeliverProtocolProcesser.处理协议:" + dtype + "异常!e=" + e);
		}
	}
}
