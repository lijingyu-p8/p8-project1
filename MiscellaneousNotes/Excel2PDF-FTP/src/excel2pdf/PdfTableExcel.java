package com.jiuqi.budget.electronicarchives.excel2pdf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
/**
 * 
 * @author lijingyu
 *
 */
public class PdfTableExcel {
	private Workbook workbook;
	private Sheet sheet;
	
	public PdfTableExcel(Workbook workbook) {
		this.workbook = workbook;
		this.sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
	}

	public PdfPTable createTable() {
		int rows = sheet.getPhysicalNumberOfRows();
		List<PdfPCell> cells = new ArrayList<PdfPCell>();
		float[] widths = null;
		float mw = 0;
		Set<String> noCols = new HashSet<>();
		for (int i = 0; i < rows; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			int columns = row.getPhysicalNumberOfCells();
			float[] cws = new float[columns];
			for (int j = 0; j < columns; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
				}
				if (noCols.contains(cell.getRowIndex() + "_" + cell.getColumnIndex())) {
					continue;
				}
				
				CellRangeAddress range = getColspanRowspanByExcel(row.getRowNum(), cell.getColumnIndex());
				int rowspan = 1;
				int colspan = 1;
				if (range != null) {
					rowspan = range.getLastRow() - range.getFirstRow() + 1;
					colspan = range.getLastColumn() - range.getFirstColumn() + 1;
				}
				
				float cw = getPOIColumnWidth(cell);
				cws[cell.getColumnIndex()] = cw;

				PdfPCell pdfpCell = new PdfPCell();
				//应该是getfillfore  此处不设置背景色
				pdfpCell.setBackgroundColor(
						new BaseColor(POIUtil.getRGB(cell.getCellStyle().getFillBackgroundColorColor())));
				pdfpCell.setVerticalAlignment(getVAlignByExcel(cell.getCellStyle().getVerticalAlignmentEnum().getCode()));
				pdfpCell.setHorizontalAlignment(getHAlignByExcel(cell.getCellStyle().getAlignmentEnum().getCode()));

				if (sheet.getDefaultRowHeightInPoints() != row.getHeightInPoints()) {
					pdfpCell.setFixedHeight(this.getPixelHeight(row.getHeightInPoints()));
				}
				pdfpCell.setPhrase(getPhrase(cell));
				addBorderByExcel(pdfpCell, cell.getCellStyle());

				pdfpCell.setColspan(colspan);
				pdfpCell.setRowspan(rowspan);

				cells.add(pdfpCell);
				//跨行
				if (rowspan > 1) {
					//获取当前单元格的跨了哪些列
					int count = j + colspan;
					for (int c = j; c < count; c++) {
						for (int r = i + 1; r < rowspan + i; r++) {
							noCols.add(r + "_" + c);
						}
					}
				}
				j += colspan - 1;
			}

			float rw = 0;
			for (int j = 0; j < cws.length; j++) {
				rw += cws[j];
			}
			if (rw > mw || mw == 0) {
				widths = cws;
				mw = rw;
			}
		}

		PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		for (PdfPCell pdfpCell : cells) {
			table.addCell(pdfpCell);
		}
		return table;
	}

	protected Phrase getPhrase(Cell cell) {
		Anchor anchor = new Anchor(String.valueOf(getCellValue(cell)), getFontByExcel(cell.getCellStyle()));
		anchor.setName("归档");
		return anchor;
	}

	/**
	 * 获取单元格值
	 *
	 * @return 单元格值
	 */
	public Object getCellValue(Cell cell) {
		Object val = "";
		try {
			if (cell != null) {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					val = cell.getNumericCellValue();
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// POI Excel 日期格式转换
						val = DateUtil.getJavaDate((Double) val);
					} else {
						if ((Double) val % 1 > 0) {
							val = new DecimalFormat("0.00").format(val);
						} else {
							val = new DecimalFormat("0").format(val);
						}
					}
				} else if (cell.getCellTypeEnum() == CellType.STRING) {
					val = cell.getStringCellValue();
				} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellTypeEnum() == CellType.ERROR) {
					val = cell.getErrorCellValue();
				}
			}
		} catch (Exception e) {
			return val;
		}
		return val;
	}

	private float getPixelHeight(float poiHeight) {
		float pixel = poiHeight / 28.6f * 26f;
		return pixel;
	}

	/**
	 * 此处获取Excel的列宽像素
	 * 
	 * @param cell
	 * @return 像素宽
	 */
	private int getPOIColumnWidth(Cell cell) {
		int poiCWidth = sheet.getColumnWidth(cell.getColumnIndex());
		return poiCWidth;
	}

	private CellRangeAddress getColspanRowspanByExcel(int rowIndex, int colIndex) {
		CellRangeAddress result = null;
		int num = sheet.getNumMergedRegions();
		for (int i = 0; i < num; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if (range.getFirstColumn() == colIndex && range.getFirstRow() == rowIndex) {
				result = range;
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	private Font getFontByExcel(CellStyle style) {
		Font result = new Font(Resource.BASE_FONT_CHINESE, 8, Font.NORMAL);
		short index = style.getFontIndex();
		org.apache.poi.ss.usermodel.Font font = workbook.getFontAt(index);
		if (font.getBold()) {
			result.setStyle(Font.BOLD);
		}
		HSSFColor color = HSSFColor.getIndexHash().get(font.getColor());
		if (color != null) {
			int rbg = POIUtil.getRGB(color);
			result.setColor(new BaseColor(rbg));
		}
		FontUnderline underline = FontUnderline.valueOf(font.getUnderline());
		if (underline == FontUnderline.SINGLE) {
			String ulString = Font.FontStyle.UNDERLINE.getValue();
			result.setStyle(ulString);
		}
		return result;
	}

	private void addBorderByExcel(PdfPCell cell, CellStyle style) {
		cell.setBorderColorLeft(new BaseColor(POIUtil.getBorderRBG(workbook, style.getLeftBorderColor())));
		cell.setBorderColorRight(new BaseColor(POIUtil.getBorderRBG(workbook, style.getRightBorderColor())));
		cell.setBorderColorTop(new BaseColor(POIUtil.getBorderRBG(workbook, style.getTopBorderColor())));
		cell.setBorderColorBottom(new BaseColor(POIUtil.getBorderRBG(workbook, style.getBottomBorderColor())));
	}

	private int getVAlignByExcel(short align) {
		int result = 0;
		if (align == VerticalAlignment.BOTTOM.getCode()) {
			result = Element.ALIGN_BOTTOM;
		}
		if (align == VerticalAlignment.CENTER.getCode()) {
			result = Element.ALIGN_MIDDLE;
		}
		if (align == VerticalAlignment.JUSTIFY.getCode()) {
			result = Element.ALIGN_JUSTIFIED;
		}
		if (align == VerticalAlignment.TOP.getCode()) {
			result = Element.ALIGN_TOP;
		}
		return result;
	}

	private int getHAlignByExcel(short align) {
		int result = 0;
		if (align == HorizontalAlignment.LEFT.getCode()) {
			result = Element.ALIGN_LEFT;
		}
		if (align == HorizontalAlignment.RIGHT.getCode()) {
			result = Element.ALIGN_RIGHT;
		}
		if (align == HorizontalAlignment.JUSTIFY.getCode()) {
			result = Element.ALIGN_JUSTIFIED;
		}
		if (align == HorizontalAlignment.CENTER.getCode()) {
			result = Element.ALIGN_CENTER;
		}
		return result;
	}
}
