#pragma once

#include "../xmliocommon.h"

namespace XMLIO {

class CAbstractINode : public CNoTrackData, public CUncopyableObject
{
public:
	CAbstractINode() {}
	virtual ~CAbstractINode() {}
public:
	virtual CAbstractINode* Clone() const = 0;
	virtual void Delete() { delete this; }
public:
	virtual NodeType GetType() const = 0;
	
	virtual size_t GetChildrenCount() const = 0;
	
	virtual bool GetChildAt( size_t index, CINode& rNode) const = 0;

	virtual void GetAttributeNames( CAttributesNames& names) const = 0;
	virtual bool GetAttribute( const std::wstring& name, std::wstring& value) const = 0;

	virtual void GetText( std::wstring& value) const = 0;
	virtual void GetSubText( std::wstring& value) const = 0;
	virtual void GetName( std::wstring& value) const = 0;
};

class CAbstractReader : public CNoTrackData, public CUncopyableObject
{
public:
	CAbstractReader() {}
	virtual ~CAbstractReader() {}
public:
	virtual int OpenData( const PFS::CImageData& data) = 0;
	virtual int OpenData( const PFS::CDataBlock& data) = 0;
	virtual int OpenFile( PFS::CBaseFile& bf) = 0;
	virtual int OpenNativeFile( const std::wstring& filename) = 0;
	virtual void Close() = 0;
	virtual bool GetRootNode(CINode& rNode) const = 0;

public:
	// 转换接口，不强制实现，返回false表示不支持该接口
	virtual bool EncodeToBinary( PFS::CDataBlock& data){ return false; }
	virtual bool EncodeToXML( std::wstring& str){ return false; }
};

} // namespace XMLIO {