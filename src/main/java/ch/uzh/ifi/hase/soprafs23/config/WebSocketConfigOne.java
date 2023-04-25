package ch.uzh.ifi.hase.soprafs23.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebSocketConfigOne {
    /**
     * This bean will automatically register the object declared with @ServerEndpoint annotation
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public static ThreadPoolExecutor executor;
    @PostConstruct
    public void messageExecutor() {
        executor = new ThreadPoolExecutor(4,40,60l, TimeUnit.SECONDS,new LinkedBlockingQueue<>(8));
    }
}