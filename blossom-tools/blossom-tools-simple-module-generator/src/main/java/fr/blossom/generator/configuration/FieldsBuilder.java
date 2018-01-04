package fr.blossom.generator.configuration;

import com.google.common.collect.Lists;
import fr.blossom.generator.configuration.model.Field;
import java.util.List;
import java.util.stream.Collectors;

public class FieldsBuilder {

  private final SettingsBuilder parent;
  private final List<FieldBuilder> fields;

  FieldsBuilder(SettingsBuilder parent) {
    this.parent = parent;
    this.fields = Lists.newArrayList();
  }

  public FieldsBuilder defaultFields() {
    return
      this.string("name").searchable(true).requiredCreate(true).updatable(true).nullable(false).notBlank(true).defaultValue(null).maxLength(50)
        .and()
        .string("description").searchable(true).requiredCreate(true).updatable(true).nullable(false).notBlank(false).isLob(true).defaultValue(null)
        .and();
  }

  public StringFieldBuilder string(String name) {
    StringFieldBuilder field = new StringFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public SettingsBuilder done() {
    return parent;
  }

  List<Field> build() {
    return this.fields.stream().map(f -> f.build()).collect(Collectors.toList());
  }

}
