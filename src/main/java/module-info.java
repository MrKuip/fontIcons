module org.kku.materialdesignicons
{
  requires java.logging;
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires static com.ibm.icu;

  exports org.kku.fonticons.ui;
  exports org.kku.fonticons.sample;
}
