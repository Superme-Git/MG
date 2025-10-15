
package xbean;

/**
 * bean factory
 */
public final class Pod {
	public static mkdb.util.BeanPool<NameState> poolNameState = new mkdb.util.BeanPool<NameState>() {
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
		mkdb.Procedure.padd(bean, poolNameState);
	}

	public static NameState newNameStateData() {
		return new xbean.__.NameState.Data();
	}

	public static mkdb.util.BeanPool<IdState> poolIdState = new mkdb.util.BeanPool<IdState>() {
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
		mkdb.Procedure.padd(bean, poolIdState);
	}

	public static IdState newIdStateData() {
		return new xbean.__.IdState.Data();
	}

}
