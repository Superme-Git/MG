
package xbean;

public interface UserDeviceInfo extends mkdb.Bean {
	public UserDeviceInfo copy(); // deep clone
	public UserDeviceInfo toData(); // a Data instance
	public UserDeviceInfo toBean(); // a Bean instance
	public UserDeviceInfo toDataIf(); // a Data instance If need. else return this
	public UserDeviceInfo toBeanIf(); // a Bean instance If need. else return this

	public String getTelcooper(); // 用户网络运营商
	public com.locojoy.base.Octets getTelcooperOctets(); // 用户网络运营商
	public String getChid(); // 渠道ID
	public com.locojoy.base.Octets getChidOctets(); // 渠道ID
	public String getPlattype(); // 平台ID(IOS,android,wp)
	public com.locojoy.base.Octets getPlattypeOctets(); // 平台ID(IOS,android,wp)
	public String getDeviid(); // 玩家设备(mac/idfv)设备唯一标识
	public com.locojoy.base.Octets getDeviidOctets(); // 玩家设备(mac/idfv)设备唯一标识
	public String getImei(); // 入网标识符
	public com.locojoy.base.Octets getImeiOctets(); // 入网标识符
	public String getIp(); // 玩家设备IP
	public com.locojoy.base.Octets getIpOctets(); // 玩家设备IP
	public String getNetenvir(); // 网络环境(如：3G,4G,wifi)
	public com.locojoy.base.Octets getNetenvirOctets(); // 网络环境(如：3G,4G,wifi)
	public String getOsver(); // 玩家系统版本(具体版本如ios9,android4.1)
	public com.locojoy.base.Octets getOsverOctets(); // 玩家系统版本(具体版本如ios9,android4.1)
	public String getBrtype(); // 玩家手机类型(如联想，酷派)
	public com.locojoy.base.Octets getBrtypeOctets(); // 玩家手机类型(如联想，酷派)
	public String getFirstdeviid(); // 玩家设备(mac/idfv) 注册设备唯一标识
	public com.locojoy.base.Octets getFirstdeviidOctets(); // 玩家设备(mac/idfv) 注册设备唯一标识
	public String getUsername(); // 账号名称
	public com.locojoy.base.Octets getUsernameOctets(); // 账号名称

	public void setTelcooper(String _v_); // 用户网络运营商
	public void setTelcooperOctets(com.locojoy.base.Octets _v_); // 用户网络运营商
	public void setChid(String _v_); // 渠道ID
	public void setChidOctets(com.locojoy.base.Octets _v_); // 渠道ID
	public void setPlattype(String _v_); // 平台ID(IOS,android,wp)
	public void setPlattypeOctets(com.locojoy.base.Octets _v_); // 平台ID(IOS,android,wp)
	public void setDeviid(String _v_); // 玩家设备(mac/idfv)设备唯一标识
	public void setDeviidOctets(com.locojoy.base.Octets _v_); // 玩家设备(mac/idfv)设备唯一标识
	public void setImei(String _v_); // 入网标识符
	public void setImeiOctets(com.locojoy.base.Octets _v_); // 入网标识符
	public void setIp(String _v_); // 玩家设备IP
	public void setIpOctets(com.locojoy.base.Octets _v_); // 玩家设备IP
	public void setNetenvir(String _v_); // 网络环境(如：3G,4G,wifi)
	public void setNetenvirOctets(com.locojoy.base.Octets _v_); // 网络环境(如：3G,4G,wifi)
	public void setOsver(String _v_); // 玩家系统版本(具体版本如ios9,android4.1)
	public void setOsverOctets(com.locojoy.base.Octets _v_); // 玩家系统版本(具体版本如ios9,android4.1)
	public void setBrtype(String _v_); // 玩家手机类型(如联想，酷派)
	public void setBrtypeOctets(com.locojoy.base.Octets _v_); // 玩家手机类型(如联想，酷派)
	public void setFirstdeviid(String _v_); // 玩家设备(mac/idfv) 注册设备唯一标识
	public void setFirstdeviidOctets(com.locojoy.base.Octets _v_); // 玩家设备(mac/idfv) 注册设备唯一标识
	public void setUsername(String _v_); // 账号名称
	public void setUsernameOctets(com.locojoy.base.Octets _v_); // 账号名称
}
