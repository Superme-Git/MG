
package xbean;

public interface CameraInfo extends mkdb.Bean {
	public CameraInfo copy(); // deep clone
	public CameraInfo toData(); // a Data instance
	public CameraInfo toBean(); // a Bean instance
	public CameraInfo toDataIf(); // a Data instance If need. else return this
	public CameraInfo toBeanIf(); // a Bean instance If need. else return this

	public long getEndtime(); // 
	public int getSizebeforezip(); // 
	public int getSizeafterzip(); // 
	public String getCamerafileurl(); // 
	public com.locojoy.base.Octets getCamerafileurlOctets(); // 

	public void setEndtime(long _v_); // 
	public void setSizebeforezip(int _v_); // 
	public void setSizeafterzip(int _v_); // 
	public void setCamerafileurl(String _v_); // 
	public void setCamerafileurlOctets(com.locojoy.base.Octets _v_); // 
}
