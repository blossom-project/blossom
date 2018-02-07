package com.blossom_project.sample;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by zoula_000 on 19/05/2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(tags = {"@login"}, features = "src/test/resources/fr/blossom/integration/features/login")
public class RunLoginTests {
}
