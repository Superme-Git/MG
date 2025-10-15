package fire.pb.battle.pvp;

import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.NpcServices;

/**
 * PvP服务工厂
 * @author XGM
 */
public class PvPServiceHandleFactory {

	/**
	 * 创建服务的处理接口
	 * @param serviceId
	 * @return
	 */
	public static IPvPServiceHandle create(int serviceId) {
		switch (serviceId) {
		// 以下是1v1
		case NpcServices.ENTER_1V1_PVP:
			return new PvPEnter(EPvPType.PVP1);
		case NpcServices.LEAVE_1V1_PVP:
			return new PvPLeave(EPvPType.PVP1);
		case 910006: // 首胜奖励
		case 910007: // 五战奖励
			return new PvPAward(EPvPType.PVP1);

		// 以下是3v3
		case NpcServices.ENTER_3V3_PVP:
			return new PvPEnter(EPvPType.PVP3);
		case NpcServices.LEAVE_3V3_PVP:
			return new PvPLeave(EPvPType.PVP3);
		case 910016: // 首胜奖励
		case 910017: // 十战奖励
		case 910018: // 五胜奖励
			return new PvPAward(EPvPType.PVP3);

		// 以下是5v5
		case NpcServices.ENTER_5V5_PVP:
			return new PvPEnter(EPvPType.PVP5);
		case NpcServices.LEAVE_5V5_PVP:
			return new PvPLeave(EPvPType.PVP5);
		case 910026: // 首胜奖励
		case 910027: // 五战奖励
			return new PvPAward(EPvPType.PVP5);
		}
		return null;
	}

	/**
	 * PvP进入进场
	 * @author XGM
	 */
	public static class PvPEnter implements IPvPServiceHandle {

		private final EPvPType pvpType;

		public PvPEnter(EPvPType pvpType) {
			this.pvpType = pvpType;
		}

		@Override
		public void handle(long roleId, int serviceId) {
			PvPControl c = PvPControlManager.getControl(pvpType);
			if (c != null) {
				c.applyEnter(roleId, SRoleEnterScene.CHEFU);
			}
		}
	}

	/**
	 * PvP离开场景
	 * @author XGM
	 */
	public static class PvPLeave implements IPvPServiceHandle {

		private final EPvPType pvpType;

		public PvPLeave(EPvPType pvpType) {
			this.pvpType = pvpType;
		}

		@Override
		public void handle(final long roleId, int serviceId) {
			PvPControl c = PvPControlManager.getControl(pvpType);
			if (c != null) {
				c.applyLeave(roleId);
			}
		}

	}

	/**
	 * PvP领奖
	 * @author XGM
	 */
	public static class PvPAward implements IPvPServiceHandle {

		private final EPvPType pvpType;

		public PvPAward(EPvPType pvpType) {
			this.pvpType = pvpType;
		}

		@Override
		public void handle(final long roleId, int serviceId) {
			PvPControl c = PvPControlManager.getControl(pvpType);
			if (c != null) {
				int boxType = PvPHelper.getBoxTypeByServiceId(serviceId);
				c.getAward(roleId, boxType);
			}
		}

	}
}
