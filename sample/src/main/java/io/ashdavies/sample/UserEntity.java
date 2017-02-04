package io.ashdavies.sample;

import com.google.firebase.database.PropertyName;

class UserEntity {

  @PropertyName("first_name")
  public String firstName;

  @PropertyName("last_name")
  public String lastName;

  @PropertyName("company_name")
  public String companyName;

  public String email;
  public String web;

  public String address;
  public String country;
  public String phone1;
  public String phone2;
  public String postal;
}
