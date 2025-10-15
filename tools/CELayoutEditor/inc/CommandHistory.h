///////////////////////////////////////////////////////////////////////////////
//  For project details and authors, refer to "CELayoutEditor.cpp".
//
//  This file is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; either version 2
//  of the License, or (at your option) any later version.
//
//  This file is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//  To view the licence online, go to: http://www.gnu.org/copyleft/gpl.html
////////////////////////////////////////////////////////////////////////////////

#ifndef _COMMAND_HISTORY_H_
#define _COMMAND_HISTORY_H_

//////////////////////////////////////////////////////////////////////////
// INCLUDES
//////////////////////////////////////////////////////////////////////////


#include "CyclicBuffer.h"
#include "CEGUIXMLParser.h"
#include "EditorDocument.h"
#include <ostream>

#include "WindowBox.h"

namespace CommandHistory
{
	enum { PR_DEFAULT, PR_POSITION, PR_PROPERTIES, PR_KEY_POSITION };
}
	//typedef CEGUI::delegates::CMultiDelegate1<bool> Event_Changes;

enum OPERATER_TYPE
{ 
	PR_DEFAULT, PR_POSITION, PR_PROPERTIES, PR_KEY_POSITION
};

class CommandManager 
{
public:
	CommandManager();
	 ~CommandManager();


	 //void addValue(int _property, CEGUI::XMLSerializer xml);

	 void Undo();											// Undo the previous command
	 void Redo();											// Redo the previously undone command
	 bool CanUndo() const;								// Is a command available to be undone?
	 bool CanRedo() const;								// Is a command available to be redone?
	//const CCommand* GetUndoCommand() const;		// Returns the available undo command, if any
	//const CCommand* GetRedoCommand() const;		// Returns the available redo command, if any

	 void initialise();			// by bayaer, 初始化
public:
	CommandManager* GetInstancePtr();
		

private:
	static CommandManager *m_instance;

	// position in the bufer (0 - newest element)
	size_t mPosition;

	//std::vector<int> mOperations;
	std::vector<CEGUI::Window> mOperations;
	//CyclicBuffer<XMLSerializer > mOperations;
	int mLastProperty;


	//Scheme *mScheme;
	//EditorWidgets* mEditorWidgets;


	// by bayaer, 文档管理器
	wxDocManager* m_docManager;

	// by bayaer, 文本管理
	EditorDocument*	m_document;
	std::ofstream file;


	bool mUnsaved;



};




#endif