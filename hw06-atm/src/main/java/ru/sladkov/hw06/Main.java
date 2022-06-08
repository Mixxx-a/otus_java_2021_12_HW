package ru.sladkov.hw06;

import ru.sladkov.hw06.atm.AtmOperations;
import ru.sladkov.hw06.atm.BanknotePack;
import ru.sladkov.hw06.atm.impl.ATM;
import ru.sladkov.hw06.atm.impl.Banknote;
import ru.sladkov.hw06.atm.impl.BanknotePackImpl;

public class Main {

    public static void main(String[] args) {

        Banknote banknote1 = new Banknote("50RUB", 50);
        Banknote banknote2 = new Banknote("100RUB", 100);
        Banknote banknote3 = new Banknote("200RUB", 200);
        Banknote banknote4 = new Banknote("500RUB", 500);
        Banknote banknote5 = new Banknote("1000RUB", 1000);

        BanknotePack pack1 = new BanknotePackImpl(banknote1, 10);
        BanknotePack pack2 = new BanknotePackImpl(banknote2, 5);
        BanknotePack pack3 = new BanknotePackImpl(banknote3, 3);
        BanknotePack pack4 = new BanknotePackImpl(banknote4, 2);
        BanknotePack pack5 = new BanknotePackImpl(banknote5, 0);

        AtmOperations atm = new ATM(pack1, pack2, pack3, pack4, pack5);

        atm.printBalance();

        atm.putMoney(banknote2, 2);
        atm.printBalance();

        atm.withdrawMoney(1300);
        atm.printBalance();

        //Shouldn't be able to put custom Banknote
        atm.putMoney(new Banknote("30RUB", 30), 5);
        atm.printBalance();

        //Shouldn't be able to withdraw money that ATM can't give
        atm.withdrawMoney(599);
        atm.printBalance();
    }
}
