package com.study.rocketmq.word;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.sl.draw.binding.CTPositiveSize2D;

public class PoiUtil  {public static int headingCount = 0;

public static int totalRows = 39;

public static XWPFDocument createDocument() {

    XWPFDocument doc = new XWPFDocument();
    //添加标题
    addCustomHeadingStyle(doc, "Heading1", 1);
    addCustomHeadingStyle(doc, "Heading2", 2);
    return doc;
}

public static void createTitle(XWPFDocument doc, String title) {
    XWPFParagraph titleParagraph = doc.createParagraph();

    //设置段落居中
    titleParagraph.setAlignment(ParagraphAlignment.CENTER);

    XWPFRun titleParagraphRun = titleParagraph.createRun();
    titleParagraphRun.setText(title);
    titleParagraphRun.setColor("000000");
    titleParagraphRun.setFontSize(20);

}

public static XWPFParagraph createHeading(XWPFDocument doc, String title) {
    //段落
    XWPFParagraph paragraph = doc.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText(title);
//    run.setColor("696969");
    run.setFontSize(18);
    run.setBold(true);//标题加粗
    return paragraph;
}

/**
 * 创建标题1
 *
 * @param doc
 * @param title
 */
public static void createHeading1(XWPFDocument doc, String title) {
    XWPFParagraph paragraph = createHeading(doc, title);
    paragraph.setStyle("Heading1");
    headingCount++;
}

/**
 * 创建标题2
 *
 * @param doc
 * @param title
 */
public static void createHeading2(XWPFDocument doc, String title) {
    XWPFParagraph paragraph = createHeading(doc, title);
    paragraph.setStyle("Heading2");
    headingCount++;
}

/**
 * 创建正文
 *
 * @param doc
 * @param body
 */
public static void createBody(XWPFDocument doc, String body) {
    // 正文
    XWPFParagraph paragraphX = doc.createParagraph();
    XWPFRun runX = paragraphX.createRun();
    runX.setText(body);
    paragraphX.setIndentationFirstLine(440);//首行缩进：567==1厘米
}


public static void addImage(XWPFDocument doc, String imagePath, String description) {
    FileInputStream in = null;
    ByteArrayInputStream byteInputStream = null;
    try {
        in = new FileInputStream(imagePath);
        byte[] ba = new byte[in.available()];
        in.read(ba);
        byteInputStream = new ByteArrayInputStream(ba);
        XWPFParagraph picture = doc.createParagraph();
        picture.setAlignment(ParagraphAlignment.CENTER);
        //添加图片
        doc.addPictureData(byteInputStream, CustomXWPFDocument.PICTURE_TYPE_JPEG);
        createPicture(doc, 400, 400, picture);
        addDescription(doc, description);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (byteInputStream != null) {
            try {
                byteInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public static void createTable(XWPFDocument doc, String title) {
    addDescription(doc, title);
    XWPFTable table = doc.createTable(3, 3);
    //列宽自动分割
    CTTblWidth infoTableWidth = table.getCTTbl().addNewTblPr().addNewTblW();
    infoTableWidth.setType(STTblWidth.DXA);
    infoTableWidth.setW(BigInteger.valueOf(9072));

    setTableFonts(table.getRow(0).getCell(0), "编号");
    setTableFonts(table.getRow(0).getCell(1), "问题");
    setTableFonts(table.getRow(0).getCell(2), "应答");
    setTableFonts(table.getRow(1).getCell(0), "1");
    setTableFonts(table.getRow(1).getCell(1), "陈述日期");
    setTableFonts(table.getRow(1).getCell(2), "2017年02月17日");
    setTableFonts(table.getRow(2).getCell(0), "2");
    setTableFonts(table.getRow(2).getCell(1), "PICS序列号");
    setTableFonts(table.getRow(2).getCell(2), "121313132131");

}

// word跨列合并单元格
public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
    for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
        XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
        if (cellIndex == fromCell) {
            // The first merged cell is set with RESTART merge value
            cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
        } else {
            // Cells which join (merge) the first one, are set with CONTINUE
            cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
        }
    }
}

// word跨行并单元格
public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
    for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
        if (rowIndex == fromRow) {
            // The first merged cell is set with RESTART merge value
            cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
        } else {
            // Cells which join (merge) the first one, are set with CONTINUE
            cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
        }
    }
}

/**
 * 设置表格中字体
 *
 * @param cell
 * @param cellText
 */
private static void setTableFonts(XWPFTableCell cell, String cellText) {
    CTP ctp = CTP.Factory.newInstance();
    XWPFParagraph p = new XWPFParagraph(ctp, cell);
    p.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun run = p.createRun();
    run.setText(cellText);
    CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();
    CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
    fonts.setAscii("仿宋");
    fonts.setEastAsia("仿宋");
    fonts.setHAnsi("仿宋");
    cell.setParagraph(p);
}

/**
 * 添加描述信息
 *
 * @param doc
 * @param description
 */
public static void addDescription(XWPFDocument doc, String description) {
    if (StringUtils.isEmpty(description)) {
        return;
    }
    XWPFParagraph title = doc.createParagraph();
    XWPFRun run = title.createRun();
    run.setText(description);
    run.setBold(true);
    title.setAlignment(ParagraphAlignment.CENTER);
}

public static void addEmptyRow(XWPFDocument doc) {
    for (int i = 0; i < totalRows - headingCount; i++) {
        doc.createParagraph();
    }
}

public static void main(String[] args) {
    try {
    } catch (Exception e) {
        e.printStackTrace();
    }
}


/**
 * 创建目录
 */
public static void createToc(String filePath) {
    WordManager wordManager = new WordManager(false);
    try {
        wordManager.openDocument(filePath);
        wordManager.insertToc();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        wordManager.close();
    }
}

/**
 * @param width     宽
 * @param height    高
 * @param paragraph 段落
 */
private static void createPicture(XWPFDocument doc, int width, int height,
                                  XWPFParagraph paragraph) {
    int id = doc.getAllPictures().size() - 1;
    final int EMU = 9525;
    width *= EMU;
    height *= EMU;
    String blipId = doc.getAllPictures().get(id).getPackageRelationship()
            .getId();
    CTInline inline = paragraph.createRun().getCTR().addNewDrawing()
            .addNewInline();
    String picXml = ""
            + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
            + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
            + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
            + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
            + id
            + "\" name=\"Generated\"/>"
            + "            <pic:cNvPicPr/>"
            + "         </pic:nvPicPr>"
            + "         <pic:blipFill>"
            + "            <a:blip r:embed=\""
            + blipId
            + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
            + "            <a:stretch>"
            + "               <a:fillRect/>"
            + "            </a:stretch>"
            + "         </pic:blipFill>"
            + "         <pic:spPr>"
            + "            <a:xfrm>"
            + "               <a:off x=\"0\" y=\"0\"/>"
            + "               <a:ext cx=\""
            + width
            + "\" cy=\""
            + height
            + "\"/>"
            + "            </a:xfrm>"
            + "            <a:prstGeom prst=\"rect\">"
            + "               <a:avLst/>"
            + "            </a:prstGeom>"
            + "         </pic:spPr>"
            + "      </pic:pic>"
            + "   </a:graphicData>" + "</a:graphic>";

    inline.addNewGraphic().addNewGraphicData();
    XmlToken xmlToken = null;
    try {
        xmlToken = XmlToken.Factory.parse(picXml);
    } catch (XmlException xe) {
        xe.printStackTrace();
    }
    inline.set(xmlToken);

    inline.setDistT(0);
    inline.setDistB(0);
    inline.setDistL(0);
    inline.setDistR(0);

    CTPositiveSize2D extent = inline.addNewExtent();
    extent.setCx(width);
    extent.setCy(height);

    CTNonVisualDrawingProps docPr = inline.addNewDocPr();
    docPr.setId(id);
    docPr.setName("图片名称");
    docPr.setDescr("描述信息");
}

private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

    CTStyle ctStyle = CTStyle.Factory.newInstance();
    ctStyle.setStyleId(strStyleId);

    CTString styleName = CTString.Factory.newInstance();
    styleName.setVal(strStyleId);
    ctStyle.setName(styleName);

    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
    indentNumber.setVal(BigInteger.valueOf(headingLevel));

    // lower number > style is more prominent in the formats bar
    ctStyle.setUiPriority(indentNumber);

    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
    ctStyle.setUnhideWhenUsed(onoffnull);

    // style shows up in the formats bar
    ctStyle.setQFormat(onoffnull);

    // style defines a heading of the given level
    CTPPr ppr = CTPPr.Factory.newInstance();
    ppr.setOutlineLvl(indentNumber);
    ctStyle.setPPr(ppr);

    XWPFStyle style = new XWPFStyle(ctStyle);

    // is a null op if already defined
    XWPFStyles styles = docxDocument.createStyles();

    style.setType(STStyleType.PARAGRAPH);
    styles.addStyle(style);

}
}
