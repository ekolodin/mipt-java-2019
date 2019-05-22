package ru.edhunter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginManager {

  private final String pluginRootDirectory;

  public PluginManager(String pluginRootDirectory) {
    this.pluginRootDirectory = pluginRootDirectory;
  }

  public Plugin load(String pluginName, String pluginClassName)
      throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

    File file = new File(pluginRootDirectory + "/" + pluginName + "/");
    URL[] urls = new URL[]{file.toURI().toURL()};
    URLClassLoader loader = new URLClassLoader(urls);
    Plugin plugin = (Plugin) loader.loadClass(pluginClassName).newInstance();

    return plugin;
  }
}