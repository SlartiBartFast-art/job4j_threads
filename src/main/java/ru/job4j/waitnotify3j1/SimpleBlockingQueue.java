package ru.job4j.waitnotify3j1;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 1. Реализовать шаблон Producer Consumer. [#1098]
 * Уровень : 3. Мидл Категория : 3.1. Multithreading Топик : 3.1.4.
 * Если очередь заполнена полностью,то при попытке добавления поток Producer блокируется,
 * до тех пор пока Consumer не извлечет очередные данные,т.е.в очереди появится свободное место.
 * И наоборот если очередь пуста поток Consumer блокируется,до тех пор пока Producer не поместит
 * в очередь данные.
 *
 * @param <T> generic type
 * @author SlartiBartFast-art
 * @version 0.1
 * @since 03.09.2021
 */
@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<T>();  //блокирующая очередь ограниченная по размеру

    public void offer(T value) { // Producer помещает данные в очередь
        synchronized (this) {
//queue.offer(value) Немедленное размещение элемента в очереди при наличие свободного места;
// метод вернет true при успешном завершении операции, в противном случае вернет false.
            while (!queue.offer(value)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            notify();
        }
    }

    /**
     * Метод poll() должен вернуть объект из внутренней коллекции.
     * Если в коллекции объектов нет, то нужно перевести текущую нить в состояние ожидания.
     * Важный момент, когда нить переводить в состояние ожидания, то она отпускает объект монитор
     * и другая нить тоже может выполнить этот метод.
     *
     * @return
     */
    public T poll() { //Consumer извлекает данные из очереди
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
                    wait(); // перевести текущую нить в состояние ожидания.
                    queue.notify(); // она отпускает объект монитор
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return queue.poll();
        }
    }
}
