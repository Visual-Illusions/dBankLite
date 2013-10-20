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
package net.visualillusionsent.dconomy.addon.bank.bukkit.api;

import net.visualillusionsent.dconomy.bukkit.api.account.AccountDebitEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Bank Debit request Event<br>
 * Plugins should call this Event to debit bank accounts
 *
 * @author Jason (darkdiplomat)
 */
public final class BankDebitEvent extends AccountDebitEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new BankDebitEvent
     *
     * @param caller
     *         the {@link Plugin} asking to take money
     * @param username
     *         the user's name who is having money taken
     * @param debit
     *         the amount to be removed
     */
    public BankDebitEvent(Plugin caller, String username, double debit) {
        super(caller, username, debit);
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
