package fr.Tarzan;

import cn.nukkit.plugin.PluginBase;
import fr.Tarzan.event.*;

public class Main extends PluginBase {
    //tous les Static esentiel XD
    private static Main main;

    @Override
    public void onEnable() {
        main = this; //tous les event existant
        getLogger().alert("Active swim plugin");
        getServer().getPluginManager().registerEvents(new CoreEvent(), this);
    }
    @Override
    public void onDisable() {
        getLogger().alert("Deactivate swimm plugin");
    }

    public static Main getInstance(){
        return main;
    }
}
