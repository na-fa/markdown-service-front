package com.example.enums;

public enum TmpUserStatusEnum {
  TEMPORARY(0),
  REGISTERED(1);

  private int status;

  TmpUserStatusEnum(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
