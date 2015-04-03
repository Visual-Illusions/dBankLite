/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2015 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.commandsys.TabComplete;
import net.visualillusionsent.dconomy.addon.bank.commands.*;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPluginInformationCommand;

import java.util.List;

import static net.visualillusionsent.dconomy.addon.bank.commands.BankPermissions.*;

/**
 * @author Jason (darkdiplomat)
 */
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

    @Command(
            aliases = {"dbanklite"},
            description = "dBankLite Information Command",
            permissions = {""},
            toolTip = "/dbanklite"
    )
    public final void information(MessageReceiver msgrec, String[] args) {
        this.sendInformation(msgrec);
    }

    @Command(
            aliases = {"bank"},
            description = "dBankLite Bank Account balance display",
            permissions = {BANK$BASE},
            toolTip = "/bank",
            version = 2
    )
    public final void bankbase(MessageReceiver msgrec, String[] args) {
        cmds[0].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"deposit"},
            description = "Bank Deposit command",
            permissions = {BANK$DEPOSIT},
            toolTip = "/bank deposit <amount>",
            helpLookup = "bank deposit",
            parent = "bank"
    )
    public final void bankdeposit(MessageReceiver msgrec, String[] args) {
        cmds[1].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"withdraw"},
            description = "Bank withdraw command",
            permissions = {BANK$WITHDRAW},
            toolTip = "/bank withdraw <amount>",
            helpLookup = "bank withdraw",
            parent = "bank"
    )
    public final void bankwithdraw(MessageReceiver msgrec, String[] args) {
        cmds[2].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"add"},
            description = "Bank add command",
            permissions = {BANK$ADMIN$ADD},
            toolTip = "/bank add <amount> <user>",
            helpLookup = "bank add",
            parent = "bank"
    )
    public final void bankadd(MessageReceiver msgrec, String[] args) {
        cmds[3].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"remove"},
            description = "Bank remove command",
            permissions = {BANK$ADMIN$REMOVE},
            toolTip = "/bank remove <amount> <user>",
            helpLookup = "bank remove",
            parent = "bank"
    )
    public final void bankremove(MessageReceiver msgrec, String[] args) {
        cmds[4].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"set"},
            description = "Bank set command",
            permissions = {BANK$ADMIN$SET},
            toolTip = "/bank set <amount> <user>",
            helpLookup = "bank set",
            parent = "bank"
    )
    public final void bankset(MessageReceiver msgrec, String[] args) {
        cmds[5].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"reset"},
            description = "Bank reset command",
            permissions = {BANK$ADMIN$RESET},
            toolTip = "/bank reset <user>",
            helpLookup = "bank reset",
            parent = "bank"
    )
    public final void bankreset(MessageReceiver msgrec, String[] args) {
        cmds[6].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"reload"},
            description = "Bank reload command",
            permissions = {BANK$ADMIN$RELOAD},
            toolTip = "/bank reload <user>",
            helpLookup = "bank reload",
            parent = "bank"
    )
    public final void bankreload(MessageReceiver msgrec, String[] args) {
        cmds[7].parseCommand(getUser(msgrec), args, false);
    }

    @Command(
            aliases = {"lock"},
            description = "Bank lock command",
            permissions = {BANK$ADMIN$LOCK},
            toolTip = "/bank lock <yes|no> <user>",
            helpLookup = "bank lock",
            parent = "bank"
    )
    public final void banklock(MessageReceiver msgrec, String[] args) {
        cmds[8].parseCommand(getUser(msgrec), args, false);
    }

    private dConomyUser getUser(MessageReceiver msgrec) {
        return msgrec instanceof Player ? new Canary_User((Player) msgrec) : dCoBase.getServer();
    }

    @TabComplete(commands = {"bank"})
    public final List<String> bankComp(MessageReceiver msgrec, String[] args) {
        return BankTabComplete.match(getUser(msgrec), args);
    }
}
