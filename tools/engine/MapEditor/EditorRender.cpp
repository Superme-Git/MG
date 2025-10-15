#include "StdAfx.h"
#include "EditorRender.h"

void DestroyEditorRenderer(Nuclear::Renderer *r)
{ 
	if( r != NULL ) 
	{
		r->Destroy();
		delete r;
	}
}

bool CreateEditorRenderer(Nuclear::Renderer **ppr, HWND hwnd, const Nuclear::XDisplayMode &dmode, Nuclear::CFileIOManager *pFileIOMan, DWORD flags)
{
	if( ppr == NULL ) 
		return false;
	*ppr = new CEditorRender(pFileIOMan);
	if( *ppr == NULL ) 
		return false;

	if( !(*ppr)->Create(hwnd, dmode, flags) )
	{
		(*ppr)->Destroy();
		delete *ppr;
		*ppr = NULL;
		return false;
	}
	return true;
}

CEditorRender::~CEditorRender(void)
{
}

CEditorRender::CEditorRender(Nuclear::CFileIOManager *pFileIOMan) : DX9Renderer(pFileIOMan)
{
	m_bDrawLayerChunk = false;
	m_ChunkHandle = Nuclear::INVALID_PICTURE_HANDLE;
}

