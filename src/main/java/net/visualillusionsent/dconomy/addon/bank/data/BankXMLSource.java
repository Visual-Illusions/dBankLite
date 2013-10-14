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

import net.visualillusionsent.dconomy.addon.bank.accounting.BankAccount;
import net.visualillusionsent.dconomy.addon.bank.dBankLiteBase;
import net.visualillusionsent.dconomy.dCoBase;
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

public final class BankXMLSource implements BankDataSource {

    private final Format xmlform = Format.getPrettyFormat().setExpandEmptyElements(false).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP);
    private final XMLOutputter outputter = new XMLOutputter(xmlform);
    private final SAXBuilder builder = new SAXBuilder();
    private final String bank_Path = dCoBase.getProperties().getConfigurationDirectory().concat("bankaccounts.xml");
    private FileWriter writer;

    @Override
    public final boolean load() {
        dBankLiteBase.info("Loading Bank Accounts...");
        File bankFile = new File(bank_Path);
        Exception ex = null;
        int load = 0;
        if (!bankFile.exists()) {
            dBankLiteBase.info("BankAccounts file not found. Creating...");
            Element accounts = new Element("bankaccounts");
            Document root = new Document(accounts);
            try {
                writer = new FileWriter(bank_Path);
                outputter.output(root, writer);
            } catch (IOException e) {
                ex = e;
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                }
                writer = null;
                if (ex != null) {
                    dBankLiteBase.severe("Failed to create new BankAccounts file...");
                    dBankLiteBase.stacktrace(ex);
                    return false;
                }
            }
        } else {
            try {
                Document doc = builder.build(bankFile);
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                for (Element account : accounts) {
                    new BankAccount(account.getAttributeValue("owner"), account.getAttribute("balance").getDoubleValue(), account.getAttribute("lockedOut").getBooleanValue(), this);
                    load++;
                }
            } catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while parsing BankAccounts file...");
                dBankLiteBase.stacktrace(ex);
                return false;
            } catch (IOException e) {
                dBankLiteBase.severe("Input/Output Exception while parsing BankAccounts file...");
                dBankLiteBase.stacktrace(ex);
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
                Document doc = builder.build(bankFile);
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                boolean found = false;
                for (Element account : accounts) {
                    String name = account.getAttributeValue("owner");
                    if (name.equals(bankaccount.getOwner())) {
                        account.getAttribute("balance").setValue(String.format("%.2f", bankaccount.getBalance()));
                        account.getAttribute("lockedOut").setValue(String.valueOf(((BankAccount) bankaccount).isLocked()));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Element newWallet = new Element("bankaccount");
                    newWallet.setAttribute("owner", bankaccount.getOwner());
                    newWallet.setAttribute("balance", String.format("%.2f", bankaccount.getBalance()));
                    newWallet.setAttribute("lockedOut", String.valueOf(((BankAccount) bankaccount).isLocked()));
                    root.addContent(newWallet);
                }
                try {
                    writer = new FileWriter(bankFile);
                    outputter.output(root, writer);
                } catch (IOException ex) {
                    dBankLiteBase.severe("Failed to write to BankAccounts file...");
                    dBankLiteBase.stacktrace(ex);
                    success = false;
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException e) {
                    }
                    writer = null;
                }
            } catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while trying to save bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(jdomex);
                success = false;
            } catch (IOException ioex) {
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
                Document doc = builder.build(bankFile);
                Element root = doc.getRootElement();
                List<Element> accounts = root.getChildren();
                for (Element account : accounts) {
                    String name = account.getChildText("owner");
                    if (name.equals(bankaccount.getOwner())) {
                        bankaccount.setBalance(account.getAttribute("balance").getDoubleValue());
                        ((BankAccount) bankaccount).setLockOut(account.getAttribute("lockedOut").getBooleanValue());
                        break;
                    }
                }
            } catch (JDOMException jdomex) {
                dBankLiteBase.severe("JDOM Exception while trying to reload bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(jdomex);
                success = false;
            } catch (IOException ioex) {
                dBankLiteBase.severe("Input/Output Exception while trying to reload bank account for User:" + bankaccount.getOwner());
                dBankLiteBase.stacktrace(ioex);
                success = false;
            }
        }
        return success;
    }

    public final void cleanUp() {
    }
}
