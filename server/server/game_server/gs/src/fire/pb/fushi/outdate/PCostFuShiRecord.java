
package fire.pb.fushi.outdate;


public class PCostFuShiRecord extends PFuShiRecord{
	/**
	 * @param roleId
	 * @param num
	 */
	public PCostFuShiRecord(long roleId, int num) {
		super(roleId, num);
	}

	@Override
	protected boolean process() throws Exception {
		FuShiReocrdRole role = getRole();
		role.costFuShi(num);
		return true;
	}
	
}
