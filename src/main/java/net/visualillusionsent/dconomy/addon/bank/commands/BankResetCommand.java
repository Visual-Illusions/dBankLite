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
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;

public final class BankResetCommand extends dConomyCommand{

    public BankResetCommand(){
        super(1);
    }

    protected final void execute(ModUser user, String[] args){
        ModUser theUser = args[0].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[0]);
        if (theUser == null && !args[0].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[0]);
            return;
        }
        if (!args[0].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
            user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
            return;
        }
        BankHandler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName()).setBalance(dCoBase.getProperties().getDouble("default.balance"));
        user.error("admin.reset.balance", theUser == null ? "SERVER" : theUser.getName(), "BANK ACCOUNT");
        // dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (IModUser) dCoBase.getServer() : theUser, WalletTransaction.ActionType.ADMIN_SET, dCoBase.getProperties().getDouble("default.balance")));
    }
}