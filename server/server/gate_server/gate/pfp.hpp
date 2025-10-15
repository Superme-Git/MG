
#pragma once

#include "common/marshal.h"

/*
 * 本文件由snail/gnet/glinkd和snail/gnet/authc/linkc两个项目使用。
 * PortForward 协议定义:
 *    公共常量；
 *    公共数据结构；
 */

namespace portforward
{

/*
PortForward 协议参数简要说明
{
	portsid ：映射编号。在某些命令下，它有其他含义。

	command     功能描述     code       data
	----------+-----------+-----------+-------------------------------------------
	eConnect    建立映射    参数版本    系列化后的参数
	eClose      关闭映射    关闭原因    ansi字符串.
	eForward    转发数据    数据类型    转发数据
	eForwardAck 流量控制    已确认位置  保留，必须为空。
	eAuthority  设置权限    操作指令    操作指令相关参数。详情见后面定义eAuthorityXXX. 注：portsid 为 userid。
	eAnsiText   文本消息    0           ansi 编码的消息。
}
详细参数请查看下面的定义
*/

enum ePortForward
{
	// command
	eConnect    = 1, // 建立映射（连接远程服务）。
	eClose      = 2, // 关闭映射（断开连接）。
	eForward    = 3, // 转发数据。
	eForwardAck = 4, // 流量控制。
	//eRemote     = 5, // 反向映射（未实现）。
	eDumpLinkd  = 6, // 命令glinkd打印一些统计信息出来。用来调试。
	eAuthority  = 7, // 设置用户权限（临时性的，用户必须在当前linkd且在线）。
	eAnsiText   = 8, // 给客户端返回一些ansi编码的信息。

	////////////////////////////////////////////////////////////////////////////
	// code

	// eConnect
	eConnectV0 = 0,  // 参数版本。具体参数见 class ConnectParameterV0;

	// eClose(尽量细分)
	eCloseUnknownConnectVersion  = 1,
	eCloseUnknownForwardType     = 2,
	eCloseForwardPortNotFound    = 3,
	eCloseConnectDuplicatePort   = 4,
	eCloseSessionAbort           = 5, // 连接失败
	eCloseSessionClose           = 6, // 连接关闭
	eCloseForwardAckPortNotFound = 7,
	eCloseActiveFromConsole      = 8,
	eCloseConnectUnsupported     = 9,
	eCloseForwardProcessExp      = 10,
	eCloseNoAuthority            = 11,
	eCloseNoAuthPort             = 12,
	eCloseNoAuthAidOrZoneid      = 13,

	// eForward
	eForwardRaw = 0, // 原生数据，直接转发。没有压缩，也没有加密。

	eAuthoritySet = 1, // 根据参数覆盖指定的用户的授权并返回旧的授权。
	eAuthorityGet = 2, // 查询指定用户授权。查询结果也使用此操作指令返回。

	/////////////////////////////////////////////////////////////////////////////
	// 公共常量（不一定是公共的）
	// Windows 流控窗口范围。
	eForwardWindowLowSize  = 48 * 1024, // 当未确认数据长度小于此数时，重启转发（读取本地连接并转发）。
	eForwardWindowHighSize = 64 * 1024, // 当未确认数据长度大于此数时，停止转发（停止读取本地连接）。
};

struct AuthorityParameter : public GNET::Marshal
{
	int allow;
	std::set<int> ports; // 为空的时候允许连接全部端口。

	void reset()           { allow = 0; ports.clear(); }

	void addForward()      { allow |= 1; }         // 是否允许使用端口转发。
	void addGrant()        { allow |= 2; }         // 是否允许给别人分配权限。
	void addPort(int port) { ports.insert(port); } // 是否允许访问特定端口。

	bool allowForward() const      { return 0 != (allow & 1); }
	bool allowGrant() const        { return 0 != (allow & 2); }
	bool allowPort(int port) const { return ports.empty() || ports.find(port) != ports.end(); }

	// 检查参数ap的权限在可授权范围之内。授权者不能授予超过自己拥有的权限。
	int grantcheck(const AuthorityParameter & ap) const
	{
		if (!ap.allowForward() && !ap.ports.empty())
			return 1; // ap 自相矛盾：没有转发权限时不允许指定端口。

		if (ap.allow != (ap.allow & this->allow))
			return 2; // allow 超出允许范围。

		if (this->ports.empty())
			return 0; // 允许授权任意端口。

		if (ap.ports.empty())
			return 3; // 有限端口没法授权任意端口。

		for (std::set<int>::const_iterator it = ap.ports.begin(), ie = ap.ports.end(); it != ie; ++it)
		{
			if (this->ports.find(*it) == ports.end())
				return 4; // 授权端口超出允许的端口。
		}
		return 0;
	}

	AuthorityParameter()
	{
		allow = 0;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << allow << GNET::MarshalContainer(ports);
		return _os_;
    }

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> allow >> GNET::MarshalContainer(ports);
		return _os_;
	}
};

struct ConnectParameterV0 : public GNET::Marshal
{
	std::string address;
	std::string port;
	int         timeout;

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << address << port << timeout;
		return _os_;
    }

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> address >> port >> timeout;
		return _os_;
	}
};

} // namespace portforward
