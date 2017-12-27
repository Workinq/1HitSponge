package kr.kieran;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Sponge extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);

		final File file = new File(this.getDataFolder(), "config.yml");
		if (!file.exists()) {
			this.saveDefaultConfig();
		}
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission(this.getConfig().getString("SpongeBreakPermission"))) {
			sender.sendMessage(Messages.format(this.getConfig().getString("NoPermission")));
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("sponge")) {
			if (args.length != 1) {
				sender.sendMessage(Messages.format(this.getConfig().getString("InvalidSyntax")));
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				this.reloadConfig();
				sender.sendMessage(Messages.format(this.getConfig().getString("ConfigReloaded")));
				return true;
			}
		}

		return true;
	}

	@EventHandler
	public void onSpongeBreak(final PlayerInteractEvent e) {
		if (this.getConfig().getBoolean("EnableInstantBreak")) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				final Block b = e.getClickedBlock();
				if (b != null && b.getType().equals(Material.SPONGE)) {
					if (e.isCancelled())
						return;
					e.getClickedBlock().breakNaturally();
					if (this.getConfig().getBoolean("SendPlayerSpongeBreakMessage")) {
						e.getPlayer().sendMessage(Messages.format(this.getConfig().getString("SpongeBroken")));
					}
				}
			}
		}
	}
}
