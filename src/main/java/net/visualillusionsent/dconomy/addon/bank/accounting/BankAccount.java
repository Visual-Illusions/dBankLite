/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.addon.bank.accounting;

import net.visualillusionsent.dconomy.accounting.Account;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.data.BankDataSource;

import java.util.UUID;

public class BankAccount extends Account {

    private boolean locked;

    public BankAccount(UUID owner, double balance, boolean locked, BankDataSource datasource) {
        super(owner, balance, datasource);
        this.locked = locked;
    }

    /**
     * Tests a debit before modifing the wallet
     *
     * @param remove
     *         the amount to test removal for
     *
     * @throws AccountingException
     *         if unable to debit the money
     */
    public final void testDebit(double remove) throws AccountingException {
        if (locked) {
            throw new AccountingException("error.lock.out", this.owner, "BANK ACCOUNT");
        }
        if (balance - remove < 0) {
            throw new AccountingException("error.no.money");
        }
    }

    /**
     * Tests a debit before modifing the wallet
     *
     * @param remove
     *         the amount to test removal for
     *
     * @throws AccountingException
     *         if unable to debit the money
     */
    public final void testDebit(String remove) throws AccountingException {
        testDebit(this.testArgumentString(remove));
    }

    /** {@inheritDoc} */
    @Override
    public void testDeposit(double add) throws AccountingException {
        if (locked) {
            throw new AccountingException("error.lock.out", this.owner, "BANK ACCOUNT");
        }
        super.testDeposit(add);
    }

    public final void setLockOut(boolean locked) {
        this.locked = locked;
        this.save();
    }

    public final boolean isLocked() {
        return locked;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    protected void save() {
        datasource.saveAccount(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public boolean reload() {
        return datasource.reloadAccount(this);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof BankAccount) {
            return this == obj;
        }
        else if (obj instanceof String) {
            return this.owner.equals(obj);
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = hash * 3 + owner.hashCode();
        hash = hash * 3 + super.hashCode();
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        return String.format("BankAccount[Owner: %s Balance: %.2f LockedOut: %b]", owner, balance, locked);
    }
}
