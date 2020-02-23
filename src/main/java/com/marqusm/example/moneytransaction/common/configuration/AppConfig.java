package com.marqusm.example.moneytransaction.common.configuration;

import com.marqusm.example.moneytransaction.common.constant.RepositoryImplementation;
import com.marqusm.example.moneytransaction.common.constant.RestImplementation;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * @author : Marko
 * @createdOn : 01-Feb-20
 */
public class AppConfig {

  @Getter private static final YamlConfig localConfig;

  @Getter public static final RepositoryImplementation repositoryImplementation;
  @Getter public static final RestImplementation restImplementation;

  static {
    Yaml yaml = new Yaml(new Constructor(YamlConfig.class));
    localConfig = yaml.load(AppConfig.class.getResourceAsStream("/application.yaml"));
    localConfig
        .getMoneyTransactionService()
        .getDatabase()
        .setUrl(
            String.format(
                "jdbc:postgresql://%s:%s/%s",
                localConfig.getMoneyTransactionService().getDatabase().getHost(),
                localConfig.getMoneyTransactionService().getDatabase().getPort(),
                localConfig.getMoneyTransactionService().getDatabase().getDatabase()));

    repositoryImplementation =
        RepositoryImplementation.valueOf(
            localConfig.getMoneyTransactionService().getRepositoryImplementation());
    restImplementation =
        RestImplementation.valueOf(
            localConfig.getMoneyTransactionService().getRestImplementation());
  }
}
