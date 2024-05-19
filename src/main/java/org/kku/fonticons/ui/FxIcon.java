package org.kku.fonticons.ui;

import java.util.EnumMap;
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
  
  public enum IconAlignment
  {
    UPPER_RIGHT,
    UPPER_CENTER,
    UPPER_LEFT,
    CENTER_RIGHT,
    CENTER_CENTER,
    CENTER_LEFT,
    LOWER_RIGHT,
    LOWER_CENTER,
    LOWER_LEFT;

    IconAlignment()
    {
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
  
  public FxIcon size(int size)
  {
    return new FxIcon(builder.size(size));
  }
  
  public FxIcon color(IconColor color)
  {
    return color(color.getColor());
  }
  
  public FxIcon color(Color color)
  {
    return new FxIcon(builder.color(color));
  }
  
  public FxIcon add(IconAlignment alignment, FxIcon fxIcon)
  {
    return new FxIcon(builder.add(alignment, fxIcon));
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
    private final int mi_size;
    private final Color mi_color;
    private final EnumMap<IconAlignment, FxIcon> mi_iconMap;

    public IconBuilder(String iconName)
    {
      this(IconFont.values()[0], iconName, IconSize.SMALL.getSize(), IconColor.DEFAULT_OUTLINE.getColor(), new EnumMap<IconAlignment, FxIcon>(IconAlignment.class));
    }
    
    public IconBuilder(IconFont iconFont, String iconName, int size, Color color, EnumMap<IconAlignment, FxIcon> iconMap)
    {
      mi_iconFont = iconFont;
      mi_iconName = IconUtil.normalizeIconName(iconName);
      mi_size = size;
      mi_color = color;
      mi_iconMap = iconMap;
    }
    
    public IconBuilder size(int size)
    {
      return new IconBuilder(mi_iconFont, mi_iconName, size, mi_color, new EnumMap<>(mi_iconMap));
    }

    public IconBuilder font(IconFont iconFont)
    {
      return new IconBuilder(iconFont, mi_iconName, mi_size, mi_color, new EnumMap<>(mi_iconMap));
    }

    public IconBuilder color(Color color)
    {
      return new IconBuilder(mi_iconFont, mi_iconName, mi_size, color, new EnumMap<>(mi_iconMap));
    }
    
    public IconBuilder add(IconAlignment alignment, FxIcon fxIcon)
    {
      EnumMap<IconAlignment, FxIcon> iconMap;
      iconMap = new EnumMap<>(mi_iconMap);
      iconMap.put(alignment, fxIcon);
      return new IconBuilder(mi_iconFont, mi_iconName, mi_size, mi_color, iconMap);
    }

    public Canvas build()
    {
      return build(new Canvas(mi_size, mi_size), 0.0f, 0.0f);
    }

    private Canvas build(Canvas canvas, double x, double y)
    {
      String text;
      GraphicsContext gc;
      Font font;

      gc = canvas.getGraphicsContext2D();
      font = mi_iconFont.getIconFont(mi_size);
      text = mi_iconFont.getCodepoint(mi_iconName);

      x = canvas.getWidth() / 2;
      y = canvas.getHeight() / 2;

      gc.setFont(font);
      gc.setStroke(mi_color);
      gc.setFill(mi_color);
      gc.setFontSmoothingType(FontSmoothingType.GRAY);
      gc.setTextAlign(TextAlignment.CENTER);
      gc.setTextBaseline(VPos.CENTER);
      gc.fillText(text, x, y);

      mi_iconMap.entrySet().forEach(entry  -> {
        IconBuilder b1;
        String t1;
        double x1, y1;
        int size1;
        Font f1;

        b1 = entry.getValue().builder;
        size1 = b1.mi_size;
        f1 = mi_iconFont.getIconFont(size1);
        t1 = mi_iconFont.getCodepoint(b1.mi_iconName);
        x1 = canvas.getWidth() / 2;
        y1 = canvas.getHeight() / 2;
        
        gc.setFont(f1);
        gc.setFill(b1.mi_color);
        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        switch(entry.getKey())
        {
          case CENTER_CENTER:
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            break;
          case CENTER_LEFT:
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.CENTER);
            break;
          case CENTER_RIGHT:
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.CENTER);
            break;
          case LOWER_CENTER:
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.TOP);
            break;
          case LOWER_LEFT:
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.TOP);
            break;
          case LOWER_RIGHT:
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.TOP);
            break;
          case UPPER_CENTER:
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.BOTTOM);
            break;
          case UPPER_LEFT:
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.BOTTOM);
            break;
          case UPPER_RIGHT:
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setTextBaseline(VPos.BOTTOM);
            break;
          default:
            break;
        }

        gc.fillText(t1, x1, y1);
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
