
package xbean;

/**
 * bean factory
 */
public final class Pod {
	public static xdb.util.BeanPool<UserInfo> poolUserInfo = new xdb.util.BeanPool<UserInfo>() {
		@Override
		protected UserInfo newBean() {
			return new xbean.__.UserInfo();
		}
	};

	public static UserInfo newUserInfo() {
		return poolUserInfo.get();
	}

	public static void _reset_unsafe_add_(UserInfo bean) {
		poolUserInfo._reset_unsafe_add_(bean);
	}

	public static void padd(UserInfo bean) {
		xdb.Procedure.padd(bean, poolUserInfo);
	}

	public static UserInfo newUserInfoData() {
		return new xbean.__.UserInfo.Data();
	}

	public static xdb.util.BeanPool<LocojoyPlatUserInfo> poolLocojoyPlatUserInfo = new xdb.util.BeanPool<LocojoyPlatUserInfo>() {
		@Override
		protected LocojoyPlatUserInfo newBean() {
			return new xbean.__.LocojoyPlatUserInfo();
		}
	};

	public static LocojoyPlatUserInfo newLocojoyPlatUserInfo() {
		return poolLocojoyPlatUserInfo.get();
	}

	public static void _reset_unsafe_add_(LocojoyPlatUserInfo bean) {
		poolLocojoyPlatUserInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LocojoyPlatUserInfo bean) {
		xdb.Procedure.padd(bean, poolLocojoyPlatUserInfo);
	}

	public static LocojoyPlatUserInfo newLocojoyPlatUserInfoData() {
		return new xbean.__.LocojoyPlatUserInfo.Data();
	}

	public static xdb.util.BeanPool<LocojoyPlatOrderInfo> poolLocojoyPlatOrderInfo = new xdb.util.BeanPool<LocojoyPlatOrderInfo>() {
		@Override
		protected LocojoyPlatOrderInfo newBean() {
			return new xbean.__.LocojoyPlatOrderInfo();
		}
	};

	public static LocojoyPlatOrderInfo newLocojoyPlatOrderInfo() {
		return poolLocojoyPlatOrderInfo.get();
	}

	public static void _reset_unsafe_add_(LocojoyPlatOrderInfo bean) {
		poolLocojoyPlatOrderInfo._reset_unsafe_add_(bean);
	}

	public static void padd(LocojoyPlatOrderInfo bean) {
		xdb.Procedure.padd(bean, poolLocojoyPlatOrderInfo);
	}

	public static LocojoyPlatOrderInfo newLocojoyPlatOrderInfoData() {
		return new xbean.__.LocojoyPlatOrderInfo.Data();
	}

}
