package ru.sberbank;

public class BeanUtilsChecker {

  BeanUtilsChecker(int firstField, int secondField, int thirdField) {
    this.firstField = firstField;
    this.secondField = secondField;
    this.thirdField = thirdField;
  }

  private int firstField;
  private int secondField;
  private int thirdField;

  public int getFirstField() {
    return firstField;
  }

  public void setFirstField(int firstField) {
    this.firstField = firstField;
  }

  public int getSecondField() {
    return secondField;
  }

  public void setSecondField(int secondField) {
    this.secondField = secondField;
  }

  public int getThirdField() {
    return thirdField;
  }

  public void setThirdField(int thirdField) {
    this.thirdField = thirdField;
  }

  @Override
  public String toString() {
    return "" + firstField + " " + secondField + " " + thirdField;
  }
}
