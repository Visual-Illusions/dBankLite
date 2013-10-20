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

import net.visualillusionsent.dconomy.api.account.AccountAction;

/**
 * Bank Action
 *
 * @author Jason (darkdiplomat)
 */
public enum BankAction implements AccountAction {

    DEPOSIT, //
    WITHDRAW, //
    ADMIN_ADD, //
    ADMIN_REMOVE, //
    ADMIN_SET, //
    ADMIN_RESET, //
    PLUGIN_DEBIT, //
    PLUGIN_DEPOSIT, //
    PLUGIN_SET, //
    ;

}
