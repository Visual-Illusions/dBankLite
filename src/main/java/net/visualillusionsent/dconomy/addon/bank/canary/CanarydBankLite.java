/* 
 * Copyright 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dBankLite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dBankLite
 */
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.commandsys.CommandDependencyException;
import net.visualillusionsent.dconomy.MessageTranslator;
import net.visualillusionsent.dconomy.addon.bank.accounting.banking.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.dBankLite;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.logging.Logger;

public final class CanarydBankLite extends VisualIllusionsCanaryPlugin implements dBankLite {

    static {
        MessageTranslator.getClassVersion();
    }

    @Override
    public final boolean enable() {
        new dBankLiteBase(this);
        BankHandler.initialize();
        try {
            new CanarydBankLiteCommandListener(this);
        } catch (CommandDependencyException ex) {
            return false;
        }
        return true;
    }

    @Override
    public final void disable() {
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
