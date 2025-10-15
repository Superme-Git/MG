//////////////////////////////////////////////////////////////////////////
// File:		CEGUINodeSerializer_v1.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUINodeSerializer_v1_h_
#define _CEGUINodeSerializer_v1_h_

#include "../CEGUISerializer.h"
#include "../CEGUIXMLFileData.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class NodeSerializer_v1 : public Serializer
		{
		public:
			virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv);

		private:
			bool readWindow(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			bool writeWindow(Stream& stream, int version, XMLFileData::WindowData* pData);

			bool readAutoWindow(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			bool writeAutoWindow(Stream& stream, int version, XMLFileData::AutoWindowData* pData);

			bool readLayoutImport(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			bool writeLayoutImport(Stream& stream, int version, XMLFileData::LayoutImportData* pData);

			bool readEvent(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			bool writeEvent(Stream& stream, int version, XMLFileData::EventData* pData);

			bool readProperties(Stream& stream, int& version, Window** ppWindow, Context* pContext);
			bool writeProperties(Stream& stream, int version, XMLFileData::NodeData* pData);

			bool readChilds(Stream& stream, int& version, Window** ppWindow, Context* pContext);
			bool writeChilds(Stream& stream, int version, XMLFileData::NodeData* pData);

			void cleanupWindow(Window* wnd);
		};
	}
}

#endif