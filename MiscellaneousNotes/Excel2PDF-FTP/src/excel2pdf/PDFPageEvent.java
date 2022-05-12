package com.jiuqi.budget.electronicarchives.excel2pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFPageEvent extends PdfPageEventHelper {
    protected PdfTemplate template;
    public BaseFont baseFont;

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try{
            this.template = writer.getDirectContent().createTemplate(100, 100);
            this.baseFont = new Font(Resource.BASE_FONT_CHINESE , 8, Font.NORMAL).getBaseFont();
        } catch(Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        //��ÿҳ������ʱ��ѡ���xҳ����Ϣд��ģ��ָ��λ��
        PdfContentByte byteContent = writer.getDirectContent();
        String text = "��" + writer.getPageNumber() + "ҳ";
        float textWidth = this.baseFont.getWidthPoint(text, 8);
        float realWidth = document.right() - textWidth;
        //
        byteContent.beginText();
        byteContent.setFontAndSize(this.baseFont , 10);
        byteContent.setTextMatrix(realWidth , document.bottom());
        byteContent.showText(text);
        byteContent.endText();
        byteContent.addTemplate(this.template , realWidth , document.bottom());
    }
}
