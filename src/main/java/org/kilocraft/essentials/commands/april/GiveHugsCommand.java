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
import org.kilocraft.essentials.api.command.EssentialCommand;
import org.kilocraft.essentials.commands.CommandUtils;

public class GiveHugsCommand extends EssentialCommand {
    public GiveHugsCommand() {
        super("ke_give");
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        argumentBuilder.then(
                argument("targets", EntityArgumentType.players())
                        .then(
                                argument("hugType", StringArgumentType.word())
                                        .suggests((context, builder) -> CommandSource.suggestMatching(Lists.newArrayList("hug", "hugs", "virtual_hugs", "minecraft:hug", "minecraft:hugs", "minecraft:virtual_hugs"), builder))
                                        .executes(ctx -> giveHugs(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "targets"), StringArgumentType.getString(ctx, "hugType"), 1))
                                        .then(
                                                argument("amount", IntegerArgumentType.integer(0, 69))
                                                        .executes(ctx -> giveHugs(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "targets"), StringArgumentType.getString(ctx, "hugType"), IntegerArgumentType.getInteger(ctx, "amount")))
                                        )
                        )
                        .then(
                                argument("item", ItemStackArgumentType.itemStack())
                                .executes(this::no_U)
                        .then(
                                argument("amount", IntegerArgumentType.integer(0, 69))
                                        .executes(this::no_U)
                        )
                )
        );
    }

    private int no_U(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Fools.makeThemUnderstand(ctx.getSource().getPlayer(), new LiteralText("NO!").formatted(Formatting.RED));
        return IM_COOL;
    }

    private int giveHugs(ServerCommandSource src, ServerPlayerEntity target, String input, int howMany) throws CommandSyntaxException {
        ServerPlayerEntity fool = src.getPlayer();

        if (CommandUtils.areTheSame(src, target)) {
            Fools.yourWrong(fool, "&cYou tried buddy...");
            return -0;
        }

        String type = input.replace("minecraft:", "");
        int amountToYeet = 0;

        switch (type) {
            case "hug":
                amountToYeet = 2;
            case "hugs":
                amountToYeet = 5;
            case "virtual_hugs":
                amountToYeet = 1;
            default:
                Fools.yourWrong(fool, "&cno you don't");
        }

        Fools.yourWrong(fool, "&a&lSending the Hug!");

        amountToYeet *= howMany;
        if (fool.experienceLevel < amountToYeet) {
            fool.playSound(SoundEvents.ENTITY_VILLAGER_NO, 3, 1);
            Fools.yourWrong(fool, "&cYOU DO NOT HAVE ENOUGH EXPERIENCE! YOU NEED AT LEAST " + amountToYeet + " LEVELS");
            return SINGLE_FAILED;
        }

        fool.experienceLevel -= amountToYeet;
        Fools.makeThemUnderstand(fool, new LiteralText((howMany == 1 ? "Hug" : "Hugs") + " Sent! <3").formatted(Formatting.LIGHT_PURPLE));
        fool.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 3, 1);

        Fools.yourWrong(target,  "&b" + this.getOnlineUser(fool).getFormattedDisplayName() + "&r&d Has sent you " + (howMany == 1 ? "a Hug" : howMany + " Hugs"));
        Fools.yourWrong(target, "&b&oThey've used " + amountToYeet + " levels of their experience for that!");

        return IM_COOL;
    }

}
