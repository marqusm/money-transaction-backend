package com.marqusm.example.moneytransaction.common.configuration;

import com.marqusm.example.moneytransaction.common.constant.RepositoryImplementation;
import com.marqusm.example.moneytransaction.common.constant.RestImplementation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class ApplicationConfiguration {

  private static final YamlConfiguration localConfiguration;

  @Getter public static final RepositoryImplementation repositoryImplementation;
  @Getter public static final RestImplementation restImplementation;

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class YamlConfiguration {
    private ServiceConfiguration moneyTransactionService;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class ServiceConfiguration {
    private String repositoryImplementation;
    private String restImplementation;
    private DatabaseConfiguration database;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class DatabaseConfiguration {
    private String url;
    private String user;
    private String password;
  }

  static {
    Yaml yaml = new Yaml(new Constructor(YamlConfiguration.class));
    localConfiguration =
        yaml.load(ApplicationConfiguration.class.getResourceAsStream("/application.yaml"));

    repositoryImplementation =
        RepositoryImplementation.valueOf(
            localConfiguration.getMoneyTransactionService().getRepositoryImplementation());
    restImplementation =
        RestImplementation.valueOf(
            localConfiguration.getMoneyTransactionService().getRestImplementation());
  }
}
