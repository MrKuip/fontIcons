package org.kku.fonticons.sample;

import java.io.IOException;
import org.kku.fonticons.ui.FxIcon;
import org.kku.fonticons.ui.FxIcon.IconAlignment;
import org.kku.fonticons.ui.FxIcon.IconColor;
import org.kku.fonticons.ui.FxIcon.IconSize;
import org.kku.fonticons.ui.IconFont;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageViewExample2
  extends Application
{
  @Override
  public void start(Stage stage) throws IOException
  {
    GridPane root;
    FxIcon orgIcon;
    FxIcon labelIcon;
    FxIcon outlineIcon;
    FxIcon icon;
    FxIcon icon2;
    int row;
    int column;

    root = new GridPane();
    root.setGridLinesVisible(true);
    orgIcon = IconFont.MATERIAL_DESIGN.getIcon("account");
    labelIcon = IconFont.MATERIAL_DESIGN.getIcon("close").size(IconSize.SMALLER).color(Color.RED);
    outlineIcon = IconFont.MATERIAL_DESIGN.getIcon("account-outline").color(Color.BLACK).size(IconSize.VERY_LARGE);

    row = 0;
    column = 0;

    for (IconSize iconSize : IconSize.values())
    {
      if (iconSize != IconSize.VERY_LARGE)
      {
        continue;
      }
      row++;
      column = 0;

      icon = orgIcon.size(iconSize);
      System.out.println("add(" + iconSize.name() + " -> " + column + ", " + row + ")");
      root.add(icon.getImageView(), column, row);

      for (IconColor iconColor : IconColor.values())
      {
        column++;
        icon2 = icon.color(iconColor);
        icon2 = icon2.add(IconAlignment.CENTER_CENTER, outlineIcon);
        icon2 = icon2.add(IconAlignment.UPPER_RIGHT, labelIcon);
        System.out.println("add(" + iconSize.name() + "." + iconColor.name() + " -> " + column + ", " + row + ")");
        root.add(icon2.getImageView(), column, row);
      }
    }

    // Setting the Scene object
    Scene scene = new Scene(root, 595, 370);
    stage.setTitle("Displaying Image");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String args[])
  {
    launch(args);
  }
}