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
    FxIcon icon;
    FxIcon icon2;
    int row;
    int column;

    root = new GridPane();
    root.setGridLinesVisible(true);
    orgIcon = IconFont.MATERIAL_DESIGN.getIcon("account");
    orgIcon = IconFont.MATERIAL_DESIGN.getIcon("square-outline");
    labelIcon = IconFont.MATERIAL_DESIGN.getIcon("close").fillColor(Color.YELLOW).strokeColor(Color.BLACK);
    labelIcon = IconFont.MATERIAL_DESIGN.getIcon("square-outline").fillColor(Color.YELLOW).strokeColor(Color.BLACK);

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
      root.add(icon.getImageView(), column, row);

      for (IconColor iconColor : IconColor.values())
      {
        column++;
        icon2 = icon.fillColor(iconColor).strokeColor(Color.BLACK);
        icon2 = icon2.add(IconAlignment.UPPER_LEFT, labelIcon);
        icon2 = icon2.add(IconAlignment.UPPER_RIGHT, labelIcon);
        icon2 = icon2.add(IconAlignment.UPPER_CENTER, labelIcon);
        icon2 = icon2.add(IconAlignment.CENTER_RIGHT, labelIcon);
        icon2 = icon2.add(IconAlignment.CENTER_CENTER, labelIcon, 3);
        icon2 = icon2.add(IconAlignment.CENTER_LEFT, labelIcon);
        icon2 = icon2.add(IconAlignment.LOWER_RIGHT, labelIcon);
        icon2 = icon2.add(IconAlignment.LOWER_CENTER, labelIcon);
        icon2 = icon2.add(IconAlignment.LOWER_LEFT, labelIcon);
        root.add(icon2.getImageView(), column, row);
        //column++;
        //root.add(labelIcon.getImageView(), column, row);
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