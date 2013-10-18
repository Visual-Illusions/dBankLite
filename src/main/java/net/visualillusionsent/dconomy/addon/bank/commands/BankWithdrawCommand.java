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
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankTransaction;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;

public final class BankWithdrawCommand extends dConomyCommand {

    public BankWithdrawCommand() {
        super(1);
    }

    @Override
    protected final void execute(dConomyUser user, String[] args) {
        Wallet userWallet = WalletHandler.getWallet(user);
        BankAccount userBank = BankHandler.getBankAccount(user);
        try {
            userWallet.testDeposit(args[0]);
            userBank.testDebit(args[0]);
            userBank.debit(args[0]);
            userWallet.deposit(args[0]);
            user.message("bank.withdraw", user, Double.parseDouble(args[0]));
            dCoBase.getServer().newTransaction(new BankTransaction(user, null, BankTransaction.BankAction.WITHDRAW, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
