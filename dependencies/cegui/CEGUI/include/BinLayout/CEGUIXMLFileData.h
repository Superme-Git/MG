//////////////////////////////////////////////////////////////////////////
// File:		CEGUIXMLFileData.h
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIXMLFileData_h_
#define _CEGUIXMLFileData_h_

#include "CEGUIXMLHandler.h"
#include "CEGUIString.h"
#include "CEGUIProperty.h"
#include "CEGUIBinLayoutFileDefine.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT XMLFileData : public XMLHandler
		{
		public:
			class NodeData: public PropertyReceiver
			{
			public:
				typedef std::vector<NodeData*> Array;

				NodeData* mpParent;
				Array mChildNodes;

			public:
				NodeData() : mpParent(NULL) {}

				virtual ~NodeData()
				{
					for (Array::iterator it = mChildNodes.begin(); it != mChildNodes.end(); ++it)
					{
						NodeData* pChild = *it;
						delete pChild;
					}
					mChildNodes.clear();
				}

				virtual NodeType getType() const = 0;

				NodeData* getParent() const { return mpParent; }

				int getChildCount() const { return (int)mChildNodes.size(); }
				NodeData* getChild(int index) const { return mChildNodes[index]; }

				void addChild(NodeData* pData)
				{
					mChildNodes.push_back(pData);
					pData->mpParent = this;
				}
			};

			class WindowData : public NodeData
			{
			public:
				String mType;
				String mName;

			public:
				virtual NodeType getType() const { return NT_Window; }
			};

			class AutoWindowData : public NodeData
			{
			public:
				String mNameSuffix;

			public:
				virtual NodeType getType() const { return NT_AutoWindow; }
			};

			class LayoutImportData : public NodeData
			{
			public:
				String mPrefix;
				String mFilename;
				String mResourceGroup;

			public:
				virtual NodeType getType() const { return NT_LayoutImport; }
			};

			class EventData : public NodeData
			{
			public:
				String mName;
				String mFunction;

			public:
				virtual NodeType getType() const { return NT_Event; }
			};

			class PropertyData : public NodeData
			{
			public:
				String mName;
				String mValue;

			public:
				virtual NodeType getType() const { return NT_Property; }
			};

		private:
			NodeData* mpRootData;
			NodeData* mpCurrentData;

		public:
			XMLFileData();
			virtual ~XMLFileData();

			virtual void elementStart(const String& element, const XMLAttributes& attributes);
			virtual void elementEnd(const String& element);
			virtual void text(const String& text) {}

			NodeData* getRootData() const { return mpRootData; }

		private:
			void elementGUILayoutStart(const XMLAttributes& attributes);
			NodeData* elementWindowStart(const XMLAttributes& attributes);
			NodeData* elementAutoWindowStart(const XMLAttributes& attributes);
			NodeData* elementPropertyStart(const XMLAttributes& attributes);
			NodeData* elementLayoutImportStart(const XMLAttributes& attributes);
			NodeData* elementEventStart(const XMLAttributes& attributes);
		};
	}
}

#endif