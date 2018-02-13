package com.blossomproject.generator.configuration.model;

import java.io.IOException;

public interface ExecutionPlan {

  void execute(Settings settings) throws IOException;

}
