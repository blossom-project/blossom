package fr.mgargadennec.blossom.core.user.web.resources;

import java.util.Arrays;

public class BlossomUserPasswordResource {
  private String oldPassword;
  private String[] newPasswords;

  /**
   * @return the oldPassword
   */
  public String getOldPassword() {
    return oldPassword;
  }

  /**
   * @param oldPassword the oldPassword to set
   */
  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  /**
   * @return the newPasswords
   */
  public String[] getNewPasswords() {
    return newPasswords;
  }

  /**
   * @param newPasswords the newPasswords to set
   */
  public void setNewPasswords(String[] someNewPasswords) {
    if (someNewPasswords == null) {
      this.newPasswords = null;
    } else {
      this.newPasswords = Arrays.copyOf(someNewPasswords, someNewPasswords.length);
    }
  }
}
