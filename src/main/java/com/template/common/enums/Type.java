package com.template.common.enums;

public enum Type {
	
	ALERT("alert"), SMS("sms"), EMAIL("email"),BOTH("both");
	
	
	private String value;
	
	private Type(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return this.value;
	}

}
