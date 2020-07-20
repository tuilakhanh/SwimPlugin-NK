package fr.Tarzan.event;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import fr.Tarzan.Main;

public class CoreEvent implements Listener {


    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        DataPacket pk = event.getPacket();
        Player player = event.getPlayer();
        if (pk instanceof PlayerActionPacket) {
            PlayerActionPacket pks = (PlayerActionPacket) event.getPacket();
            switch (pks.action) {

                case PlayerActionPacket.ACTION_START_SWIMMING:
                     SwimEvent ev = new SwimEvent(player, true);
                    Main.getInstance().getServer().getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        player.sendData(player);
                    } else {
                        setSwimming(player,true);
                    }

                    break;

                case PlayerActionPacket.ACTION_STOP_SWIMMING:

                    ev = new SwimEvent(player, false);
                    Main.getInstance().getServer().getPluginManager().callEvent(ev);
                    if (ev.isCancelled()) {
                        player.sendData(player);
                    } else {
                        setSwimming(player,false);
                    }

                    break;

            }

        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isSwimming()) {

            if (player.isSprinting()) {
                setSwimming(player,true);
            } else {

                setSwimming(player, false);
            }

        }

    }
    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        Entity player = event.getEntity();
        EntityDamageEvent.DamageCause cause = event.getCause();

        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {

            if (player instanceof Player) {
                  Player p = (Player) player;
                if (isSwimming(p)) {

                    event.setCancelled();

                }

            }

        }

    }

    public static boolean isSwimming(Player player){
        return player.getDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_SWIMMING);

    }

    public static void setSwimming(Player player, boolean value) {
        if (value) {
            player.setDataProperty(new FloatEntityData(Entity.DATA_BOUNDING_BOX_HEIGHT, player.getWidth()));
            recalculateBoundingBox(player);
        }else {
            player.setDataProperty(new FloatEntityData(Entity.DATA_BOUNDING_BOX_HEIGHT, (float) (1.8 * player.getScale())));
            recalculateBoundingBox(player);
        }
        player.setDataFlag(Player.DATA_FLAGS, Player.DATA_FLAG_SWIMMING, value);
    }
    public static void recalculateBoundingBox(Player player)
    {
        float halfWidth = player.getWidth() / 2;
        player.boundingBox.setBounds(
                player.x - halfWidth,
                player.y,
                player.z - halfWidth,
                player.x + halfWidth,
                player.y + player.getHeight(),
                player.z + halfWidth
        );

    }

}
