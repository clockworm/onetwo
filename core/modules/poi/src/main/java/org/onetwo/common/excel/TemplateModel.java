package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;

public class TemplateModel {
	private static final String DEFAULLT_VARNAME = "_sheet";

	private String name;
	private String varname;
	
	private List<RowModel> rows;
	
//	private final Freezer freezer = new Freezer(TemplateModel.class.getSimpleName());
	
	//new
	private String label;
	private boolean multiSheet;
	private String datasource;
	private Integer sizePerSheet;
	
	private String columnWidth;
	
	private Map<Integer, Short> columnWidthMap = LangUtils.newHashMap();
	
	public TemplateModel(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.varname = name;
	}

	public List<RowModel> getRows() {
		return rows;
	}

	public void setRows(List<RowModel> rows) {
//		this.freezer.checkOperation("setRows");
		this.rows = rows;
	}

	public String getLabel() {
		if(StringUtils.isBlank(label))
			return getName();
		return label;
	}
	
	public String getVarName(){
		if(StringUtils.isBlank(varname))
			varname = name;
		if(StringUtils.isBlank(varname) || !LangUtils.isWord(varname))
			varname = DEFAULLT_VARNAME;
		return varname;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public Integer getSizePerSheet() {
		return sizePerSheet;
	}

	public void setSizePerSheet(Integer sizePerSheet) {
		this.sizePerSheet = sizePerSheet;
	}

	public boolean isMultiSheet() {
		return multiSheet;
	}

	public void setMultiSheet(boolean multiSheet) {
		this.multiSheet = multiSheet;
	}

	public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
		
		if(StringUtils.isNotBlank(columnWidth)){
			String[] attrs = StringUtils.split(columnWidth, ";");
			for(String attr : attrs){
				String[] av = StringUtils.split(attr.trim(), ":");
				if(av!=null && av.length==2){
					try {
						this.columnWidthMap.put(Types.convertValue(av[0], Integer.class), Types.convertValue(av[1], Short.class));
					} catch (Exception e) {
						throw new ServiceException("parse sheet coluomnWidth error", e);
					}
				}
			}
		}
	}

	public Map<Integer, Short> getColumnWidthMap() {
		return columnWidthMap;
	} 
	
	/*public boolean isMultiSheet(){
		return this.sizePerSheet!=null && this.sizePerSheet>0 && StringUtils.isNotBlank(datasource);
	}*/

}
