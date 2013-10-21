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
package net.visualillusionsent.dconomy.addon.bank.api;

import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.Set;

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEBIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEPOSIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_SET;

/**
 *
 */
public class BankAPIListener {

    /**
     * Gets the balance of a user's bank account
     *
     * @param userName
     *         the name of the user
     * @param forceAccount
     *
     * @return bank account balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     */
    public static double bankAccountBalance(String userName, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (BankHandler.verifyAccount(userName) || forceAccount) {
            return BankHandler.getBankAccountByName(userName).getBalance();
        }
        throw new AccountNotFoundException("Bank Account", userName);
    }

    /**
     * Checks if a bank account is locked out
     *
     * @param userName
     *         the name of the user
     *
     * @return {@code true} if locked; {@code false} if not
     *
     * @throws AccountNotFoundException
     */
    public static boolean isLocked(String userName) throws AccountNotFoundException {
        if (BankHandler.verifyAccount(userName)) {
            return BankHandler.getBankAccountByName(userName).isLocked();
        }
        throw new AccountNotFoundException("Wallet", userName);
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
     * @throws InvalidPluginException
     */
    public static double bankDeposit(dConomyAddOn addOn, String userName, double deposit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (BankHandler.verifyAccount(userName) || forceAccount) {
            double newBalance = BankHandler.getBankAccountByName(userName).deposit(deposit);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_DEPOSIT, deposit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
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
     * @throws InvalidPluginException
     */
    public static double bankDebit(dConomyAddOn addOn, String userName, double debit, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (BankHandler.verifyAccount(userName) || forceAccount) {
            double newBalance = BankHandler.getBankAccountByName(userName).debit(debit);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_DEBIT, debit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
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
     * @throws InvalidPluginException
     */
    public static double bankSet(dConomyAddOn addOn, String userName, double set, boolean forceAccount) throws AccountingException, AccountNotFoundException {
        if (BankHandler.verifyAccount(userName) || forceAccount) {
            double newBalance = BankHandler.getBankAccountByName(userName).setBalance(set);
            dCoBase.getServer().newTransaction(new BankTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    /**
     * Tests a bank account debit before doing any modifications
     *
     * @param userName
     *         the name of the user who's bank account to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testBankDebit(String userName, double debit) throws AccountingException {
        if (BankHandler.verifyAccount(userName)) {
            BankHandler.getBankAccountByName(userName).testDebit(debit);
        }
    }

    /**
     * Tests a bank account deposit before doing any modifications
     *
     * @param userName
     *         the name of the user who's bank account to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testBankDeposit(String userName, double deposit) throws AccountingException {
        if (BankHandler.verifyAccount(userName)) {
            BankHandler.getBankAccountByName(userName).testDeposit(deposit);
        }
    }

    /**
     * Gets the names of known bank account owners
     *
     * @return set of owner names
     */
    public static Set<String> getBankAccountOwners() {
        return BankHandler.getBankAccounts().keySet();
    }

    private static dConomyAddOn pluginNameToAddOn(String pluginName) throws InvalidPluginException {
        if (pluginName == null) {
            throw new InvalidPluginException("Plugin Name cannot be null");
        }
        return dCoBase.getServer().getPluginAsAddOn(pluginName);
    }
}
