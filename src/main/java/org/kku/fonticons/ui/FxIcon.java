package org.kku.fonticons.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import org.kku.fonticons.util.IconUtil;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;

public class FxIcon
{
  static public boolean m_debug = false;

  public enum IconSize
  {
    VERY_SMALL(12),
    SMALLER(18),
    SMALL(24),
    LARGE(32),
    VERY_LARGE(128);

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

  public enum IconAlignment
  {
    UPPER_RIGHT(3, TextAlignment.RIGHT, VPos.BOTTOM),
    UPPER_CENTER(3, TextAlignment.CENTER, VPos.BOTTOM),
    UPPER_LEFT(3, TextAlignment.LEFT, VPos.BOTTOM),
    CENTER_RIGHT(3, TextAlignment.RIGHT, VPos.CENTER),
    CENTER_CENTER(1, TextAlignment.CENTER, VPos.CENTER),
    CENTER_LEFT(3, TextAlignment.LEFT, VPos.CENTER),
    LOWER_RIGHT(3, TextAlignment.RIGHT, VPos.TOP),
    LOWER_CENTER(3, TextAlignment.CENTER, VPos.TOP),
    LOWER_LEFT(3, TextAlignment.LEFT, VPos.TOP);

    private final double mi_defaultSizeFactor;
    private final TextAlignment mi_textAlign;
    private final VPos mi_textBaseline;

    IconAlignment(double sizeFactor, TextAlignment textAlign, VPos textBaseline)
    {
      mi_defaultSizeFactor = sizeFactor;
      mi_textAlign = textAlign;
      mi_textBaseline = textBaseline;
    }

    public TextAlignment getTextAlign()
    {
      return mi_textAlign;
    }

    public VPos getTextBaseline()
    {
      return mi_textBaseline;
    }

    public double getDefaultSizeFactor()
    {
      return mi_defaultSizeFactor;
    }
  }

  private final IconBuilder builder;

  public FxIcon(String iconName)
  {
    this(new IconBuilder(iconName));
  }

  public FxIcon(IconBuilder builder)
  {
    this.builder = builder;
  }

  public FxIcon font(IconFont font)
  {
    return new FxIcon(builder.font(font));
  }

  public FxIcon size(IconSize size)
  {
    return size(size.getSize());
  }

  public FxIcon size(double size)
  {
    return new FxIcon(builder.size(size));
  }

  public double getSize()
  {
    return builder.getSize();
  }

  public FxIcon fillColor(IconColor color)
  {
    return fillColor(color.getColor());
  }

  public FxIcon fillColor(Color color)
  {
    return new FxIcon(builder.fillColor(color));
  }

  public FxIcon strokeColor(IconColor strokeColor)
  {
    return strokeColor(strokeColor.getColor());
  }

  public FxIcon strokeColor(Color strokeColor)
  {
    return new FxIcon(builder.strokeColor(strokeColor));
  }

  public FxIcon add(IconAlignment alignment, FxIcon fxIcon)
  {
    return add(alignment, fxIcon, alignment.getDefaultSizeFactor());
  }

  public FxIcon add(IconAlignment alignment, FxIcon fxIcon, double sizeFactor)
  {
    return new FxIcon(builder.add(alignment, fxIcon.size(getSize() / sizeFactor)));
  }

  public ImageView getImageView()
  {
    return new ImageView(getImage());
  }

  public Image getImage()
  {
    Image result;
    Canvas canvas;

    WritableImage image;
    SnapshotParameters snapshotParameters;

    canvas = builder.build();
    snapshotParameters = new SnapshotParameters();
    snapshotParameters.setFill(Color.TRANSPARENT);
    image = canvas.snapshot(snapshotParameters, null);
    result = image;

    return result;
  }

  public static class IconBuilder
  {
    private final IconFont mi_iconFont;
    private final String mi_iconName;
    private final double mi_size;
    private final Color mi_fillColor;
    private final Color mi_strokeColor;
    private final HashMap<IconAlignment, FxIcon> mi_iconMap;

    public IconBuilder(String iconName)
    {
      this(IconFont.values()[0], iconName, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), null,
          new LinkedHashMap<IconAlignment, FxIcon>());
    }

    public IconBuilder(IconFont iconFont, String iconName, double size, Color fillColor, Color strokeColor,
        LinkedHashMap<IconAlignment, FxIcon> iconMap)
    {
      mi_iconFont = iconFont;
      mi_iconName = IconUtil.normalizeIconName(iconName);
      mi_size = size;
      mi_fillColor = fillColor;
      mi_iconMap = iconMap;
      mi_strokeColor = strokeColor;
    }

