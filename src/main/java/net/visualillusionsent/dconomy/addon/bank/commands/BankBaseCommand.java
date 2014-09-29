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
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

/**
 * @author Jason (darkdiplomat)
 */
public final class BankBaseCommand extends BankCommand {

    public BankBaseCommand(BankHandler bank_handler) {
        super(0, bank_handler);
    }

    protected final void execute(dConomyUser user, String[] args) {
        BankAccount theAccount;
        if (args.length == 1 && (user.hasPermission("dconomy.admin.bank") || !dCoBase.getProperties().getBooleanValue("adminonly.balance.check"))) {
            dConomyUser theUser = dCoBase.getServer().getUser(args[0]);
            if (theUser == null) {
                dBankLiteBase.translateErrorMessageFor(user, "error.404.user", args[0]);
                return;
            }
            if (!bank_handler.verifyAccount(theUser.getUUID())) {
                dBankLiteBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "BANK ACCOUNT");
                return;
            }
            theAccount = bank_handler.getBankAccount(theUser);
            if (theAccount.isLocked()) {
                dBankLiteBase.translateErrorMessageFor(user, "error.account.lock", theUser.getName(), "BANK ACCOUNT");
            }
            else {
                dBankLiteBase.translateMessageFor(user, "account.balance.other", theUser.getName(), theAccount.getBalance());
            }
        }
        else {
            theAccount = bank_handler.getBankAccount(user);
            if (theAccount.isLocked()) {
                dBankLiteBase.translateErrorMessageFor(user, "error.account.lock", user.getName(), "BANK ACCOUNT");
            }
            else {
                dBankLiteBase.translateMessageFor(user, "account.balance", Double.valueOf(theAccount.getBalance()), "BANK ACCOUNT");
            }
        }
    }
}
