/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.bukkit.api;

import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.bukkit.api.account.AccountTransactionEvent;
import org.bukkit.event.HandlerList;

/**
 * Bank Transaction Event
 *
 * @author Jason (darkdiplomat)
 */
public class BankTransactionEvent extends AccountTransactionEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new Bank Transaction Hook
     *
     * @param action
     *         the {@link BankTransaction} done
     */
    public BankTransactionEvent(BankTransaction action) {
        super(action);
    }

    /** {@inheritDoc} */
    @Override
    public final BankTransaction getTransaction() {
        return (BankTransaction) action;
    }

    // Bukkit Event methods
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    //
}
