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
package net.visualillusionsent.dconomy.addon.bank.accounting;

import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.addon.bank.data.BankDataSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankMySQLSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankSQLiteSource;
import net.visualillusionsent.dconomy.addon.bank.data.BankXMLSource;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.DataSourceType;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bank Handler class<br>
 * manages Bank Accounts
 *
 * @author Jason (darkdiplomat)
 */
public final class BankHandler {

    private final ConcurrentHashMap<UUID, BankAccount> accounts;
    private final BankDataSource source;

    public BankHandler(DataSourceType type) {
        accounts = new ConcurrentHashMap<UUID, BankAccount>();
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

    public final BankAccount getBankAccountByUUID(UUID owner) {
        if (verifyAccount(owner)) {
            return accounts.get(owner);
        }
        return newBankAccount(owner);
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
        return getBankAccountByUUID(user.getUUID());
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
     * @param owner
     *         the user's {@link UUID} to check Wallet for
     *
     * @return {@code true} if the wallet exists; {@code false} otherwise
     */
    public final boolean verifyAccount(UUID owner) {
        return owner.equals(dCoBase.getServer().getUUID()) || accounts.containsKey(owner);
    }

    /**
     * Creates a new {@link Wallet} with default balance
     *
     * @param username
     *         the user's name to create a wallet for
     *
     * @return the new {@link Wallet}
     */
    public final BankAccount newBankAccount(UUID username) {
        BankAccount account = new BankAccount(username, 0, false, source);
        addAccount(account);
        return account;
    }

    /** Cleans up */
    public final void cleanUp() {
        accounts.clear();
    }

    public final Map<UUID, BankAccount> getBankAccounts() {
        return Collections.unmodifiableMap(accounts);
    }
}
