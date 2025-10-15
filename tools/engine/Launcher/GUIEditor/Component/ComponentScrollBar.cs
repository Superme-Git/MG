using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentScrollBar : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormScrollBar m_form;

		private IBrush m_brushup0;
		private IBrush m_brushup1;
		private IBrush m_brushup2;
		private IBrush m_brushup3;
		private IBrush m_brushdown0;
		private IBrush m_brushdown1;
		private IBrush m_brushdown2;
		private IBrush m_brushdown3;
		private IBrush m_brushslot;
		private IBrush m_brushblock1;
		private IBrush m_brushblock2;
		private IBrush m_brushblock3;

		private IBrush m_brushup;
		private IBrush m_brushdown;
		private IBrush m_brushblock;

		public CFormScrollBar Form
		{
			get { return m_form; }
		}

		public IBrush BrushUp0
		{
			get { return m_brushup0; }
			protected set { m_brushup0 = value; }
		}

		public IBrush BrushUp1
		{
			get { return m_brushup1; }
			protected set { m_brushup1 = value; }
		}

		public IBrush BrushUp2
		{
			get { return m_brushup2; }
			protected set { m_brushup2 = value; }
		}

		public IBrush BrushUp3
		{
			get { return m_brushup3; }
			protected set { m_brushup3 = value; }
		}

		public IBrush BrushDown0
		{
			get { return m_brushdown0; }
			protected set { m_brushdown0 = value; }
		}

		public IBrush BrushDown1
		{
			get { return m_brushdown1; }
			protected set { m_brushdown1 = value; }
		}

		public IBrush BrushDown2
		{
			get { return m_brushdown2; }
			protected set { m_brushdown2 = value; }
		}

		public IBrush BrushDown3
		{
			get { return m_brushdown3; }
			protected set { m_brushdown3 = value; }
		}

		public IBrush BrushSlot
		{
			get { return m_brushslot; }
			protected set { m_brushslot = value; }
		}

		public IBrush BrushBlock1
		{
			get { return m_brushblock1; }
			protected set { m_brushblock1 = value; }
		}

		public IBrush BrushBlock2
		{
			get { return m_brushblock2; }
			protected set { m_brushblock2 = value; }
		}

		public IBrush BrushBlock3
		{
			get { return m_brushblock3; }
			protected set { m_brushblock3 = value; }
		}

		public void GetImageUp0Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup0.GetImageRect();
			m_brushup0.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup0.GetImageExpandMode();
		}

		public void GetImageUp1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup1.GetImageRect();
			m_brushup1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup1.GetImageExpandMode();
		}

		public void GetImageUp2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup2.GetImageRect();
			m_brushup2.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup2.GetImageExpandMode();
		}

		public void GetImageUp3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup3.GetImageRect();
			m_brushup3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup3.GetImageExpandMode();
		}

		public void GetImageDown0Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup0.GetImageRect();
			m_brushup0.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup0.GetImageExpandMode();
		}

		public void GetImageDown1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup1.GetImageRect();
			m_brushup1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup1.GetImageExpandMode();
		}

		public void GetImageDown2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup2.GetImageRect();
			m_brushup2.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup2.GetImageExpandMode();
		}

		public void GetImageDown3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushup3.GetImageRect();
			m_brushup3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushup3.GetImageExpandMode();
		}

		public void GetImageSlotParam(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushslot.GetImageRect();
			m_brushslot.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushslot.GetImageExpandMode();
		}

		public void GetImageBlock1Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushblock1.GetImageRect();
			m_brushblock1.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushblock1.GetImageExpandMode();
		}

		public void GetImageBlock2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushblock2.GetImageRect();
			m_brushblock2.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushblock2.GetImageExpandMode();
		}

		public void GetImageBlock3Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
		{
			rect = m_brushblock3.GetImageRect();
			m_brushblock3.GetImageBlock(out w0, out w1, out h0, out h1);
			mode = m_brushblock3.GetImageExpandMode();
		}

		public void SetImageUp0Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushup0.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageUp1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushup1.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageUp2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushup2.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageUp3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushup3.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageDown0Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushdown0.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageDown1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushdown1.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageDown2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushdown2.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageDown3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushdown3.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageSlotParam(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushslot.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageBlock1Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushblock1.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageBlock2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushblock2.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetImageBlock3Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
		{
			m_brushblock3.SetImageBlock(rect, w0, w1, h0, h1, mode);
		}

		public void SetBrushUp(int i)
		{
			switch(i)
			{
			case  0: m_brushup = m_brushup0; break;
			case  1: m_brushup = m_brushup1; break;
			case  2: m_brushup = m_brushup2; break;
			case  3: m_brushup = m_brushup3; break;
			default: goto case 1;
			}
			Desktop.Control.Invalidate();
		}

		public void SetBrushDown(int i)
		{
			switch(i)
			{
			case  0: m_brushdown = m_brushdown0; break;
			case  1: m_brushdown = m_brushdown1; break;
			case  2: m_brushdown = m_brushdown2; break;
			case  3: m_brushdown = m_brushdown3; break;
			default: goto case 1;
			}
			Desktop.Control.Invalidate();
		}

		public void SetBrushBlock(int i)
		{
			switch(i)
			{
			case  1: m_brushblock = m_brushblock1; break;
			case  2: m_brushblock = m_brushblock2; break;
			case  3: m_brushblock = m_brushblock3; break;
			default: goto case 1;
			}
			Desktop.Control.Invalidate();
		}

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentScrollBar(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Name = name;
			m_mainform = mainform;
			Brush = null;
			m_brushup0 = Render.CreateBrush();
			m_brushup1 = Render.CreateBrush();
			m_brushup2 = Render.CreateBrush();
			m_brushup3 = Render.CreateBrush();
			m_brushdown0 = Render.CreateBrush();
			m_brushdown1 = Render.CreateBrush();
			m_brushdown2 = Render.CreateBrush();
			m_brushdown3 = Render.CreateBrush();
			m_brushslot = Render.CreateBrush();
			m_brushblock1 = Render.CreateBrush();
			m_brushblock2 = Render.CreateBrush();
			m_brushblock3 = Render.CreateBrush();
			SetBrushUp(1);
			SetBrushDown(1);
			SetBrushBlock(1);
			m_form = new CFormScrollBar(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Color.FromArgb(255, 255, 255, 255);
		}

		public override CComponent Clone()
		{
			CComponentScrollBar one = new CComponentScrollBar(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;            
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.BrushUp0 = BrushUp0.Clone();
			one.BrushUp1 = BrushUp1.Clone();
			one.BrushUp2 = BrushUp2.Clone();
			one.BrushUp3 = BrushUp3.Clone();
			one.BrushDown0 = BrushDown0.Clone();
			one.BrushDown1 = BrushDown1.Clone();
			one.BrushDown2 = BrushDown2.Clone();
			one.BrushDown3 = BrushDown3.Clone();
			one.BrushSlot = BrushSlot.Clone();
			one.BrushBlock1 = BrushBlock1.Clone();
			one.BrushBlock2 = BrushBlock2.Clone();
			one.BrushBlock3 = BrushBlock3.Clone();
			one.Form.ImageUp0Name = m_form.ImageUp0Name;
			one.Form.ImageUp1Name = m_form.ImageUp1Name;
			one.Form.ImageUp2Name = m_form.ImageUp2Name;
			one.Form.ImageUp3Name = m_form.ImageUp3Name;
			one.Form.ImageDown0Name = m_form.ImageDown0Name;
			one.Form.ImageDown1Name = m_form.ImageDown1Name;
			one.Form.ImageDown2Name = m_form.ImageDown2Name;
			one.Form.ImageDown3Name = m_form.ImageDown3Name;
			one.Form.ImageSlotName = m_form.ImageSlotName;
			one.Form.ImageBlock1Name = m_form.ImageBlock1Name;
			one.Form.ImageBlock2Name = m_form.ImageBlock2Name;
			one.Form.ImageBlock3Name = m_form.ImageBlock3Name;
			one.SetBrushUp(1);
			one.SetBrushDown(1);
			one.SetBrushBlock(1);
			one.Form.BkColor = m_form.BkColor;
			one.Form.IsViewOnly = m_form.IsViewOnly;
			one.Form.IsAutoBlock = m_form.IsAutoBlock;
			one.Form.TipInfo = m_form.TipInfo;
			return one;
		}

		public override Form GetDialog()
		{
			return m_form;
		}

		public override void OnMove()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
		}

		public override void OnSize()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
			m_form.W = rect.Width;
			m_form.H = rect.Height;
		}

		public override void OnDraw()
		{
			if(Render != null)
			{
				Rectangle rect = AbsRect;

				if(m_form.Vertical)
				{
					Render.DrawRect(new Rectangle(rect.X, rect.Y, rect.Width, m_brushup.GetImageRect().Height), m_brushup);
					Render.DrawRect(new Rectangle(rect.X, rect.Y + rect.Height - m_brushdown.GetImageRect().Height, rect.Width, m_brushdown.GetImageRect().Height), m_brushdown);
					Render.DrawRect(new Rectangle(rect.X, rect.Y + m_brushup.GetImageRect().Height, rect.Width, rect.Height - m_brushup.GetImageRect().Height - m_brushdown.GetImageRect().Height), m_brushslot);
					Render.DrawRect(new Rectangle(rect.X, rect.Y + m_brushup.GetImageRect().Height, rect.Width, m_brushblock.GetImageRect().Height), m_brushblock);
				}
				else
				{
					Render.DrawRect(new Rectangle(rect.X, rect.Y, m_brushup.GetImageRect().Width, rect.Height), m_brushup);
					Render.DrawRect(new Rectangle(rect.X + rect.Width - m_brushdown.GetImageRect().Width, rect.Y, m_brushdown.GetImageRect().Width, rect.Height), m_brushdown);
					Render.DrawRect(new Rectangle(rect.X + m_brushup.GetImageRect().Width, rect.Y, rect.Width - m_brushup.GetImageRect().Width - m_brushdown.GetImageRect().Width, rect.Height), m_brushslot);
					Render.DrawRect(new Rectangle(rect.X + m_brushup.GetImageRect().Width, rect.Y, m_brushblock.GetImageRect().Width, rect.Height), m_brushblock);
				}
			}

			base.OnDraw();
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("ScrollBar");
			base.SaveXML(doc, sub);
			sub.SetAttribute("b", "255,255,255,255");
			sub.SetAttribute("vertical", m_form.Vertical ? "true" : "false");
			SaveImage(doc, sub, m_brushup0, "ImageUp0");
			SaveImage(doc, sub, m_brushup1, "ImageUp1");
			SaveImage(doc, sub, m_brushup2, "ImageUp2");
			SaveImage(doc, sub, m_brushup3, "ImageUp3");
			SaveImage(doc, sub, m_brushdown0, "ImageDown0");
			SaveImage(doc, sub, m_brushdown1, "ImageDown1");
			SaveImage(doc, sub, m_brushdown2, "ImageDown2");
			SaveImage(doc, sub, m_brushdown3, "ImageDown3");
			SaveImage(doc, sub, m_brushslot, "ImageSlot");
			SaveImage(doc, sub, m_brushblock1, "ImageBlock1");
			SaveImage(doc, sub, m_brushblock2, "ImageBlock2");
			SaveImage(doc, sub, m_brushblock3, "ImageBlock3");			
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("autoblock", m_form.IsAutoBlock ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.Vertical = (GetAttributeString(elem, "vertical") == "true");
			LoadImage(elem, m_brushup0, "ImageUp0");
			LoadImage(elem, m_brushup1, "ImageUp1");
			LoadImage(elem, m_brushup2, "ImageUp2");
			LoadImage(elem, m_brushup3, "ImageUp3");
			LoadImage(elem, m_brushdown0, "ImageDown0");
			LoadImage(elem, m_brushdown1, "ImageDown1");
			LoadImage(elem, m_brushdown2, "ImageDown2");
			LoadImage(elem, m_brushdown3, "ImageDown3");
			LoadImage(elem, m_brushslot, "ImageSlot");
			LoadImage(elem, m_brushblock1, "ImageBlock1");
			LoadImage(elem, m_brushblock2, "ImageBlock2");
			LoadImage(elem, m_brushblock3, "ImageBlock3");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = BrushUp0.GetColor();
			m_form.ImageUp0Name = m_brushup0.GetImageFileName();
			m_form.ImageUp1Name = m_brushup1.GetImageFileName();
			m_form.ImageUp2Name = m_brushup2.GetImageFileName();
			m_form.ImageUp3Name = m_brushup3.GetImageFileName();
			m_form.ImageDown0Name = m_brushdown0.GetImageFileName();
			m_form.ImageDown1Name = m_brushdown1.GetImageFileName();
			m_form.ImageDown2Name = m_brushdown2.GetImageFileName();
			m_form.ImageDown3Name = m_brushdown3.GetImageFileName();
			m_form.ImageSlotName = m_brushslot.GetImageFileName();
			m_form.ImageBlock1Name = m_brushblock1.GetImageFileName();
			m_form.ImageBlock2Name = m_brushblock2.GetImageFileName();
			m_form.ImageBlock3Name = m_brushblock3.GetImageFileName();
			SetBrushUp(1);
			SetBrushDown(1);
			SetBrushBlock(1);
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.IsAutoBlock = (elem.HasAttribute("autoblock") ? GetAttributeBool(elem, "autoblock") : true);
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
