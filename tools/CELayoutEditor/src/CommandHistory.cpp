#include "pch.h"
#include "CommandHistory.h"


CommandManager *CommandManager::m_instance = NULL;

CommandManager::CommandManager()
{
	size_t mPosition = 0;

	mLastProperty = 0;

	wxDocManager* m_docManager = NULL;

	// by bayaer, 文本管理
	EditorDocument*	m_document = NULL;
	
	mUnsaved = true;
	
}

void CommandManager::initialise()
{
	//mPosition = 0;
	//mLastProperty = PR_DEFAULT;
	//mEditorWidgets = _ew;
	//UndoManager::getInstance().addValue();
	//setUnsaved(false);
	CEGUI::String str("tempFile.xml");
	file.open(wxConvLibc.cWX2MB(L"tempFile.xml"), std::ios::out);

}


CommandManager* CommandManager::GetInstancePtr()
{
	// Create instance, if not done yet
	if (!m_instance)
	{
		m_instance = new CommandManager();
	}
	return m_instance;
}


// void CommandManager::addValue(int _property, CEGUI::XMLSerializer xml)
// {
// 	//setUnsaved(true);设置可以保存
// 
// 	//CEGUI::XMLSerializer temp( xml );
// 	// 记录满了， 删掉队首
// 	//if (mOperations.size() > 64) delete mOperations.Back();
// 	mOperations.push_back(xml);
// 	mPosition = 0;
////XMLSerializer xml(out_stream);