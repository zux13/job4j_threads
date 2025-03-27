package ru.job4j.cash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AccountStorageTest {

    private AccountStorage storage;

    @BeforeEach
    void setUp() {
        storage = new AccountStorage();
    }

    @Test
    void whenAdd() {
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenDelete() {
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenUpdateNonExistingAccountThenFalse() {
        boolean updated = storage.update(new Account(99, 500));
        assertThat(updated).isFalse();
    }

    @Test
    void whenTransferFailsDueToInsufficientFundsThenNoChange() {
        Account from = new Account(1, 100);
        Account to = new Account(2, 100);
        storage.add(from);
        storage.add(to);

        boolean transferred = storage.transfer(1, 2, 200);

        assertThat(transferred).isFalse();
        assertThat(storage.getById(1)).isPresent().contains(from);
        assertThat(storage.getById(2)).isPresent().contains(to);
    }

    @Test
    void whenTransferFailsDueToNonExistingAccountThenNoChange() {
        Account to = new Account(2, 100);
        storage.add(to);

        boolean transferred = storage.transfer(1, 2, 50);

        assertThat(transferred).isFalse();
        assertThat(storage.getById(2)).isPresent().contains(to);
    }
}