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
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.addon.bank.commands.*;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_User;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;

public final class CanarydBankLiteCommandListener implements CommandListener{

    private final dConomyCommand infoCmd, bankbase, bankdeposit, bankwithdraw, bankadd, bankremove, bankset, bankreset, bankreload, banklock;

    CanarydBankLiteCommandListener(dBankLite dBL) throws CommandDependencyException{
        infoCmd = new dBankLiteInformationCommand();
        bankbase = new BankBaseCommand();
        bankdeposit = new BankDepositCommand();
        bankwithdraw = new BankWithdrawCommand();
        bankadd = new BankAddCommand();
        bankremove = new BankRemoveCommand();
        bankset = new BankSetCommand();
        bankreset = new BankResetCommand();
        bankreload = new BankReloadCommand();
        banklock = new BankLockCommand();
        Canary.commands().registerCommands(this, dBL, false);
    }

    @Command(aliases = { "dbanklite" },
        description = "dBankLite Information Command",
        permissions = { "" },
        toolTip = "/dbanklite")
    public final void information(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        infoCmd.parseCommand(user, args, true);
    }

    @Command(aliases = { "bank" },
        description = "dBankLite Bank Account balance display",
        permissions = { "dconomy.bank.base" },
        toolTip = "/bank")
    public final void bankbase(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankbase.parseCommand(user, args, true);
    }

    @Command(aliases = { "deposit" },
        description = "Bank Deposit command",
        permissions = { "dconomy.bank.deposit" },
        toolTip = "/bank deposit <amount>",
        parent = "bank")
    public final void bankdeposit(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankdeposit.parseCommand(user, args, true);
    }

    @Command(aliases = { "withdraw" },
        description = "Bank withdraw command",
        permissions = { "dconomy.bank.withdraw" },
        toolTip = "/bank withdraw <amount>",
        parent = "bank")
    public final void bankwithdraw(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankwithdraw.parseCommand(user, args, true);
    }

    @Command(aliases = { "add" },
        description = "Bank add command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank add <amount> <user>",
        parent = "bank")
    public final void bankadd(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankadd.parseCommand(user, args, true);
    }

    @Command(aliases = { "remove" },
        description = "Bank remove command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank remove <amount> <user>",
        parent = "bank")
    public final void bankremove(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankremove.parseCommand(user, args, true);
    }

    @Command(aliases = { "set" },
        description = "Bank set command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank set <amount> <user>",
        parent = "bank")
    public final void bankset(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankset.parseCommand(user, args, true);
    }

    @Command(aliases = { "reset" },
        description = "Bank reset command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank reset <amount> <user>",
        parent = "bank")
    public final void bankreset(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankreset.parseCommand(user, args, true);
    }

    @Command(aliases = { "reload" },
        description = "Bank reload command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank reload <user>",
        parent = "bank")
    public final void bankreload(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        bankreload.parseCommand(user, args, true);
    }

    @Command(aliases = { "lock" },
        description = "Bank lock command",
        permissions = { "dconomy.admin.bank" },
        toolTip = "/bank lock <user>",
        parent = "bank")
    public final void banklock(MessageReceiver msgrec, String[] args){
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();
        banklock.parseCommand(user, args, true);
    }
}
