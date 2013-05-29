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
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class BankAddCommand extends dConomyCommand{

    public BankAddCommand(){
        super(2);
    }

    protected final void execute(IModUser user, String[] args){
        IModUser theUser = args[1].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[1]);
        if (theUser == null && !args[1].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !WalletHandler.verifyAccount(theUser.getName())) {
            if (!(args.length > 2) || !args[2].equals("-force")) {
                user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
                return;
            }
        }
        try {
            BankHandler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName()).deposit(args[0]);
            user.error("admin.add.balance", theUser == null ? "SERVER" : theUser.getName(), Double.valueOf(args[0]), "BANK ACCOUNT");
            // dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (IModUser) dCoBase.getServer() : theUser, WalletTransaction.ActionType.ADMIN_ADD, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
