package org.onetwo.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultPOIExcelReader implements ExcelReader {
	
	public DefaultPOIExcelReader(){
	}
	
	@Override
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor) {
		return readData(createWorkbook(in), extractor);
	}


	@Override
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor, int startSheet, int endSheet) {
		return readData(createWorkbook(in), extractor, startSheet, endSheet);
	}
	

	@Override
	public <T> Map<String, T> readData(File file, ExcelDataExtractor<T> extractor, int startSheet, int endSheet) {
		return readData(createWorkbook(file), extractor, startSheet, endSheet);
	}

	public <T> Map<String, T> readData(Workbook workbook, ExcelDataExtractor<T> extractor){
		return readData(workbook, extractor, -1, -1);
	}
	
	/****
	 * 
	 * @param workbook
	 * @param extractor
	 * @param startSheet include
	 * @param endSheet not include
	 * @return
	 */
	public <T> Map<String, T> readData(Workbook workbook, ExcelDataExtractor<T> extractor, int startSheet, int readCount){
		Assert.notNull(workbook);
		try {
			int sheetCount = workbook.getNumberOfSheets();
			Sheet sheet = null;
			Map<String, T> datas = new LinkedHashMap<String, T>();
			
			if(startSheet<0)
				startSheet = 0;
			if(readCount<0)
				readCount = sheetCount;
			
			int hasReadCount = 0;
			for(int i=startSheet; i<sheetCount; i++){
				if(hasReadCount<readCount){
					sheet = workbook.getSheetAt(i);
					String name = sheet.getSheetName();
					if(sheet.getPhysicalNumberOfRows()<1)
						continue;
					if(StringUtils.isBlank(name))
						name = "" + i;
					T extractData = extractor.extractData(sheet, i);
					datas.put(name, extractData);
					
					hasReadCount++;
				}
			}
			return datas;
		}catch (ServiceException e) {
			throw e;
		}catch (Exception e) {
			throw new BaseException("read excel file error.", e);
		}
	}
	
	protected Workbook createWorkbook(String path){
		File file = new File(path);
		if(!file.exists()){
			path = FileUtils.getResourcePath(path);
			file = new File(path);
		}
		Workbook workbook = createWorkbook(file);
		return workbook;
	}
	
	protected Workbook createWorkbook(File file){
		Workbook workbook = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			workbook = createWorkbook(in);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException("read excel error : " + file.getPath(), e);
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
	}
	
	/*
	protected Workbook createWorkbook(InputStream in, boolean isXssf) {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new ServiceException("createWorkbook error : " + in, e);
		}
		return workbook;
	}*/
	
	protected Workbook createWorkbook(InputStream in){
		Workbook workbook = null;
		try {
//			br.mark(1024*10);
			workbook = WorkbookFactory.create(in);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException("read excel inputstream error : " + in, e);
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
	}
	
	protected List<?> mapRow(Sheet sheet, int sheetIndex, SSFRowMapper<?> mapper){
		int rowCount = sheet.getPhysicalNumberOfRows();
		
		List<String> names = mapper.mapTitleRow(sheetIndex, sheet);
		
		Row row = null;
		List<Object> datas = new ArrayList<Object>();
		for(int rowIndex=mapper.getDataRowStartIndex(); rowIndex<rowCount; rowIndex++){
			row = sheet.getRow(rowIndex);
			Object value = mapper.mapDataRow(sheet, names, row, rowIndex);
			if(value==null)
				continue;
			datas.add(value);
		}
		return datas;
	}
	
}
