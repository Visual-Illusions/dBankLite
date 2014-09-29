/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.WITHDRAW;

/**
 * @author Jason (darkdiplomat)
 */
public final class BankWithdrawCommand extends BankCommand {
    private final dBankLite dbl;

    public BankWithdrawCommand(dBankLite dbl, BankHandler bank_handler) {
        super(1, bank_handler);
        this.dbl = dbl;
    }

    @Override
    protected final void execute(dConomyUser user, String[] args) {
        BankAccount userBank = bank_handler.getBankAccount(user);
        try {
            WalletAPIListener.testWalletDeposit(user.getUUID(), args[0]);
            userBank.testDebit(args[0]);
            userBank.debit(args[0]);
            WalletAPIListener.walletDeposit(dbl, user, args[0], false);
            dBankLiteBase.translateMessageFor(user, "bank.withdraw", Double.parseDouble(args[0]));
            dCoBase.getServer().newTransaction(new WalletTransaction(dbl, user, WalletAction.PLUGIN_DEPOSIT, Double.valueOf(args[0])));
            dCoBase.getServer().newTransaction(new BankTransaction(null, user, WITHDRAW, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getLocalizedMessage(user.getUserLocale()));
        }
        catch (AccountNotFoundException anfex) {
            user.error(anfex.getMessage());
        }
    }
}
