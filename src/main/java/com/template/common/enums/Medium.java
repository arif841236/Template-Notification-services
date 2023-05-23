package com.template.common.enums;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Medium {

	
	EMAIL(1), SMS(2), BOTH(3);

	private Integer value;

	private Medium(Integer mediumNumber) {
		this.value = mediumNumber;
	}
	
	public static Medium getMedium(int value)
	{
		List<Medium> mediums = List.of(Medium.values()).stream()
				.filter(m -> m.getValue() == value)
				.collect(Collectors.toList());
		
		if(!mediums.isEmpty())
		{
			return mediums.get(0);
		}
		return null;
	}
}
