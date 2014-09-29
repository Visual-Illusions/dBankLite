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
package net.visualillusionsent.dconomy.addon.bank.data;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.dCoBase;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Jason (darkdiplomat)
 */
public final class BankSQLiteSource extends BankSQLSource {

    private static BankSQLiteSource $;
    private final String db_Path = dCoBase.getProperties().getString("sql.database.url");

    public BankSQLiteSource(BankHandler bank_handler) {
        super(bank_handler);
        if ($ == null) {
            $ = this;
        }
        bank_table = db_Path + "." + bank_table;
    }

    @Override
    public final boolean load() {
        Statement st = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:".concat(db_Path));
            st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS `" + bank_table + "` (`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL, `lockedOut` TINYINT(1) NOT NULL, PRIMARY KEY (`owner`))");
            st.close();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while parsing BankAccounts table...");
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.load();
    }

    public static void cleanUp() {
        try {
            $.conn.close();
        }
        catch (Exception e) {
        }
        $ = null;
    }

}
