/***********************************************************************
filename: 	CSceneMovieNpc.h
author:		eagle
purpose:	场景动画上的NPC
*************************************************************************/
#pragma once
#include "ComponentsObject.h"

class MovieSceneNpc : public ComponentsObject
{
public:
	MovieSceneNpc();
	virtual ~MovieSceneNpc();

	Nuclear::TextBlockHandle m_TitleTextureHandle;  //称谓纹理句柄
	Nuclear::ITextBlock*	 m_pTitleTextBlock; //称谓的TextBlock
	
	void SetTitle(int title){ if(title != m_data.title) m_data.title = title;}
	int GetTitle() const { return m_data.title; }
	std::wstring GetName(){ return m_data.name; }
	void UpdateAction(Nuclear::XPUSA_TYPE type);
	void Run(int now,int delta);
	void Draw(int now);
	bool SetAttribute(const stMovieSceneNpcData& pData);
	void SetArea1Color(int color) { }
	void SetArea2Color(int color) { }

	virtual int GetShapeID(){return m_data.model;}
	bool IsCharacterModel(){ return m_data.model<1000 ||m_data.model>1000000; }
	
	virtual void StopMove();

	virtual bool PlayAction(eActionType actionType,float freq = g_defaultActionFrequence);

private:
	stMovieSceneNpcData	m_data;
};
