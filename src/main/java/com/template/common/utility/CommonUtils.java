package com.template.common.utility;



import com.template.common.dto.request.SMSDTO;
import com.template.common.enums.Code;
import com.template.common.enums.Medium;
import com.template.common.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonUtils {
	
	public static boolean validatePhone(String phone) {
		if(Objects.isNull(phone))
		{
			return false;
		}

		// Regex to check string contains only digits
		String regex = "[0-9-]{7,16}";
		Pattern p = Pattern.compile(regex);

		if (!(!Objects.isNull(phone) && !phone.isBlank())) {
			return false;
		}
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	public static boolean validateEmail(String email) {
		if(Objects.isNull(email))
		{
			return false;
		}

		String emailRegex = "[a-zA-Z0-9._]+[@]{1}[a-zA-Z]+[.]{1}[a-zA-Z]+[.]{0,1}[a-zA-Z]+";
		Pattern pat = Pattern.compile(emailRegex);
		if (!(!Objects.isNull(email) && !email.isBlank()))
		{
			return false;
		}
		String[] emailArr = email.split(Pattern.quote("."));
		if(!Objects.isNull(emailArr[emailArr.length-1]) && !Objects.isNull(emailArr[emailArr.length-2]))
		{
			if(emailArr[emailArr.length-1].equals(emailArr[emailArr.length-2]))
			{
				return false;
			}
		}

		return true;

	}
	
	public static void validateParameters(Map<String, Object> parameters) {
		if(!Objects.isNull(parameters)) {
					if (!parameters.isEmpty()) {
						for(Map.Entry<String, Object> entry:parameters.entrySet())
						{
							if(Objects.isNull(entry.getKey()) || Objects.isNull(entry.getValue()))
							{
								throw new ServiceException("Invalid parameter");
							}
						}
					}
			}


	}
	
	public static void validateCode(String code) {
		if(Objects.isNull(code))
		{
			throw new ServiceException("Invalid phone");
		}
		System.out.println(List.of(Code.values()).get(0)+","+code);
		List<String> codes = List.of(Code.values()).stream().map(a -> a.getValue()).collect(Collectors.toList());
		if(!codes.contains(code))
		{
			throw new ServiceException("Invalid code");
		}
	}
	
	public static void validateProcessId(String processId) {
		if(Objects.isNull(processId))
		{
			throw new ServiceException("Invalid process ID");
		}
	}
	
	/**
	 * @param smsDTO
	 */
	public static void validateMedium(Integer  medium) {
		if(Objects.isNull(medium))
		{
			throw new ServiceException("Invalid medium");
		}
		if(Objects.isNull(Medium.getMedium(medium)))
		{
			throw new ServiceException("Invalid medium");
		}
	}

	public static void validateSource(SMSDTO smsDTO) {
		if(Objects.isNull(smsDTO.getMedium()) || smsDTO.getSource().equals(""))
		{
			throw new ServiceException("Invalid source");
		}

	}

	public static void validateEmailAndMobile(SMSDTO smsDTO){
		if(Medium.getMedium(smsDTO.getMedium()).equals(Medium.SMS)  || Medium.getMedium(smsDTO.getMedium()).equals(Medium.BOTH)){
			if(!CommonUtils.validatePhone(smsDTO.getPhone())) {
				throw new ServiceException("Invalid phone");
			}
		} else if (Medium.getMedium(smsDTO.getMedium()).equals(Medium.EMAIL)  || Medium.getMedium(smsDTO.getMedium()).equals(Medium.BOTH)) {
			if(!CommonUtils.validateEmail(smsDTO.getEmail())) {
				throw new ServiceException("Invalid email");
			}
		}
	}
}
