#include "pch.h"
#include "CommandHistory.h"


CommandManager *CommandManager::m_instance = NULL;

CommandManager::CommandManager()
{
	size_t mPosition = 0;

	mLastProperty = 0;

	wxDocManager* m_docManager = NULL;

	// by bayaer, �ı�����
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
// 	//setUnsaved(true);���ÿ��Ա���
// 
// 	//CEGUI::XMLSerializer temp( xml );
// 	// ��¼���ˣ� ɾ������
// 	//if (mOperations.size() > 64) delete mOperations.Back();
// 	mOperations.push_back(xml);
// 	mPosition = 0;
////XMLSerializer xml(out_stream);