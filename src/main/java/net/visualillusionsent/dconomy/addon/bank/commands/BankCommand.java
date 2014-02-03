/*
 * This file is part of dBankLite.
 *
 * Copyright © 2013-2014 Visual Illusions Entertainment
 *
 * dBankLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.addon.bank.commands;

import net.visualillusionsent.dconomy.addon.bank.accounting.BankHandler;
import net.visualillusionsent.dconomy.commands.dConomyCommand;

/**
 * Created with IntelliJ IDEA.
 * User: darkdiplomat
 * Date: 10/23/13
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class BankCommand extends dConomyCommand {
    protected final BankHandler bank_handler;

    public BankCommand(int minArgs, BankHandler bank_handler) {
        super(minArgs);
        this.bank_handler = bank_handler;
    }
}
