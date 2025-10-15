#ifndef __Nuclear_EFFECTKEYTABLE_H_
#define __Nuclear_EFFECTKEYTABLE_H_

#if _MSC_VER > 1000
#pragma once
#endif
#include "..\common\xml.h"
#include "keyframe.h"

namespace Nuclear
{

	class CPosKeyTable
		: public CKeyTable<CPosKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	x;
			float	y;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;
				frame = (float)keyNode.GetFloatAttribute( L"frame");
				x = (float)keyNode.GetFloatAttribute( L"x");
				y = (float)keyNode.GetFloatAttribute( L"y");

				Insert( CPosKeyValue( frame, XPVECTOR2(x,y)));
			}

			return true;

		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CPosKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"x", key->GetValue().x);
				keyNode.SetAttribute( L"y", key->GetValue().y);
			}

			return true;
		}

	};


	class CScaleKeyTable
		: public CKeyTable<CScaleKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	x;
			float	y;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;
				frame = (float)keyNode.GetFloatAttribute( L"frame");
				x = (float)keyNode.GetFloatAttribute( L"x");
				y = (float)keyNode.GetFloatAttribute( L"y");

				Insert( CScaleKeyValue( frame, XPVECTOR2(x,y)));
			}

			return true;

		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CScaleKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"x", key->GetValue().x);
				keyNode.SetAttribute( L"y", key->GetValue().y);
			}

			return true;
		}

	};


	class CRotationKeyTable
		: public CKeyTable<CRotationKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	z;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;
				frame = (float)keyNode.GetFloatAttribute( L"frame");
				z = (float)keyNode.GetFloatAttribute( L"z");

				Insert( CRotationKeyValue( frame, z));
			}

			return true;

		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CRotationKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"z", key->GetValue());
			}

			return true;
		}

	};

	class CDivideKeyTable
		: public CKeyTable<CDivideKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	div;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;

				frame = (float)keyNode.GetFloatAttribute( L"frame");
				div = (float)keyNode.GetFloatAttribute( L"d");

				Insert( CDivideKeyValue( frame, div));
			}

			return true;

		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CDivideKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"d", key->GetValue());
			}

			return true;
		}

	};


	class CAlphaKeyTable
		: public CKeyTable<CAlphaKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	a;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;

				frame = (float)keyNode.GetFloatAttribute( L"frame");
				a = (float)keyNode.GetFloatAttribute( L"a");

				Insert( CAlphaKeyValue( frame, a));
			}

			return true;
		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CAlphaKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"a", key->GetValue());
			}
			return true;
		}

	};


	class CColorKeyTable
		: public CKeyTable<CColorKeyValue>
	{
	public:
		virtual bool operator<<( void* data)
		{
			XMLIO::CINode& node = *(( XMLIO::CINode*)(data));

			XMLIO::CNodeList keyNodeList;
			node.GetChildren( keyNodeList);
			float	frame;
			float	b;
			float	g;
			float	r;
			float	a;

			for ( XMLIO::CNodeList::iterator i = keyNodeList.begin(), e = keyNodeList.end(); i != e; ++i)
			{
				XMLIO::CINode& keyNode = *i;
				if (keyNode.GetType() != XMLIO::NT_ELEMENT)
					continue;

				frame = (float)keyNode.GetFloatAttribute( L"frame");
				b = (float)keyNode.GetFloatAttribute( L"b");
				g = (float)keyNode.GetFloatAttribute( L"g");
				r = (float)keyNode.GetFloatAttribute( L"r");
				a = (float)keyNode.GetFloatAttribute( L"a");

				Insert( CColorKeyValue( frame, XPVECTOR4(b, g, r, a)));
			}

			return true;

		}

		virtual bool operator>>( void* data)
		{
			XMLIO::CONode& node = *((XMLIO::CONode*) data);

			unsigned int keyCount = GetKeyCount();
			for ( unsigned int i = 0; i < keyCount; ++i)
			{
				const CColorKeyValue* key = GetKey( i);
				assert( key != NULL);
				if ( key == NULL)
					continue;

				XMLIO::CONode keyNode;
				node.CreateSubElement( L"key", keyNode);
				keyNode.SetAttribute( L"frame", key->GetFrame());
				keyNode.SetAttribute( L"b", key->GetValue().x);
				keyNode.SetAttribute( L"g", key->GetValue().y);
				keyNode.SetAttribute( L"r", key->GetValue().z);
				keyNode.SetAttribute( L"a", key->GetValue().w);
			}

			return true;
		}

	};





}	// namesapce Nuclear

#endif	//__Nuclear_EFFECTKEYTABLE_H_
