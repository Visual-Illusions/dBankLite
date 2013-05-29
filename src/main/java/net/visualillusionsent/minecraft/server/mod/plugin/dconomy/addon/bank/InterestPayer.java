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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank;

import java.util.TimerTask;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankAccount;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankHandler;

final class InterestPayer extends TimerTask{
    private final dBankLiteBase base;

    public InterestPayer(dBankLiteBase base){
        this.base = base;
    }

    @Override
    public void run(){
        for (BankAccount account : BankHandler.getAccounts()) {
            double interest = account.getBalance() * (dCoBase.getProperties().getDouble("interest.rate") / 100);
            double maxout = dCoBase.getProperties().getDouble("interest.max.payout");
            if (interest > maxout) {
                interest = maxout;
            }
            try {
                account.deposit(interest);
            }
            catch (AccountingException aex) {
                // Probably over balanced
            }
        }
        dBankLiteBase.info(String.format("Interest Paid to %d Accounts", BankHandler.getAccounts().size()));
        base.setResetTime();
    }
}
