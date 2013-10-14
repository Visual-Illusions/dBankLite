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
package net.visualillusionsent.dconomy.addon.bank.accounting;

import net.visualillusionsent.dconomy.accounting.AccountTransaction;
import net.visualillusionsent.dconomy.modinterface.ModUser;

public final class BankTransaction extends AccountTransaction {

    public enum BankAction {
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

    private final ModUser owner, admin;
    private final BankAction action;
    private final double amount;

    public BankTransaction(ModUser owner, ModUser admin, BankAction action, double amount) {
        this.owner = owner;
        this.admin = admin;
        this.action = action;
        this.amount = amount;
    }

    public final ModUser getOwner() {
        return owner;
    }

    public final ModUser getAdmin() {
        return admin;
    }

    public final BankAction getAction() {
        return action;
    }

    public final double getAmountChange() {
        return amount;
    }

}
