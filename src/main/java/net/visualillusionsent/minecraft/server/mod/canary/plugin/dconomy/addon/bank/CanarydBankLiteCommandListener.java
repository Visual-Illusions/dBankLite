/* 
 * Copyright 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dBankLite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dBankLite
 */
package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.addon.bank;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.CommandListener;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_User;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands.BankAddCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands.BankBaseCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands.BankDepositCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands.BankWithdrawCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands.dBankLiteInformationCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class CanarydBankLiteCommandListener implements CommandListener{
    private final dConomyCommand infoCmd, bankbase, bankdeposit, bankwithdraw, bankadd;

    CanarydBankLiteCommandListener(dBankLite dBL) throws CommandDependencyException{
        infoCmd = new dBankLiteInformationCommand();
        bankbase = new BankBaseCommand();
        bankdeposit = new BankDepositCommand();
        bankwithdraw = new BankWithdrawCommand();
        bankadd = new BankAddCommand();
        Canary.commands().registerCommands(this, dBL, false);
    }

    @Command(aliases = { "dbanklite" },
            description = "dBankLite Information Command",
            permissions = { "" },
            toolTip = "/dbanklite")
    public final void information(MessageReceiver msgrec, String[] args){
        IModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (IModUser) dCoBase.getServer();
        infoCmd.parseCommand(user, args, true);
    }

    @Command(aliases = { "bank" },
            description = "dBankLite Bank Account balance display",
            permissions = { "dconomy.bank.base" },
            toolTip = "/bank")
    public final void bankbase(MessageReceiver msgrec, String[] args){
        IModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (IModUser) dCoBase.getServer();
        bankbase.parseCommand(user, args, true);
    }

    @Command(aliases = { "deposit" },
            description = "Bank Deposit command",
            permissions = { "dconomy.bank.deposit" },
            toolTip = "/bank deposit <amount>",
            parent = "bank")
    public final void bankdeposit(MessageReceiver msgrec, String[] args){
        IModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (IModUser) dCoBase.getServer();
        bankdeposit.parseCommand(user, args, true);
    }

    @Command(aliases = { "withdraw" },
            description = "Bank withdraw command",
            permissions = { "dconomy.bank.withdraw" },
            toolTip = "/bank withdraw <amount>",
            parent = "bank")
    public final void bankwithdraw(MessageReceiver msgrec, String[] args){
        IModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (IModUser) dCoBase.getServer();
        bankwithdraw.parseCommand(user, args, true);
    }
}
