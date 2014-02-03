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
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

public final class BankBaseCommand extends BankCommand {

    public BankBaseCommand(BankHandler bank_handler) {
        super(0, bank_handler);
    }

    protected final void execute(dConomyUser user, String[] args) {
        BankAccount theAccount;
        if (args.length == 1 && (user.hasPermission("dconomy.admin.bank") || !dCoBase.getProperties().getBooleanValue("adminonly.balance.check"))) {
            dConomyUser theUser = dCoBase.getServer().getUser(args[0]);
            if (theUser == null && !args[0].toUpperCase().equals("SERVER")) {
                dBankLiteBase.translateErrorMessageFor(user, "error.404.user", args[0]);
                return;
            }
            if (!args[0].toUpperCase().equals("SERVER") && !bank_handler.verifyAccount(theUser.getName())) {
                dBankLiteBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "BANK ACCOUNT");
                return;
            }
            theAccount = bank_handler.getBankAccountByName(theUser == null ? "SERVER" : theUser.getName());
            if (theAccount.isLocked()) {
                dBankLiteBase.translateErrorMessageFor(user, "error.lock.out", theUser == null ? "SERVER" : theUser.getName(), "BANK ACCOUNT");
            }
            else {
                dBankLiteBase.translateMessageFor(user, "account.balance.other", theUser == null ? "SERVER" : theUser.getName(), theAccount.getBalance());
            }
        }
        else {
            theAccount = bank_handler.getBankAccountByName(user.getName());
            if (theAccount.isLocked()) {
                dBankLiteBase.translateErrorMessageFor(user, "error.lock.out", user.getName(), "BANK ACCOUNT");
            }
            else {
                dBankLiteBase.translateMessageFor(user, "account.balance", Double.valueOf(theAccount.getBalance()), "BANK ACCOUNT");
            }
        }
    }
}
