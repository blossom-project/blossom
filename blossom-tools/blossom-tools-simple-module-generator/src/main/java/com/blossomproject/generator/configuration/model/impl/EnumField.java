package com.blossomproject.generator.configuration.model.impl;

public class EnumField extends  DefaultField {

    private final Class<? extends Enum> enumClass;

    public EnumField(String name, String columnName, Class<?> className, String jdbcType, boolean required, boolean updatable, boolean nullable, String defaultValue, boolean searchable, Class<? extends Enum> enumClass) {
        super(name, columnName, className, jdbcType, required, updatable, nullable, defaultValue, searchable);
        this.enumClass = enumClass;
    }
}
