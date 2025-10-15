#pragma once

#include "../xmliocommon.h"

namespace XMLIO {

class CCOMException {};

class CXMLException
{
	int		m_nError;
public:
	CXMLException( int error) : m_nError( error) {}
	~CXMLException() {}
public:
	int GetError() const { return m_nError; }
};

struct CINode::Data : public CNoTrackData
{
	Data() : Node( NULL) {}
	~Data();
	CAbstractINode*		Node;
};

struct CFileReader::Data
{
	Data() : Reader( NULL) {}
	~Data();
	
	CAbstractReader*	Reader;

	void CreateReader( unsigned int type);
	void DeleteReader();
};

const unsigned int OCTETS_IO_PREFIX = 0xEFCDAB89;

} //namespace XMLIO {