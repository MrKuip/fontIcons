package org.kku.fonticons.ui;

import java.util.function.Supplier;
import org.kku.fonticons.ui.IconFonts.FontMetrics;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

public class FxIcon
{
  public static FxIcon WEATHER_MOONSET_UP = new FxIcon(() -> new IconBuilder("WEATHER_MOONSET_UP"));
  public static FxIcon NEW = new FxIcon(() -> new IconBuilder("F0224").fill("F0214"));
  public static FxIcon DELETE = new FxIcon(() -> new IconBuilder("F09E7").fill("F01B4"));
  public static FxIcon SAVE = new FxIcon(() -> new IconBuilder("F0818").fill("F0193"));
  public static FxIcon SAVE_AS = new FxIcon(() -> new IconBuilder("F0E28").fill("F0E27"));
  public static FxIcon RELOAD = new FxIcon(() -> new IconBuilder("F0453"));
  public static FxIcon UNDO = new FxIcon(() -> new IconBuilder("F054C"));
  public static FxIcon REDO = new FxIcon(() -> new IconBuilder("F044E"));
  public static FxIcon HELP = new FxIcon(() -> new IconBuilder("F02D7").fill("F0625"));
  public static FxIcon SETTINGS = new FxIcon(() -> new IconBuilder("F1064"));
  public static FxIcon REFRESH = new FxIcon(() -> new IconBuilder("F0450"));

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

  private final Supplier<IconBuilder> builder;

  private FxIcon(Supplier<IconBuilder> builder)
  {
    this.builder = builder;
  }

  public Image getSmallImage()
  {
    return getImage(IconSize.SMALL);
  }

  public Image getLargeImage()
  {
    return getImage(IconSize.LARGE);
  }

  public Image getVeryLargeImage()
  {
    return getImage(IconSize.VERY_LARGE);
  }

  public Image getImage(IconSize size)
  {
    Image result;
    Canvas canvas;

    WritableImage image;
    SnapshotParameters snapshotParameters;

    canvas = builder.get().size(size).build();
    snapshotParameters = new SnapshotParameters();
    snapshotParameters.setFill(Color.TRANSPARENT);
    image = canvas.snapshot(snapshotParameters, null);
    result = image;

    return result;
  }

  public static class IconBuilder
  {
    private Icon mi_icon;
    private IconSize mi_size;

    IconBuilder(String codepoint)
    {
      mi_icon = new Icon(codepoint);
      mi_icon.color = IconColor.DEFAULT_OUTLINE;
    }

    public IconBuilder size(IconSize size)
    {
      mi_size = size;
      return this;
    }

    public IconBuilder fill(String codepoint)
    {
      mi_icon.fill = new Icon(codepoint);
      mi_icon.fill.color = IconColor.DEFAULT_FILL;
      return this;
    }

    public IconBuilder color(IconColor color)
    {
      getCurrentIcon().setColor(color);
      return this;
    }

    private Icon getCurrentIcon()
    {
      return mi_icon.fill != null ? mi_icon.fill : mi_icon;
    }

    public Canvas build()
    {
      return build(new Canvas(mi_size.getSize(), mi_size.getSize()), 0.0f, 0.0f);
    }

    private Canvas build(Canvas canvas, double x, double y)
    {
      String text;
      GraphicsContext gc;
      Font font;
      FontMetrics fontMetrics;
      Icon icon;
      double correction;

      // Correction because we do not have access to real FontMetrics
      correction = 2.0;

      gc = canvas.getGraphicsContext2D();

      font = IconFonts.MATERIAL_DESIGN.getIconFont(mi_size.getSize());
      fontMetrics = IconFonts.MATERIAL_DESIGN.getFontMetrics(font);

      if (mi_icon.fill != null)
      {
        icon = mi_icon.fill;
        text = IconFonts.MATERIAL_DESIGN.getCodepoint(icon.getText());

        x = (canvas.getWidth() - fontMetrics.computeStringWidth(text)) / 2.0;
        y = (canvas.getHeight() + fontMetrics.getAscent()) / 2 - correction;

        gc.setFont(font);
        gc.setFill(icon.getColor());
        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        gc.fillText(text, x, y);
      }

      icon = mi_icon;
      text = IconFonts.MATERIAL_DESIGN.getCodepoint(icon.getText());

      x = (canvas.getWidth() - fontMetrics.computeStringWidth(text)) / 2.0;
      y = (canvas.getHeight() + fontMetrics.getAscent()) / 2 - correction;

      gc.setFont(font);
      gc.setStroke(icon.getColor());
      gc.setFill(icon.getColor());
      gc.setFontSmoothingType(FontSmoothingType.GRAY);
      gc.setLineWidth(1.2);
      gc.setLineJoin(StrokeLineJoin.MITER);
      gc.setMiterLimit(2.0);
      gc.fillText(text, x, y);

      return canvas;
    }

    static class Icon
    {
      private String text;
      private IconColor color = IconColor.DEFAULT_OUTLINE;
      private Icon fill;

      public Icon(String codepoint)
      {
        text = new String(Character.toChars(Integer.parseInt(codepoint, 16)));
      }

      public String getText()
      {
        return text;
      }

      public void setColor(IconColor color)
      {
        this.color = color;
      }

      public Color getColor()
      {
        return color.getColor();
      }
    }
  }
}
