/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2015 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.addon.bank.api;

import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.Set;
import java.util.UUID;

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.*;

/**
 * Bank API Listener
 *
 * @author Jason (darkdiplomat)
 */
public class BankAPIListener {
    private static BankHandler bank_handler;

    public static void setBankHandler(BankHandler handler) {
        if (bank_handler == null) {
            bank_handler = handler;
        }
    }

    /**
     * Gets the balance of a user's bank account
     *
     * @param owner
     *         the {@link UUID} of the owner
     * @param forceAccount
     *
     * @return bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     */
    public static double bankAccountBalance(UUID owner, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (bank_handler.verifyAccount(owner) || forceAccount) {
            return bank_handler.getBankAccountByUUID(owner).getBalance();
        }
        throw new AccountNotFoundException("Bank Account", owner);
    }

    /**
     * Checks if a bank account is locked out
     *
     * @param owner
     *         the {@link UUID} of the user
     *
     * @return {@code true} if locked; {@code false} if not
     *
     * @throws AccountNotFoundException
     */
    public static boolean isLocked(UUID owner) throws AccountNotFoundException {
        if (bank_handler.verifyAccount(owner)) {
            return bank_handler.getBankAccountByUUID(owner).isLocked();
        }
        throw new AccountNotFoundException("Wallet", owner);
    }

    /**
     * Deposits money into a user's bank account
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param deposit
     *         the amount to deposit
     * @param forceAccount
     *         {@code true} to force a new bank account; {@code false} otherwise
     *
     * @return the new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double bankDeposit(String pluginName, String userName, double deposit, boolean forceAccount) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return bankDeposit(pluginNameToAddOn(pluginName), userName, deposit, forceAccount);
    }

    /**
     * Deposits money into a user's bank account
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param deposit
     *         the amount to deposit
     * @param forceAccount
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     */
    public static double bankDeposit(dConomyAddOn addOn, String userName, double deposit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        return bankDeposit(addOn, dCoBase.getServer().getUser(userName), deposit, forceAccount);
    }

    public static double bankDeposit(dConomyAddOn addOn, dConomyUser user, double deposit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (bank_handler.verifyAccount(user.getUUID()) || forceAccount) {
            double newBalance = bank_handler.getBankAccount(user).deposit(deposit);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, user, PLUGIN_DEPOSIT, deposit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", user.getUUID());
    }

    /**
     * Debits money from a user's bank account
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param debit
     *         the amount to debit
     * @param forceAccount
     *         {@code true} to force a new bank account; {@code false} otherwise
     *
     * @return the new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double bankDebit(String pluginName, String userName, double debit, boolean forceAccount) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return bankDebit(pluginNameToAddOn(pluginName), userName, debit, forceAccount);
    }

    /**
     * Debits money from a user's bank account
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param debit
     *         the amount to debit
     * @param forceAccount
     *         {@code true} to force a new bank account; {@code false} otherwise
     *
     * @return the new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     */
    public static double bankDebit(dConomyAddOn addOn, String userName, double debit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        return bankDebit(addOn, dCoBase.getServer().getUser(userName), debit, forceAccount);
    }

    public static double bankDebit(dConomyAddOn addOn, dConomyUser user, double debit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (bank_handler.verifyAccount(user.getUUID()) || forceAccount) {
            double newBalance = bank_handler.getBankAccount(user).debit(debit);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, user, PLUGIN_DEBIT, debit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", user.getUUID());
    }

    /**
     * Sets the balance of a user's bank account
     *
     * @param pluginName
     *         the name of the plugin doing the setting
     * @param userName
     *         the name of the user
     * @param set
     *         the amount to be set
     * @param forceAccount
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double bankSet(String pluginName, String userName, double set, boolean forceAccount) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return bankSet(pluginNameToAddOn(pluginName), userName, set, forceAccount);
    }

    /**
     * Sets the balance of a user's bank account
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param set
     *         the amount to be set
     * @param forceAccount
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     */
    public static double bankSet(dConomyAddOn addOn, String userName, double set, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        return bankSet(addOn, dCoBase.getServer().getUser(userName), set, forceAccount);
    }

    public static double bankSet(dConomyAddOn addOn, dConomyUser user, double set, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (bank_handler.verifyAccount(user.getUUID()) || forceAccount) {
            double newBalance = bank_handler.getBankAccount(user).setBalance(set);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, user, PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", user.getUUID());
    }

    /**
     * Tests a bank account debit before doing any modifications
     *
     * @param owner
     *         the {@link UUID} of the user who's bank account to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testBankDebit(UUID owner, double debit) throws AccountingException {
        if (bank_handler.verifyAccount(owner)) {
            bank_handler.getBankAccountByUUID(owner).testDebit(debit);
        }
    }

    /**
     * Tests a bank account deposit before doing any modifications
     *
     * @param owner
     *         the {@link UUID} of the user who's bank account to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testBankDeposit(UUID owner, double deposit) throws AccountingException {
        if (bank_handler.verifyAccount(owner)) {
            bank_handler.getBankAccountByUUID(owner).testDeposit(deposit);
        }
    }

    /**
     * Gets the names of known bank account owners
     *
     * @return set of owner {@link UUID}(s)
     */
    public static Set<UUID> getBankAccountOwners() {
        return bank_handler.getBankAccounts().keySet();
    }

    private static dConomyAddOn pluginNameToAddOn(String pluginName) throws InvalidPluginException {
        if (pluginName == null) {
            throw new InvalidPluginException("Plugin Name cannot be null");
        }
        return dCoBase.getServer().getPluginAsAddOn(pluginName);
    }
}
