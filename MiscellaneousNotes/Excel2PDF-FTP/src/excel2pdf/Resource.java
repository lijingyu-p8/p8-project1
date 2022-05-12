package com.jiuqi.budget.electronicarchives.excel2pdf;

import org.apache.poi.hssf.usermodel.HSSFFont;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

public class Resource {
    /**
     * 中文字体支持
     */
    protected static BaseFont BASE_FONT_CHINESE;
    static {
        try {
            BASE_FONT_CHINESE = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            FontFactory.registerDirectories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将POI Font 转换到 iText Font
     * @param font
     * @return
     */
    public static Font getFont(HSSFFont font) {
        try {
            Font iTextFont = FontFactory.getFont(font.getFontName(),BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
                    font.getFontHeightInPoints());
            return iTextFont;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
