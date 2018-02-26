package com.blossomproject.generator.configuration;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.impl.DefaultField;
import com.blossomproject.generator.configuration.model.impl.EnumField;

public class EnumFieldBuilder extends FieldBuilder<EnumFieldBuilder> {


    private final Class<? extends Enum> enumClass;

    EnumFieldBuilder(FieldsBuilder parent, String name, Class<? extends Enum> enumClass) {
        super(parent, name, enumClass, "varchar");
        this.enumClass = enumClass;
    }

    @Override
    Field build() {
        return new EnumField(name, columnName, className, jdbcType, required, updatable, nullable,
                defaultValue, searchable, enumClass);
    }
}
