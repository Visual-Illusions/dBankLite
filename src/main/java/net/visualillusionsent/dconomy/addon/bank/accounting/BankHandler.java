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
package net.visualillusionsent.dconomy.addon.bank.accounting;

import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.addon.bank.data.BankDataSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankMySQLSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankSQLiteSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankXMLSource;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.data.DataSourceType;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bank Handler class<br>
 * manages Bank Accounts
 *
 * @author Jason (darkdiplomat)
 */
public final class BankHandler {

    private final ConcurrentHashMap<String, BankAccount> accounts;
    private final BankDataSource source;

    public BankHandler(DataSourceType type) {
        accounts = new ConcurrentHashMap<String, BankAccount>();
        if (type == DataSourceType.MYSQL) {
            source = new BankMySQLSource(this);
        }
        else if (type == DataSourceType.SQLITE) {
            source = new BankSQLiteSource(this);
        }
        else {
            source = new BankXMLSource(this);
        }
        source.load();
    }

    public final BankAccount getBankAccountByName(String username) {
        if (verifyAccount(username)) {
            return accounts.get(username);
        }
        return newBankAccount(username);
    }

    /**
     * Gets a {@link Wallet} for a {@link net.visualillusionsent.dconomy.api.dConomyUser}
     *
     * @param user
     *         the {@link dConomyUser} to get a wallet for
     *
     * @return the {@link Wallet} for the user if found; {@code null} if not found
     */
    public final BankAccount getBankAccount(dConomyUser user) {
        return getBankAccountByName(user.getName());
    }

    /**
     * Adds a {@link BankAccount} to the manager
     *
     * @param account
     *         the {@link BankAccount} to be added
     */
    public final void addAccount(BankAccount account) {
        accounts.put(account.getOwner(), account);
    }

    /**
     * Checks if a {@link Wallet} exists
     *
     * @param username
     *         the user's name to check Wallet for
     *
     * @return {@code true} if the wallet exists; {@code false} otherwise
     */
    public final boolean verifyAccount(String username) {
        return accounts.containsKey(username);
    }

    /**
     * Creates a new {@link Wallet} with default balance
     *
     * @param username
     *         the user's name to create a wallet for
     *
     * @return the new {@link Wallet}
     */
    public final BankAccount newBankAccount(String username) {
        BankAccount account = new BankAccount(username, 0, false, source);
        addAccount(account);
        return account;
    }

    /** Cleans up */
    public final void cleanUp() {
        accounts.clear();
    }

    public final Map<String, BankAccount> getBankAccounts() {
        return Collections.unmodifiableMap(accounts);
    }
}
