package org.kku.fonticons.util;

public class IconUtil
{
  public static String normalizeIconName(String iconName)
  {
    return iconName.toUpperCase().replace('-', '_');
  }
}
