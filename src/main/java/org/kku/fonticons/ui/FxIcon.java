package org.kku.fonticons.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.kku.fonticons.util.IconUtil;
import javafx.geometry.BoundingBox;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FxIcon
{
  public enum IconSize
  {
    VERY_SMALL(12),
    SMALLER(18),
    SMALL(24),
    LARGE(32),
    VERY_LARGE(48);

    private final int m_size;

    IconSize(int size)
    {
      m_size = size;
    }

    public int getSize()
    {
      return m_size;
    }
  }

  public enum IconColor
  {
    DEFAULT_OUTLINE(Color.rgb(0, 74, 131)),
    DEFAULT_FILL(Color.WHITE),
    LIGHT_BLUE_FILL(Color.rgb(160, 200, 255)),
    WHITE(Color.WHITE),
    BLACK(Color.BLACK),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW);

    private Color m_color;

    IconColor(Color color)
    {
      m_color = color;
    }

    public Color getColor()
    {
      return m_color;
    }
  }

  public enum IconColorModifier
  {
    DARKER(color -> color.darker()),
    BRIGHTER(color -> color.brighter());

    private final Function<Color, Color> m_modifier;

    private IconColorModifier(Function<Color, Color> modifier)
    {
      m_modifier = modifier;
    }

    public Color modify(Color color)
    {
      return m_modifier.apply(color);
    }
  }

  public enum IconAlignment
  {
    UPPER_RIGHT(0.33),
    UPPER_CENTER(0.33),
    UPPER_LEFT(0.33),
    CENTER_RIGHT(0.33),
    CENTER_CENTER(1),
    CENTER_LEFT(0.33),
    LOWER_RIGHT(0.33),
    LOWER_CENTER(0.33),
    LOWER_LEFT(0.33);

    private final double mi_defaultSizeFactor;

    IconAlignment(double sizeFactor)
    {
      mi_defaultSizeFactor = sizeFactor;
    }

    public double getDefaultSizeFactor()
    {
      return mi_defaultSizeFactor;
    }
  }

  private final IconFont m_iconFont;
  private final String m_iconId;
  private final double m_size;
  private final Color m_fillColor;
  private final Color m_strokeColor;
  private final Map<IconAlignment, FxIcon> m_iconMap;
  private final Font m_font;
  private final String m_text;

  public FxIcon(FxIcon icon)
  {
    this(icon.m_iconFont, icon.m_iconId, icon.m_size, icon.m_fillColor, icon.m_strokeColor,
        new LinkedHashMap<>(icon.m_iconMap));
  }

  public FxIcon(String iconId)
  {
    this(IconFont.values()[0], iconId, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), null,
        new LinkedHashMap<IconAlignment, FxIcon>());
  }

  public FxIcon(IconFont iconFont, String iconId, double size, Color fillColor, Color strokeColor,
      Map<IconAlignment, FxIcon> iconMap)
  {
    m_iconFont = iconFont;
    m_iconId = IconUtil.normalizeIconName(iconId);
    m_size = size;
    m_fillColor = fillColor;
    m_iconMap = iconMap;
    m_strokeColor = strokeColor;

    m_font = m_iconFont.getIconFont(size);
    m_text = m_iconFont.getCodepoint(getId());
  }

  public String getId()
  {
    return m_iconId;
  }

  public FxIcon font(IconFont font)
  {
    return new FxIcon(font, m_iconId, m_size, m_fillColor, m_strokeColor, m_iconMap);
  }

  public FxIcon size(IconSize size)
  {
    return size(size.getSize());
  }

  public FxIcon size(double size)
  {
    return new FxIcon(m_iconFont, m_iconId, size, m_fillColor, m_strokeColor, m_iconMap);
  }

  public double getSize()
  {
    return m_size;
  }

  public String getText()
  {
    return m_text;
  }

  public Font getFont()
  {
    return m_font;
  }

  public Color getFillColor()
  {
    return m_fillColor;
  }

  public Color getStrokeColor()
  {
    return m_strokeColor;
  }

  public FxIcon fillColor(IconColor fillColor)
  {
    return fillColor(fillColor.getColor());
  }

  public FxIcon fillColor(Color fillColor)
  {
    return new FxIcon(m_iconFont, m_iconId, m_size, fillColor, m_strokeColor, m_iconMap);
  }

  public FxIcon strokeColor(IconColor strokeColor)
  {
    return strokeColor(strokeColor.getColor());
  }

  public FxIcon strokeColor(IconColorModifier iconColorModifier)
  {
    return strokeColor(iconColorModifier.modify(m_fillColor));
  }

  public FxIcon strokeColor(Color strokeColor)
  {
    return new FxIcon(m_iconFont, m_iconId, m_size, m_fillColor, strokeColor, m_iconMap);
  }

  public FxIcon add(IconAlignment alignment, FxIcon fxIcon)
  {
    return add(alignment, fxIcon, alignment.getDefaultSizeFactor());
  }

  public FxIcon add(IconAlignment alignment, FxIcon fxIcon, double sizeFactor)
  {
    LinkedHashMap<IconAlignment, FxIcon> iconMap;

    iconMap = new LinkedHashMap<>(m_iconMap);
    iconMap.put(alignment, new FxIcon(fxIcon).size(getSize() * sizeFactor));

    return new FxIcon(m_iconFont, m_iconId, m_size, m_fillColor, m_strokeColor, iconMap);
  }

  public IconLabel getIconLabel()
  {
    return new IconLabel(this);
  }

  public static class IconLabel
    extends Pane
  {
    private static final String BOUNDING_BOX = "BOUNDING_BOX";

    private final FxIcon m_icon;

    private IconLabel(FxIcon icon)
    {
      m_icon = icon;
      init();
    }

    private void init()
    {
      addIcon(m_icon.getText(), m_icon.getFont(), m_icon.getFillColor(), m_icon.getStrokeColor(), 0.0, 0.0,
          m_icon.getSize(), m_icon.getSize());

      if (!m_icon.m_iconMap.isEmpty())
      {
        m_icon.m_iconMap.entrySet().forEach(entry -> {
          FxIcon icon;
          IconAlignment alignment;
          double x;
          double y;

          icon = entry.getValue();
          alignment = entry.getKey();

          switch (alignment)
          {
            case CENTER_CENTER:
              x = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              y = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              break;
            case CENTER_LEFT:
              x = 0;
              y = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              break;
            case CENTER_RIGHT:
              x = 0 + (m_icon.getSize() - icon.getSize());
              y = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              break;
            case LOWER_CENTER:
              x = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              y = 0 + (m_icon.getSize() - icon.getSize());
              break;
            case LOWER_LEFT:
              x = 0;
              y = 0 + (m_icon.getSize() - icon.getSize());
              break;
            case LOWER_RIGHT:
              x = 0 + (m_icon.getSize() - icon.getSize());
              y = 0 + (m_icon.getSize() - icon.getSize());
              break;
            case UPPER_CENTER:
              x = 0 + (m_icon.getSize() - icon.getSize()) / 2;
              y = 0;
              break;
            case UPPER_LEFT:
              x = 0;
              y = 0;
              break;
            case UPPER_RIGHT:
              x = 0 + (m_icon.getSize() - icon.getSize());
              y = 0;
              break;
            default:
              x = 0;
              y = 0;
              break;
          }

          addIcon(icon.getText(), icon.getFont(), icon.getFillColor(), icon.getStrokeColor(), x, y, icon.getSize(),
              icon.getSize());
        });
      }
    }

    private void addIcon(String text, Font font, Color fillColor, Color strokeColor, double x, double y, double width,
        double height)
    {
      Text label;
      StringBuilder style;

      label = new Text(text);
      label.setFont(font);
      style = new StringBuilder("");
      if (fillColor != null)
      {
        style.append("-fx-fill: ");
        style.append(toRgb(fillColor));
        style.append(";");
      }
      if (strokeColor != null)
      {
        style.append("-fx-stroke: ");
        style.append(toRgb(strokeColor));
        style.append(";");
        style.append("-fx-stroke-width: 1;");
      }
      label.setStyle(style.toString());
      label.getProperties().put(BOUNDING_BOX, new BoundingBox(snap(x), snap(y), width, height));

      getChildren().add(label);
    }

    private double snap(double y)
    {
      return y;
    }

    private Object toRgb(Color color)
    {
      return "rgba(" + ((int) (color.getRed() * 255.0)) + "," + ((int) (color.getGreen() * 255.0)) + ","
          + ((int) (color.getBlue() * 255.0)) + ", " + color.getOpacity() + ")";
    }

    @Override
    protected void layoutChildren()
    {
      for (Node child : getChildren())
      {
        BoundingBox bb;

        bb = (BoundingBox) child.getProperties().get(BOUNDING_BOX);
        if (bb != null)
        {
          child.resizeRelocate(bb.getMinX(), bb.getMinY(), bb.getWidth(), bb.getHeight());
        }
      }
    }
  }
}
