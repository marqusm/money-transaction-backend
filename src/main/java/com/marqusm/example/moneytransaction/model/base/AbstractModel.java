package com.marqusm.example.moneytransaction.model.base;

import com.marqusm.example.moneytransaction.annotation.JsonExclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Marko
 * @createdOn : 26-Jan-20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbstractModel {
  @JsonExclude private Boolean metaActive;

  public void setMetadata(AbstractModel metaRecord) {
    this.metaActive = metaRecord.getMetaActive();
  }
}
