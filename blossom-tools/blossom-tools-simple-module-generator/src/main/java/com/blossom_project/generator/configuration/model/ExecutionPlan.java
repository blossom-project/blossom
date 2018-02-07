package com.blossom_project.generator.configuration.model;

import java.io.IOException;

public interface ExecutionPlan {

  void execute(Settings settings) throws IOException;

}
