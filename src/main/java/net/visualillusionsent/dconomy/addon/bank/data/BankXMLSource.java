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
import net.visualillusionsent.dconomy.data.DataLock;
import net.visualillusionsent.minecraft.plugin.util.Tools;
import net.visualillusionsent.utils.SystemUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public final class BankXMLSource extends BankDataSource {
    private final DataLock lock = new DataLock();
    private final Format xmlform = Format.getPrettyFormat().setExpandEmptyElements(false).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP);
    private final XMLOutputter outputter = new XMLOutputter(xmlform);
    private final SAXBuilder builder = new SAXBuilder();
    private final String bank_Path = ("config/dBankLite/bankaccounts.xml");
    private FileWriter writer;
    private Document doc;

    public BankXMLSource(BankHandler bank_handler) {
        super(bank_handler);
    }

    @Override
    public final boolean load() {
        dBankLiteBase.info("Loading Bank Accounts...");
        File bankFile = new File(bank_Path);
        Exception ex = null;
        int load = 0;
        if (!bankFile.exists()) {
            dBankLiteBase.info("BankAccounts file not found. Creating...");
            Element accounts = new Element("bankaccounts");
            doc = new Document(accounts);
            try {
                writer = new FileWriter(bank_Path);
                outputter.output(doc, writer);
            }
            catch (IOException e) {
                ex = e;
            }
            finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                }
                catch (IOException e) {
                    //
                }
            }
            if (ex != null) {
                dBankLiteBase.severe("Failed to create new BankAccounts file...");
                dBankLiteBase.stacktrace(ex);
                return false;
            }
        }
        else {
            try {
                if (doc == null) {
                    doc = builder.build(bankFile);
                }
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                for (Element account : accounts) {
                    String owner = account.getAttributeValue("owner");
                    UUID ownerUUID;
                    if (Tools.isUserName(owner)) {
                        ownerUUID = dCoBase.getServer().getUUIDFromName(owner);
                        account.detach(); // Remove the old account
                    }
                    else {
                        ownerUUID = UUID.fromString(owner);
                    }
                    bank_handler.addAccount(new BankAccount(ownerUUID, account.getAttribute("balance").getDoubleValue(), account.getAttribute("lockedOut").getBooleanValue(), this));
                    load++;
                }
            }
            catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while parsing BankAccounts file...");
                dBankLiteBase.stacktrace(jdomex);
                return false;
            }
            catch (IOException ioex) {
                dBankLiteBase.severe("Input/Output Exception while parsing BankAccounts file...");
                dBankLiteBase.stacktrace(ioex);
                return false;
            }
        }
        dBankLiteBase.info(String.format("Loaded %d Bank Accounts...", load));
        return true;
    }

    @Override
    public final boolean saveAccount(BankAccount bankaccount) {
        boolean success = true;
        synchronized (lock) {
            File bankFile = new File(bank_Path);
            try {
                if (doc == null) {
                    doc = builder.build(bankFile);
                }
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                boolean found = false;
                for (Element account : accounts) {
                    String name = account.getAttributeValue("owner");
                    if (name.equals(bankaccount.getOwner().toString())) {
                        account.getAttribute("balance").setValue(String.format("%.2f", bankaccount.getBalance()));
                        account.getAttribute("lockedOut").setValue(String.valueOf(bankaccount.isLocked()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Element newWallet = new Element("bankaccount");
                    newWallet.setAttribute("owner", bankaccount.getOwner().toString());
                    newWallet.setAttribute("balance", String.format("%.2f", bankaccount.getBalance()));
                    newWallet.setAttribute("lockedOut", String.valueOf(bankaccount.isLocked()));
                    root.addContent(newWallet);
                }
                try {
                    writer = new FileWriter(bankFile);
                    outputter.output(root, writer);
                }
                catch (IOException ex) {
                    dBankLiteBase.severe("Failed to write to BankAccounts file...");
                    dBankLiteBase.stacktrace(ex);
                    success = false;
                }
                finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                    catch (IOException e) {
                    }
                    writer = null;
                }
            }
            catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while trying to save bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(jdomex);
                success = false;
            }
            catch (IOException ioex) {
                dBankLiteBase.severe("Input/Output Exception while trying to save bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(ioex);
                success = false;
            }
        }
        return success;
    }

    @Override
    public final boolean reloadAccount(BankAccount bankaccount) {
        boolean success = true;
        synchronized (lock) {
            File bankFile = new File(bank_Path);
            try {
                if (doc == null) {
                    doc = builder.build(bankFile);
                }
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                for (Element account : accounts) {
                    String name = account.getChildText("owner");
                    if (name.equals(bankaccount.getOwner().toString())) {
                        bankaccount.setBalance(account.getAttribute("balance").getDoubleValue());
                        bankaccount.setLockOut(account.getAttribute("lockedOut").getBooleanValue());
                        break;
                    }
                }
            }
            catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while trying to reload bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(jdomex);
                success = false;
            }
            catch (IOException ioex) {
                dBankLiteBase.severe("Input/Output Exception while trying to reload bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(ioex);
                success = false;
            }
            catch (AccountingException aex) {
                dBankLiteBase.severe("Accounting Exception while reloading Wallet for: " + bankaccount.getOwner());
                dBankLiteBase.stacktrace(aex);
                success = false;
            }
        }
        return success;
    }
}
