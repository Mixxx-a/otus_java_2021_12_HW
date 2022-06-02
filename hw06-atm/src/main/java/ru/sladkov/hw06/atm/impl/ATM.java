package ru.sladkov.hw06.atm.impl;

import ru.sladkov.hw06.atm.AtmOperational;
import ru.sladkov.hw06.atm.BanknotePack;
import ru.sladkov.hw06.atm.NonPerformableOperation;

import java.util.*;

public class ATM implements AtmOperational {
    SortedMap<Integer, BanknotePack> packs = new TreeMap<>(Collections.reverseOrder());

    public ATM(BanknotePack... packs) {
        for (BanknotePack pack : packs) {
            this.packs.put(pack.getBanknoteValue(), pack);
        }
    }

    @Override
    public void putMoney(Banknote banknote, int numberOfBanknotes) {
        BanknotePack pack = packs.get(banknote.getValue());
        if (pack != null) {
            pack.addBanknotes(numberOfBanknotes);
            System.out.println("Added " + numberOfBanknotes + " of " + banknote.getName() + " to account\n");
        } else {
            System.out.println("Can't add banknote with value " + banknote.getValue() + "\n");
        }
    }

    @Override
    public void withdrawMoney(int amount) {
        System.out.println("Client wants to withdraw " + amount);
        try {
            Map<BanknotePack, Integer> withdrawMoneyMap = prepareTransaction(amount);
            performTransaction(withdrawMoneyMap);
        } catch (NonPerformableOperation e) {
            System.out.println("Can't withdraw " + amount + " money\n");
        }
    }

    @Override
    public void printBalance() {
        int total = 0;
        for (BanknotePack pack : packs.values()) {
            System.out.println("Have " + pack.getAmount() + " of " + pack.getBanknoteName());
            total += pack.getAmount() * pack.getBanknoteValue();
        }
        System.out.println("Total : " + total + "\n");
    }

    private Map<BanknotePack, Integer> prepareTransaction(int amount) throws NonPerformableOperation {
        Map<BanknotePack, Integer> withdrawMoneyMap = new HashMap<>();
        int remainingMoney = amount;
        for (BanknotePack pack : packs.values()) {
            int banknoteValue = pack.getBanknoteValue();
            int givenBanknotesCount = 0;
            while (banknoteValue <= remainingMoney) {
                if (pack.getAmount() == 0) {
                    break;
                }
                givenBanknotesCount++;
                remainingMoney -= banknoteValue;
            }
            if (givenBanknotesCount > 0) {
                withdrawMoneyMap.put(pack, givenBanknotesCount);
            }
        }
        if (remainingMoney != 0) {
            throw new NonPerformableOperation();
        }
        return withdrawMoneyMap;
    }

    private void performTransaction(Map<BanknotePack, Integer> withdrawBanknotesMap) {
        int total = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<BanknotePack, Integer> entry : withdrawBanknotesMap.entrySet()) {
            entry.getKey().removeBanknotes(entry.getValue());
            stringBuilder.append("Gave ")
                    .append(entry.getValue())
                    .append(" banknote(s) of ")
                    .append(entry.getKey().getBanknoteName())
                    .append("\n");
            total += entry.getKey().getBanknoteValue() * entry.getValue();
        }
        System.out.println(stringBuilder.deleteCharAt(stringBuilder.length() - 1));
        System.out.println("Total withdrawn money: " + total + "\n");
    }
}
