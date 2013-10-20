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
package net.visualillusionsent.dconomy.addon.bank.canary.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.dconomy.canary.api.account.AccountDebitHook;

/**
 * Bank Debit request Hook<br>
 * Plugins should call this Hook to debit bank accounts
 *
 * @author Jason (darkdiplomat)
 */
public final class BankDebitHook extends AccountDebitHook {

    /**
     * Constructs a new BankDebitHook
     *
     * @param caller
     *         the {@link Plugin} asking to take money
     * @param username
     *         the user's name who is having money taken
     * @param debit
     *         the amount to be removed
     */
    public BankDebitHook(Plugin caller, String username, double debit) {
        super(caller, username, debit);
    }

}