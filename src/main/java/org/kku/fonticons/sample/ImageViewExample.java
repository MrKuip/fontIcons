package org.kku.fonticons.sample;

import java.io.IOException;
import org.kku.fonticons.ui.FxIcon;
import org.kku.fonticons.ui.FxIcon.IconSize;
import org.kku.fonticons.ui.IconFont;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageViewExample
  extends Application
{
  @Override
  public void start(Stage stage) throws IOException
  {
    ScrollPane scrollPane;
    FlowPane root;

    root = new FlowPane();

    IconFont.MATERIAL_DESIGN.getAllIconNameList().forEach(s -> {
      FxIcon icon;

      //icon = new FxIcon(s).size(IconSize.LARGE).fillColor(Color.ORANGE).strokeColor(IconColorModifier.DARKER);
      icon = new FxIcon(s).size(IconSize.LARGE).fillColor(Color.ORANGE);
      root.getChildren().add(new Button(icon.getId(), icon.getImageView()));
    });
    scrollPane = new ScrollPane(root);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);

    // Setting the Scene object
    root.setPrefWidth(800);
    root.setPrefHeight(400);
    Scene scene = new Scene(new ScrollPane(root), 800, 400);
    stage.setTitle("Displaying Image");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String args[])
  {
    launch(args);
  }
}