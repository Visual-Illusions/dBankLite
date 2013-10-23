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
package net.visualillusionsent.dconomy.addon.bank.data;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.dCoBase;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
