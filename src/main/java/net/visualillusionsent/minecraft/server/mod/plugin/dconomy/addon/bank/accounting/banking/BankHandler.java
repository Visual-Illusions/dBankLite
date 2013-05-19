/* 
 * Copyright 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dBankLite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dBankLite
 */
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking;

import java.util.concurrent.ConcurrentHashMap;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.data.BankDataSource;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.data.BankXMLSource;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.DataSourceType;

/**
 * Bank Handler class<br>
 * manages Wallets
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class BankHandler{

    private final ConcurrentHashMap<String, BankAccount> accounts;
    private static final BankHandler $;
    private final BankDataSource source;
    private static boolean init;

    static {
        $ = new BankHandler(dCoBase.getDataHandler().getDataSourceType());
    }

    private BankHandler(DataSourceType type){
        accounts = new ConcurrentHashMap<String, BankAccount>();
        if (type == DataSourceType.MYSQL) {
            source = new BankXMLSource();
        }
        else if (type == DataSourceType.SQLITE) {
            source = new BankXMLSource();
        }
        else {
            source = new BankXMLSource();
        }
    }

    public static final BankAccount getBankAccountByName(String username){
        if (verifyAccount(username)) {
            return $.accounts.get(username);
        }
        return newBankAccount(username);
    }

    /**
     * Gets a {@link Wallet} for a {@link IModUser}
     * 
     * @param user
     *            the {@link IModUser} to get a wallet for
     * @return the {@link Wallet} for the user if found; {@code null} if not found
     */
    public static final BankAccount getBankAccount(IModUser user){
        return getBankAccountByName(user.getName());
    }

    /**
     * Adds a {@link Wallet} to the manager
     * 
     * @param wallet
     *            the {@link Wallet} to be added
     */
    public static final void addAccount(BankAccount account){
        $.accounts.put(account.getOwner(), account);
    }

    /**
     * Checks if a {@link Wallet} exists
     * 
     * @param username
     *            the user's name to check Wallet for
     * @return {@code true} if the wallet exists; {@code false} otherwise
     */
    public static final boolean verifyAccount(String username){
        return $.accounts.containsKey(username);
    }

    /**
     * Creates a new {@link Wallet} with default balance
     * 
     * @param username
     *            the user's name to create a wallet for
     * @return the new {@link Wallet}
     */
    public static final BankAccount newBankAccount(String username){
        BankAccount account = new BankAccount(username, 0, false, $.source);
        addAccount(account);
        return account;
    }

    /**
     * Initializer method
     */
    public static final void initialize(){
        if (!init) {
            $.source.load();
            init = true;
        }
    }

    /**
     * Cleans up
     */
    public static final void cleanUp(){
        $.accounts.clear();
    }
}
