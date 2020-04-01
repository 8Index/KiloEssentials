package org.kilocraft.essentials.commands.april;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.ItemStackArgumentType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.kilocraft.essentials.Fools;
import org.kilocraft.essentials.api.chat.LangText;
import org.kilocraft.essentials.api.command.EssentialCommand;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.commands.CommandUtils;

public class GiveHugsCommand extends EssentialCommand {
    public GiveHugsCommand() {
        super("hug");
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        argumentBuilder.then(
                argument("player", EntityArgumentType.player())
                        .then(
                                argument("amount", IntegerArgumentType.integer(0, 6))
                                        .executes(ctx -> giveHugs(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), IntegerArgumentType.getInteger(ctx, "amount")))
                        )
                        .executes(ctx -> giveHugs(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "player"), 1))
        ).executes(this::execute);
    }

    private int execute(CommandContext<ServerCommandSource> ctx) {
        KiloChat.sendLangMessageTo(ctx.getSource(), "command.hug");
        return SINGLE_SUCCESS;
    }

    private int giveHugs(ServerCommandSource src, ServerPlayerEntity target, int howMany) throws CommandSyntaxException {
        ServerPlayerEntity fool = src.getPlayer();

//        if (CommandUtils.areTheSame(src, target)) {
//            Fools.yourWrong(fool, "&cYou tried buddy...");
//            return -0;
//        }

        int amountToYeet = Fools.randomNumber(1, 3);
        amountToYeet *= howMany;

        if (fool.experienceLevel < amountToYeet) {
            fool.playSound(SoundEvents.ENTITY_VILLAGER_NO, 3, 1);
            Fools.yourWrong(fool, "&cYou need at least " + amountToYeet + " experience levels to do that!");
            return SINGLE_FAILED;
        }

        Fools.yourWrong(fool, "&a&lSending the Hug!");

        fool.addExperienceLevels(-amountToYeet);
        Fools.makeThemUnderstand(fool, new LiteralText((howMany == 1 ? "Hug" : "Hugs") + " Sent! <3").formatted(Formatting.LIGHT_PURPLE));
        fool.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 3, 1);

        Fools.yourWrong(target,  "&b" + this.getOnlineUser(fool).getFormattedDisplayName() + "&r&d Has sent you " + (howMany == 1 ? "a Hug" : howMany + " Hugs"));
        Fools.yourWrong(target, "&b&oThey've used " + amountToYeet + " levels of their experience for that!");

        return IM_COOL;
    }

}
