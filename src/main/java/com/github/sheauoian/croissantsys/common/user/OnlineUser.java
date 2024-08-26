package com.github.sheauoian.croissantsys.common.user;

import com.github.sheauoian.croissantsys.common.menu.MainMenu;
import com.github.sheauoian.croissantsys.util.Damage;
import com.github.sheauoian.croissantsys.util.SidebarTitle;
import com.github.sheauoian.croissantsys.util.UserLevelUp;
import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;


// UserInfoとOnlineなユーザーを紐付けるClass
public class OnlineUser {
    public final UserInfo info;
    public final Player player;

    public final MainMenu menu;
    public final FastBoard board;

    public OnlineUser(UserInfo info, Player player) {
        this.info = info;
        this.player = player;

        this.menu = new MainMenu(this);
        this.board = new FastBoard(player);
        board.updateTitle(SidebarTitle.getSidebarTitle());
    }
    public void save() {
        UserInfoDao.getInstance().update(this.info);
        menu.save();
    }

    public void sideBar() {
        board.updateLine(1, Component.text(String.format("レベル: %d (残り %.2f xp)",
                info.level,
                info.required_xp - info.xp
        )));
    }
    public void actionBar() {
        player.sendActionBar(Component.text(info.health + " / " + info.getStatus(StatusType.MAX_HP)));
    }
    public void updateHealthBar() {
        player.setHealth(Math.min(20, Math.max(0, 20 * info.health/info.getStatus(StatusType.MAX_HP))));
    }

    // Reset Health
    public void resetHealth() {
        this.info.health = info.getStatus(StatusType.MAX_HP);
        updateHealthBar();
    }
    public void getXp(float added_xp) {
        int current_level = info.level;
        info.xp += added_xp;
        while (info.xp >= info.required_xp) {
            info.xp -= info.required_xp;
            info.level += 1;
            info.required_xp = UserLevelUp.getRequiredXp(info.level);
        }
        if (current_level < info.level) {
            player.getWorld().playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    SoundCategory.MASTER,
                    1.7f,
                    1.7f);
            player.sendMessage(String.format(
                            "Level Up: %s -> %s",
                            current_level,
                            info.level
                    )
            );
        }
    }
    public boolean damage(Damage d) {
        info.health = Math.max(info.health - d.damage, 0);
        if (info.health <= 0d) {
            player.sendMessage("お前は死んでしまった");
            return false;
        };
        updateHealthBar();
        return true;
    }

    public void message(String msg) {
        Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
        player.sendMessage(comp);
    }

    public void update() {
        if (!player.isOnline()) {
            UserManager.getInstance().remove(info.uuid);
            return;
        }
        // Check GameMode
        if (player.getGameMode().equals(GameMode.SURVIVAL))
            player.setGameMode(GameMode.ADVENTURE);

        // show sidebar constantly
        sideBar();
        actionBar();
    }

    public MainMenu getMenu() {
        return this.menu;
    }
}
