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

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.util.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class BankSQLSource extends BankDataSource {

    protected Connection conn;
    protected String bank_table = dCoBase.getProperties().getString("sql.bank.table");

    public BankSQLSource(BankHandler bank_handler) {
        super(bank_handler);
    }

    @Override
    public boolean load() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int load = 0;
        boolean success = true;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String owner = rs.getString("owner");
                UUID ownerUUID;
                if (Tools.isUserName(owner)) {
                    ownerUUID = dCoBase.getServer().getUUIDFromName(owner);
                    if (ownerUUID == null) {
                        ownerUUID = UUID.nameUUIDFromBytes("OfflinePlayer:".concat(owner).getBytes());
                    }
                }
                else {
                    ownerUUID = UUID.fromString(owner);
                }
                double balance = rs.getDouble("balance");
                boolean locked = rs.getBoolean("lockedOut");
                bank_handler.addAccount(new BankAccount(ownerUUID, balance, locked, this));
                load++;
            }
            dBankLiteBase.info(String.format("Loaded %d Bank Accounts...", load));
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
            catch (AbstractMethodError e) {
            } // SQLite weird stuff
            catch (Exception e) {
            }
        }
        return success;
    }

    @Override
    public boolean saveAccount(BankAccount account) {
        boolean success = true;
        synchronized (lock) {
            dBankLiteBase.debug("Saving Bank Account for: ".concat(account.getOwner().toString()));
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "` WHERE `owner`=?");
                ps.setString(1, account.getOwner().toString());
                rs = ps.executeQuery();
                boolean found = rs.next();
                if (found) {
                    ps.close();
                    ps = conn.prepareStatement("UPDATE `" + bank_table + "` SET `balance`=?, `lockedOut`=? WHERE `owner`=?");
                    ps.setDouble(1, account.getBalance());
                    ps.setInt(2, ((BankAccount) account).isLocked() ? 1 : 0);
                    ps.setString(3, account.getOwner().toString());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + bank_table + "` (`owner`,`balance`,`lockedOut`) VALUES(?,?,?)");
                    ps.setString(1, account.getOwner().toString());
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
                catch (AbstractMethodError e) {
                } // SQLite weird stuff
                catch (Exception e) {
                }
            }
        }
        return success;
    }

    @Override
    public boolean reloadAccount(BankAccount account) {
        boolean success = true;
        dBankLiteBase.debug("Reloading Wallet for: ".concat(account.getOwner().toString()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + bank_table + "` WHERE `owner`=?");
            ps.setString(1, account.getOwner().toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                account.setBalance(rs.getDouble("balance"));
                ((BankAccount) account).setLockOut(rs.getBoolean("lockedOut"));
            }
            dBankLiteBase.debug("Reloaded Wallet for: ".concat(account.getOwner().toString()));
        }
        catch (SQLException sqlex) {
            dBankLiteBase.severe("SQL Exception while reloading Wallet for: " + account.getOwner());
            dBankLiteBase.stacktrace(sqlex);
            success = false;
        }
        catch (AccountingException aex) {
            dBankLiteBase.severe("Accounting Exception while reloading Wallet for: " + account.getOwner());
            dBankLiteBase.stacktrace(aex);
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
            catch (AbstractMethodError e) {
            } // SQLite weird stuff
            catch (Exception e) {
            }
        }
        return success;
    }
}
