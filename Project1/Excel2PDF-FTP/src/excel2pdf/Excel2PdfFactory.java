package com.jiuqi.budget.electronicarchives.excel2pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jiuqi.dna.core.spi.application.AppUtil;
import com.jiuqi.grid.GridData;
import com.jiuqi.office.excel.ExcelException;
import com.jiuqi.office.excel.SimpleExportor;
/**
 * 
 * @author lijingyu
 *
 */
public class Excel2PdfFactory {
	
	/**
	 * 将Excel转成PDF
	 * @param griddata
	 * @param fileName
	 * @param sheetName
	 * @return pdf路径
	 * @throws ExcelException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	public static String convertExcel2Pdf(GridData griddata,String fileName,String sheetName) throws ExcelException, IOException, DocumentException, EncryptedDocumentException, InvalidFormatException{
		String path = AppUtil.getDefaultApp().getDNAWork().getPath();
		path = path.replace("\\", "/");

		File webFile = new File(path + "/excel2pdftemp");
		if(!webFile.exists()) {
			webFile.mkdir();
		}
		if (fileName.endsWith("xls") || fileName.endsWith("pdf")) {
			fileName = fileName.substring(0, fileName.length() - 4);
		}
		String excelPath = path + "/excel2pdftemp/" + fileName + ".xls";
		String pdfPath = path + "/excel2pdftemp/" + fileName + ".pdf";
		//将griddata转为Excel，输出到指定路径
		FileOutputStream outputStream = new FileOutputStream(excelPath);
		SimpleExportor exportor = new SimpleExportor(sheetName, griddata);
		exportor.setTitle(sheetName);
		exportor.setPreSheetName(sheetName);
		exportor.export(outputStream);
		if (outputStream != null) {
			outputStream.close();
		}
		convertExcel2Pdf(excelPath,pdfPath);
		return pdfPath;
	}
	
	/**
	 * 将Excel转成PDF
	 * @param excelFilePath
	 * @param pdfFilePath
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws EncryptedDocumentException 
	 * @throws InvalidFormatException 
	 */
	public static void convertExcel2Pdf(String excelFilePath, String pdfFilePath) throws DocumentException, EncryptedDocumentException, IOException, InvalidFormatException{
		FileInputStream excelInputStream = new FileInputStream(excelFilePath);
		FileOutputStream pdfOutputStream = new FileOutputStream(pdfFilePath);
		convertExcel2Pdf(excelInputStream,pdfOutputStream);
		//删除临时Excel文件 
		File file = new File(excelFilePath); 
		if (file != null && file.isFile() && file.exists()) {
			file.delete();
		}
	}
	
    public static void convertExcel2Pdf(FileInputStream excelInputStream,FileOutputStream pdfOutputStream) {
    	Document document = new Document();
    	document.setPageSize(PageSize.A4.rotate());
    	PdfPTable table;
		try {
			PdfWriter writer = PdfWriter.getInstance(document, pdfOutputStream);
			writer.setPageEvent(new PDFPageEvent());
			document.open();
			//获取Excel的sheet页
			Workbook workbook = WorkbookFactory.create(excelInputStream);
			table = createPdfTable(workbook,document,writer);
			document.add(table);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (document != null) {
				document.close();
			}
			if (pdfOutputStream != null) {
				try {
					pdfOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (excelInputStream != null) {
				try {
					excelInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		table = null;
		document = null;
	}

    /**
     * 创建表格
     * @param workbook
     * @param document
     * @param writer
     * @param griddata 
     * @return
     */
	private static PdfPTable createPdfTable(Workbook workbook, Document document, PdfWriter writer) {
		PdfTableExcel pdfTableExcel = new PdfTableExcel(workbook);
		PdfPTable table = pdfTableExcel.createTable();
		table.setKeepTogether(true);
	    table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	    return table;
	}
}
