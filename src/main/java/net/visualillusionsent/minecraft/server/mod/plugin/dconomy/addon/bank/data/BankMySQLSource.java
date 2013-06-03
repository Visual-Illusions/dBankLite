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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.data;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankAccount;

public final class BankMySQLSource extends BankSQLSource{

    @Override
    public final boolean load(){
        try {
            testConnection();
            PreparedStatement ps = null;
            dCoBase.debug("Testing BankAccount table and creating if needed...");
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `" + bank_table + "` " + //
                    "(`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL, `lockedOut` TINYINT(1) NOT NULL, PRIMARY KEY (`owner`))");
            ps.execute();
            ps.close();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while testing Wallets tables...");
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.load();
    }

    @Override
    public final boolean saveAccount(BankAccount bankaccount){
        try {
            testConnection();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Connection failed while saving BankAccount for " + bankaccount.getOwner());
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.saveAccount(bankaccount);
    }

    @Override
    public final boolean reloadAccount(BankAccount bankaccount){
        try {
            testConnection();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Connection failed while reloading BankAccount for " + bankaccount.getOwner());
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.reloadAccount(bankaccount);
    }

    private final void testConnection() throws SQLException{
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            conn = DriverManager.getConnection("jdbc:mysql://" + dCoBase.getProperties().getString("sql.database.url"), dCoBase.getProperties().getString("sql.user"), dCoBase.getProperties().getString("sql.password"));
        }
    }
}
