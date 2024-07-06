package org.kku.fonticons.sample;

import java.io.IOException;
import org.kku.fonticons.ui.FxIcon;
import org.kku.fonticons.ui.FxIcon.IconAlignment;
import org.kku.fonticons.ui.FxIcon.IconColor;
import org.kku.fonticons.ui.FxIcon.IconColorModifier;
import org.kku.fonticons.ui.FxIcon.IconSize;
import org.kku.fonticons.ui.IconFont;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    orgIcon = IconFont.MATERIAL_DESIGN.getIcon("square-outline");
    orgIcon = IconFont.MATERIAL_DESIGN.getIcon("account");
    labelIcon = IconFont.MATERIAL_DESIGN.getIcon("square-outline").fillColor(Color.YELLOW).strokeColor(Color.BLACK);
    labelIcon = IconFont.MATERIAL_DESIGN.getIcon("close").fillColor(Color.RED);
    labelIcon = labelIcon.fillColor(new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 1));

    row = 0;
    column = 0;

    for (IconSize iconSize : IconSize.values())
    {
      row++;
      column = 0;

      icon = orgIcon.size(iconSize);
      root.add(icon.getIconLabel(), column, row);

      IconColor iconColor = IconColor.YELLOW;

      icon2 = icon.fillColor(iconColor).strokeColor(Color.BLACK);
      icon2 = icon2.add(IconAlignment.UPPER_LEFT, labelIcon);
      icon2 = icon2.add(IconAlignment.UPPER_RIGHT, labelIcon);
      icon2 = icon2.add(IconAlignment.UPPER_CENTER, labelIcon);
      icon2 = icon2.add(IconAlignment.CENTER_RIGHT, labelIcon);
      icon2 = icon2.add(IconAlignment.CENTER_CENTER, labelIcon);
      icon2 = icon2.add(IconAlignment.CENTER_LEFT, labelIcon);
      icon2 = icon2.add(IconAlignment.LOWER_RIGHT, labelIcon);
      icon2 = icon2.add(IconAlignment.LOWER_CENTER, labelIcon);
      icon2 = icon2.add(IconAlignment.LOWER_LEFT, labelIcon);
      root.add(icon2.getIconLabel(), ++column, row);
      root.add(new Button("Press me", new FxIcon("account").fillColor(Color.YELLOW)
          .strokeColor(IconColorModifier.DARKER).size(iconSize.LARGE).getIconLabel()), ++column, row);
    }

    Label label;
    FxIcon icon10;
    double size;

    size = 100.0;

    icon10 = new FxIcon("account").size(size).fillColor(Color.YELLOW).strokeColor(IconColorModifier.DARKER);
    icon10 = icon10.add(IconAlignment.UPPER_RIGHT, labelIcon);
    icon10 = icon10.add(IconAlignment.LOWER_RIGHT, labelIcon);
    icon10 = icon10.add(IconAlignment.UPPER_LEFT, labelIcon);
    icon10 = icon10.add(IconAlignment.LOWER_LEFT, labelIcon);
    icon10 = icon10.add(IconAlignment.CENTER_CENTER, labelIcon);
    icon10 = icon10.add(IconAlignment.CENTER_LEFT, labelIcon);
    icon10 = icon10.add(IconAlignment.CENTER_RIGHT, labelIcon);
    icon10 = icon10.add(IconAlignment.UPPER_CENTER, labelIcon);
    icon10 = icon10.add(IconAlignment.LOWER_CENTER, labelIcon);
    root.add(icon10.getIconLabel(), 0, ++row);

    labelIcon = labelIcon.add(IconAlignment.UPPER_RIGHT, IconFont.MATERIAL_DESIGN.getIcon("square-outline"));
    icon10 = new FxIcon("account").size(2 * size).fillColor(Color.YELLOW).strokeColor(IconColorModifier.DARKER);
    icon10 = icon10.add(IconAlignment.UPPER_RIGHT, labelIcon);
    root.add(icon10.getIconLabel(), 0, ++row);
    root.add(labelIcon.getIconLabel(), 0, ++row);

    // Setting the Scene object
    Scene scene = new Scene(new ScrollPane(root), 800, 500);
    stage.setTitle("Displaying Image");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String args[])
  {
    launch(args);
  }
}