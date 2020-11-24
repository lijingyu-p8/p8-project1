package com.jiuqi.budget.electronicarchives.excel2pdf;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.jiuqi.budget.electronicarchives.pojo.EA_FtpFileInfo;
import com.jiuqi.budget.electronicarchives.pojo.ElectronicArchivesConstans;

public class POIUtil {
	
	
	/**
	 * 获取单元格颜色
	 *
	 * @param color
	 *            单元格颜色
	 * @return RGB 三通道颜色, 默认: 黑色
	 */
	public static int getRGB(Color color) {
		int result = 0x00FFFFFF;

		int red = 0;
		int green = 0;
		int blue = 0;

		if (color instanceof HSSFColor) {
			HSSFColor hssfColor = (HSSFColor) color;
			short[] rgb = hssfColor.getTriplet();
			red = rgb[0];
			green = rgb[1];
			blue = rgb[2];
		}

		if (color instanceof XSSFColor) {
			XSSFColor xssfColor = (XSSFColor) color;
			byte[] rgb = xssfColor.getRGB();
			if (rgb != null) {
				red = (rgb[0] < 0) ? (rgb[0] + 256) : rgb[0];
				green = (rgb[1] < 0) ? (rgb[1] + 256) : rgb[1];
				blue = (rgb[2] < 0) ? (rgb[2] + 256) : rgb[2];
			}
		}

		if (red != 0 || green != 0 || blue != 0) {
			result = new java.awt.Color(red, green, blue).getRGB();
		}
		return result;
	}

	/**
	 * 获取边框的颜色
	 *
	 * @param wb
	 *            文档对象
	 * @param index
	 *            颜色版索引
	 * @return RGB 三通道颜色
	 */
	@SuppressWarnings("deprecation")
	public static int getBorderRBG(Workbook wb, short index) {
		int result = 0;

		if (wb instanceof HSSFWorkbook) {
			HSSFWorkbook hwb = (HSSFWorkbook) wb;
			HSSFColor color = hwb.getCustomPalette().getColor(index);
			if (color != null) {
				result = getRGB(color);
			}
		}

		if (wb instanceof XSSFWorkbook) {
			XSSFColor color = new XSSFColor();
			color.setIndexed(index);
			result = getRGB(color);
		}
		return result;
	}

	public static com.itextpdf.text.Font toItextFont(org.apache.poi.ss.usermodel.Font font) {
		com.itextpdf.text.Font iTextFont = FontFactory.getFont(font.getFontName(), BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED, font.getFontHeightInPoints());
		return iTextFont;
	}

	public static byte[] scale(byte[] bytes, double width, double height) {
		BufferedImage bufferedImage = null;
		BufferedImage bufTarget = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			bufferedImage = ImageIO.read(bais);
			double sx = width / bufferedImage.getWidth();
			double sy = height / bufferedImage.getHeight();
			int type = bufferedImage.getType();
			if (type == BufferedImage.TYPE_CUSTOM) {
				ColorModel cm = bufferedImage.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster((int) width, (int) height);
				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				bufTarget = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else {
				bufTarget = new BufferedImage((int) width, (int) height, type);
			}

			Graphics2D g = bufTarget.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(bufferedImage, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();

			if (bufTarget != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufTarget, "png", baos);
				byte[] result = baos.toByteArray();
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成xml结构化文件
	 * @param key
	 * @param list
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String generateExcel(String key, List<EA_FtpFileInfo> list) {
		String fileName = key + "_" + list.get(0).getOrgCode();
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(fileName);
		//设置表头
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] xlsTitle = ElectronicArchivesConstans.getXlsTitle();
		int length = xlsTitle.length;
		HSSFCell cell = null;
		for (int i = 0; i < length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(xlsTitle[i]);
			cell.setCellStyle(style);
			sheet.setColumnWidth(i, 5000);
		}
		//设置数据行
		int size = list.size();
		for (int i = 0; i < size; i++) {
			row = sheet.createRow(i+1);
			EA_FtpFileInfo ea_FtpFileInfo = list.get(i);
			List<String> xlsRowData = ea_FtpFileInfo.getXlsRowData();
			for (int j = 0; j < length; j++) {
				cell = row.createCell(j);
				cell.setCellValue(xlsRowData.get(j));
				cell.setCellStyle(style);
			}
		}
		String path = ElectronicArchivesConstans.TEMPPATH;

		File webFile = new File(path);
		if(!webFile.exists()) {
			webFile.mkdir();
		}
		String excelPath = path + File.separator + fileName + ".xls";
		//文档输出
		FileOutputStream out = null;
        try {
        	out = new FileOutputStream(excelPath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return excelPath;
	}
}
