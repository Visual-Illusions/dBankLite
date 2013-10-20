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

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.api.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.canary.api.BankTransactionHook;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class CanaryBankLite extends VisualIllusionsCanaryPlugin implements dBankLite {

    @Override
    public final boolean enable() {
        super.enable();
        try {
            new dBankLiteBase(this);
            BankHandler.initialize();
            new CanaryBankLiteCommandListener(this);
            new CanaryBankLiteAPIListener(this);
            new CanaryBankLiteMOTDListener(this);
            dCoBase.getServer().registerTransactionHandler(BankTransactionHook.class, BankTransaction.class);

            return true;
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogman().log(Level.SEVERE, "dBankLite failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogman().severe("dBankLite failed to start. Reason: ".concat(reason));
            }
        }
        return false;
    }

    @Override
    public final void disable() {
        dCoBase.getServer().unregisterTransactionHandler(BankTransactionHook.class);
        dBankLiteBase.cleanUp();
    }

    @Override
    public final Logger getPluginLogger() {
        return this.getLogman();
    }

    @Override
    public void error(String message) {
        getLogman().warning(message);
    }

    @Override
    public void message(String message) {
        getLogman().info(message);
    }

    @Override
    public boolean hasPermission(String message) {
        return true;
    }

    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
