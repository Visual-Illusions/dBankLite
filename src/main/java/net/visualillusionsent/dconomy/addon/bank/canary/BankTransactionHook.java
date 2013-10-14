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
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankTransaction;
import net.visualillusionsent.dconomy.canary.api.AccountTransactionHook;

/**
 * Bank Transaction Hook
 *
 * @author Jason (darkdiplomat)
 */
public class BankTransactionHook extends AccountTransactionHook {

    /**
     * Constructs a new Bank Transaction Hook
     *
     * @param action the {@link BankTransaction} done
     */
    public BankTransactionHook(BankTransaction action) {
        super(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BankTransaction getTransaction() {
        return (BankTransaction) action;
    }
}
