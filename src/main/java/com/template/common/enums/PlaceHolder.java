package com.template.common.enums;

import java.util.List;
import java.util.stream.Collectors;

public enum PlaceHolder {
	OTP("otp", "[OTP]"),
	MERCHANT("merchant","[MERCHANT]"),
	MAX_TXN_AMOUNT("maxTxnAmount","[MAX_TXN_AMOUNT]"),
	FREQUENCY("frequency","[FREQUENCY]"),
	START_DATE("startDate","[START_DATE]"),
	END_DATE("endDate","[END_DATE]"),
	BANK_DATE("bankName","[BANK_NAME]"),
	LOAN_AMOUNT("loanAmount","[LOAN_AMOUNT]"),
	LOAN_DATE("loanDate","[LOAN_DATE]"),
	LOAN_APPLICATION_NO("loanApplicationNo","[LOAN_APPLICATION_NO]"),
	URL("url", "[URL]"),
	ATTACHMENT("attachment", "attachment"),
	FILE_NAME("fileName", "fileName")
	;


	private String key;
	private String value;
	
	private PlaceHolder(String key, String value)
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

	public static PlaceHolder getPlaceHolder(String key)
	{
		List<PlaceHolder> placeHolders = List.of(PlaceHolder.values()).stream().filter(a -> a.getKey().equals(key)).collect(Collectors.toList());
		
		if(!placeHolders.isEmpty())
		{
			return placeHolders.get(0);
		}
		else
		{
			return null;
		}
	}
}
