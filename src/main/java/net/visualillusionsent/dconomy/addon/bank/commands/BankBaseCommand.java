/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dBankLite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dBankLite.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;

public final class BankBaseCommand extends dConomyCommand {

    public BankBaseCommand() {
        super(0);
    }

    protected final void execute(ModUser user, String[] args) {
        BankAccount theAccount;
        if (args.length == 1 && (user.hasPermission("dconomy.admin.bank") || !dCoBase.getProperties().getBooleanValue("adminonly.balance.check"))) {
            ModUser theUser = args[0].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[0]);
            if (theUser == null && !args[0].toUpperCase().equals("SERVER")) {
                user.error("error.404.user", args[0]);
                return;
            }
            if (!args[0].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
                user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
                return;
            }
            theAccount = BankHandler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName());
            if (theAccount.isLocked()) {
                user.message("error.lock.out", theUser == null ? "SERVER" : theUser.getName(), "BANK ACCOUNT");
            } else {
                user.message("account.balance.other", theUser == null ? "SERVER" : theUser.getName(), theAccount.getBalance());
            }
        } else {
            theAccount = BankHandler.getBankAccountByName(user.getName());
            if (theAccount.isLocked()) {
                user.message("error.lock.out", user.getName(), "BANK ACCOUNT");
            } else {
                user.message("account.balance", Double.valueOf(theAccount.getBalance()), "BANK ACCOUNT");
            }
        }
    }
}
