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

import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAPIListener;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAction;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.DEPOSIT;

public final class BankDepositCommand extends BankCommand {
    private final dBankLite dbl;

    public BankDepositCommand(dBankLite dbl, BankHandler bank_handler) {
        super(1, bank_handler);
        this.dbl = dbl;
    }

    @Override
    protected final void execute(dConomyUser user, String[] args) {
        BankAccount userBank = bank_handler.getBankAccount(user);
        try {
            WalletAPIListener.testWalletDebit(user.getName(), args[0]);
            userBank.testDeposit(args[0]);
            userBank.deposit(args[0]);
            WalletAPIListener.walletDebit(dbl, user, args[0], false);
            dBankLiteBase.translateMessageFor(user, "bank.deposit", Double.parseDouble(args[0]));
            dCoBase.getServer().newTransaction(new WalletTransaction(dbl, user, WalletAction.PLUGIN_DEBIT, Double.parseDouble(args[0])));
            dCoBase.getServer().newTransaction(new BankTransaction(null, user, DEPOSIT, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
        catch (AccountNotFoundException anfex) {
            user.error(anfex.getMessage());
        }
    }
}
