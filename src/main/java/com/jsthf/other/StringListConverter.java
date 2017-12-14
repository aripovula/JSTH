package com.jsthf.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> list) {
    if (list!=null) return String.join(",", list);
    return "";
  }

  @Override
  public List<String> convertToEntityAttribute(String joined) {
	  if (joined==null) return Arrays.asList();
    return new ArrayList<>(Arrays.asList(joined.split(",")));
  }

}