    public IconBuilder size(double size)
    {
      return new IconBuilder(mi_iconFont, mi_iconName, size, mi_fillColor, mi_strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public double getSize()
    {
      return mi_size;
    }

    public IconBuilder font(IconFont iconFont)
    {
      return new IconBuilder(iconFont, mi_iconName, mi_size, mi_fillColor, mi_strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder fillColor(Color color)
    {
      return new IconBuilder(mi_iconFont, mi_iconName, mi_size, color, mi_strokeColor, new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder strokeColor(Color strokeColor)
    {
      return new IconBuilder(mi_iconFont, mi_iconName, mi_size, mi_fillColor, strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder add(IconAlignment alignment, FxIcon fxIcon)
    {
      LinkedHashMap<IconAlignment, FxIcon> iconMap;
      iconMap = new LinkedHashMap<>(mi_iconMap);
      iconMap.put(alignment, fxIcon);
      return new IconBuilder(mi_iconFont, mi_iconName, mi_size, mi_fillColor, mi_strokeColor, iconMap);
    }

    public Canvas build()
    {
      Canvas canvas;

      canvas = new Canvas(mi_size, mi_size);
      if (m_debug)
      {
        drawGrid(canvas);
      }

      return build(IconAlignment.CENTER_CENTER, this, canvas);
    }

    private void drawGrid(Canvas canvas)
    {
      int blockSize;

      blockSize = 4;

      for (int x = 0; x < mi_size / blockSize; x++)
      {
        for (int y = 0; y < mi_size / blockSize; y++)
        {
          if ((y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1))
          {
            canvas.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
            canvas.getGraphicsContext2D().fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
          }
        }
      }
    }

    private static Canvas build(IconAlignment iconAlignment, IconBuilder builder, Canvas canvas)
    {
      String text;
      GraphicsContext gc;
      Font font;
      double x, y;

      gc = canvas.getGraphicsContext2D();
      font = builder.mi_iconFont.getIconFont(builder.mi_size);
      System.out.println("fontsize=" + font.getSize());
      text = builder.mi_iconFont.getCodepoint(builder.mi_iconName);

      x = canvas.getWidth() / 2;
      y = canvas.getHeight() / 2;

      gc.setFont(font);
      gc.setFill(builder.mi_fillColor);
      gc.setFontSmoothingType(FontSmoothingType.GRAY);
      gc.setTextAlign(iconAlignment.getTextAlign());
      gc.setTextBaseline(iconAlignment.getTextBaseline());
      if (iconAlignment == IconAlignment.UPPER_LEFT)
      {
        x = 0;
        y = 0;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
      }
      if (iconAlignment == IconAlignment.UPPER_RIGHT)
      {
        x = canvas.getWidth();
        y = 0;
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.TOP);
      }
      if (iconAlignment == IconAlignment.UPPER_CENTER)
      {
        x = canvas.getWidth() / 2.0;
        y = 0;
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
      }
      if (iconAlignment == IconAlignment.LOWER_RIGHT)
      {
        x = canvas.getWidth();
        y = canvas.getHeight();
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.BOTTOM);
      }
      if (iconAlignment == IconAlignment.LOWER_LEFT)
      {
        x = 0;
        y = canvas.getHeight();
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BOTTOM);
      }
      if (iconAlignment == IconAlignment.LOWER_CENTER)
      {
        x = canvas.getWidth() / 2.0;
        y = canvas.getWidth();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.BOTTOM);
      }
      if (iconAlignment == IconAlignment.CENTER_RIGHT)
      {
        x = canvas.getWidth();
        y = canvas.getHeight() / 2.0;
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTextBaseline(VPos.CENTER);
      }
      if (iconAlignment == IconAlignment.CENTER_LEFT)
      {
        x = 0;
        y = canvas.getHeight() / 2.0;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
      }
      if (iconAlignment == IconAlignment.CENTER_CENTER)
      {
        x = canvas.getWidth() / 2.0;
        y = canvas.getWidth() / 2.0;
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
      }

      gc.fillText(text, x, y);
      if (builder.mi_strokeColor != null)
      {
        gc.setStroke(builder.mi_strokeColor);
        gc.strokeText(text, x, y);
        gc.setLineWidth(1.0);
      }

      builder.mi_iconMap.entrySet().forEach(entry -> {
        build(entry.getKey(), entry.getValue().builder, canvas);
      });

      return canvas;
    }

    static class Icon
    {
      private String iconName;

      public Icon(String iconName)
      {
        this.iconName = IconUtil.normalizeIconName(iconName);
      }

      public String getIconName()
      {
        return iconName;
      }
    }
  }
}
