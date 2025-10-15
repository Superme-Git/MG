
package fire.pb.fushi.outdate;

import mkdb.Procedure;

/**
 * 
 */
public class PFuShiRecord extends Procedure{
	protected long roleId;
	/**
	 * 符石数量
	 */
	protected int num;

	/**
	 * @param roleId
	 */
	public PFuShiRecord(long roleId, int num) {
		this.roleId = roleId;
		this.num = num;
	}

	protected FuShiReocrdRole getRole(){
		return new FuShiReocrdRole(roleId);
	}

}
