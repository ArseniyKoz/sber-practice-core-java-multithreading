package org.example;

public class Store {
    public static void main(String[] args) {
        Shelf shelf = Shelf.getInstance();

        new Thread(new Customer(shelf)).start();

        new Thread(new ShopAssistant(shelf)).start();
        new Thread(new ShopAssistant(shelf)).start();


    }
}

class Shelf {
    private static Shelf instance = new Shelf();
    private int itemCount = 0;

    private Shelf() {}

    public static Shelf getInstance() {
        return instance;
    }

    public synchronized void put() throws InterruptedException {
        while (itemCount >= 5) {
            wait();
        }
        itemCount++;
        System.out.println("Продавец положил товар. Теперь на полке " + itemCount + " товаров");
        notify();
    }

    public synchronized void get() throws InterruptedException {
        while (itemCount == 0) {
            wait();
        }
        itemCount--;
        System.out.println("Покупатель забрал товар. Теперь на полке " + itemCount + " товаров");
        notify();
    }
}

class Customer implements Runnable {
    private Shelf shelf;

    public Customer(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                shelf.get();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ShopAssistant implements Runnable {
    private Shelf shelf;

    public ShopAssistant(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                shelf.put();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
