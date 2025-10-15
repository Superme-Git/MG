
#pragma once

#include "common/marshal.h"

/*
 * ���ļ���snail/gnet/glinkd��snail/gnet/authc/linkc������Ŀʹ�á�
 * PortForward Э�鶨��:
 *    ����������
 *    �������ݽṹ��
 */

namespace portforward
{

/*
PortForward Э�������Ҫ˵��
{
	portsid ��ӳ���š���ĳЩ�����£������������塣

	command     ��������     code       data
	----------+-----------+-----------+-------------------------------------------
	eConnect    ����ӳ��    �����汾    ϵ�л���Ĳ���
	eClose      �ر�ӳ��    �ر�ԭ��    ansi�ַ���.
	eForward    ת������    ��������    ת������
	eForwardAck ��������    ��ȷ��λ��  ����������Ϊ�ա�
	eAuthority  ����Ȩ��    ����ָ��    ����ָ����ز�������������涨��eAuthorityXXX. ע��portsid Ϊ userid��
	eAnsiText   �ı���Ϣ    0           ansi �������Ϣ��
}
��ϸ������鿴����Ķ���
*/

enum ePortForward
{
	// command
	eConnect    = 1, // ����ӳ�䣨����Զ�̷��񣩡�
	eClose      = 2, // �ر�ӳ�䣨�Ͽ����ӣ���
	eForward    = 3, // ת�����ݡ�
	eForwardAck = 4, // �������ơ�
	//eRemote     = 5, // ����ӳ�䣨δʵ�֣���
	eDumpLinkd  = 6, // ����glinkd��ӡһЩͳ����Ϣ�������������ԡ�
	eAuthority  = 7, // �����û�Ȩ�ޣ���ʱ�Եģ��û������ڵ�ǰlinkd�����ߣ���
	eAnsiText   = 8, // ���ͻ��˷���һЩansi�������Ϣ��

	////////////////////////////////////////////////////////////////////////////
	// code

	// eConnect
	eConnectV0 = 0,  // �����汾����������� class ConnectParameterV0;

	// eClose(����ϸ��)
	eCloseUnknownConnectVersion  = 1,
	eCloseUnknownForwardType     = 2,
	eCloseForwardPortNotFound    = 3,
	eCloseConnectDuplicatePort   = 4,
	eCloseSessionAbort           = 5, // ����ʧ��
	eCloseSessionClose           = 6, // ���ӹر�
	eCloseForwardAckPortNotFound = 7,
	eCloseActiveFromConsole      = 8,
	eCloseConnectUnsupported     = 9,
	eCloseForwardProcessExp      = 10,
	eCloseNoAuthority            = 11,
	eCloseNoAuthPort             = 12,
	eCloseNoAuthAidOrZoneid      = 13,

	// eForward
	eForwardRaw = 0, // ԭ�����ݣ�ֱ��ת����û��ѹ����Ҳû�м��ܡ�

	eAuthoritySet = 1, // ���ݲ�������ָ�����û�����Ȩ�����ؾɵ���Ȩ��
	eAuthorityGet = 2, // ��ѯָ���û���Ȩ����ѯ���Ҳʹ�ô˲���ָ��ء�

	/////////////////////////////////////////////////////////////////////////////
	// ������������һ���ǹ����ģ�
	// Windows ���ش��ڷ�Χ��
	eForwardWindowLowSize  = 48 * 1024, // ��δȷ�����ݳ���С�ڴ���ʱ������ת������ȡ�������Ӳ�ת������
	eForwardWindowHighSize = 64 * 1024, // ��δȷ�����ݳ��ȴ��ڴ���ʱ��ֹͣת����ֹͣ��ȡ�������ӣ���
};

struct AuthorityParameter : public GNET::Marshal
{
	int allow;
	std::set<int> ports; // Ϊ�յ�ʱ����������ȫ���˿ڡ�

	void reset()           { allow = 0; ports.clear(); }

	void addForward()      { allow |= 1; }         // �Ƿ�����ʹ�ö˿�ת����
	void addGrant()        { allow |= 2; }         // �Ƿ���������˷���Ȩ�ޡ�
	void addPort(int port) { ports.insert(port); } // �Ƿ���������ض��˿ڡ�

	bool allowForward() const      { return 0 != (allow & 1); }
	bool allowGrant() const        { return 0 != (allow & 2); }
	bool allowPort(int port) const { return ports.empty() || ports.find(port) != ports.end(); }

	// ������ap��Ȩ���ڿ���Ȩ��Χ֮�ڡ���Ȩ�߲������賬���Լ�ӵ�е�Ȩ�ޡ�
	int grantcheck(const AuthorityParameter & ap) const
	{
		if (!ap.allowForward() && !ap.ports.empty())
			return 1; // ap ����ì�ܣ�û��ת��Ȩ��ʱ������ָ���˿ڡ�

		if (ap.allow != (ap.allow & this->allow))
			return 2; // allow ��������Χ��

		if (this->ports.empty())
			return 0; // ������Ȩ����˿ڡ�

		if (ap.ports.empty())
			return 3; // ���޶˿�û����Ȩ����˿ڡ�

		for (std::set<int>::const_iterator it = ap.ports.begin(), ie = ap.ports.end(); it != ie; ++it)
		{
			if (this->ports.find(*it) == ports.end())
				return 4; // ��Ȩ�˿ڳ�������Ķ˿ڡ�
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
