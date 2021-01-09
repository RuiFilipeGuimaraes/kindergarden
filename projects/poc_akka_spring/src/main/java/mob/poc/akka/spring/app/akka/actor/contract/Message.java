package mob.poc.akka.spring.app.akka.actor.contract;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Message {
    private String content;
    public Message(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("content", content)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equal(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }
}
