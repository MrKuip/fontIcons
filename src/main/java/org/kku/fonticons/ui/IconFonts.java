package org.kku.fonticons.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public enum IconFonts
{
  MATERIAL_DESIGN("/font/materialdesignicons-webfont.ttf");

  private final String m_trueTypeFontResourceName;
  private Map<Integer, Font> fontBySizeMap = new HashMap<>();
  private Map<String, FontMetrics> fontMetricMap = new HashMap<>();
  private Properties m_nameToCodepointProperties;

  IconFonts(String trueTypeFontResourceName)
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
    return IconFonts.class.getResourceAsStream(getTrueTypeFontResourceName());
  }

  public InputStream getTrueTypeFontDictionaryAsStream()
  {
    return IconFonts.class.getResourceAsStream(getTrueTypeFontResourceDictionaryName());
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

  public FontMetrics getFontMetrics(Font font)
  {
    FontMetrics fontMetrics;

    fontMetrics = fontMetricMap.get(font.toString());
    if (fontMetrics == null)
    {
      fontMetrics = new FontMetrics(font);
      fontMetricMap.put(font.toString(), fontMetrics);
    }

    return fontMetrics;
  }

  public static class FontMetrics
  {
    final private Text text;
    public final float ascent;
    public final float descent;
    public final float lineHeight;

    private FontMetrics(Font fnt)
    {
      Bounds b;

      text = new Text();
      text.setTextAlignment(TextAlignment.CENTER);
      text.setFont(fnt);

      b = text.getLayoutBounds();

      ascent = (float) -b.getMinY();
      descent = (float) b.getMaxY();
      lineHeight = (float) b.getHeight();
    }

    public float getAscent()
    {
      return ascent;
    }

    public float getDescent()
    {
      return descent;
    }

    public float getLineHeight()
    {
      return lineHeight;
    }

    public float computeStringWidth(String txt)
    {
      text.setText(txt);
      return (float) text.getLayoutBounds().getWidth();
    }
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
