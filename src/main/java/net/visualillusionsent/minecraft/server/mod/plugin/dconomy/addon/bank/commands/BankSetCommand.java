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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.commands;

import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class BankSetCommand extends dConomyCommand{

    public BankSetCommand(){
        super(2);
    }

    protected final void execute(IModUser user, String[] args){
        IModUser theUser = args[1].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[1]);
        if (theUser == null && !args[1].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
            if (!(args.length > 2) || !args[2].equals("-force")) {
                user.error("error.404.account", theUser.getName(), "WALLET");
                return;
            }
        }
        BankHandler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName()).setBalance(args[0]);
        user.error("admin.set.balance", theUser == null ? "SERVER" : theUser.getName(), Double.valueOf(args[0]), "WALLET");
        // dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (IModUser) dCoBase.getServer() : theUser, WalletTransaction.ActionType.ADMIN_SET, Double.parseDouble(args[0])));
    }
}
