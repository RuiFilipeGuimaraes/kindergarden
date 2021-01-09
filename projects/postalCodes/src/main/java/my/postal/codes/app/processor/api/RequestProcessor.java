package my.postal.codes.app.processor.api;


import org.springframework.stereotype.Controller;

@Controller
public interface RequestProcessor<T, R> {
    R processRequest(T request);
}
