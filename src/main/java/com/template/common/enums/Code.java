package com.template.common.enums;

import java.util.List;
import java.util.stream.Collectors;

public enum Code {
	
	SENT_OTP("SENT_OTP", "SENT_OTP"),
	ACCOUNT_OPENING("ACCOUNT_OPENING","ACCOUNT_OPENING"),
	SAVE_PAN("SAVE_PAN","SAVE_PAN"),
	SAVE_AADHAAR("SAVE_AADHAAR","SAVE_AADHAAR"),
	SAVE_CONTACT("SAVE_CONTACT","SAVE_CONTACT"),
	SAVE_EMPLOYMENT("SAVE_EMPLOYMENT","SAVE_EMPLOYMENT"),
	SAVE_FINANCIAL("SAVE_FINANCIAL","SAVE_FINANCIAL"),
	SAVE_PARTNER_DATA("SAVE_PARTNER_DATA","SAVE_PARTNER_DATA"),
	SAVE_PERSONAL_DETAILS("SAVE_PERSONAL_DETAILS","SAVE_PERSONAL_DETAILS"),
	SAVE_E_MANDATE("SAVE_E_MANDATE","SAVE_E_MANDATE"),
	SAVE_MOBILE("SAVE_MOBILE","SAVE_MOBILE"),
	LOAN_AVAILED("LOAN_AVAILED","LOAN_AVAILED"),
	SAVE_ADDRESS("SAVE_ADDRESS","SAVE_ADDRESS"),
	TERMS_AND_CONDITION("TERMS_AND_CONDITION","TERMS_AND_CONDITION"),
	DATA_PRIVACY("DATA_PRIVACY","DATA_PRIVACY"),
	SUBMIT_PARTNER_DETAILS_SUCCESS("SUBMIT_PARTNER_DETAILS_SUCCESS","SUBMIT_PARTNER_DETAILS_SUCCESS")
	;


	private String key;
	private String value;
	
	private Code(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	public String getKey()
	{
		return this.key;
	}
	
	public String getValue()
	{
		return this.value;
	}
	

	public static Code getCode(String key)
	{
		List.of(Code.values()).stream().forEach(a-> System.out.println(a.key));
		List<Code> codes = List.of(Code.values()).stream().filter(a -> a.getKey().equals(key)).collect(Collectors.toList());
		
		if(!codes.isEmpty())
		{
			return codes.get(0);
		}
		else
		{
			return null;
		}
	}

}
