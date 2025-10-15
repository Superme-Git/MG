using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentSliderBar : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormSliderBar m_form;

		private IBrush m_brushslot;
		private IBrush m_brushblock1;
		private IBrush m_brushblock2;
		private IBrush m_brushblock3;

		private IBrush m_brushblock;

		public CFormSliderBar Form
		{
			get { return m_form; }
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

		public CComponentSliderBar(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Name = name;
			m_mainform = mainform;
			Brush = null;
			m_brushslot = Render.CreateBrush();
			m_brushblock1 = Render.CreateBrush();
			m_brushblock2 = Render.CreateBrush();
			m_brushblock3 = Render.CreateBrush();
			SetBrushBlock(1);
			m_form = new CFormSliderBar(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;
			m_form.BkColor = Color.FromArgb(255, 255, 255, 255);
		}

		public override CComponent Clone()
		{
			CComponentSliderBar one = new CComponentSliderBar(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
            one.Rect = rect;
			one.Lock = Lock;
			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.BrushSlot = BrushSlot.Clone();
			one.BrushBlock1 = BrushBlock1.Clone();
			one.BrushBlock2 = BrushBlock2.Clone();
			one.BrushBlock3 = BrushBlock3.Clone();
			one.Form.ImageSlotName = m_form.ImageSlotName;
			one.Form.ImageBlock1Name = m_form.ImageBlock1Name;
			one.Form.ImageBlock2Name = m_form.ImageBlock2Name;
			one.Form.ImageBlock3Name = m_form.ImageBlock3Name;
			one.SetBrushBlock(1);
			one.Form.BkColor = m_form.BkColor;
			one.Form.IsViewOnly = m_form.IsViewOnly;
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
					Render.DrawRect(rect, m_brushslot);
					Render.DrawRect(new Rectangle(rect.X, rect.Y, rect.Width, m_brushblock.GetImageRect().Height), m_brushblock);
				}
				else
				{
					Render.DrawRect(rect, m_brushslot);
					Render.DrawRect(new Rectangle(rect.X, rect.Y, m_brushblock.GetImageRect().Width, rect.Height), m_brushblock);
				}
			}

			base.OnDraw();
		}

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("SliderBar");
			base.SaveXML(doc, sub);
			sub.SetAttribute("b", "255,255,255,255");
			sub.SetAttribute("vertical", m_form.Vertical ? "true" : "false");
			SaveImage(doc, sub, m_brushslot, "ImageSlot");
			SaveImage(doc, sub, m_brushblock1, "ImageBlock1");
			SaveImage(doc, sub, m_brushblock2, "ImageBlock2");
			SaveImage(doc, sub, m_brushblock3, "ImageBlock3");			
			sub.SetAttribute("viewonly", m_form.IsViewOnly ? "true" : "false");
			sub.SetAttribute("tipinfo", m_form.TipInfo);
			elem.AppendChild(sub);
		}

		public override void LoadXML(XmlElement elem)
		{
			base.LoadXML(elem);
			m_form.Vertical = (GetAttributeString(elem, "vertical") == "true");
			LoadImage(elem, m_brushslot, "ImageSlot");
			LoadImage(elem, m_brushblock1, "ImageBlock1");
			LoadImage(elem, m_brushblock2, "ImageBlock2");
			LoadImage(elem, m_brushblock3, "ImageBlock3");
			m_form.N = Name;
			m_form.C = Class;
			m_form.BkColor = m_brushslot.GetColor();
			m_form.ImageSlotName = m_brushslot.GetImageFileName();
			m_form.ImageBlock1Name = m_brushblock1.GetImageFileName();
			m_form.ImageBlock2Name = m_brushblock2.GetImageFileName();
			m_form.ImageBlock3Name = m_brushblock3.GetImageFileName();
			SetBrushBlock(1);
			m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
			m_form.TipInfo = GetAttributeString(elem, "tipinfo");
		}
	}
}
