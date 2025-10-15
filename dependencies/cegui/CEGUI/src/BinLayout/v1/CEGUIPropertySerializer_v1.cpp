//////////////////////////////////////////////////////////////////////////
// File:		CEGUIPropertySerializer_v1.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/v1/CEGUIPropertySerializer_v1.h"
#include "elements/CEGUIAnimationButton.h"
#include "elements/CEGUICheckbox.h"
#include "elements/CEGUICombobox.h"
#include "elements/CEGUIEditbox.h"
#include "elements/CEGUIMultiLineEditbox.h"
#include "elements/CEGUIRichEditbox.h"
#include "elements/CEGUIListbox.h"
#include "elements/CEGUIMultiColumnList.h"
#include "elements/CEGUIScrollablePane.h"
#include "elements/CEGUIScrolledItemListBase.h"
#include "elements/CEGUITree.h"
#include "elements/CEGUIDragContainer.h"
#include "elements/CEGUITitlebar.h"
#include "CEGUIPropertyHelper.h"
#include "elements/CEGUIProgressBar.h"
#include "elements/CEGUIProgressBarTwoValue.h"
#include "elements/CEGUIFrameWindow.h"
#include "WindowRendererSets/Falagard/FalStatic.h"
#include "elements/CEGUIGroupButton.h"
#include "elements/CEGUIRadioButton.h"
#include "elements/CEGUIIrregularButton.h"
#include "elements/CEGUIItemCell.h"
#include "elements/CEGUIItemTable.h"
#include "elements/CEGUIItemListbox.h"
#include "elements/CEGUIPopupMenu.h"
#include "elements/CEGUISpinner.h"
#include "elements/CEGUIPushButton.h"
#include "elements/CEGUIScrolledContainer.h"
#include "elements/CEGUISlider.h"
#include "elements/CEGUISwitch.h"
#include "elements/CEGUITabControl.h"
#include "elements/CEGUIThumb.h"
#include "elements/CEGUITooltip.h"
#include "elements/CEGUILinkText.h"
#include "WindowRendererSets/Falagard/FalGroupBtnTree.h"
#include "WindowRendererSets/Falagard/FalEditbox.h"
#include "WindowRendererSets/Falagard/FalMultiLineEditbox.h"
#include "WindowRendererSets/Falagard/FalRichEditbox.h"
#include "WindowRendererSets/Falagard/FalListHeader.h"
#include "WindowRendererSets/Falagard/FalProgressBar.h"
#include "WindowRendererSets/Falagard/FalScrollbar.h"
#include "WindowRendererSets/Falagard/FalSlider.h"
#include "WindowRendererSets/Falagard/FalStaticImage.h"
#include "WindowRendererSets/Falagard/FalStaticText.h"
#include "WindowRendererSets/Falagard/FalTabControl.h"
#include "utils/StringUtil.h"
#include "CEGUIFreeTypeFont.h"
#include "CEGUIPixmapFont.h"

