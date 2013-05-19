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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.data.BankDataSource;

public class BankAccount extends Account{
    private boolean locked;

    public BankAccount(String owner, double balance, boolean locked, BankDataSource datasource){
        super(owner, balance, datasource);
        this.locked = locked;
    }

    /**
     * Tests a debit before modifing the wallet
     * 
     * @param remove
     *            the amount to test removal for
     * @throws AccountingException
     *             if unable to debit the money
     */
    public final void testDebit(double remove) throws AccountingException{
        if (locked) {
            throw new AccountingException(MessageTranslator.transFormMessage("error.lock.out", true, this.owner, "BANK ACCOUNT"));
        }
        if (balance - remove < 0) {
            throw new AccountingException("error.no.money");
        }
    }

    /**
     * Tests a debit before modifing the wallet
     * 
     * @param remove
     *            the amount to test removal for
     * @throws AccountingException
     *             if unable to debit the money
     */
    public final void testDebit(String remove) throws AccountingException{
        testDebit(this.testArgumentString(remove));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testDeposit(double add) throws AccountingException{
        if (locked) {
            throw new AccountingException(MessageTranslator.transFormMessage("error.lock.out", true, this.owner, "BANK ACCOUNT"));
        }
        super.testDeposit(add);
    }

    public final void setLockOut(boolean locked){
        this.locked = locked;
        this.save();
    }

    public final boolean isLocked(){
        return locked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void save(){
        datasource.saveAccount(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reload(){
        return datasource.reloadAccount(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj){
        if (obj instanceof Wallet) {
            return this == obj;
        }
        else if (obj instanceof String) {
            return this.owner.equals(obj);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode(){
        int hash = 7;
        hash = hash * 3 + owner.hashCode();
        hash = hash * 3 + super.hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString(){
        return String.format("BankAccount[Owner: %s Balance: %.2f LockedOut: %b]", owner, balance, locked);
    }
}
