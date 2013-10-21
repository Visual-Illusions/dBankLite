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
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;

import static net.canarymod.plugin.Priority.PASSIVE;

public final class CanaryBankLiteAPIListener implements PluginListener {

    CanaryBankLiteAPIListener(CanaryBankLite dBL) {
        Canary.hooks().registerListener(this, dBL);
    }

    @HookHandler(priority = PASSIVE)
    public final void debugTransaction(final BankTransactionHook hook) {
        dCoBase.debug("BankTransactionHook processed");
    }
}
