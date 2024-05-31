package org.kku.fonticons.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Function;
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

public class FxFontIcon
{
  static public boolean m_debug = false;

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
    UPPER_RIGHT(3, TextAlignment.RIGHT, VPos.TOP, c -> c.getWidth(), c -> 0.0),
    UPPER_CENTER(3, TextAlignment.CENTER, VPos.TOP, c -> c.getWidth() / 2.0, c -> 0.0),
    UPPER_LEFT(3, TextAlignment.LEFT, VPos.TOP, c -> 0.0, c -> 0.0),
    CENTER_RIGHT(3, TextAlignment.RIGHT, VPos.CENTER, c -> c.getWidth(), c -> c.getHeight() / 2.0),
    CENTER_CENTER(1, TextAlignment.CENTER, VPos.CENTER, c -> ((int) (c.getWidth() / 2.0)) + 0.5, c -> ((int) (c.getWidth() / 2.0)) + 0.5),
    CENTER_LEFT(3, TextAlignment.LEFT, VPos.CENTER, c -> 0.0, c -> c.getHeight() / 2.0),
    LOWER_RIGHT(3, TextAlignment.RIGHT, VPos.BOTTOM, c -> c.getWidth(), c -> c.getHeight()),
    LOWER_CENTER(3, TextAlignment.CENTER, VPos.BOTTOM, c -> c.getWidth() / 2.0, c -> c.getWidth()),
    LOWER_LEFT(3, TextAlignment.LEFT, VPos.BOTTOM, c -> 0.0, c -> c.getHeight());

    private final double mi_defaultSizeFactor;
    private final Function<Canvas, Double> mi_x;
    private final Function<Canvas, Double> mi_y;
    private final TextAlignment mi_textAlign;
    private final VPos mi_textBaseline;

    IconAlignment(double sizeFactor, TextAlignment textAlign, VPos textBaseline, Function<Canvas, Double> x,
        Function<Canvas, Double> y)
    {
      mi_defaultSizeFactor = sizeFactor;
      mi_textAlign = textAlign;
      mi_textBaseline = textBaseline;
      mi_x = x;
      mi_y = y;
    }

    public double getX(Canvas c)
    {
      return mi_x.apply(c);
    }

    public double getY(Canvas c)
    {
      return mi_y.apply(c);
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

  public FxFontIcon(String iconId)
  {
    this(new IconBuilder(iconId));
  }

  public FxFontIcon(IconBuilder builder)
  {
    this.builder = builder;
  }

  public String getId()
  {
    return builder.getId();
  }

  public FxFontIcon font(IconFont font)
  {
    return new FxFontIcon(builder.font(font));
  }

  public FxFontIcon size(IconSize size)
  {
    return size(size.getSize());
  }

  public FxFontIcon size(double size)
  {
    return new FxFontIcon(builder.size(size));
  }

  public double getSize()
  {
    return builder.getSize();
  }

  public FxFontIcon fillColor(IconColor color)
  {
    return fillColor(color.getColor());
  }

  public FxFontIcon fillColor(Color color)
  {
    return new FxFontIcon(builder.fillColor(color));
  }

  public FxFontIcon strokeColor(IconColor strokeColor)
  {
    return strokeColor(strokeColor.getColor());
  }

  public FxFontIcon strokeColor(Color strokeColor)
  {
    return new FxFontIcon(builder.strokeColor(strokeColor));
  }

  public FxFontIcon strokeColor(IconColorModifier iconColorModifier)
  {
    return new FxFontIcon(builder.strokeColor(iconColorModifier));
  }

  public FxFontIcon add(IconAlignment alignment, FxFontIcon fxIcon)
  {
    return add(alignment, fxIcon, alignment.getDefaultSizeFactor());
  }

  public FxFontIcon add(IconAlignment alignment, FxFontIcon fxIcon, double sizeFactor)
  {
    return new FxFontIcon(builder.add(alignment, fxIcon.size(getSize() / sizeFactor)));
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
  
  public Canvas getCanvas()
  {
    return builder.build();
  }

  public static class IconBuilder
  {
    private final IconFont mi_iconFont;
    private final String mi_iconId;
    private final double mi_size;
    private final Color mi_fillColor;
    private final Color mi_strokeColor;
    private final HashMap<IconAlignment, FxFontIcon> mi_iconMap;

    public IconBuilder(String iconId)
    {
      this(IconFont.values()[0], iconId, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), null,
          new LinkedHashMap<IconAlignment, FxFontIcon>());
    }

    public IconBuilder(IconFont iconFont, String iconId, double size, Color fillColor, Color strokeColor,
        LinkedHashMap<IconAlignment, FxFontIcon> iconMap)
    {
      mi_iconFont = iconFont;
      mi_iconId = IconUtil.normalizeIconName(iconId);
      mi_size = size;
      mi_fillColor = fillColor;
      mi_iconMap = iconMap;
      mi_strokeColor = strokeColor;
    }

    public String getId()
    {
      return mi_iconId;
    }

    public IconBuilder size(double size)
    {
      return new IconBuilder(mi_iconFont, mi_iconId, size, mi_fillColor, mi_strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public double getSize()
    {
      return mi_size;
    }

    public IconBuilder font(IconFont iconFont)
    {
      return new IconBuilder(iconFont, mi_iconId, mi_size, mi_fillColor, mi_strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder fillColor(Color color)
    {
      return new IconBuilder(mi_iconFont, mi_iconId, mi_size, color, mi_strokeColor, new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder strokeColor(Color strokeColor)
    {
      return new IconBuilder(mi_iconFont, mi_iconId, mi_size, mi_fillColor, strokeColor,
          new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder strokeColor(IconColorModifier iconColorModifier)
    {
      return new IconBuilder(mi_iconFont, mi_iconId, mi_size, mi_fillColor, mi_fillColor.darker(),
          new LinkedHashMap<>(mi_iconMap));
    }

    public IconBuilder add(IconAlignment alignment, FxFontIcon fxIcon)
    {
      LinkedHashMap<IconAlignment, FxFontIcon> iconMap;
      iconMap = new LinkedHashMap<>(mi_iconMap);
      iconMap.put(alignment, fxIcon);
      return new IconBuilder(mi_iconFont, mi_iconId, mi_size, mi_fillColor, mi_strokeColor, iconMap);
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
      text = builder.mi_iconFont.getCodepoint(builder.mi_iconId);

      gc.setFont(font);
      gc.setFill(builder.mi_fillColor);
      gc.setFontSmoothingType(FontSmoothingType.LCD);
      gc.setTextAlign(iconAlignment.getTextAlign());
      gc.setTextBaseline(iconAlignment.getTextBaseline());

      x = snap(iconAlignment.getX(canvas));
      y = snap(iconAlignment.getY(canvas));
      System.out.println("x.y = " + x + ", " + y);
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
    
    private static double snap(double y) {
        return y;
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
