package org.kku.fonticons.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javafx.scene.text.Font;

public enum IconFont
{
  MATERIAL_DESIGN("/font/materialdesignicons-webfont.ttf");

  private final String m_trueTypeFontResourceName;
  private Map<Integer, Font> fontBySizeMap = new HashMap<>();
  private Properties m_nameToCodepointProperties;

  IconFont(String trueTypeFontResourceName)
  {
    m_trueTypeFontResourceName = trueTypeFontResourceName;
  }

  public String getTrueTypeFontResourceName()
  {
    return m_trueTypeFontResourceName;
  }

  public String getTrueTypeFontResourceDictionaryName()
  {
    return getTrueTypeFontResourceName().replace(".ttf", ".properties");
  }

  public InputStream getTrueTypeFontAsStream()
  {
    return IconFont.class.getResourceAsStream(getTrueTypeFontResourceName());
  }

  public InputStream getTrueTypeFontDictionaryAsStream()
  {
    return IconFont.class.getResourceAsStream(getTrueTypeFontResourceDictionaryName());
  }

  public FxIcon getIcon(String iconName)
  {
    return new FxIcon(iconName).font(this);
  }
  
  public Font getIconFont(int iconSize)
  {
    Font font;

    font = fontBySizeMap.get(iconSize);
    if (font == null)
    {
      try (InputStream is = getTrueTypeFontAsStream())
      {
        font = Font.loadFont(is, iconSize);
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
        return null;
      }

      fontBySizeMap.put(iconSize, font);
    }

    return font;
  }

  public String getCodepoint(String text)
  {
    text = getIconNameProperties().getProperty(text, text);
    text = new String(Character.toChars(Integer.parseInt(text, 16)));

    return text;
  }

  public List<String> getAllIconNameList()
  {
    Enumeration<Object> keys;
    List<String> list;

    list = new ArrayList<>();
    keys = getIconNameProperties().keys();
    while (keys.hasMoreElements())
    {
      list.add(keys.nextElement().toString());
    }

    return list;
  }

  private Properties getIconNameProperties()
  {
    if (m_nameToCodepointProperties == null)
    {
      try (InputStream is = getTrueTypeFontDictionaryAsStream())
      {
        m_nameToCodepointProperties = new Properties();
        m_nameToCodepointProperties.load(is);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    return m_nameToCodepointProperties;
  }
}
