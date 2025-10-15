#include "StdAfx.h"
#include "TempFileTestCase.h"

#include "../pfs2.h"
#include "../src/native/rawfileio.h"
#include "../src/native/tempfile.h"

//CPPUNIT_TEST_SUITE_REGISTRATION( CTempFileTestCase);

CTempFileTestCase::CTempFileTestCase(void)
{
}

CTempFileTestCase::~CTempFileTestCase(void)
{
}

void CTempFileTestCase::testOpen()
{
	PFS::CTempFile tf;
	bool b = tf.Open();
	CPPUNIT_ASSERT( b);
}

void CTempFileTestCase::testClose()
{
	PFS::CTempFile tf;
	bool b = tf.Open();
	std::wstring filename = tf.GetFileName();
	tf.Close();

	CPPUNIT_ASSERT( !PFS::CFileIO::IsFileExisting( filename));
}

void CTempFileTestCase::testGetFileName()
{
	PFS::CTempFile tf1;
	bool b = tf1.Open();
	std::wstring filename1 = tf1.GetFileName();

	PFS::CTempFile tf2;
	b = tf2.Open();
	std::wstring filename2 = tf2.GetFileName();

	CPPUNIT_ASSERT( filename1 != filename2);
}