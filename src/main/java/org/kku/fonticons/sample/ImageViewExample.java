package org.kku.fonticons.sample;

import java.io.IOException;
import org.kku.fonticons.ui.FxIcon;
import org.kku.fonticons.ui.FxIcon.IconSize;
import org.kku.fonticons.ui.IconFonts;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ImageViewExample
  extends Application
{
  @Override
  public void start(Stage stage) throws IOException
  {
    FlowPane root = new FlowPane();

    IconFonts.MATERIAL_DESIGN.getAllIconNameList().forEach(s -> {
      FxIcon icon;

      icon = new FxIcon(s);
      root.getChildren().add(new ImageView(icon.getImage(IconSize.LARGE)));
    });

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