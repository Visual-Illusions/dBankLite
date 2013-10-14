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

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankTransaction;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.ModUser;

public final class BankResetCommand extends dConomyCommand {

    public BankResetCommand() {
        super(1);
    }

    protected final void execute(ModUser user, String[] args) {
        ModUser theUser = args[0].toUpperCase().equals("SERVER") ? (ModUser) dCoBase.getServer() : dCoBase.getServer().getUser(args[0]);
        if (theUser == null) {
            user.error("error.404.user", args[0]);
            return;
        }
        if (!args[0].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
            user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
            return;
        }
        BankHandler.getBankAccountByName(theUser.getName()).setBalance(0);
        user.error("admin.reset.balance", theUser.getName(), "BANK ACCOUNT");
        dCoBase.getServer().newTransaction(new BankTransaction(theUser, user, BankTransaction.BankAction.ADMIN_RESET, 0));
    }
}
