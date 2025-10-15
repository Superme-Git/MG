/***********************************************************************
    filename:   CEGUISpecialTreeItem_xmlHandler.h
*************************************************************************/

#ifndef _CEGUISpecialTreeItem_xmlHandler_h_
#define _CEGUISpecialTreeItem_xmlHandler_h_

#include "CEGUIXMLHandler.h"


// Start of CEGUI namespace section

namespace CEGUI
{
//! Handler class used to parse the Font XML files to create Font objects.
class SpecialTreeItem;
class CEGUIEXPORT SpecialTreeItem_xmlHandler : public XMLHandler
{
public:
    
    static const String TextElement;
	static const String ImageElement;
	static const String GoToElement; //自动寻路链接

	static const String TextAttribute;
	static const String ColorAttribute;
	static const String FontAttribute;

	static const String ImageSetNameAttribute;
	static const String ImageAttribute;

	static const String ChefuAttribute;
    static const String GoToDirectlyAttribute;
	static const String MapAttribute;
	static const String XPosAttribute;
	static const String YPosAttribute;
	static const String NpcidAttribute;
	static const String idAttribute;
	static const String TextColor;



    //! Constructor.
    SpecialTreeItem_xmlHandler(const String& filename, const String& resource_group);
	SpecialTreeItem_xmlHandler(const String& parseText,SpecialTreeItem* pSpecialTreeItem);


    //! Destructor.
    ~SpecialTreeItem_xmlHandler();

    //! Return reference to the created Richeditbox object.
    SpecialTreeItem& getObject() const;
	void SetObject(SpecialTreeItem* pSpecialTreeItem){ d_SpecialTreeItem=pSpecialTreeItem; }

    // XMLHandler overrides
    void elementStart(const String& element, const XMLAttributes& attributes);
    void elementEnd(const String& element);

	static ColourRect GetColourectByIndex(size_t index);

private:
    //! handles the opening Font XML element.
    void elementTextStart(const XMLAttributes& attributes);
    //! handles the closing Font XML element.
    void elementTextEnd();
    //! handles the opening Mapping XML element.
    void elementImageStart(const XMLAttributes& attributes);

    void elementGoToLinkStart(const XMLAttributes& attributes);

    //! Font object that we are constructing.
    SpecialTreeItem* d_SpecialTreeItem;
    //! inidcates whether client read the created object
    mutable bool d_objectRead;
};

} // End of  CEGUI namespace section

#endif
