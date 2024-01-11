package com.orm.poc.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryStrings {

    public final String FIND_BY_ID_QUERY = "SELECT * FROM %s WHERE %s = ?";

    public final String UPDATE_QUERY = "UPDATE %s SET %s WHERE %s = ?";
    
    public final String SET_VALUE = "%s = ?";
    
}
