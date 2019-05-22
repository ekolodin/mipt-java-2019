package ru.sberbank;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

  /**
   * Scans object "from" for all getters. If object "to" contains correspondent setter, it will
   * invoke it to set property value for "to" which equals to the property of "from".
   * <p/>
   * The type in setter should be compatible to the value returned by getter (if not, no invocation
   * performed). Compatible means that parameter type in setter should be the same or be superclass
   * of the return type of the getter.
   * <p/>
   * The method takes care only about public methods. *
   *
   * @param to Object which properties will be set.
   * @param from Object which properties will be used to get values.
   */

  public static void assign(Object to, Object from) {
    Method[] methods = to.getClass().getMethods();
    for (Method method : methods) {
      String getterName = method.getName();

      if (getterName.startsWith("get")) {
        try {
          String setterName = "set" + getterName.substring(3);
          Method setter = from.getClass().getMethod(setterName, method.getReturnType());
          setter.invoke(from, method.invoke(to));

        } catch (NoSuchMethodException e) {
          System.out.println(method.getName() + " setter was not found");
        } catch (IllegalAccessException e) {
          System.out.println("Getter or/and setter was private");
        } catch (InvocationTargetException e) {
          System.out.println("Something went wrong");
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) {
    BeanUtilsChecker first = new BeanUtilsChecker(1, 2, 3);
    BeanUtilsChecker second = new BeanUtilsChecker(3, 4, 5);
    BeanUtils.assign(first, second);

    System.out.println(first);
    System.out.println(second);
  }
}
