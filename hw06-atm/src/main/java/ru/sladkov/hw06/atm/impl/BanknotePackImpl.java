package ru.sladkov.hw06.atm.impl;

import ru.sladkov.hw06.atm.BanknotePack;

public class BanknotePackImpl implements BanknotePack {

    private final Banknote banknote;

    private int amount;

    public BanknotePackImpl(Banknote banknote, int amount) {
        this.banknote = banknote;
        this.amount = amount;
    }

    @Override
    public String getBanknoteName() {
        return this.banknote.getName();
    }

    @Override
    public int getBanknoteValue() {
        return this.banknote.getValue();
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void addBanknotes(int quantity) {
        this.amount += quantity;
    }

    @Override
    public void removeBanknotes(int quantity) {
        this.amount -= quantity;
    }
}
