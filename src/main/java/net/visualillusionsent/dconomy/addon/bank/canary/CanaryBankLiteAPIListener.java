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
/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2013 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankBalanceHook;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankDebitHook;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankDepositHook;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankSetBalanceHook;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;

import static net.canarymod.plugin.Priority.CRITICAL;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEBIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEPOSIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_SET;

public final class CanaryBankLiteAPIListener implements PluginListener {

    CanaryBankLiteAPIListener(CanaryBankLite dBL) {
        Canary.hooks().registerListener(this, dBL);
    }

    @HookHandler(priority = CRITICAL)
    public final void bankBalance(final BankBalanceHook hook) {
        try {
            if (BankHandler.verifyAccount(hook.getUserName())) {
                hook.setBalance(BankHandler.getBankAccountByName(hook.getUserName()).getBalance());
            }
            else {
                hook.setErrorMessage("Bank Account Not Found");
            }
        }
        catch (Exception aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = CRITICAL)
    public final void bankDeposit(final BankDepositHook hook) {
        try {
            if (BankHandler.verifyAccount(hook.getUserName())) {
                BankHandler.getBankAccountByName(hook.getUserName()).deposit(hook.getDeposit());
                Canary.hooks().callHook(new BankTransactionHook(new BankTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), PLUGIN_DEPOSIT, hook.getDeposit())));
            }
            else {
                hook.setErrorMessage("Bank Account Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = CRITICAL)
    public final void bankDebit(final BankDebitHook hook) {
        try {
            if (BankHandler.verifyAccount(hook.getUserName())) {
                BankHandler.getBankAccountByName(hook.getUserName()).debit(hook.getDebit());
                Canary.hooks().callHook(new BankTransactionHook(new BankTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), PLUGIN_DEBIT, hook.getDebit())));
            }
            else {
                hook.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = CRITICAL)
    public final void bankSet(final BankSetBalanceHook hook) {
        try {
            if (BankHandler.verifyAccount(hook.getUserName())) {
                BankHandler.getBankAccountByName(hook.getUserName()).setBalance(hook.getToSet());
                Canary.hooks().callHook(new BankTransactionHook(new BankTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), PLUGIN_SET, hook.getToSet())));
            }
            else {
                hook.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = CRITICAL)
    public final void debugTransaction(final BankTransactionHook hook) {
        dCoBase.debug("BankTransactionHook processed");
    }
}
