module org.kku.materialdesignicons
{
  requires java.logging;
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires static com.ibm.icu;
  requires org.kordamp.ikonli.core;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.ikonli.materialdesign2;

  exports org.kku.fonticons.ui;
  exports org.kku.fonticons.sample;
}
