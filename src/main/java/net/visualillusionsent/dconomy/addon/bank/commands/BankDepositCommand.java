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
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.ModUser;

public final class BankDepositCommand extends dConomyCommand {

    public BankDepositCommand() {
        super(1);
    }

    @Override
    protected final void execute(ModUser user, String[] args) {
        Wallet userWallet = WalletHandler.getWallet(user);
        BankAccount userBank = BankHandler.getBankAccount(user);
        try {
            userWallet.testDebit(args[0]);
            userBank.testDeposit(args[0]);
            userBank.deposit(args[0]);
            userWallet.debit(args[0]);
            user.message("bank.deposit", user, Double.parseDouble(args[0]));
            dCoBase.getServer().newTransaction(new BankTransaction(user, null, BankTransaction.BankAction.DEPOSIT, Double.parseDouble(args[0])));
        } catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
