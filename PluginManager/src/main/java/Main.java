import ru.edhunter.Plugin;
import ru.edhunter.PluginManager;

public class Main {

  public static void main(String[] args) throws Exception {

    PluginManager pluginManager = new PluginManager("target");
    Plugin plugin = pluginManager.load("test-classes", "MyPlugin");
    plugin.doUseful();

  }
}
