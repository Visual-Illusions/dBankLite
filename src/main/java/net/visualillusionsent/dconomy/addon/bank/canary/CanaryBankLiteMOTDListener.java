/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.canary;

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.motd.MOTDKey;
import net.canarymod.motd.MessageOfTheDayListener;
import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.api.BankAPIListener;

import java.text.MessageFormat;

/**
 * Canary dConomy Message Of The Day Listener
 *
 * @author Jason (darkdiplomat)
 */
public final class CanaryBankLiteMOTDListener implements MessageOfTheDayListener {

    public CanaryBankLiteMOTDListener(CanaryBankLite cBankLite) {
        Canary.motd().registerMOTDListener(this, cBankLite, false);
    }

    @MOTDKey(key = "{bank.balance}")
    public String bank_balance(MessageReceiver msgrec) {
        try {
            return MessageFormat.format("{0,number,0.00}", BankAPIListener.bankAccountBalance(msgrec.getName(), msgrec.hasPermission("dconomy.bank.base")));
        }
        catch (AccountingException e) {
            return "no bank access";
        }
        catch (AccountNotFoundException e) {
            return "no bank access";
        }
    }
}
