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

import net.visualillusionsent.dconomy.bukkit.api.AccountBalanceEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Bank Balance request Event<br>
 * Plugins should call this Hook for BankBalance details
 *
 * @author Jason (darkdiplomat)
 */
public final class BankBalanceEvent extends AccountBalanceEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new BankBalanceEvent
     *
     * @param plugin
     *         the {@link Plugin} requesting balance information
     * @param username
     *         the user's name to get balance for
     */
    public BankBalanceEvent(Plugin plugin, String username) {
        super(plugin, username);
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
