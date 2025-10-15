/**
 * 
 */
package fire.log;


/**
 *
 */
public abstract class BusinessLog implements IBusinessLog{
	public long Userid;
	public int Type;
	public BusinessLog()
	{
		Userid = 0;
		Type = 0;
	};
	
	void setUserid(long nUserid)
	{
		Userid = nUserid;
	}

	void setType(int Type)
	{
		this.Type = Type;
	}

}

