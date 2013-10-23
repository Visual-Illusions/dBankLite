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

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.ADMIN_REMOVE;

public final class BankRemoveCommand extends BankCommand {

    public BankRemoveCommand(BankHandler bank_handler) {
        super(2, bank_handler);
    }

    protected final void execute(dConomyUser user, String[] args) {
        dConomyUser theUser = dCoBase.getServer().getUser(args[1]);
        if (theUser == null) {
            dBankLiteBase.translateErrorMessageFor(user, "error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !bank_handler.verifyAccount(theUser.getName())) {
            dBankLiteBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "BANK ACCOUNT");
            return;
        }
        try {
            bank_handler.getBankAccountByName(theUser.getName()).debit(args[0]);
            dBankLiteBase.translateErrorMessageFor(user, "admin.remove.balance", theUser.getName(), Double.valueOf(args[0]), "BANK ACCOUNT");
            dCoBase.getServer().newTransaction(new BankTransaction(user, theUser, ADMIN_REMOVE, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
