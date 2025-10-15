
package xbean;

/**
 * bean factory
 */
public final class Pod {
	public static xdb.util.BeanPool<NameState> poolNameState = new xdb.util.BeanPool<NameState>() {
		@Override
		protected NameState newBean() {
			return new xbean.__.NameState();
		}
	};

	public static NameState newNameState() {
		return poolNameState.get();
	}

	public static void _reset_unsafe_add_(NameState bean) {
		poolNameState._reset_unsafe_add_(bean);
	}

	public static void padd(NameState bean) {
		xdb.Procedure.padd(bean, poolNameState);
	}

	public static NameState newNameStateData() {
		return new xbean.__.NameState.Data();
	}

	public static xdb.util.BeanPool<IdState> poolIdState = new xdb.util.BeanPool<IdState>() {
		@Override
		protected IdState newBean() {
			return new xbean.__.IdState();
		}
	};

	public static IdState newIdState() {
		return poolIdState.get();
	}

	public static void _reset_unsafe_add_(IdState bean) {
		poolIdState._reset_unsafe_add_(bean);
	}

	public static void padd(IdState bean) {
		xdb.Procedure.padd(bean, poolIdState);
	}

	public static IdState newIdStateData() {
		return new xbean.__.IdState.Data();
	}

}
