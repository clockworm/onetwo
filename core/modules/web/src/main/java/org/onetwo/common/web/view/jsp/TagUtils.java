package org.onetwo.common.web.view.jsp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.csrf.CsrfPreventor;
import org.onetwo.common.web.filter.BaseInitFilter;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.view.jsp.form.FormTagBean;

final public class TagUtils {

	public static String PARAM_FORMAT = "format";
	public static String BASE_TAG_DIR = "/WEB-INF/tags/";
	public static String BASE_VIEW_DIR = "/WEB-INF/views/";


	public static String getViewPage(String path){
		return getDirPage(BASE_VIEW_DIR, path);
	}
	public static String getTagPage(String path){
		return getDirPage(BASE_TAG_DIR, path);
	}
	public static String getDirPage(String baseDir, String path){
		if(StringUtils.isBlank(path))
			return path;
		
		if(path.startsWith(BASE_VIEW_DIR))
			return path;
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return baseDir + path;
	}
	
	public static Page<Object> toPage(Object dsValue){
		Page<Object> page = null;
		if(dsValue==null){
			page = Page.create();
		}else if (dsValue instanceof Page) {
			page = (Page<Object>) dsValue;
		}else {
			List<Object> list = null;
			if(Map.class.isInstance(dsValue)){
				Map<?, ?> dataMap = (Map<?, ?>)dsValue;
				list = LangUtils.newArrayList(dataMap.size());
				for(Entry<?, ?> entry : dataMap.entrySet()){
//					list.add(KVEntry.create(entry));
					list.add(entry);
				}
			}else{
				list = LangUtils.asList(dsValue);
			}
			if (list == null)
				list = Collections.EMPTY_LIST;
			page = Page.create();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
		return page;
	}
	
	public static String pageLink(String action, int numb){
		String result = action;
		if(numb<=1)
			return result;
		if (action.indexOf("?")!=-1){
			result += "&pageNo="+numb;
		}else{
			result += "?pageNo="+numb;
		}
		return result;
	}

	public static String appendXlsFormat(String action){
		return appendParam(action, "format", "xls");
	}
	
	public static String appendParam(String action, String name, String value){
		String result = action;
		if (action.indexOf("?")!=-1){
			result += "&"+name+"="+value;
		}else{
			result += "?"+name+"="+value;
		}
		return result;
	}
	

	public static String getFormVarName(){
		return FormTagBean.class.getSimpleName();
	}
	

	public static String getRequestUri(HttpServletRequest request){
		String surl = BaseSiteConfig.getInstance().getBaseURL()+(String)request.getAttribute(BaseInitFilter.REQUEST_URI);
		return surl;
	}
	

	public static String getRequsetUriWithQueryString(HttpServletRequest request){
		String surl = getRequestUri(request);
		String queryString = request.getQueryString();
		if(StringUtils.isBlank(queryString))
			return surl;
		if(surl.contains("?")){
			surl += "&" + queryString;
		}else{
			surl += "?" + queryString;
		}
		return surl;
	}
	

	public static String getRequsetUrlFilterPageNo(HttpServletRequest request){
		String surl = getRequestUri(request);
		String queryString = getQueryStringFilterPageNo(request);
		if(StringUtils.isBlank(queryString))
			return surl;
		if(surl.contains("?")){
			surl += "&" + queryString;
		}else{
			surl += "?" + queryString;
		}
		return surl;
	}


	public static String getQueryStringFilterPageNo(HttpServletRequest request) {
		String str = request.getQueryString();
		if(StringUtils.isBlank(str))
			return "";
		CasualMap params = new CasualMap(str);
		params.filter("pageNo");
		str = params.toParamString();
		return str;
	}
	
	public static String parseAction(HttpServletRequest request, String action, CsrfPreventor csrfPreventor){
		String surl = getRequestUri(request);
		if(StringUtils.isBlank(action)){
			return surl;
		}
		String[] symbols = StringUtils.split(action, "|");
		int index = 0;
		for (String symbol : symbols) {
			if (StringUtils.isBlank(symbol))
				continue;
			String qstr = processUrlSymbol(request, symbol, csrfPreventor);
			if (StringUtils.isNotBlank(qstr)) {
				if (index == 0)
					surl += "?";
				else
					surl += "&";
				surl += qstr;
				index++;
			}
		}
		return surl;
	}
	
	public static String processUrlSymbol(HttpServletRequest request, String symbol, CsrfPreventor csrfPreventor) {
		String str = null;
		if (symbol.equals(":qstr")) {
			str = request.getQueryString();
			if(StringUtils.isBlank(str))
				return "";
			CasualMap params = new CasualMap(str);
			params.filter("pageNo", "order", "orderBy");
			str = params.toParamString();
		} else if (symbol.equals(":post2get")) {
			if(csrfPreventor!=null){
				str = RequestUtils.getPostParametersWithout(request, "pageNo", "order", "orderBy", csrfPreventor.getFieldOfTokenFieldName(), request.getParameter(csrfPreventor.getFieldOfTokenFieldName())).toParamString();
			}else{
				str = RequestUtils.getPostParametersWithout(request, "pageNo", "order", "orderBy").toParamString();
			}
		}else{
			str = symbol;
		}
		return str;
	}

	
	private TagUtils(){
	}
}
