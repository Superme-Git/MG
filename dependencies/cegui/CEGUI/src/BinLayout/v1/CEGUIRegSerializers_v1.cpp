//////////////////////////////////////////////////////////////////////////
// File:		CEGUIRegSerializers_v1.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/04
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/v1/CEGUIRegSerializers_v1.h"
#include "BinLayout/v1/CEGUILayoutSerializer_v1.h"
#include "BinLayout/CEGUISerializerManager.h"
#include "BinLayout/v1/CEGUINodeSerializer_v1.h"
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
#include "BinLayout/CEGUIPropertyIds.h"

namespace CEGUI
{
	namespace BinLayout
	{
		void g_RegSerializers_v1()
		{
			static LayoutSerializer_v1 s_LayoutSerializer_v1;
			SerializerManager::getSingleton().addLayoutSerializer(1, &s_LayoutSerializer_v1);

			static NodeSerializer_v1 s_NodeSerializer_v1;
			SerializerManager::getSingleton().addNodeSerializer(1, &s_NodeSerializer_v1);

			PROPERTY_SERIALIZER_INSTANCE(ImageSetName, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ImageName, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(TotalFrame, int, AnimationButton, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AnimationSpeed, int, AnimationButton, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Selected, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ReadOnly, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ValidationString, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(CaratIndex, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(EditSelectionStart, uint, Combobox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MaxEditTextLength, uint, Combobox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SortList, bool, Combobox, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(ForceVertScrollbar, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ForceHorzScrollbar, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(SingleClickMode, bool, Combobox, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(DraggingEnabled, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(DragAlpha, float, DragContainer, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DragThreshold, float, DragContainer, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(DragCursorImage, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(StickyMode, bool, DragContainer, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(FixedDragOffset, UVector2, DragContainer, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UseFixedDragOffset, bool, DragContainer, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MaskText, bool, Editbox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MaskCodepoint, uint, Editbox, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(SelectionStart, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SelectionLength, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(MaxTextLength, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(NormalTextColour, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SelectedTextColour, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ActiveSelectionColour, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(InactiveSelectionColour, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BackGroundEnable, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(SupportTabKey, bool, Editbox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TabOrderID, int, Editbox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SizingEnabled, bool, FrameWindow, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(FrameEnabled, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(TitlebarEnabled, bool, FrameWindow, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CloseButtonEnabled, bool, FrameWindow, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(RollUpEnabled, bool, FrameWindow, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(RollUpState, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(DragMovingEnabled, bool, FrameWindow, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SizingBorderThickness, float, FrameWindow, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(NSSizingCursorImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(EWSizingCursorImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(NWSESizingCursorImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(NESWSizingCursorImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(GroupID, 1, 1);
			
			TPL_PROPERTY_SERIALIZER_INSTANCE(StateImageExtendID, int, GroupButton, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(ItemCellStyle, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(Selectable, bool, ItemEntry, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AutoResizeEnabled, bool, ItemListBase, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SortEnabled, bool, ItemListBase, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SortMode, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(MultiSelect, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(RowCount, 1, 1);
			
			TPL_PROPERTY_SERIALIZER_INSTANCE(ColCount, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CellWidth, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CellHeight, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SpaceX, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SpaceY, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(StartX, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(StartY, int, ItemTable, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UserID, int, LinkText, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Sort, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ItemTooltips, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ColumnsSizable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ColumnsMovable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SortSettingEnabled, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SortDirection, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SortColumnID, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(Sizable, bool, ListHeaderSegment, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Clickable, bool, ListHeaderSegment, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Dragable, bool, ListHeaderSegment, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(SizingCursorImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(MovingCursorImage, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(ItemSpacing, float, MenuBase, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AllowMultiplePopups, bool, MenuBase, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(NominatedSelectionColumnID, uint, MultiColumnList, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(NominatedSelectionRow, uint, MultiColumnList, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SelectionMode, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(ColumnHeader, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(WordWrap, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SelectionBrushImage, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(FadeInTime, float, PopupMenu, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(FadeOutTime, float, PopupMenu, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(CurrentProgress, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(FrameEnable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(StepSize, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(SizeType, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(LineSpace, float, RichEditbox, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TopAfterLoadFont, bool, RichEditbox, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(ContentPaneAutoSized, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ContentArea, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(HorzStepSize, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(HorzOverlapSize, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(HorzScrollPosition, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(VertStepSize, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(VertOverlapSize, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(VertScrollPosition, float, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(PaneScrollMode, int, ScrollablePane, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DocumentSize, float, Scrollbar, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(PageSize, float, Scrollbar, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(OverlapSize, float, Scrollbar, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(ScrollPosition, float, Scrollbar, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(ChildExtentsArea, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(CurrentValue, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(MaximumValue, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(ClickStepSize, float, Slider, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MinimumValue, float, Spinner, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(TextInputMode, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Status, String, Switch, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TabHeight, UDim, TabControl, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TabTextPadding, UDim, TabControl, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(TabPanePosition, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TabSeparation, float, TabControl, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TabFirstInterval, float, TabControl, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(HotTracked, bool, Thumb, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(VertFree, bool, Thumb, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(HorzFree, bool, Thumb, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(VertRange, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(HorzRange, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(HoverTime, float, Tooltip, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DisplayTime, float, Tooltip, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(FadeTime, float, Tooltip, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(MaskImage, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(PropertyDefinition, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BlinkCaret, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BlinkCaretTimeout, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(SegmentWidgetType, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VerticalProgress, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ReversedProgress, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VerticalScrollbar, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VerticalSlider, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ReversedDirection, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(Image, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ImageSizeEnable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BackgroundEnabled, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(TextColours, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(HorzFormatting, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VertFormatting, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VertScrollbar, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(HorzScrollbar, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(HorzExtent, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VertExtent, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BorderEnable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(BorderColour, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(DefaultColourEnable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(DefaultBorderEnable, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(Title, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(TabButtonType, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(ID, uint, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Alpha, float, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Font, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(Text, String, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(MouseCursorImage, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(ClippedByParent, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(InheritsAlpha, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AlwaysOnTop, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TopMost, bool, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Disabled, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(Visible, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(RestoreOldCapture, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DestroyedByParent, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(ZOrderChangeEnabled, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(WantsMultiClickEvents, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MouseButtonDownAutoRepeat, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AutoRepeatDelay, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AutoRepeatRate, float, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DistributeCapturedInputs, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CustomTooltipType, String, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Tooltip, String, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(InheritsTooltipText, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(RiseOnClick, bool, Window, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(VerticalAlignment, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(HorizontalAlignment, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedAreaRect, URect, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedPosition, UVector2, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedXPosition, UDim, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedYPosition, UDim, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedSize, UVector2, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedWidth, UDim, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedHeight, UDim, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedMinSize, UVector2, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(UnifiedMaxSize, UVector2, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(MousePassThroughEnabled, bool, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(WindowRenderer, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(LookNFeel, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(DragDropTarget, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(AutoRenderingSurface, bool, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Scale, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(Rotation, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(XRotation, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(YRotation, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(ZRotation, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(NonClient, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(TextParsingEnabled, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DisplaySizeChangePosEnabled, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(WindowDieTime, float, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(DragMoveEnable, bool, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(XPos, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(YPos, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(Width, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(Height, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(EnableSound, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(SoundResource, String, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CloseSoundResource, String, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(LimitWindowSize, bool, Window, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(CreateEffectType, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(CloseEffectType, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(AllowModalStateClick, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(AllowShowWithModalState, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(ModalState, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(CloseIsHide, bool, Window, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(IsPixelDecide, bool, Window, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(LuaClass, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(LuaMemberName, 1, 1);
			PROPERTY_SERIALIZER_INSTANCE(LuaEventOnClicked, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(NativeRes, Size, Font, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Name, 1, 1);

			TPL_PROPERTY_SERIALIZER_INSTANCE(AutoScaled, bool, Font, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(PointSize, float, FreeTypeFont, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Antialiased, bool, FreeTypeFont, 1, 1);
			TPL_PROPERTY_SERIALIZER_INSTANCE(Imageset, String, PixmapFont, 1, 1);

			PROPERTY_SERIALIZER_INSTANCE(Mapping, 1, 1);
		}
	}
}