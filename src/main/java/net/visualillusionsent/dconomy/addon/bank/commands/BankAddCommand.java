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
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankTransaction;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;

public final class BankAddCommand extends dConomyCommand {

    public BankAddCommand() {
        super(2);
    }

    protected final void execute(ModUser user, String[] args) {
        ModUser theUser = args[1].toUpperCase().equals("SERVER") ? (ModUser) dCoBase.getServer() : dCoBase.getServer().getUser(args[1]);
        if (theUser == null) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !BankHandler.verifyAccount(theUser.getName())) {
            if (!(args.length > 2) || !args[2].equals("-force")) {
                user.error("error.404.account", theUser.getName(), "BANK ACCOUNT");
                return;
            }
        }
        try {
            BankHandler.getBankAccountByName(theUser.getName()).deposit(args[0]);
            user.error("admin.add.balance", theUser.getName(), Double.valueOf(args[0]), "BANK ACCOUNT");
            dCoBase.getServer().newTransaction(new BankTransaction(theUser, user, BankTransaction.BankAction.ADMIN_ADD, Double.parseDouble(args[0])));
        } catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
