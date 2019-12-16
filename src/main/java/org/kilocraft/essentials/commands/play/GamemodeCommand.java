package org.kilocraft.essentials.commands.play;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.CommandPermission;
import org.kilocraft.essentials.KiloCommands;
import org.kilocraft.essentials.api.command.TabCompletions;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.commands.CommandHelper;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.arguments.EntityArgumentType.getPlayers;
import static net.minecraft.command.arguments.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static org.kilocraft.essentials.KiloCommands.*;

public class GamemodeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> gamemodeCommand = literal("ke_gamemode")
                .requires(src -> KiloCommands.hasPermission(src, CommandPermission.GAMEMODE));

        LiteralArgumentBuilder<ServerCommandSource> gmCommand = literal("gm")
                .requires(src -> KiloCommands.hasPermission(src, CommandPermission.GAMEMODE));

        build(gamemodeCommand);
        build(gmCommand);
        dispatcher.register(gamemodeCommand);
        dispatcher.register(gmCommand);
    }

    private static void build(LiteralArgumentBuilder<ServerCommandSource> argumentBuilder) {
        RequiredArgumentBuilder<ServerCommandSource, String> gameTypeArgument = argument("gameType", string())
                .suggests(GamemodeCommand::suggestGameModes)
                .executes(ctx -> execute(ctx, Collections.singletonList(ctx.getSource().getPlayer()), null,false));

        RequiredArgumentBuilder<ServerCommandSource, EntitySelector> targetArgument = argument("target", players())
                .suggests(TabCompletions::allPlayers)
                .executes(ctx -> execute(ctx, getPlayers(ctx, "target"), null,false))
                .then(literal("-silent")
                        .executes(ctx -> execute(ctx, getPlayers(ctx, "target"), null, true)));


        gameTypeArgument.then(targetArgument);
        argumentBuilder.then(gameTypeArgument);
    }

    private static int execute(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> players, @Nullable GameMode cValue, boolean silent) throws CommandSyntaxException {
        ServerCommandSource src = ctx.getSource();
        String arg = cValue == null ? getString(ctx, "gameType") : cValue.getName();
        GameMode selectedMode = getMode(arg);

        if (selectedMode == null)
            throw new SimpleCommandExceptionType(new LiteralText("Please select a valid Game type!")).create();

        if (players.size() == 1 && !hasPermission(src, getPermission("self", selectedMode), 2))
            throw new SimpleCommandExceptionType(getPermissionError(getPermission("self", selectedMode).getNode())).create();

        if (players.size() > 1 && !hasPermission(src, getPermission("others", selectedMode), 2))
            throw new SimpleCommandExceptionType(getPermissionError(getPermission("others", selectedMode).getNode())).create();

        for (ServerPlayerEntity player : players) {
            if (!silent && !CommandHelper.areTheSame(src, player))
                KiloChat.sendLangMessageTo(player, "template.#1.announce", src.getName(), "gamemode", selectedMode.getName());
            player.setGameMode(selectedMode);

        }

        KiloChat.sendLangMessageTo(src, "template.#1", "gamemode",
                selectedMode.getName(), (players.size() == 1) ? src.getName() : players.size() + " players");

        return SUCCESS();
    }

    private static GameMode getMode(String arg) {
        if  (arg.startsWith("sp") || arg.startsWith("3"))
            return GameMode.SPECTATOR;
        if  (arg.startsWith("s") || arg.startsWith("0"))
            return GameMode.SURVIVAL;
        if  (arg.startsWith("c") || arg.startsWith("1"))
            return GameMode.CREATIVE;
        if  (arg.startsWith("a") || arg.startsWith("2"))
            return GameMode.ADVENTURE;

        return null;
    }

    private static CommandPermission getPermission(String type, GameMode mode) {
        return CommandPermission.byName("gamemode." + type + mode.getName());
    }

    private static CompletableFuture<Suggestions> suggestGameModes(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        int select = new Random().nextInt((1) + 1);
        List<String> strings = new ArrayList<>();
        List<String> integers = new ArrayList<>();
        List<String> firstChar = new ArrayList<>();
        firstChar.add("sp");
        for (GameMode value : GameMode.values()) {
            if (value.equals(GameMode.NOT_SET)) continue;
            strings.add(value.getName());
            integers.add(String.valueOf(value.getId()));
            if (!value.equals(GameMode.SPECTATOR))
                firstChar.add(String.valueOf(value.getName().charAt(0)));
        }

        System.out.println(select);

        List<String> finalStrings = new ArrayList<>();
        switch (select) {
            case 0:
                finalStrings = integers;
            case 1:
                finalStrings = firstChar;
            default:
                finalStrings = strings;
        }

        return CommandSource.suggestMatching(finalStrings, builder);
    }

}
