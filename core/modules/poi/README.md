# onetw-poi
基于poi，对操作excel的简单封装。

## 使用 ##
###定义xml模板
excel_template.xml:   
```xml

<?xml version="1.0" encoding="UTF-8"?>
<template name="用户卡列表" columnWidth="2:30">
	<rows>
		<row span="2" fieldStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldFont="boldweight:BOLDWEIGHT_BOLD">
			<fields>
				<field value="'报名客户资料'" rowspan="2" colspan="4"/>
			</fields>
		</row>
		<row renderHeader="true" 
		      fieldHeaderStyle="alignment:ALIGN_CENTER;verticalAlignment:VERTICAL_CENTER;" fieldHeaderFont="boldweight:BOLDWEIGHT_BOLD"
		      name="element" type="iterator" datasource="#cardList" fieldFont="boldweight:BOLDWEIGHT_NORMAL;color:COLOR_RED"> 
			<fields>
				<field label="主键" name="id" />
				<field label="卡号" name="cardNo"/>
				<field label="卡密码" name="cardPwd" />
			</fields>
		</row>
	</rows>
</template>

```
###java代码
创建Java类Card：   

```Java

public class CardEntity {
	
	protected Long id;
	protected String cardNo;
	protected String cardPwd;
	
	//...getter and setter
}

```

使用TemplateGenerator生成excel：   
```Java

        
		List<CardEntity> cardList = LangOps.generateList(10, i->{
			CardEntity card = new CardEntity();
			card.setId(Long.valueOf(i));
			card.setCardNo("card_no_"+i);
			card.setCardPwd("password"+i);
			card.setStartTime(new Date());
			return card;
		});
		Map<String, Object> context = new HashMap<>();
		context.put("cardList", cardList);
		TemplateGenerator g = ExcelGenerators.createExcelGenerator("c:/excel_template.xml", context);
		String path = "c:/excel_generated.xls";
		g.write(path);

```
