package gui;

import java.io.File;
import java.io.FileFilter;

public class ApkFileFilter implements FileFilter
{
	private final String[] okFileExtensions = 
		new String[] {"apk", "APK", "Apk"};

  public boolean accept(File file)
  {
    for (String extension : okFileExtensions)
    {
      if (file.getName().toLowerCase().endsWith(extension) && file.isFile())
      {
        return true;
      }
    }
    return false;
  }
}
