package com.norwood.mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import com.norwood.mcheli.MCH_Lib;
import com.norwood.mcheli.helper.MCH_Utils;
import com.norwood.mcheli.helper.network.HandleSide;
import com.norwood.mcheli.aircraft.MCH_EntityAircraft;
import lombok.Getter;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MCH_MultiplayPacketHandler {
    public static EntityPlayer modListRequestPlayer = null;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static byte[] imageData = null;
    private static String lastPlayerName = "";
    private static double lastDataPercent = 0.0;
    @Getter
    private static int playerInfoId = 0;

    public static void destoryAllAircraft(EntityPlayer player) {
        CommandSummon cmd = new CommandSummon();
        if (cmd.checkPermission(MCH_Utils.getServer(), player)) {
            for (Entity e :  new ArrayList<>(player.world.loadedEntityList)) {
                if (e instanceof MCH_EntityAircraft) {
                    e.setDead();
                }
            }
        }
    }

    @HandleSide({Side.SERVER})
    public static void onPacket_LargeData(EntityPlayer player, ByteArrayDataInput data, IThreadListener scheduler) {
        if (!player.world.isRemote) {
            MCH_PacketLargeData pc = new MCH_PacketLargeData();
            pc.readData(data);
            scheduler.addScheduledTask(
                    () -> {
                        try {
                            if (pc.imageDataIndex < 0 || pc.imageDataTotalSize <= 0) {
                                return;
                            }

                            if (pc.imageDataIndex == 0) {
                                if (imageData != null && !lastPlayerName.isEmpty()) {
                                    LogError("[mcheli]Err1:Saving the %s screen shot to server FAILED!!!", lastPlayerName);
                                }

                                imageData = new byte[pc.imageDataTotalSize];
                                lastPlayerName = player.getDisplayName().getFormattedText();
                                lastDataPercent = 0.0;
                            }

                            double dataPercent = (double) (pc.imageDataIndex + pc.imageDataSize) / pc.imageDataTotalSize * 100.0;
                            if (dataPercent - lastDataPercent >= 10.0 || lastDataPercent == 0.0) {
                                LogInfo(
                                        "[mcheli]Saving the %s screen shot to server. %.0f%% : %dbyte / %dbyte",
                                        player.getDisplayName(),
                                        dataPercent,
                                        pc.imageDataIndex,
                                        pc.imageDataTotalSize
                                );
                                lastDataPercent = dataPercent;
                            }

                            if (imageData == null) {

                                imageData = null;
                                lastPlayerName = "";
                                lastDataPercent = 0.0;
                                return;
                            }

                            if (pc.imageDataSize >= 0)
                                System.arraycopy(pc.buf, 0, imageData, pc.imageDataIndex, pc.imageDataSize);

                            if (pc.imageDataIndex + pc.imageDataSize >= pc.imageDataTotalSize) {
                                DataOutputStream dos;
                                String dt = dateFormat.format(new Date());
                                File file = new File("screenshots_op");
                                file.mkdir();
                                file = new File(file, player.getDisplayName() + "_" + dt + ".png");
                                String s = file.getAbsolutePath();
                                LogInfo("[mcheli]Save Screenshot has been completed: %s", s);
                                FileOutputStream fos = new FileOutputStream(s);
                                dos = new DataOutputStream(fos);
                                dos.write(imageData);
                                dos.flush();
                                dos.close();
                                imageData = null;
                                lastPlayerName = "";
                                lastDataPercent = 0.0;
                            }
                        } catch (Exception var9) {
                            var9.printStackTrace();
                        }
                    }
            );
        }
    }

    public static void LogInfo(String format, Object... args) {
        MCH_Lib.Log(String.format(format, args));
    }

    public static void LogError(String format, Object... args) {
        MCH_Lib.Log(String.format(format, args));
    }

    public static int getPlayerInfoId(EntityPlayer player) {
        modListRequestPlayer = player;
        playerInfoId++;
        if (playerInfoId > 1000000) {
            playerInfoId = 1;
        }

        return playerInfoId;
    }

}
