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
package net.visualillusionsent.dconomy.addon.bank.bukkit;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankBalanceEvent;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankDebitEvent;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankDepositEvent;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankSetBalanceEvent;
import net.visualillusionsent.dconomy.addon.bank.bukkit.api.BankTransactionEvent;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEBIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_DEPOSIT;
import static net.visualillusionsent.dconomy.addon.bank.api.BankAction.PLUGIN_SET;
import static org.bukkit.event.EventPriority.HIGHEST;

/**
 * Bukkit dBankLite API Listener
 *
 * @author Jason (darkdiplomat)
 */
public class BukkitBankLiteAPIListener implements Listener {

    BukkitBankLiteAPIListener(BukkitBankLite plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = HIGHEST)
    public final void bankBalance(BankBalanceEvent event) {
        try {
            if (BankHandler.verifyAccount(event.getUserName())) {
                event.setBalance(BankHandler.getBankAccountByName(event.getUserName()).getBalance());
            }
            else {
                event.setErrorMessage("Bank Account Not Found");
            }
        }
        catch (AccountingException aex) {
            dBankLiteBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void bankDeposit(BankDepositEvent event) {
        try {
            if (BankHandler.verifyAccount(event.getUserName())) {
                BankHandler.getBankAccountByName(event.getUserName()).deposit(event.getDeposit());
                Bukkit.getPluginManager().callEvent(new BankTransactionEvent(new BankTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_DEPOSIT, event.getDeposit())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dBankLiteBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void bankDebit(BankDebitEvent event) {
        try {
            if (BankHandler.verifyAccount(event.getUserName())) {
                BankHandler.getBankAccountByName(event.getUserName()).debit(event.getDebit());
                Bukkit.getPluginManager().callEvent(new BankTransactionEvent(new BankTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_DEBIT, event.getDebit())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dBankLiteBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void bankSet(BankSetBalanceEvent event) {
        try {
            if (BankHandler.verifyAccount(event.getUserName())) {
                BankHandler.getBankAccountByName(event.getUserName()).setBalance(event.getToSet());
                Bukkit.getPluginManager().callEvent(new BankTransactionEvent(new BankTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_SET, event.getToSet())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dBankLiteBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void debugTransaction(BankTransactionEvent event) {
        dBankLiteBase.debug("BankTransactionEvent called: " + event.getTransaction());
    }
}
