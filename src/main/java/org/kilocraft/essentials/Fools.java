package org.kilocraft.essentials;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.chat.KiloChat;


public class Fools {

    public static void makeThemUnderstand(ServerPlayerEntity fool, Text what) {
        TitleS2CPacket poopoo = new TitleS2CPacket(TitleS2CPacket.Action.TITLE, what);
        fool.networkHandler.sendPacket(poopoo);
    }

    public static void yourWrong(ServerPlayerEntity fool, String why) {
        KiloChat.sendMessageTo(fool, TextFormat.translateToLiteralText('&', why));
    }
}
