import java.util.*;
import java.util.concurrent.*;

class InventoryManager {

    private Map<String, Integer> stock = new ConcurrentHashMap<>();
    private Map<String, Queue<Integer>> waitingList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }

    public synchronized String purchase(String productId, int userId) {

        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {
            stock.put(productId, available - 1);
            return "Success";
        } else {
            waitingList.get(productId).add(userId);
            return "Added to waiting list";
        }
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.addProduct("P101", 2);

        System.out.println(manager.purchase("P101", 1));
        System.out.println(manager.purchase("P101", 2));
        System.out.println(manager.purchase("P101", 3));

        System.out.println("Remaining stock: "
                + manager.checkStock("P101"));
    }
}