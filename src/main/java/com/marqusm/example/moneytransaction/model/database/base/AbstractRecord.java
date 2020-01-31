package com.marqusm.example.moneytransaction.model.database.base;

import java.time.OffsetDateTime;
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
public class AbstractRecord {
  private Boolean metaActive;
  private OffsetDateTime metaCreationDate;
  private OffsetDateTime metaModifiedDate;

  public void setMetadata(AbstractRecord metaRecord) {
    this.metaActive = metaRecord.getMetaActive();
    this.metaCreationDate = metaRecord.getMetaCreationDate();
    this.metaModifiedDate = metaRecord.getMetaModifiedDate();
  }
}
