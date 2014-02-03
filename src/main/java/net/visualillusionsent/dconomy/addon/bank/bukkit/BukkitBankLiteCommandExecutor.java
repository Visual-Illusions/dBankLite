/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.bukkit;

import net.visualillusionsent.dconomy.addon.bank.commands.*;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.bukkit.api.Bukkit_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPluginInformationCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Bukkit dBankLite Command executor
 *
 * @author Jason (darkdiplomat)
 */
public class BukkitBankLiteCommandExecutor extends VisualIllusionsBukkitPluginInformationCommand {
    private final dConomyCommand[] cmds = new dConomyCommand[9];

    BukkitBankLiteCommandExecutor(BukkitBankLite bBankLite) {
        super(bBankLite);
        // Initialize Commands
        cmds[0] = new BankBaseCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[1] = new BankDepositCommand(bBankLite, bBankLite.getBaseInstance().getHandlerInstance());
        cmds[2] = new BankWithdrawCommand(bBankLite, bBankLite.getBaseInstance().getHandlerInstance());
        cmds[3] = new BankAddCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[4] = new BankRemoveCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[5] = new BankSetCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[6] = new BankResetCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[7] = new BankReloadCommand(bBankLite.getBaseInstance().getHandlerInstance());
        cmds[8] = new BankLockCommand(bBankLite.getBaseInstance().getHandlerInstance());

        // Register commands
        bBankLite.getCommand("dbanklite").setExecutor(this);
        bBankLite.getCommand("bank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dConomyUser user = sender instanceof Player ? new Bukkit_User((Player) sender) : (dConomyUser) dCoBase.getServer();
        if (label.equals("dbanklite")) {
            this.sendInformation(sender);
            return true;
        }
        else if (label.equals("bank")) {
            if (args.length == 0 && sender.hasPermission("dconomy.bank.base") && !cmds[0].parseCommand(user, args, false)) {
                sender.sendMessage(ChatColor.RED + "/bank [subcommand|user] [args]");
            }
            else if (args.length > 0) {
                String sub = args[0].toLowerCase();
                // Check Permissions
                if ((TabCompleteUtil.matchA.reset(sub).matches() && !sender.hasPermission("dconomy.admin.bank.".concat(sub)))
                        || (TabCompleteUtil.matchP.reset(sub).matches() && !sender.hasPermission("dconomy.bank.".concat(sub)))) {
                    sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error."); // Borrow Bukkit's default message
                }
                else if (sub.equals("deposit") && !cmds[1].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank deposit <amount>");
                }
                else if (sub.equals("withdraw") && !cmds[2].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank withdraw <amount>");
                }
                else if (sub.equals("add") && !cmds[3].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank add <amount> <user> [-force]");
                }
                else if (sub.equals("remove") && !cmds[4].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank remove <amount> <user>");
                }
                else if (sub.equals("set") && !cmds[5].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank set <amount> <user>");
                }
                else if (sub.equals("reset") && !cmds[6].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank reset <user>");
                }
                else if (sub.equals("reload") && !cmds[7].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank reload <user>");
                }
                else if (sub.equals("lock") && !cmds[8].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/bank lock <yes|no> <user>");
                }
                else if (!cmds[0].parseCommand(user, args, false)) {
                    sender.sendMessage(ChatColor.RED + "/bank [subcommand|user] [args]");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        dConomyUser user = sender instanceof Player ? new Bukkit_User((Player) sender) : (dConomyUser) dCoBase.getServer();
        return TabCompleteUtil.match(user, args);
    }
}
