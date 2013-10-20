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

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.motd.MOTDKey;
import net.canarymod.motd.MessageOfTheDayListener;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.dCoBase;

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
        return MessageFormat.format("{0,number,0.00}", BankHandler.getBankAccount(asUser(msgrec)).getBalance());
    }

    private dConomyUser asUser(MessageReceiver msgrec) {
        return msgrec instanceof Player ? new Canary_User((Player) msgrec) : dCoBase.getServer();
    }
}
