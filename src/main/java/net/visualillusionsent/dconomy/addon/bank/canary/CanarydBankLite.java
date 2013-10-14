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

import net.canarymod.commandsys.CommandDependencyException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankTransaction;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.logging.Logger;

public final class CanarydBankLite extends VisualIllusionsCanaryPlugin implements dBankLite {

    @Override
    public final boolean enable() {
        new dBankLiteBase(this);
        BankHandler.initialize();
        try {
            new CanarydBankLiteCommandListener(this);
        } catch (CommandDependencyException ex) {
            return false;
        }
        dCoBase.getServer().registerTransactionHandler(BankTransactionHook.class, BankTransaction.class);
        return true;
    }

    @Override
    public final void disable() {
        dCoBase.getServer().deregisterTransactionHandler(BankTransactionHook.class);
        dBankLiteBase.cleanUp();
    }

    @Override
    public final Logger getPluginLogger() {
        return this.getLogman();
    }

    @Override
    public final void check() {
        checkStatus();
        checkVersion();
    }
}
