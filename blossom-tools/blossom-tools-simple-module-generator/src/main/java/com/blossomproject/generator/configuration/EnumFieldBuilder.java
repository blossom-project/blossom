package com.blossomproject.generator.configuration;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.impl.EnumField;

public class EnumFieldBuilder extends FieldBuilder<EnumFieldBuilder> {


    private final Class<? extends Enum> enumClass;

    EnumFieldBuilder(FieldsBuilder parent, String name, Class<? extends Enum> enumClass) {
        super(parent, name, enumClass, "varchar(" + maxSize(enumClass) + ")");
        this.enumClass = enumClass;
    }

    @Override
    Field build() {
        return new EnumField(name, columnName, className, jdbcType, required, updatable, nullable,
                defaultValue, searchable, maxSize(enumClass));
    }

    private static int maxSize(Class<? extends Enum> enumClass) {
        int maxSize = 0;
        for (Enum enumValue : enumClass.getEnumConstants()) {
            maxSize = Math.max(maxSize, enumValue.name().length());
        }
        return maxSize;
    }
}
