package com.axelor.message.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.message.db.EmailAddress;

public class EmailAddressRepository extends JpaRepository<EmailAddress> {

  public EmailAddressRepository() {
    super(EmailAddress.class);
  }

  public EmailAddress findByName(String name) {
    return Query.of(EmailAddress.class)
      .filter("self.name = :name")
      .bind("name", name)
      .fetchOne();
  }

  public EmailAddress findByAddress(String address) {
    return Query.of(EmailAddress.class)
      .filter("self.address = :address")
      .bind("address", address)
      .fetchOne();
  }
}