namespace CEGUI
{
	namespace BinLayout
	{
		PROPERTY_SERIALIZER_READ_IMPL(ImageSetName, 1)
		{
			String val;
			stream >> val;

			PROP_SET_TARGET_b(ImageSetName, AnimationButton);
			PROP_SET_TARGET_b(ImageSetName, IrregularButton);

			PROP_SET_TARGET_PROP(ImageSetName);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ImageSetName, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(ImageName, 1)
		{
			String val;
			stream >> val;

			PROP_SET_TARGET_b(ImageName, AnimationButton);
			PROP_SET_TARGET_b(ImageName, IrregularButton);

			PROP_SET_TARGET_PROP(ImageName);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ImageName, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Selected, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET_s(Selected, Checkbox);
			PROP_SET_TARGET_s(Selected, GroupButton);
			PROP_SET_TARGET_s(Selected, ItemEntry);
			PROP_SET_TARGET_s(Selected, RadioButton);

			PROP_SET_TARGET_PROP(Selected);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Selected, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ReadOnly, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET_s(ReadOnly, Combobox);
			PROP_SET_TARGET_s(ReadOnly, Editbox);
			PROP_SET_TARGET_s(ReadOnly, MultiLineEditbox);
			PROP_SET_TARGET_s(ReadOnly, RichEditbox);

			PROP_SET_TARGET_PROP(ReadOnly);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ReadOnly, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ValidationString, 1)
		{
			String val;
			stream >> val;

			PROP_SET_TARGET_s(ValidationString, Combobox);
			PROP_SET_TARGET_s(ValidationString, Editbox);

			PROP_SET_TARGET_PROP(ValidationString);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ValidationString, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(CaratIndex, 1)
		{
			int val;
			stream >> val;

			PROP_SET_TARGET_s(CaratIndex, Combobox);
			PROP_SET_TARGET_s(CaratIndex, Editbox);
			PROP_SET_TARGET_s(CaratIndex, MultiLineEditbox);

			PROP_SET_TARGET_PROP(CaratIndex);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(CaratIndex, 1, int);

		PROPERTY_SERIALIZER_READ_IMPL(ForceVertScrollbar, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(Combobox, setShowVertScrollbar);
			PROP_SET_TARGET(Listbox, setShowVertScrollbar);
			PROP_SET_TARGET(MultiColumnList, setShowVertScrollbar);
			PROP_SET_TARGET(MultiLineEditbox, setShowVertScrollbar);
			PROP_SET_TARGET(RichEditbox, setShowVertScrollbar);
			PROP_SET_TARGET(ScrollablePane, setShowVertScrollbar);
			PROP_SET_TARGET(ScrolledItemListBase, setShowVertScrollbar);
			PROP_SET_TARGET(Tree, setShowVertScrollbar);

			PROP_SET_TARGET_PROP(ForceVertScrollbar);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ForceVertScrollbar, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ForceHorzScrollbar, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(Combobox, setShowHorzScrollbar);
			PROP_SET_TARGET(Listbox, setShowHorzScrollbar);
			PROP_SET_TARGET(MultiColumnList, setShowHorzScrollbar);
			PROP_SET_TARGET(ScrollablePane, setShowHorzScrollbar);
			PROP_SET_TARGET(ScrolledItemListBase, setShowHorzScrollbar);
			PROP_SET_TARGET(Tree, setShowHorzScrollbar);

			PROP_SET_TARGET_PROP(ForceHorzScrollbar);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ForceHorzScrollbar, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(DraggingEnabled, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET_s(DraggingEnabled, DragContainer);
			PROP_SET_TARGET_s(DraggingEnabled, Titlebar);

			PROP_SET_TARGET_PROP(DraggingEnabled);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(DraggingEnabled, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(DragCursorImage, 1)
		{
			String strImg;
			stream >> strImg;

			const Image* val = PropertyHelper::stringToImage(strImg);

			PROP_SET_TARGET_s(DragCursorImage, DragContainer);

			{
				const String& val = strImg;
				PROP_SET_TARGET_PROP(DragCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(DragCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(SelectionStart, 1)
		{
			uint val;
			stream >> val;

			PROP_SET_TARGET_s(SelectionStart, Editbox);
			PROP_SET_TARGET_s(SelectionStart, MultiLineEditbox);

			PROP_SET_TARGET_PROP(SelectionStart);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SelectionStart, 1, uint);

		PROPERTY_SERIALIZER_READ_IMPL(SelectionLength, 1)
		{
			uint val;
			stream >> val;

			PROP_SET_TARGET_s(SelectionLength, Editbox);
			PROP_SET_TARGET_s(SelectionLength, MultiLineEditbox);

			PROP_SET_TARGET_PROP(SelectionLength);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SelectionLength, 1, uint);

		PROPERTY_SERIALIZER_READ_IMPL(MaxTextLength, 1)
		{
			uint val;
			stream >> val;

			PROP_SET_TARGET_s(MaxTextLength, Editbox);
			PROP_SET_TARGET_s(MaxTextLength, MultiLineEditbox);
			PROP_SET_TARGET_s(MaxTextLength, RichEditbox);

			PROP_SET_TARGET_PROP(MaxTextLength);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MaxTextLength, 1, uint);

		PROPERTY_SERIALIZER_READ_IMPL(NormalTextColour, 1)
		{
			colour val;
			stream >> val;

			PROP_SET_TARGET_PROP(NormalTextColour);

			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(NormalTextColour, 1, colour);

		PROPERTY_SERIALIZER_READ_IMPL(SelectedTextColour, 1)
		{
			colour val;
			stream >> val;

			PROP_SET_TARGET_PROP(SelectedTextColour);

			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SelectedTextColour, 1, colour);

		PROPERTY_SERIALIZER_READ_IMPL(ActiveSelectionColour, 1)
		{
			colour val;
			stream >> val;

			PROP_SET_TARGET_PROP(ActiveSelectionColour);

			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ActiveSelectionColour, 1, colour);

		PROPERTY_SERIALIZER_READ_IMPL(InactiveSelectionColour, 1)
		{
			colour val;
			stream >> val;

			PROP_SET_TARGET_PROP(InactiveSelectionColour);

			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(InactiveSelectionColour, 1, colour);

		PROPERTY_SERIALIZER_READ_IMPL(BackGroundEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(Editbox, SetFrameEnabled);
			PROP_SET_TARGET(MultiLineEditbox, SetFrameEnable);
			PROP_SET_TARGET(RichEditbox, SetBackGroundEnable);

			PROP_SET_TARGET_PROP(BackGroundEnable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BackGroundEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(FrameEnabled, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET_s(FrameEnabled, FrameWindow);
			PROP_SET_RENDERER_s(FrameEnabled, FalagardStatic);

			PROP_SET_TARGET_PROP(FrameEnabled);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(FrameEnabled, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(RollUpState, 1)
		{
			bool val;
			stream >> val;

			FrameWindow* pFrameWindow = dynamic_cast<FrameWindow*>(*ppPropRecv);
			if (pFrameWindow)
			{
				if (val != pFrameWindow->isRolledup())
				{
					pFrameWindow->toggleRollup();
				}
				return true;
			}

			PROP_SET_TARGET_PROP(RollUpState);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(RollUpState, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(NSSizingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(NSSizingCursorImage, FrameWindow);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(NSSizingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(NSSizingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(EWSizingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(EWSizingCursorImage, FrameWindow);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(EWSizingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(EWSizingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(NWSESizingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(NWSESizingCursorImage, FrameWindow);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(NWSESizingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(NWSESizingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(NESWSizingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(NESWSizingCursorImage, FrameWindow);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(NESWSizingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(NESWSizingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(GroupID, 1)
		{
			uint val;
			stream >> val;

			PROP_SET_TARGET_s(GroupID, GroupButton);
			PROP_SET_TARGET_s(GroupID, RadioButton);

			PROP_SET_TARGET_PROP(GroupID);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(GroupID, 1, uint);

		PROPERTY_SERIALIZER_READ_IMPL(ItemCellStyle, 1)
		{
			ItemCellStyle val;
			stream >> val;

			PROP_SET_TARGET(ItemCell, SetStyle);
			PROP_SET_TARGET_b(ItemCellStyle, ItemTable);

			PROP_SET_TARGET_PROP(ItemCellStyle);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(ItemCellStyle, 1, ItemCellStyle)
		{
			const String& value = pPropData->mValue;

			CEGUI::ItemCellStyle style;

			if (value == "IconInside")
			{
				style = ItemCellStyle_IconInside;
			}
			else if (value == "IconExtend")
			{
				style = ItemCellStyle_IconExtend;
			}

			stream << style;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(SortMode, 1)
		{
			ItemListBase::SortMode val;
			stream >> val;

			PROP_SET_TARGET(ItemListBase, setSortMode);

			PROP_SET_TARGET_PROP(SortMode);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(SortMode, 1, ItemListBase::SortMode)
		{
			const String& value = pPropData->mValue;

			ItemListBase::SortMode sm = ItemListBase::Ascending;
			if (value == "Descending")
			{
				sm = ItemListBase::Descending;
			}
			else if (value == "UserSort")
			{
				sm = ItemListBase::UserSort;
			}

			stream << sm;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(MultiSelect, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(ItemListbox, setMultiSelectEnabled);
			PROP_SET_TARGET(Listbox, setMultiselectEnabled);
			PROP_SET_TARGET(Tree, setMultiselectEnabled);

			PROP_SET_TARGET_PROP(MultiSelect);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MultiSelect, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(RowCount, 1)
		{
			int val;
			stream >> val;

			PROP_SET_TARGET_b(RowCount, ItemTable);

			if (dynamic_cast<MultiColumnList*>(*ppPropRecv))
			{
				return true;
			}

			PROP_SET_TARGET_PROP(RowCount);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(RowCount, 1, int);

		PROPERTY_SERIALIZER_READ_IMPL(Sort, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(Listbox, setSortingEnabled);
			PROP_SET_TARGET(Tree, setSortingEnabled);

			PROP_SET_TARGET_PROP(Sort);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Sort, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ItemTooltips, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(Listbox, setItemTooltipsEnabled);
			PROP_SET_TARGET(Tree, setItemTooltipsEnabled);

			PROP_SET_TARGET_PROP(ItemTooltips);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ItemTooltips, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ColumnsSizable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(ListHeader, setColumnSizingEnabled);
			PROP_SET_TARGET(MultiColumnList, setUserColumnSizingEnabled);

			PROP_SET_TARGET_PROP(ColumnsSizable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ColumnsSizable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ColumnsMovable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(ListHeader, setColumnDraggingEnabled);
			PROP_SET_TARGET(MultiColumnList, setUserColumnDraggingEnabled);

			PROP_SET_TARGET_PROP(ColumnsMovable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ColumnsMovable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(SortSettingEnabled, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(ListHeader, setSortingEnabled);
			PROP_SET_TARGET(MultiColumnList, setUserSortControlEnabled);

			PROP_SET_TARGET_PROP(SortSettingEnabled);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SortSettingEnabled, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(SortDirection, 1)
		{
			ListHeaderSegment::SortDirection val;
			stream >> val;

			PROP_SET_TARGET_s(SortDirection, ListHeader);
			PROP_SET_TARGET_s(SortDirection, ListHeaderSegment);
			PROP_SET_TARGET_s(SortDirection, MultiColumnList);

			PROP_SET_TARGET_PROP(SortDirection);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(SortDirection, 1, ListHeaderSegment::SortDirection)
		{
			ListHeaderSegment::SortDirection dir;

			if (value == "Ascending")
			{
				dir = ListHeaderSegment::Ascending;
			}
			else if (value == "Descending")
			{
				dir = ListHeaderSegment::Descending;
			}
			else
			{
				dir = ListHeaderSegment::None;
			}

			stream << dir;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(SortColumnID, 1)
		{
			uint val;
			stream >> val;

			PROP_SET_TARGET(ListHeader, setSortColumnFromID);
			PROP_SET_TARGET(MultiColumnList, setSortColumnByID);

			PROP_SET_TARGET_PROP(SortColumnID);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SortColumnID, 1, uint);

		PROPERTY_SERIALIZER_READ_IMPL(SizingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(SizingCursorImage, ListHeaderSegment);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(SizingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SizingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(MovingCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(MovingCursorImage, ListHeaderSegment);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(MovingCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MovingCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(SelectionMode, 1)
		{
			MultiColumnList::SelectionMode val;
			stream >> val;

			PROP_SET_TARGET(MultiColumnList, setSelectionMode);

			PROP_SET_TARGET_PROP(SelectionMode);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(SelectionMode, 1, MultiColumnList::SelectionMode)
		{
			const String& value = pPropData->mValue;

			MultiColumnList::SelectionMode mode;

			if (value == "RowMultiple")
			{
				mode = MultiColumnList::RowMultiple;
			}
			else if (value == "ColumnSingle")
			{
				mode = MultiColumnList::ColumnSingle;
			}
			else if (value == "ColumnMultiple")
			{
				mode = MultiColumnList::ColumnMultiple;
			}
			else if (value == "CellSingle")
			{
				mode = MultiColumnList::CellSingle;
			}
			else if (value == "CellMultiple")
			{
				mode = MultiColumnList::CellMultiple;
			}
			else if (value == "NominatedColumnSingle")
			{
				mode = MultiColumnList::NominatedColumnSingle;
			}
			else if (value == "NominatedColumnMultiple")
			{
				mode = MultiColumnList::NominatedColumnMultiple;
			}
			else if (value == "NominatedRowSingle")
			{
				mode = MultiColumnList::NominatedRowSingle;
			}
			else if (value == "NominatedRowMultiple")
			{
				mode = MultiColumnList::NominatedRowMultiple;
			}
			else
			{
				mode = MultiColumnList::RowSingle;
			}

			stream << mode;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(ColumnHeader, 1)
		{
			String value;
			stream >> value;

			size_t idstart = value.rfind("id:");
			size_t wstart = value.rfind("width:");
			size_t capstart = value.find("text:");

			// some defaults in case of missing data
			String caption, id("0"), width("{0.33,0}");

			// extract the caption field
			if (capstart != String::npos)
			{
				capstart = value.find_first_of(":") + 1;

				if (wstart == String::npos)
					if (idstart == String::npos)
						caption = value.substr(capstart);
					else
						caption = value.substr(capstart, idstart - capstart);
				else
					caption = value.substr(capstart, wstart - capstart);

				// trim trailing whitespace
				TextUtils::trimTrailingChars(caption, TextUtils::DefaultWhitespace);
			}

			// extract the width field
			if (wstart != String::npos)
			{
				width = value.substr(wstart);
				width = width.substr(width.find_first_of("{"));
				width = width.substr(0, width.find_last_of("}") + 1);
			}

			// extract the id field.
			if (idstart != String::npos)
			{
				id = value.substr(idstart);
				id = id.substr(id.find_first_of(":") + 1);
			}

			// add the column accordingly
			MultiColumnList* pMultiColumnList = dynamic_cast<MultiColumnList*>(*ppPropRecv);
			if (pMultiColumnList)
			{
				pMultiColumnList->addColumn(caption, PropertyHelper::stringToUint(id), PropertyHelper::stringToUDim(width));
				return true;
			}

			const String& val = value;
			PROP_SET_TARGET_PROP(ColumnHeader);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ColumnHeader, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(WordWrap, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(MultiLineEditbox, setWordWrapping);
			PROP_SET_TARGET(RichEditbox, setWordWrapping);

			PROP_SET_TARGET_PROP(WordWrap);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(WordWrap, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(SelectionBrushImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET_s(SelectionBrushImage, MultiLineEditbox);
			PROP_SET_TARGET_s(SelectionBrushImage, RichEditbox);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(SelectionBrushImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SelectionBrushImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(CurrentProgress, 1)
		{
			float val;
			stream >> val;

			PROP_SET_TARGET(ProgressBar, setProgress);
			PROP_SET_TARGET(ProgressBarTwoValue, setProgress);

			PROP_SET_TARGET_PROP(val);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(CurrentProgress, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(FrameEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET(ProgressBar, SetFrameEnable);
			PROP_SET_TARGET(ProgressBarTwoValue, SetFrameEnable);

			PROP_SET_TARGET_PROP(val);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(FrameEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(StepSize, 1)
		{
			float val;
			stream >> val;

			PROP_SET_TARGET_s(StepSize, ProgressBar);
			PROP_SET_TARGET_s(StepSize, ProgressBarTwoValue);
			PROP_SET_TARGET_s(StepSize, Scrollbar);
			PROP_SET_TARGET_s(StepSize, Spinner);

			PROP_SET_TARGET_PROP(val);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(StepSize, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(SizeType, 1)
		{
			PushButtonSizeType val;
			stream >> val;

			PROP_SET_TARGET(PushButton, SetSizeType);

			PROP_SET_TARGET_PROP(SizeType);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(SizeType, 1, PushButtonSizeType)
		{
			const String& value = pPropData->mValue;

			PushButtonSizeType eType = ePushButtonSizeType_Auto;
			if (value == "Small")
			{
				eType = ePushButtonSizeType_Small;
			}
			else if (value == "Normal")
			{
				eType = ePushButtonSizeType_Normal;
			}
			else if (value == "Big")
			{
				eType = ePushButtonSizeType_Big;
			}
			else if (value == "Image")
			{
				eType = ePushButtonSizeType_Image;
			}
			else if (value == "Auto")
			{
				eType = ePushButtonSizeType_Auto;
			}

			stream << eType;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(ContentPaneAutoSized, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_TARGET_s(ContentPaneAutoSized, ScrollablePane);
			PROP_SET_TARGET_s(ContentPaneAutoSized, ScrolledContainer);

			PROP_SET_TARGET_PROP(val);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ContentPaneAutoSized, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ContentArea, 1)
		{
			Rect val;
			stream >> val;

			PROP_SET_TARGET(ScrollablePane, setContentPaneArea);
			PROP_SET_TARGET_s(ContentArea, ScrolledContainer);

			PROP_SET_TARGET_PROP(val);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ContentArea, 1, Rect);

		PROPERTY_SERIALIZER_READ_IMPL(ChildExtentsArea, 1)
		{
			Rect val;
			stream >> val;

			if (dynamic_cast<ScrolledContainer*>(*ppPropRecv))
			{
				return true;
			}

			PROP_SET_TARGET_PROP(ChildExtentsArea);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ChildExtentsArea, 1, Rect);

		PROPERTY_SERIALIZER_READ_IMPL(CurrentValue, 1)
		{
			float val;
			stream >> val;

			PROP_SET_TARGET_s(CurrentValue, Slider);
			PROP_SET_TARGET_s(CurrentValue, Spinner);

			PROP_SET_TARGET_PROP(CurrentValue);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(CurrentValue, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(MaximumValue, 1)
		{
			float val;
			stream >> val;

			PROP_SET_TARGET(Slider, setMaxValue);
			PROP_SET_TARGET_s(MaximumValue, Spinner);

			PROP_SET_TARGET_PROP(MaximumValue);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MaximumValue, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(TextInputMode, 1)
		{
			Spinner::TextInputMode val;
			stream >> val;

			PROP_SET_TARGET(Spinner, setTextInputMode);

			PROP_SET_TARGET_PROP(TextInputMode);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(TextInputMode, 1, Spinner::TextInputMode)
		{
			const String& value = pPropData->mValue;

			Spinner::TextInputMode mode;

			if (value == "FloatingPoint")
			{
				mode = Spinner::FloatingPoint;
			}
			else if (value == "Hexadecimal")
			{
				mode = Spinner::Hexadecimal;
			}
			else if (value == "Octal")
			{
				mode = Spinner::Octal;
			}
			else
			{
				mode = Spinner::Integer;
			}

			stream << mode;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(TabPanePosition, 1)
		{
			TabControl::TabPanePosition val;
			stream >> val;

			PROP_SET_TARGET(TabControl, setTabPanePosition);

			PROP_SET_TARGET_PROP(TabPanePosition);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(TabPanePosition, 1, TabControl::TabPanePosition)
		{
			const String& value = pPropData->mValue;

			TabControl::TabPanePosition tpp;
			if ((value == "top") || (value == "Top"))
				tpp = TabControl::Top;
			else if ((value == "bottom") || (value == "Bottom"))
				tpp = TabControl::Bottom;
			else if ((value == "left") || (value == "Left"))
				tpp = TabControl::Left;
			//else
			//	return;

			stream << tpp;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(VertRange, 1)
		{
			float rangeMin = 0, rangeMax = 0;
			stream >> rangeMin >> rangeMax;

			Thumb* pThumb = dynamic_cast<Thumb*>(*ppPropRecv);
			if (pThumb)
			{
				pThumb->setVertRange(rangeMin, rangeMax);
				return true;
			}

			char sz[256];
			sprintf(sz, " min:%f max:%f", rangeMin, rangeMax);
			String val = sz;
			PROP_SET_TARGET_PROP(VertRange);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VertRange, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(HorzRange, 1)
		{
			float rangeMin = 0, rangeMax = 0;
			stream >> rangeMin >> rangeMax;

			Thumb* pThumb = dynamic_cast<Thumb*>(*ppPropRecv);
			if (pThumb)
			{
				pThumb->setHorzRange(rangeMin, rangeMax);
				return true;
			}

			char sz[256];
			sprintf(sz, " min:%f max:%f", rangeMin, rangeMax);
			String val = sz;
			PROP_SET_TARGET_PROP(HorzRange);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(HorzRange, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(MaskImage, 1)
		{
			String val;
			stream >> val;

			PROP_SET_RENDERER_s(MaskImage, FalagardGroupBtnTree);

			PROP_SET_TARGET_PROP(MaskImage);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MaskImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(PropertyDefinition, 1)
		{
			String name, value;
			stream >> name >> value;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->setProperty(name, value);
				return true;
			}

			return false;
		}
		bool PROPERTY_SERIALIZER_CLASS_NAME(PropertyDefinition, 1)::write(Stream& stream, int version, PropertyReceiver* pPropRecv)
		{
			XMLFileData::PropertyData* pPropData = dynamic_cast<XMLFileData::PropertyData*>(pPropRecv);
			if (pPropData)
			{
				stream << pPropData->mName << pPropData->mValue;
				return true;
			}
			return false;
		}

		PROPERTY_SERIALIZER_READ_IMPL(BlinkCaret, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardEditbox, setCaretBlinkEnabled);
			PROP_SET_RENDERER(FalagardMultiLineEditbox, setCaretBlinkEnabled);
			PROP_SET_RENDERER(FalagardRichEditbox, setCaretBlinkEnabled);

			PROP_SET_TARGET_PROP(BlinkCaret);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BlinkCaret, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(BlinkCaretTimeout, 1)
		{
			float val;
			stream >> val;

			PROP_SET_RENDERER(FalagardEditbox, setCaretBlinkTimeout);
			PROP_SET_RENDERER(FalagardMultiLineEditbox, setCaretBlinkTimeout);
			PROP_SET_RENDERER(FalagardRichEditbox, setCaretBlinkTimeout);

			PROP_SET_TARGET_PROP(BlinkCaretTimeout);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BlinkCaretTimeout, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(SegmentWidgetType, 1)
		{
			String val;
			stream >> val;

			PROP_SET_RENDERER_s(SegmentWidgetType, FalagardListHeader);

			PROP_SET_TARGET_PROP(SegmentWidgetType);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(SegmentWidgetType, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(VerticalProgress, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardProgressBar, setVertical);

			PROP_SET_TARGET_PROP(VerticalProgress);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VerticalProgress, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ReversedProgress, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardProgressBar, setReversed);

			PROP_SET_TARGET_PROP(ReversedProgress);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ReversedProgress, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(VerticalScrollbar, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardScrollbar, setVertical);

			PROP_SET_TARGET_PROP(VerticalScrollbar);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VerticalScrollbar, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(VerticalSlider, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardSlider, setVertical);

			PROP_SET_TARGET_PROP(VerticalSlider);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VerticalSlider, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(ReversedDirection, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardSlider, setReversedDirection);

			PROP_SET_TARGET_PROP(ReversedDirection);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ReversedDirection, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(Image, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_RENDERER_s(Image, FalagardStaticImage);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(Image);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Image, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(ImageSizeEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER_b(ImageSizeEnable, FalagardStaticImage);

			PROP_SET_TARGET_PROP(ImageSizeEnable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ImageSizeEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(BackgroundEnabled, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStatic, setBackgroundEnabled);

			PROP_SET_TARGET_PROP(BackgroundEnabled);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BackgroundEnabled, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(TextColours, 1)
		{
			ColourRect val;
			stream >> val;

			PROP_SET_RENDERER_s(TextColours, FalagardStaticText);

			PROP_SET_TARGET_PROP(TextColours);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(TextColours, 1, ColourRect);

		PROPERTY_SERIALIZER_READ_IMPL(HorzFormatting, 1)
		{
			HorizontalTextFormatting val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, setHorizontalFormatting);

			PROP_SET_TARGET_PROP(HorzFormatting);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(HorzFormatting, 1, HorizontalTextFormatting)
		{
			const String& value = pPropData->mValue;

			HorizontalTextFormatting fmt;

			if (value == "RightAligned")
			{
				fmt = HTF_RIGHT_ALIGNED;
			}
			else if (value == "HorzCentred")
			{
				fmt = HTF_CENTRE_ALIGNED;
			}
			else if (value == "HorzJustified")
			{
				fmt = HTF_JUSTIFIED;
			}
			else if (value == "WordWrapLeftAligned")
			{
				fmt = HTF_WORDWRAP_LEFT_ALIGNED;
			}
			else if (value == "WordWrapRightAligned")
			{
				fmt = HTF_WORDWRAP_RIGHT_ALIGNED;
			}
			else if (value == "WordWrapCentred")
			{
				fmt = HTF_WORDWRAP_CENTRE_ALIGNED;
			}
			else if (value == "WordWrapJustified")
			{
				fmt = HTF_WORDWRAP_JUSTIFIED;
			}
			else
			{
				fmt = HTF_LEFT_ALIGNED;
			}

			stream << fmt;

			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(VertFormatting, 1)
		{
			VerticalTextFormatting val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, setVerticalFormatting);

			PROP_SET_TARGET_PROP(VertFormatting);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(VertFormatting, 1, VerticalTextFormatting)
		{
			VerticalTextFormatting fmt;

			if (value == "BottomAligned")
			{
				fmt = VTF_BOTTOM_ALIGNED;
			}
			else if (value == "VertCentred")
			{
				fmt = VTF_CENTRE_ALIGNED;
			}
			else
			{
				fmt = VTF_TOP_ALIGNED;
			}

			stream << fmt;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(VertScrollbar, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, setVerticalScrollbarEnabled);

			PROP_SET_TARGET_PROP(VerticalScrollbar);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VertScrollbar, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(HorzScrollbar, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, setHorizontalScrollbarEnabled);

			PROP_SET_TARGET_PROP(HorzScrollbar);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(HorzScrollbar, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(HorzExtent, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				if (dynamic_cast<FalagardStaticText*>(pWnd->getWindowRenderer()))
				{
					return true;
				}
			}

			PROP_SET_TARGET_PROP(HorzExtent);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(HorzExtent, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(VertExtent, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				if (dynamic_cast<FalagardStaticText*>(pWnd->getWindowRenderer()))
				{
					return true;
				}
			}

			PROP_SET_TARGET_PROP(VertExtent);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(VertExtent, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(BorderEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER_b(BorderEnable, FalagardStaticText);

			PROP_SET_TARGET_PROP(BorderEnable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BorderEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(BorderColour, 1)
		{
			colour val;
			stream >> val;

			PROP_SET_RENDERER_b(BorderColour, FalagardStaticText);

			PROP_SET_TARGET_PROP(BorderColour);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(BorderColour, 1, colour);

		PROPERTY_SERIALIZER_READ_IMPL(DefaultColourEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, SetDefaultColorEnable);

			PROP_SET_TARGET_PROP(DefaultColourEnable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(DefaultColourEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(DefaultBorderEnable, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER_b(DefaultBorderEnable, FalagardStaticText);

			PROP_SET_TARGET_PROP(DefaultBorderEnable);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(DefaultBorderEnable, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(Title, 1)
		{
			bool val;
			stream >> val;

			PROP_SET_RENDERER(FalagardStaticText, SetTitleEnable);

			PROP_SET_TARGET_PROP(Title);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Title, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(TabButtonType, 1)
		{
			String val;
			stream >> val;

			PROP_SET_RENDERER_s(TabButtonType, FalagardTabControl);

			PROP_SET_TARGET_PROP(TabButtonType);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(TabButtonType, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Font, 1)
		{
			String value;
			stream >> value;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				if (value.empty())
				{
					pWindow->setFont(0);
				}
				else
				{
					pWindow->setFont(value);
				}
				return true;
			}

			const String& val = value;
			PROP_SET_TARGET_PROP(Font);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Font, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(MouseCursorImage, 1)
		{
			String str;
			stream >> str;

			const Image* val = PropertyHelper::stringToImage(str);

			PROP_SET_TARGET(Window, setMouseCursor);

			{
				const String& val = str;
				PROP_SET_TARGET_PROP(MouseCursorImage);
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(MouseCursorImage, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Disabled, 1)
		{
			bool val;
			stream >> val;

			val = !val;

			PROP_SET_TARGET(Window, setEnabled);

			PROP_SET_TARGET_PROP(Enabled);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Disabled, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(VerticalAlignment, 1)
		{
			VerticalAlignment val;
			stream >> val;

			PROP_SET_TARGET(Window, setVerticalAlignment);

			PROP_SET_TARGET_PROP(VerticalAlignment);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(VerticalAlignment, 1, VerticalAlignment)
		{
			const String& value = pPropData->mValue;

			CEGUI::VerticalAlignment align;

			if (value == "Centre")
			{
				align = VA_CENTRE;
			}
			else if (value == "Bottom")
			{
				align = VA_BOTTOM;
			}
			else
			{
				align = VA_TOP;
			}

			stream << align;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(HorizontalAlignment, 1)
		{
			HorizontalAlignment val;
			stream >> val;

			PROP_SET_TARGET(Window, setHorizontalAlignment);

			PROP_SET_TARGET_PROP(HorizontalAlignment);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(HorizontalAlignment, 1, HorizontalAlignment)
		{
			const String& value = pPropData->mValue;

			CEGUI::HorizontalAlignment align;

			if (value == "Centre")
			{
				align = HA_CENTRE;
			}
			else if (value == "Right")
			{
				align = HA_RIGHT;
			}
			else
			{
				align = HA_LEFT;
			}

			stream << align;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(WindowRenderer, 1)
		{
			String val;
			stream >> val;

			PROP_SET_TARGET_s(WindowRenderer, Window);

			PROP_SET_TARGET_PROP(WindowRenderer);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(WindowRenderer, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(LookNFeel, 1)
		{
			String val;
			stream >> val;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->setLookNFeel(val, false);
				return true;
			}

			PROP_SET_TARGET_PROP(LookNFeel);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(LookNFeel, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Scale, 1)
		{
			Vector3 val;
			stream >> val;

			Window* wnd = dynamic_cast<Window*>(*ppPropRecv);
			if (wnd)
			{
				wnd->setGeomScale(val);
				Size sz = wnd->getPixelSize();
				wnd->setGeomPivot(CEGUI::Vector3(sz.d_width / 2, sz.d_height / 2, 0));
				return true;
			}

			PROP_SET_TARGET_PROP(Scale);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Scale, 1, Vector3);

		PROPERTY_SERIALIZER_READ_IMPL(Rotation, 1)
		{
			Vector3 val;
			stream >> val;

			Window* wnd = dynamic_cast<Window*>(*ppPropRecv);
			if (wnd)
			{
				wnd->setGeomRotation(val);
				Size sz = wnd->getPixelSize();
				wnd->setGeomPivot(CEGUI::Vector3(sz.d_width / 2, sz.d_height / 2, 0));
				return true;
			}

			PROP_SET_TARGET_PROP(Rotation);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Rotation, 1, Vector3);

		PROPERTY_SERIALIZER_READ_IMPL(XRotation, 1)
		{
			float val;
			stream >> val;

			Window* wnd = dynamic_cast<Window*>(*ppPropRecv);
			if (wnd)
			{
				Vector3 r = wnd->getRotation();
				r.d_x = val;
				wnd->setRotation(r);

				return true;
			}

			PROP_SET_TARGET_PROP(XRotation);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(XRotation, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(YRotation, 1)
		{
			float val;
			stream >> val;

			Window* wnd = dynamic_cast<Window*>(*ppPropRecv);
			if (wnd)
			{
				Vector3 r = wnd->getRotation();
				r.d_y = val;
				wnd->setRotation(r);

				return true;
			}

			PROP_SET_TARGET_PROP(YRotation);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(YRotation, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(ZRotation, 1)
		{
			float val;
			stream >> val;

			Window* wnd = dynamic_cast<Window*>(*ppPropRecv);
			if (wnd)
			{
				Vector3 r = wnd->getRotation();
				r.d_z = val;
				wnd->setRotation(r);

				return true;
			}

			PROP_SET_TARGET_PROP(ZRotation);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(ZRotation, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(XPos, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				float width = pWnd->getPixelSize().d_width;
				pWnd->setXPosition(cegui_absdim(val));
				pWnd->setWidth(cegui_absdim(width));
				return true;
			}

			PROP_SET_TARGET_PROP(XPos);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(XPos, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(YPos, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				float height = pWnd->getPixelSize().d_height;
				pWnd->setYPosition(cegui_absdim(val));
				pWnd->setHeight(cegui_absdim(height));
				return true;
			}

			PROP_SET_TARGET_PROP(YPos);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(YPos, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(Width, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				pWnd->setWidth(cegui_absdim(val));
				return true;
			}

			PROP_SET_TARGET_PROP(Width);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Width, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(Height, 1)
		{
			float val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				pWnd->setHeight(cegui_absdim(val));
				return true;
			}

			PROP_SET_TARGET_PROP(Height);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Height, 1, float);

		PROPERTY_SERIALIZER_READ_IMPL(CreateEffectType, 1)
		{
			CreateWindowEffect val;
			stream >> val;

			PROP_SET_TARGET(Window, SetCreateEffectType);

			PROP_SET_TARGET_PROP(CreateEffectType);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(CreateEffectType, 1, CreateWindowEffect)
		{
			CEGUI::CreateWindowEffect effect;

			if (value == "")
			{
				effect = CreateWndEffect_None;
			}
			else if (value == "Drop")
			{
				effect = CreateWndEffect_Drop;
			}
			else if (value == "Left")
			{
				effect = CreateWndEffect_FlyFromLeft;
			}
			else if (value == "Right")
			{
				effect = CreateWndEffect_FlyFromRight;
			}
			else if (value == "ZoomOut")
			{
				effect = CreateWndEffect_ZoomOut;
			}
			else
			{
				effect = CreateWndEffect_None;
			}

			stream << effect;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(CloseEffectType, 1)
		{
			CloseWndEffect val;
			stream >> val;

			PROP_SET_TARGET(Window, SetCloseEffectType);

			PROP_SET_TARGET_PROP(CloseEffectType);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_BEGIN(CloseEffectType, 1, CloseWndEffect)
		{
			CEGUI::CloseWndEffect effect;

			if (value == "")
			{
				effect = CloseWndEffect_None;
			}
			else if (value == "Up")
			{
				effect = CloseWndEffect_FlyUp;
			}
			else if (value == "Down")
			{
				effect = CloseWndEffect_FlyDown;
			}
			else if (value == "Left")
			{
				effect = CloseWndEffect_FlyLeft;
			}
			else if (value == "Right")
			{
				effect = CloseWndEffect_FlyRight;
			}
			else if (value == "ZoomIn")
			{
				effect = CloseWndEffect_ZoomIn;
			}
			else
			{
				effect = CloseWndEffect_None;
			}

			stream << effect;
			return true;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL_END;

		PROPERTY_SERIALIZER_READ_IMPL(AllowModalStateClick, 1)
		{
			bool val;
			stream >> val;

			Window* pWnd = dynamic_cast<Window*>(*ppPropRecv);
			if (pWnd)
			{
				pWnd->EnableAllowModalState(val);
				return true;
			}

			PROP_SET_TARGET_PROP(AllowModalStateClick);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(AllowModalStateClick, 1, bool);

		PROPERTY_SERIALIZER_READ_IMPL(LuaClass, 1)
		{
			String val;
			stream >> val;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->setProperty("LuaClass", val);
				return true;
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(LuaClass, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(LuaMemberName, 1)
		{
			String val;
			stream >> val;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->setProperty("LuaMemberName", val);
				return true;
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(LuaMemberName, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(LuaEventOnClicked, 1)
		{
			String val;
			stream >> val;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->setProperty("LuaEventOnClicked", val);
				return true;
			}

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(LuaEventOnClicked, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Name, 1)
		{
			String val;
			stream >> val;

			if (dynamic_cast<Font*>(*ppPropRecv))
			{
				return true;
			}

			PROP_SET_TARGET_PROP(Name);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Name, 1, String);

		PROPERTY_SERIALIZER_READ_IMPL(Mapping, 1)
		{
			String value;
			stream >> value;

			char img[33];
			utf32 codepoint;
			float adv;
			if (sscanf(value.c_str(), " %u , %g , %32s", &codepoint, &adv, img) != 3)
			{
				return false;
			}

			PixmapFont* pPixmapFont = dynamic_cast<PixmapFont*>(*ppPropRecv);
			if (pPixmapFont)
			{
				pPixmapFont->defineMapping(codepoint, img, adv);
				return true;
			}

			const String& val = value;
			PROP_SET_TARGET_PROP(Mapping);

			return false;
		}
		PROPERTY_SERIALIZER_WRITE_IMPL(Mapping, 1, String);
	}
}