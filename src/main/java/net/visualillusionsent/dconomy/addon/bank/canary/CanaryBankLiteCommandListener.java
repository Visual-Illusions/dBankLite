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
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.CommandListener;
import net.visualillusionsent.dconomy.addon.bank.commands.BankAddCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankBaseCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankDepositCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankLockCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankReloadCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankRemoveCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankResetCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankSetCommand;
import net.visualillusionsent.dconomy.addon.bank.commands.BankWithdrawCommand;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPluginInformationCommand;

public final class CanaryBankLiteCommandListener extends VisualIllusionsCanaryPluginInformationCommand implements CommandListener {

    private final dConomyCommand[] cmds = new dConomyCommand[9];

    CanaryBankLiteCommandListener(CanaryBankLite cBankLite) throws CommandDependencyException {
        super(cBankLite);
        cmds[0] = new BankBaseCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[1] = new BankDepositCommand(cBankLite, cBankLite.getBaseInstance().getHandlerInstance());
        cmds[2] = new BankWithdrawCommand(cBankLite, cBankLite.getBaseInstance().getHandlerInstance());
        cmds[3] = new BankAddCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[4] = new BankRemoveCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[5] = new BankSetCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[6] = new BankResetCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[7] = new BankReloadCommand(cBankLite.getBaseInstance().getHandlerInstance());
        cmds[8] = new BankLockCommand(cBankLite.getBaseInstance().getHandlerInstance());
        Canary.commands().registerCommands(this, cBankLite, false);
    }

    @Command(aliases = { "dbanklite" },
            description = "dBankLite Information Command",
            permissions = { "" },
            toolTip = "/dbanklite")
    public final void information(MessageReceiver msgrec, String[] args) {
        this.sendInformation(msgrec);
    }

    @Command(aliases = { "bank" },
            description = "dBankLite Bank Account balance display",
            permissions = { "dconomy.bank.base" },
            toolTip = "/bank")
    public final void bankbase(MessageReceiver msgrec, String[] args) {
        cmds[0].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "deposit" },
            description = "Bank Deposit command",
            permissions = { "dconomy.bank.deposit" },
            toolTip = "/bank deposit <amount>",
            parent = "bank")
    public final void bankdeposit(MessageReceiver msgrec, String[] args) {
        cmds[1].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "withdraw" },
            description = "Bank withdraw command",
            permissions = { "dconomy.bank.withdraw" },
            toolTip = "/bank withdraw <amount>",
            parent = "bank")
    public final void bankwithdraw(MessageReceiver msgrec, String[] args) {
        cmds[2].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "add" },
            description = "Bank add command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank add <amount> <user>",
            parent = "bank")
    public final void bankadd(MessageReceiver msgrec, String[] args) {
        cmds[3].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "remove" },
            description = "Bank remove command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank remove <amount> <user>",
            parent = "bank")
    public final void bankremove(MessageReceiver msgrec, String[] args) {
        cmds[4].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "set" },
            description = "Bank set command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank set <amount> <user>",
            parent = "bank")
    public final void bankset(MessageReceiver msgrec, String[] args) {
        cmds[5].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "reset" },
            description = "Bank reset command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank reset <user>",
            parent = "bank")
    public final void bankreset(MessageReceiver msgrec, String[] args) {
        cmds[6].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "reload" },
            description = "Bank reload command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank reload <user>",
            parent = "bank")
    public final void bankreload(MessageReceiver msgrec, String[] args) {
        cmds[7].parseCommand(getUser(msgrec), args, true);
    }

    @Command(aliases = { "lock" },
            description = "Bank lock command",
            permissions = { "dconomy.admin.bank" },
            toolTip = "/bank lock <yes|no> <user>",
            parent = "bank")
    public final void banklock(MessageReceiver msgrec, String[] args) {
        cmds[8].parseCommand(getUser(msgrec), args, true);
    }

    private dConomyUser getUser(MessageReceiver msgrec) {
        return msgrec instanceof Player ? new Canary_User((Player) msgrec) : dCoBase.getServer();
    }
}
