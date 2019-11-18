package org.kilocraft.essentials.user;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.user.punishment.PunishmentManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author CODY_AI
 * An easy way to handle the User (Instance of player)
 *
 * @see ServerUser
 * @see UserHomeHandler
 */


public class UserManager {
    private static List<ServerUser> loadedServerUsers = new ArrayList<>();
    private List<ServerUser> offlineServerUsers = new ArrayList<>();
    private PlayerManager playerManager;
    private UserHandler handler = new UserHandler();
    private PunishmentManager banManager;

    public UserManager(PlayerManager manager) {
        this.playerManager = manager;
        this.banManager = new PunishmentManager(manager);
    }

    public static List<ServerUser> getUsers() {
        return loadedServerUsers;
    }

    public ServerUser getUser(UUID uuid) {
        ServerUser serverUser = null;
        for (ServerUser loadedServerUser : loadedServerUsers) {
            if (loadedServerUser.uuid.equals(uuid)) serverUser = loadedServerUser;
        }

        return serverUser;
    }

    ServerUser getUser(String name) {
        for (ServerUser loadedServerUser : loadedServerUsers) {
            if (loadedServerUser.getUsername().equals(name)) return loadedServerUser;
        }

        return null;
    }

    private ServerUser getOfflineUser(UUID uuid) {
        ServerUser serverUser = null;

        return serverUser;
    }
    
    Text getUserDisplayName(ServerUser serverUser) {
    	if (serverUser.getNickname().equals("")) {
    		return Objects.requireNonNull(KiloServer.getServer().getPlayerManager().getPlayer(serverUser.getUuid())).getDisplayName();
    	} else {
    		return new LiteralText(serverUser.getNickname());
    	}
    }

    ServerUser getUserByNickname(String nickName) {
        ServerUser requested = null;
        for (ServerUser serverUser : loadedServerUsers) {
            if (serverUser.getNickname().equals(nickName))
                requested = serverUser;
        }

        return requested;
    }

    public void triggerSave() throws IOException {
        for (ServerUser serverUser : loadedServerUsers) {
            this.handler.saveData(serverUser);
        }
    }

    public void onPlayerJoin(ServerPlayerEntity player) {
       ServerUser serverUser = ServerUser.of(player);
        serverUser.name = player.getGameProfile().getName();
        loadedServerUsers.add(serverUser);

        try {
            this.handler.handleUser(serverUser);
        } catch (IOException ignored) { }

        KiloChat.broadcastUserJoinEventMessage(player);
    }

    public void onPlayerLeave(ServerPlayerEntity player) {
       ServerUser serverUser = ServerUser.of(player);
        serverUser.name = player.getGameProfile().getName();
        loadedServerUsers.remove(serverUser);

        try {
            this.handler.saveData(serverUser);
        } catch (IOException ignored) { }

        KiloChat.broadcastUserLeaveEventMessage(player);
    }

    public PunishmentManager getPunishmentManager() {
        return this.banManager;
    }

}
