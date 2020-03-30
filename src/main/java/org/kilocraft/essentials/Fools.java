package org.kilocraft.essentials;

import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.chat.KiloChat;

import java.util.concurrent.ThreadLocalRandom;


public class Fools {

    public static final String[] SMART_SUGGESTIONS = new String[]{
            "owo", "minecraft:hugs", "DredHX"
    };

    public static void makeThemUnderstand(ServerPlayerEntity fool, Text what) {
        TitleS2CPacket poopoo = new TitleS2CPacket(TitleS2CPacket.Action.TITLE, what);
        fool.networkHandler.sendPacket(poopoo);
    }

    public static void yourWrong(ServerPlayerEntity fool, String why) {
        KiloChat.sendMessageTo(fool, TextFormat.translateToLiteralText('&', why));
    }

    public static boolean shouldDo() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static int randomNumber(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }
}
