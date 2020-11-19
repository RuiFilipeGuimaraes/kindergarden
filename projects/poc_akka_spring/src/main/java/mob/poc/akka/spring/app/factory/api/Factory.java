package mob.poc.akka.spring.app.factory.api;

public interface Factory<T> {
    T produce();
}
