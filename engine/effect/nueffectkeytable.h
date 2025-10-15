#ifndef __Nuclear_EFFECTKEYTABLE_H_
#define __Nuclear_EFFECTKEYTABLE_H_

#include "../common/nuxml.h"
#include "nukeyframe.h"
#include <platform/platform_types.h>

namespace Nuclear
{

	class CPosKeyTable : public CKeyTable<CPosKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	x;
			float	y;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				x = (float)_wtof(keyNodeList[i]->first_attribute(L"x")->value());
				y = (float)_wtof(keyNodeList[i]->first_attribute(L"y")->value());

				Insert( CPosKeyValue( frame, NuclearVector2(x,y)));
			}

			return true;
		}

	};


	class CScaleKeyTable : public CKeyTable<CScaleKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	x;
			float	y;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				x = (float)_wtof(keyNodeList[i]->first_attribute(L"x")->value());
				y = (float)_wtof(keyNodeList[i]->first_attribute(L"y")->value());

				Insert( CScaleKeyValue( frame, NuclearVector2(x,y)));
			}

			return true;
		}

	};


	class CRotationKeyTable : public CKeyTable<CRotationKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	z;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				z = (float)_wtof(keyNodeList[i]->first_attribute(L"z")->value());

				Insert( CRotationKeyValue( frame, z));
			}

			return true;
		}

	};

	class CDivideKeyTable : public CKeyTable<CDivideKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	div;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				div = (float)_wtof(keyNodeList[i]->first_attribute(L"d")->value());

				Insert( CDivideKeyValue( frame, div));
			}

			return true;
		}

	};


	class CAlphaKeyTable : public CKeyTable<CAlphaKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	a;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				a = (float)_wtof(keyNodeList[i]->first_attribute(L"a")->value());

				Insert( CAlphaKeyValue( frame, a));
			}

			return true;
		}

	};


	class CColorKeyTable : public CKeyTable<CColorKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			LJXML::LJXML_Node<LJXML::Char>* pNode = (LJXML::LJXML_Node<LJXML::Char>*)data;
			LJXML::LJXML_NodeList keyNodeList;
			pNode->GetSubNodeList(keyNodeList);

			float	frame;
			float	b;
			float	g;
			float	r;
			float	a;

			for (size_t i = 0; i < keyNodeList.size(); ++i)
			{
				if (keyNodeList[i]->type() != rapidxml::node_element)
					continue;

				frame = (float)_wtof(keyNodeList[i]->first_attribute(L"frame")->value());
				b = (float)_wtof(keyNodeList[i]->first_attribute(L"b")->value());
				g = (float)_wtof(keyNodeList[i]->first_attribute(L"g")->value());
				r = (float)_wtof(keyNodeList[i]->first_attribute(L"r")->value());
				a = (float)_wtof(keyNodeList[i]->first_attribute(L"a")->value());

				Insert( CColorKeyValue( frame, NuclearVector4(b, g, r, a)));
			}

			return true;
		}

	};

}	// namesapce Nuclear

#endif	//__Nuclear_EFFECTKEYTABLE_H_
