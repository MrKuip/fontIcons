package org.kku.fonticons.generate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.kku.fonticons.ui.FxIcon;
import org.kku.fonticons.ui.IconFont;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.core.PostScriptTable;

public class GenerateTtfDictionaries
{
  private final File m_buildDir;

  public GenerateTtfDictionaries(File buildDir)
  {
    m_buildDir = buildDir;
  }

  public void generate()
  {
    Stream.of(IconFont.values()).forEach(iconFont -> {
      File dictonaryFile;

      dictonaryFile = new File(m_buildDir, iconFont.getTrueTypeFontResourceDictionaryName());
      try (InputStream is = iconFont.getTrueTypeFontAsStream())
      {
        Font font;
        CMapTable cmapTable;
        PostScriptTable postTable;
        int numberOfGlyphs;
        Map<Integer, String> nameByIndexMap;
        Map<Integer, Integer> unicodeByIndexMap;

        font = FontFactory.getInstance().loadFonts(is)[0];

        cmapTable = font.getTable(Tag.cmap);
        postTable = font.getTable(Tag.post);

        nameByIndexMap = new HashMap<>();
        numberOfGlyphs = postTable.numberOfGlyphs();
        for (int glyphNum = 0; glyphNum < numberOfGlyphs; glyphNum++)
        {
          nameByIndexMap.put(glyphNum, postTable.glyphName(glyphNum));
        }

        unicodeByIndexMap = new HashMap<>();
        cmapTable.forEach(c -> {
          c.readFontData();
          c.iterator().forEachRemaining(character -> {
            unicodeByIndexMap.put(c.glyphId(character), character);
          });
        });

        try (FileWriter fileWriter = new FileWriter(dictonaryFile))
        {
          nameByIndexMap.forEach((key, _) -> {
            String name;
            name = nameByIndexMap.get(key);
            if (!name.isEmpty())
            {
              try
              {
                fileWriter.write(FxIcon.normalizeIconName(name) + "="
                    + Integer.toHexString(Integer.valueOf(unicodeByIndexMap.get(key))).toUpperCase() + "\r\n");
              }
              catch (IOException ex)
              {
                ex.printStackTrace();
              }
            }
          });
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    });
  }

  public static void main(String[] args)
  {
    File buildDir;

    if (args.length <= 0)
    {
      System.out.println("Usage:");
      System.out.println("java " + GenerateTtfDictionaries.class.getName() + " <buildDir>");
      System.exit(-1);
    }

    buildDir = new File(args[0]);
    if (!buildDir.exists())
    {
      System.out.println("Error: " + buildDir.getPath() + " doesn't exist");
      System.exit(-1);
    }

    new GenerateTtfDictionaries(buildDir).generate();
  }
}
