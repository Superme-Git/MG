//////////////////////////////////////////////////////////////////////////
// File:		CEGUIPropertySerializer.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIPropertySerializer_h_
#define _CEGUIPropertySerializer_h_

#include "CEGUISerializer.h"
#include "CEGUIPropertyIds.h"
#include "CEGUIXMLFileData.h"
#include "elements/CEGUIItemListBase.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class PropertySerializer : public Serializer
		{
		public:
			PropertySerializer(int propId, int version);

		protected:
			static void stringTo(const String& str, float& dst);
			static void stringTo(const String& str, uint& dst);
			static void stringTo(const String& str, int& dst);
			static void stringTo(const String& str, int64_t& dst);
			static void stringTo(const String& str, bool& dst);
			static void stringTo(const String& str, Size& dst);
			static void stringTo(const String& str, Point& dst);
			static void stringTo(const String& str, Vector3& dst);
			static void stringTo(const String& str, Rect& dst);
			static void stringTo(const String& str, colour& dst);
			static void stringTo(const String& str, ColourRect& dst);
			static void stringTo(const String& str, UDim& dst);
			static void stringTo(const String& str, UVector2& dst);
			static void stringTo(const String& str, URect& dst);
			static void stringTo(const String& str, String& dst);

			static String toString(float dst);
			static String toString(uint dst);
			static String toString(int dst);
			static String toString(int64_t dst);
			static String toString(bool dst);
			static String toString(const Size& dst);
			static String toString(const Point& dst);
			static String toString(const Vector3& dst);
			static String toString(const Rect& dst);
			static String toString(const colour& dst);
			static String toString(const ColourRect& dst);
			static String toString(const UDim& dst);
			static String toString(const UVector2& dst);
			static String toString(const URect& dst);
			static String toString(const String& dst);
		};

#define PROPERTY_SERIALIZER_CLASS_NAME(prop, ver) Property_##prop##_Serializer_v##ver

#define PROPERTY_SERIALIZER_CLASS_DECL(prop, ver)\
	class PROPERTY_SERIALIZER_CLASS_NAME(prop, ver): public PropertySerializer\
	{\
	public:\
		PROPERTY_SERIALIZER_CLASS_NAME(prop, ver)(int propId, int version): PropertySerializer(propId, version) {}\
		\
		virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);\
		virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv);\
	};

#define PROPERTY_SERIALIZER_READ_IMPL(prop, ver) bool PROPERTY_SERIALIZER_CLASS_NAME(prop, ver)::read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)

#define PROP_SET_TARGET(TargetClass, SETTER)\
	TargetClass* p##TargetClass = dynamic_cast<TargetClass*>(*ppPropRecv);\
	if(p##TargetClass)\
	{\
		p##TargetClass->SETTER(val);\
		return true;\
	}

#define PROP_SET_TARGET_b(prop, TargetClass) PROP_SET_TARGET(TargetClass, Set##prop)
#define PROP_SET_TARGET_s(prop, TargetClass) PROP_SET_TARGET(TargetClass, set##prop)

#define PROP_SET_TARGET_PROP(prop)\
	PropertySet* pPropSet = dynamic_cast<PropertySet*>(*ppPropRecv);\
	if(pPropSet && pPropSet->isPropertyPresent(#prop))\
	{\
		pPropSet->setProperty(#prop, toString(val));\
		return true;\
	}

#define PROP_SET_RENDERER(RendererClass, SETTER)\
	Window* pWindowUsing##RendererClass = dynamic_cast<Window*>(*ppPropRecv);\
	if (pWindowUsing##RendererClass)\
	{\
		RendererClass* p##RendererClass = dynamic_cast<RendererClass*>(pWindowUsing##RendererClass->getWindowRenderer()); \
		if(p##RendererClass)\
		{\
			p##RendererClass->SETTER(val);\
			return true;\
		}\
	}

#define PROP_SET_RENDERER_b(prop, RendererClass) PROP_SET_RENDERER(RendererClass, Set##prop)
#define PROP_SET_RENDERER_s(prop, RendererClass) PROP_SET_RENDERER(RendererClass, set##prop)

#define PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(prop, ver, propClass)\
	bool PROPERTY_SERIALIZER_CLASS_NAME(prop, ver)::write(Stream& stream, int version, PropertyReceiver* pPropRecv)\
	{\
		XMLFileData::PropertyData* pPropData = dynamic_cast<XMLFileData::PropertyData*>(pPropRecv);\
		if(pPropData)\
		{\
			const String& value = pPropData->mValue;

#define PROPERTY_SERIALIZER_WRITE_IMPL_END\
		}\
		return false;\
	}

#define PROPERTY_SERIALIZER_WRITE_IMPL(prop, ver, propClass)\
	bool PROPERTY_SERIALIZER_CLASS_NAME(prop, ver)::write(Stream& stream, int version, PropertyReceiver* pPropRecv)\
	{\
		XMLFileData::PropertyData* pPropData = dynamic_cast<XMLFileData::PropertyData*>(pPropRecv);\
		if(pPropData)\
		{\
			propClass val;\
			stringTo(pPropData->mValue, val);\
			stream << val;\
			return true;\
		}\
		return false;\
	}

#define PROPERTY_SERIALIZER_INSTANCE(prop, ver, useVer)\
	static PROPERTY_SERIALIZER_CLASS_NAME(prop, useVer) s_Property_##prop##_Serializer_Instance_v##ver##_use##useVer(PI_##prop, ver)

#define TPL_PROPERTY_SERIALIZER_CLASS_GS(prop, ver, GETTER, SETTER)\
	template <class PropClass, class TargetClass>\
	class PROPERTY_SERIALIZER_CLASS_NAME(prop, ver): public PropertySerializer\
	{\
	public:\
		PROPERTY_SERIALIZER_CLASS_NAME(prop, ver)(int propId, int version): PropertySerializer(propId, version) {}\
		\
		virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)\
		{\
			PropClass propVal;\
			stream >> propVal;\
			\
			TargetClass* pWindow = dynamic_cast<TargetClass*>(*ppPropRecv);\
			if(!pWindow)\
			{\
				return false;\
			}\
			\
			pWindow->SETTER(propVal);\
			return true;\
		}\
		\
		virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv)\
		{\
			XMLFileData::PropertyData* pPropData = dynamic_cast<XMLFileData::PropertyData*>(pPropRecv);\
			if(pPropData)\
			{\
				PropClass val;\
				stringTo(pPropData->mValue, val);\
				stream << val;\
				return true;\
			}\
			return false;\
		}\
	}

#define TPL_PROPERTY_SERIALIZER_CLASS_b(prop, ver) TPL_PROPERTY_SERIALIZER_CLASS_GS(prop, ver, Get##prop, Set##prop)
#define TPL_PROPERTY_SERIALIZER_CLASS_s(prop, ver) TPL_PROPERTY_SERIALIZER_CLASS_GS(prop, ver, get##prop, set##prop)

#define TPL_PROPERTY_SERIALIZER_CLASS(prop, ver) TPL_PROPERTY_SERIALIZER_CLASS_s(prop, ver)

#define TPL_PROPERTY_SERIALIZER_INSTANCE(prop, PropClass, TargetClass, ver, useVer)\
	static PROPERTY_SERIALIZER_CLASS_NAME(prop, useVer)<PropClass, TargetClass> s_##TargetClass##_Property_##prop##_Serializer_Instance_v##ver##_use##useVer(PI_##prop, ver)

	}
}

#endif