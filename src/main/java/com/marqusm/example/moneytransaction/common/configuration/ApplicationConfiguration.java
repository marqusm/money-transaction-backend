package com.marqusm.example.moneytransaction.common.configuration;

import com.marqusm.example.moneytransaction.common.constant.ApplicationMode;
import java.util.Map;
import lombok.Getter;
import lombok.val;
import org.yaml.snakeyaml.Yaml;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class ApplicationConfiguration {

  private static final Map<String, String> environmentVariables;
  private static final Map<String, String> localVariables;
  @Getter private static final ApplicationMode applicationMode;

  static {
    environmentVariables = System.getenv();
    localVariables =
        new Yaml().load(ApplicationConfiguration.class.getResourceAsStream("/application.yaml"));
    applicationMode =
        ApplicationMode.valueOf(getConfiguration("money-transaction-backend.application-mode"));
  }

  private static String getConfiguration(String name) {
    val envValue = environmentVariables.get(name.toUpperCase().replaceAll("[-,.]", "_"));
    if (envValue != null) {
      return envValue;
    }

    return localVariables.get(name);
  }
}
