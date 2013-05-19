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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.addon.bank.accounting.banking.BankAccount;

public abstract class BankSQL_Source implements BankDataSource{
    protected Connection conn;
    protected String bank_table = dCoBase.getProperties().getString("sql.bank.table");

    @Override
    public boolean load(){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int load = 0;
        boolean success = true;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("owner");
                double balance = rs.getDouble("balance");
                boolean locked = rs.getBoolean("lockedOut");
                new BankAccount(name, balance, locked, this);
                load++;
            }
            dCoBase.info(String.format("Loaded %d Bank Accounts...", load));
        }
        catch (SQLException sqlex) {
            dBankLiteBase.severe("SQL Exception while parsing Bank Accounts table...");
            dBankLiteBase.stacktrace(sqlex);
            success = false;
        }
        finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }
            catch (AbstractMethodError e) {} // SQLite weird stuff
            catch (Exception e) {}
        }
        return success;
    }

    @Override
    public boolean saveAccount(Account account){
        boolean success = true;
        synchronized (lock) {
            dBankLiteBase.debug("Saving Bank Account for: ".concat(account.getOwner()));
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "` WHERE `owner`=?");
                ps.setString(1, account.getOwner());
                rs = ps.executeQuery();
                boolean found = rs.next();
                if (found) {
                    ps.close();
                    ps = conn.prepareStatement("UPDATE `" + bank_table + "` SET `balance`=?, `lockedOut`=? WHERE `owner`=?");
                    ps.setDouble(1, account.getBalance());
                    ps.setInt(2, ((BankAccount) account).isLocked() ? 1 : 0);
                    ps.setString(3, account.getOwner());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + bank_table + "` (`owner`,`balance`,`lockedOut`) VALUES(?,?,?)");
                    ps.setString(1, account.getOwner());
                    ps.setInt(2, ((BankAccount) account).isLocked() ? 1 : 0);
                    ps.setDouble(3, account.getBalance());
                    ps.execute();
                }
                dBankLiteBase.debug("Bank Accoutn saved!");
            }
            catch (SQLException sqlex) {
                dBankLiteBase.severe("Failed to save Bank Account for: " + account.getOwner());
                dBankLiteBase.stacktrace(sqlex);
            }
            finally {
                try {
                    if (rs != null && !rs.isClosed()) {
                        rs.close();
                    }
                    if (ps != null && !ps.isClosed()) {
                        ps.close();
                    }
                }
                catch (AbstractMethodError e) {} // SQLite weird stuff
                catch (Exception e) {}
            }
        }
        return success;
    }

    @Override
    public boolean reloadAccount(Account account){
        boolean success = true;
        dBankLiteBase.debug("Reloading Wallet for: ".concat(account.getOwner()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "` WHERE `owner`=?");
            ps.setString(1, account.getOwner());
            rs = ps.executeQuery();
            while (rs.next()) {
                account.setBalance(rs.getDouble("balance"));
                ((BankAccount) account).setLockOut(rs.getBoolean("lockedOut"));
            }
            dBankLiteBase.debug("Reloaded Wallet for: ".concat(account.getOwner()));
        }
        catch (SQLException sqlex) {
            dBankLiteBase.severe("SQL Exception while reloading Wallet for: " + account.getOwner());
            dBankLiteBase.stacktrace(sqlex);
            success = false;
        }
        finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }
            catch (AbstractMethodError e) {} // SQLite weird stuff
            catch (Exception e) {}
        }
        return success;
    }
}
