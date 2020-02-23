package com.marqusm.example.moneytransaction.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 23-Feb-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class YamlConfig {
  private ServiceConfiguration moneyTransactionService;

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
    private String host;
    private Integer port;
    private String database;
    private String user;
    private String password;
    private String url;
    private Integer maxConnections;
  }
}
